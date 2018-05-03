
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * The corpus reader.
 * <p>
 * The format needs to be this, in BNF:
 *      RealWord      = [any word.]
 *      WordType      = [any word that represents a word type.]
 *      Spaces        = [at least 1 or more spaces.]
 *      Sentence    ::= Word
 *                    | Word Spaces Sentence
 *      Body        ::= Sentence
 *                    | Sentence "\n" Body
 * </p>
 * @author bctnry
 */
public class CorpusReader {
    private Scanner corpusReader;
    
    private Set<String> words;
    private Set<String> wordTypes;
    private Map<String, Integer> initializationFrequency;
    private Map<String, Map<String, Integer>> transitionFrequency, projectionFrequency;
    
    // for controlling embellishment & instances.
    private boolean isEmbellishmentOutdated = true;
    private Enumerable<String> wordEnumInstance = null,
                               wordTypeEnumInstance = null;
    
    // Constructors.
    /**
     * Default constructor that creates a corpus reader
     * with empty word/wordtype set & matrices.
     */
    public CorpusReader() {
        this.words = new HashSet<>();
        this.wordTypes = new HashSet<>();
        this.initializationFrequency = new HashMap<>();
        this.transitionFrequency = new HashMap<>();
        this.projectionFrequency = new HashMap<>();
    }
    
    /**
     * Create a corpus reader which reads from a file.
     * @param file
     * @throws FileNotFoundException 
     */
    public CorpusReader(File file) throws FileNotFoundException {
        this();
        this.corpusReader = new Scanner(file);
    }
    
    /**
     * Create a corpus reader which reads from an input stream.
     * @param inputStream 
     */
    public CorpusReader(InputStream inputStream) {
        this();
        this.corpusReader = new Scanner(inputStream);
    }
    
    /**
     * Create a corpus reader which reads from a reader.
     * @param reader 
     */
    public CorpusReader(Reader reader) {
        this();
        this.corpusReader = new Scanner(reader);
    }
    
    /**
     * Read a word from a reader.
     * @param cp
     * @param source
     * @return
     * @throws IOException 
     */
    public static Word nextWordFrom(CorpusReader cp, Scanner source) throws IOException {
        if(source.hasNext()) {
            String preWord = source.next().trim();
            return new Word(preWord.replace("/", ","));
        }
        return null;
    }
    /** Read a word from the CorpusReader's source.
     * @return
     * @throws IOException 
     */
    public Word nextWord() throws IOException {
        return CorpusReader.nextWordFrom(this, corpusReader);
    }
    
    /**
     * Learn a line of corpus.
     * 
     * Methods that directly call learnLineFrom() should call embellish() manually
     * to update the matrices & instances.
     * @param source A line of corpus
     * @throws IOException 
     */
    public void learnLineFrom(String source) throws IOException {
        // first word.
        Scanner sourceReader = new Scanner(source);
        Word firstWord = CorpusReader.nextWordFrom(this,sourceReader);
        if(firstWord == null) return; // nothing to learn from this line of corpus.
        
        // we have things to learn. previous embellishment might be outdated.
        this.isEmbellishmentOutdated = true;
        
        // insert into sets.
        String firstWordStr = firstWord.getWord(),
               firstWordType = firstWord.getWordClass()[0];
        this.words.add(firstWordStr);
        this.wordTypes.add(firstWordType);
        this.initializationFrequency.putIfAbsent(firstWordType, 0);
        this.initializationFrequency.put(firstWordType, this.initializationFrequency.get(firstWordType) + 1);
        this.projectionFrequency.putIfAbsent(firstWordType, new HashMap<>());
        this.projectionFrequency.get(firstWordType).putIfAbsent(firstWordStr, 0);
        this.projectionFrequency.get(firstWordType).put(
                firstWordStr,
                this.projectionFrequency.get(firstWordType).get(firstWordStr) + 1
        );
                
        // main learning
        Word previousWord = firstWord;
        Word currentWord;
        while((currentWord = CorpusReader.nextWordFrom(this,sourceReader)) != null) {
            String previousWordStr = previousWord.getWord(),
                    previousWordType = previousWord.getWordClass()[0],
                    currentWordStr = currentWord.getWord(),
                    currentWordType = currentWord.getWordClass()[0];
            // modifying sets.
            this.words.add(currentWordStr);
            this.wordTypes.add(currentWordType);
            // learn transition.
            this.transitionFrequency.putIfAbsent(previousWordType, new HashMap<>());
            this.transitionFrequency.get(previousWordType).putIfAbsent(currentWordType, 0);
            this.transitionFrequency.get(previousWordType).put(
                    currentWordType,
                    this.transitionFrequency.get(previousWordType).get(currentWordType) + 1
            );
            // learn projection.
            this.projectionFrequency.putIfAbsent(currentWordType, new HashMap<>());
            this.projectionFrequency.get(currentWordType).putIfAbsent(currentWordStr, 0);
            this.projectionFrequency.get(currentWordType).put(
                    currentWordStr,
                    this.projectionFrequency.get(currentWordType).get(currentWordStr) + 1
            );
        }
    }
    
    /**
     * Learn from lines.
     * @param source
     * @throws IOException 
     */
    public void learnFrom(String source) throws IOException {
        String[] lines = source.split("\n");
        for(String line : lines) {
            this.learnLineFrom(line.trim());
        }
        // embellish!
        this.embellish();
    }
    
    /**
     * Learn from the source specified at construction or through the
     * setCorpusReaderSource() setter.
     * @throws IOException 
     */
    public void learn() throws IOException {
        while(this.corpusReader.hasNextLine()) {
            this.learnLineFrom(this.corpusReader.nextLine());
        }
        // embellish!
        this.embellish();
    }
    
    // embellishment.
    /**
     * Complete the initialization frequency matrix with zeroes.
     */
    private void embellishInitializationFrequency() {
        this.wordTypes.forEach((wordType) -> {
            this.initializationFrequency.putIfAbsent(wordType, 0);
        });
    }
    
    /**
     * Complete the transition frequency matrix with zeroes.
     */
    private void embellishTransitionMatrix() {
        this.wordTypes.forEach((wordType) -> {
            this.transitionFrequency.putIfAbsent(wordType, new HashMap<>());
        });
        this.transitionFrequency.forEach((prev, map) -> {
            this.wordTypes.forEach((next) -> {
                map.putIfAbsent(next, 0);
            });
        });
    }
    
    /**
     * Complete the projection frequency matrix with zeroes.
     */
    private void embellishProjectionMatrix() {
        this.wordTypes.forEach((wordType) -> {
            this.projectionFrequency.putIfAbsent(wordType, new HashMap<>());
        });
        this.projectionFrequency.forEach((source, map) -> {
            this.words.forEach((target) -> {
                map.putIfAbsent(target, 0);
            });
        });
    }
    
    /**
     * Complete all matrices.
     */
    public void embellish() {
        if(this.isEmbellishmentOutdated) {
            this.embellishInitializationFrequency();
            this.embellishProjectionMatrix();
            this.embellishTransitionMatrix();
            this.getWordEnumInstance();
            this.getWordTypeEnumInstance();
            this.isEmbellishmentOutdated = false;
        }
    }
    
    // getters
    /**
     * Retrieve the word Enumerable instance.
     * @return The word set's Enumerable instance..
     */
    public Enumerable<String> getWordEnumInstance() {
        if(isEmbellishmentOutdated) {
            String[] wordArray = this.words.toArray(new String[0]);
            return (this.wordEnumInstance = Enumerable.fromStrings(wordArray));
        } else {
            return this.wordEnumInstance;
        }
    }
    
    /**
     * Retrieve the wordtype set's Enumerable instance.
     * @return The wordtype set's Enumerable instance.
     */
    public Enumerable<String> getWordTypeEnumInstance() {
        if(isEmbellishmentOutdated) {
            String[] wordTypeArray = this.wordTypes.toArray(new String[0]);
            return (this.wordTypeEnumInstance = Enumerable.fromStrings(wordTypeArray));
        } else {
            return this.wordTypeEnumInstance;
        }
    }
    
    // your normal getters.
    public Map<String, Integer> getInitializationFrequency() {
        return this.initializationFrequency;
    }
    public Map<String, Map<String, Integer>> getTransitionFrequency() {
        return this.transitionFrequency;
    }
    public Map<String, Map<String, Integer>> getProjectionFrequency() {
        return this.projectionFrequency;
    }
    
    // getters, but these getters return normal arrays.
    public int[] getInitializationFrequencyAsArray() {
        this.embellish();
        int[] res = new int[this.initializationFrequency.size()];
        Enumerable<String> instance = this.getWordTypeEnumInstance();
        this.initializationFrequency.forEach((string, count) -> {
            res[instance.toInt(string)] = count;
        });
        return res;
    }
    public int[][] getTransitionFrequencyAsArray() {
        this.embellish();
        Enumerable<String> instance = this.getWordTypeEnumInstance();
        int[][] res = new int[instance.size()][instance.size()];
        this.transitionFrequency.forEach((prev, map) -> {
            map.forEach((next, count) -> {
                res[instance.toInt(prev)][instance.toInt(next)] = count;
            });
        });
        return res;
    }
    public int[][] getProjetionFrequencyAsArray() {
        this.embellish();
        Enumerable<String> wordInstance = this.getWordEnumInstance(),
                           wordTypeInstance = this.getWordTypeEnumInstance();
        int[][] res = new int[wordTypeInstance.size()][wordInstance.size()];
        this.projectionFrequency.forEach((type, map) -> {
            System.out.println("Currently generating for type " + type);
            map.forEach((word, count) -> {
                res[wordTypeInstance.toInt(type)][wordInstance.toInt(word)] = count;
            });
        });
        return res;
    }
    
    // debugging facilities.
    public void displayCurrentWordSet() {
        this.words.forEach((str) -> {
            System.out.println("=> " + str);
        });
    }
    public void displayCurrentWordTypeSet() {
        this.wordTypes.forEach((str) -> {
            System.out.println("-> " + str);
        });
    }
    public void displayCurrentInitializationFrequency() {
        /*
        this.initializationFrequency.forEach((str, integer) -> {
            System.out.print(str + ":\t" + integer + " ");
        });
        System.out.println();
        */
        int[] array = this.getInitializationFrequencyAsArray();
        for(int a : array) {
            System.out.print(a + "\t");
        }
        System.out.println();
    }
    public void displayCurrentTransitionFrequency() {
        this.transitionFrequency.forEach((prev, map) -> {
            System.out.print(prev + ": \t");
            map.forEach((now, count) -> {
                System.out.print(now + ": " + count + " \t");
            });
            System.out.println();
        });
    }
    public void displayCurrentProjectionFrequency() {
        this.projectionFrequency.forEach((source, map) -> {
            System.out.print(source + ": \t");
            map.forEach((target, count) -> {
                System.out.print(target + ": " + count + " \t");
            });
            System.out.println();
        });
    }
    
    /**
     * Generate an HMM from what have learned so far.
     * @return The result HMM.
     */
    public HMM<String, String> generateHMM() {
        this.embellish();
        HMM<String, String> hmm = new HMM<>(
                this.getWordEnumInstance(),
                this.getWordTypeEnumInstance()
        );
        hmm.setInitializationMatrixFromFrequency(this.getInitializationFrequencyAsArray());
        hmm.setTransitionMatrixFromFrequency(this.getTransitionFrequencyAsArray());
        hmm.setProjectionMatrixFromFrequency(this.getProjetionFrequencyAsArray());
        return hmm;
    }
    
    public static void main(String[] args) throws IOException {
        Scanner stdin = new Scanner(System.in);
        String in = "this/AT is/BEZ a/AT test/N";
        String in2 = "bear/N is/BEZ an/AT animal/N";
        Scanner inScanner = new Scanner(in);
        CorpusReader cp = new CorpusReader();
        cp.learnFrom("this/AT is/BEZ a/AT test/N ./PUNCT\r\n\r\nbear/N is/BEZ an/AT animal/N ./PUNCT\r\n[best/ADJ test/N]/NT is/BEZ a/AT test/N ./PUNCT");
        Enumerable<String> wordInstance = cp.getWordEnumInstance();
        Enumerable<String> wordTypeInstance = cp.getWordTypeEnumInstance();
        System.out.println(wordInstance);
        System.out.println(wordTypeInstance);
        cp.displayCurrentProjectionFrequency();
        cp.displayCurrentTransitionFrequency();
        cp.displayCurrentInitializationFrequency();
        
        HMM<String, String> hmm = cp.generateHMM();
        hmm.displayProjectionMatrix();
        hmm.displayTransitionMatrix();
    }
}
