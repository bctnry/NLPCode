import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dictionary {
    public static Dictionary fromFile(String dictFileName) throws FileNotFoundException {
        Dictionary result;
        if (dictFileName == null) {
            throw new FileNotFoundException();
        }
        result = new Dictionary();
        File dictFile = new File(dictFileName);
        Scanner dictFileReader = new Scanner(dictFile);
        result.clear();
        dictFileReader.forEachRemaining((String s) -> result.addWord(new Word(s)));
        return result;
    }

    private int maximumWordLength;
    private Map<Integer, Set<Word>> dictionary;
    public Dictionary() {
        dictionary = new HashMap<>();
    }

    public int getMaximumWordLength() {
        return maximumWordLength;
    }
    public void addWord(Word w) {
        int wordLength = w.getWord().length();
        if(this.maximumWordLength < wordLength) maximumWordLength = wordLength;
        if(!dictionary.containsKey(wordLength)){
            dictionary.put(wordLength, new HashSet<>());
        }
        dictionary.get(w.getWord().length()).add(w);

    }
    public boolean hasWord(String w) {
        if(!this.dictionary.containsKey(w.length())) return false;
        Set<Word> wordSet = dictionary.get(w.length());
        for(Word word : wordSet) {
            if(word.getWord().contentEquals(w)) return true;
        }
        return false;
    }
    public void clear() {
        dictionary.clear();
    }
}
