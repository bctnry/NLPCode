
package spellcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bctnry
 */
public class SpellCheck {
    public final static int INSERTION_COST = 1;
    public final static int REMOVAL_COST = 1;
    public final static int SUBSTITUTION_COST = 1;
    public final static int TRANSPOSITION_COST = 1;
    public static int levenshteinDistance(String a, String b) {
        int[][] resultMatrix = new int[a.length() + 1][b.length() + 1];
        for(int i = 0; i <= a.length(); i++) {
            resultMatrix[i][0] = i;
        }
        for(int i = 0; i <= b.length(); i++) {
            resultMatrix[0][i] = i;
        }
        for(int i = 1; i <= a.length(); i++) {
            for(int j = 1; j <= b.length(); j++) {
                if(a.charAt(i - 1) == b.charAt(j - 1)) {
                    resultMatrix[i][j] = resultMatrix[i - 1][j - 1];
                } else {
                    resultMatrix[i][j] =
                            Math.min(resultMatrix[i - 1][j] + INSERTION_COST,
                                    Math.min(resultMatrix[i][j - 1] + REMOVAL_COST,
                                            resultMatrix[i - 1][j - 1] + SUBSTITUTION_COST));
                    if(i > 1 && j > 1 && a.charAt(i-1) == b.charAt(j-2) && b.charAt(j-1) == a.charAt(i-2)) {
                        resultMatrix[i][j] = Math.min(
                                resultMatrix[i][j],
                                resultMatrix[i-2][j-2] + TRANSPOSITION_COST
                        );
                    }
                }
            }
        }
        return resultMatrix[a.length()][b.length()];
    }
    /**
     * The real readline.
     * @param inputReader
     * @return 
     */
    private static String readLine(BufferedReader inputReader) throws IOException {
        int pivot; StringBuilder resbuilder = new StringBuilder();
        boolean hasRead = false;
        while((pivot = inputReader.read()) != -1) {
            if(pivot == '\n') {
                break;
            } else {
                hasRead = true;
                resbuilder.append((char)pivot);
            }
        }
        return hasRead?resbuilder.toString():null;
    }
    
    
    private Set<String> dictionary;
    public SpellCheck() {
        this.dictionary = new HashSet<>();
    }
    public void initDictionary(String dictFileName) throws FileNotFoundException, IOException {
        this.initDictionary(new File(dictFileName));
    }
    /**
     * Load a dictionary.
     * The dictionary must be one word per line.
     * @param file 
     */
    public void initDictionary(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file))
        );
        String inl = null;
        while((inl = reader.readLine()) != null) {
            this.dictionary.add(inl);
        }
    }
    public boolean checkSingleWord(String word) {
        return this.dictionary.contains(word);
    }
    public List<String> nearestStrings(String str, int n) {
        List<SimpleEntry<String, Integer>> distanceList = new ArrayList<>(this.dictionary.size());
        this.dictionary.forEach((word) -> {
            distanceList.add(new SimpleEntry<>(word, SpellCheck.levenshteinDistance(word, str)));
        });
        SimpleEntry<String, Integer>[] distanceArray = distanceList.toArray(new SimpleEntry[0]);
        Arrays.sort(distanceArray, (Object t, Object t1) -> {
            SimpleEntry<String, Integer> tt = (SimpleEntry<String, Integer>) t;
            SimpleEntry<String, Integer> tt1 = (SimpleEntry<String, Integer>) t1;
            return Integer.compare(tt.getValue(), tt1.getValue());
        });
        String[] result = new String[n];
        for(int i = 0; i < n; i++) {
            result[i] = distanceArray[i].getKey();
        }
        return Arrays.asList(result);
    }
    
    private int row = 1, col = 1;
    private boolean useRecommendation;
    private int recommendationCount = 10;
    public List<Correction> check(InputStream input) throws IOException {
        List<Correction> result = new ArrayList<>();
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
        String inl;
        
        while((inl = SpellCheck.readLine(inputReader)) != null) {
            System.out.println(inl);
            StringReader lineReader = new StringReader(inl);
            lineReader.mark(16);
            int pivot;
            while((pivot = lineReader.read()) != -1) {
                StringBuilder strbuilder = new StringBuilder();
                lineReader.reset();
                while((pivot = lineReader.read()) != -1 && "\n\r \t`~'\"[]{}()*+-/,.<>?;:|!@#$%^&".contains(String.valueOf((char)pivot))) {
                    lineReader.mark(16);
                    this.col++;
                }
                lineReader.reset();
                while((pivot = lineReader.read()) != -1 && !"\n\r \t`~'\"[]{}()*+-/,.<>?;:|!@#$%^&".contains(String.valueOf((char)pivot))) {
                    strbuilder.append((char)pivot);
                    lineReader.mark(16);
                    this.col++;
                }
                lineReader.reset();
                String word = strbuilder.toString().trim();
                if(word.contentEquals(""))continue;
                else if(word.matches("^[0-9].*")) {
                    if(this.checkOrdinal(word));
                    else {
                        result.add(new Correction(row, col, null));
                    }
                } else {
                    if(this.checkSingleWord(word)
                            // this is to compensate cases like the word "Thus" in ".... Thus it will ...".
                            || this.checkSingleWord(word.replace(word.charAt(0), Character.toLowerCase(word.charAt(0)))));
                    else {
                        result.add(new Correction(
                                row, col - word.length(),
                                this.useRecommendation?this.nearestStrings(word, this.recommendationCount):null));
                    }
                }
                lineReader.mark(16);
            }
            row++; col = 1;
            
        }
        return result;
    }
    public void check(InputStream input, PrintStream error) throws IOException {
        this.check(input).forEach((correction) -> {
            error.print(correction.getRow() + " " + correction.getCol() + " ");
            correction.getCorrectionList().forEach((string) -> {
                error.print(string + " ");
            });
            error.println();
            error.flush();
        });
    }
    
    public static void main(String[] args) throws IOException {
        SpellCheck sc = new SpellCheck();
        // checking comamndline argument.
        sc.useRecommendation = true;
        if(args.length >= 2) {
            if(args[1].startsWith("-")) {
                String nstr = args[1].substring(1).trim();
                if(!nstr.contentEquals("")) sc.setRecommendationCount(Integer.parseInt(nstr));
            }
        }
        sc.initDictionary(args[0]);
        
        sc.check(System.in, System.err);
        
    }

    private void setRecommendationCount(int newRecommendationCount) {
        this.recommendationCount = newRecommendationCount;
    }

    private boolean checkOrdinal(String word) {
        return word.matches("[0-9]*1[0-9]th") || word.matches("[0-9]*[02-9](1st|2nd|3rd|[4-9]th)")
                || word.matches("(1st|2nd|3rd|[4-9]th)");
    }
    
    
}
