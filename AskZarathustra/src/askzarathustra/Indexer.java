/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package askzarathustra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author bctnry
 */
public class Indexer {
    private static String[] DOUBLE_MATCHING_RULE = {
        "\'s", "\'ve", "\'", "\'d",
        "’s", "’ve", "’", "’d",
    };
    private static Word readWord(int pivot, BufferedReader reader) throws IOException {
        StringBuilder resultStr = new StringBuilder();
        resultStr.append((char)Character.toLowerCase(pivot));
        reader.mark(16);
        while((pivot = reader.read()) != -1
                && pivot != '"' && pivot != '!' && pivot != '?' && pivot != ',' && pivot != '.'
                && pivot != '-' && !Character.isWhitespace(pivot)) {
            resultStr.append((char)Character.toLowerCase(pivot));
            reader.mark(16);
        }
        reader.reset();
        String resultStrx = resultStr.toString();
        Word resultWord = new Word(resultStr.toString());
        // check doublematching: words like Zarathustra's should match both
        // zarathustra and zarathustra's.
        // assuming that these patterns only occur once.
        for(int i = 0; i < DOUBLE_MATCHING_RULE.length; i++) {
            if(resultStrx.matches(".*" + DOUBLE_MATCHING_RULE[i])) {
                resultWord.addMatching(resultStrx.replace(DOUBLE_MATCHING_RULE[i], ""));
                break;
            }
        }
        return resultWord;
    }
    public static Stream<Word> tokenize(BufferedReader reader) throws IOException {
        Stream.Builder<Word> resultBuilder = Stream.builder();
        int pivot;
        while((pivot = reader.read()) != -1) {
            if(Character.isAlphabetic(pivot)) {
                resultBuilder.accept(Indexer.readWord(pivot, reader));
            }
        }
        return resultBuilder.build();
    }
    public static Stream<Word> tokenize(File file) throws IOException {
        return tokenize(new BufferedReader(new InputStreamReader(new FileInputStream(file))));
    }
    public static Stream<Word> tokenize(String str) throws IOException {
        return tokenize(new BufferedReader(new StringReader(str)));
    }
    // Words ~ [Word]
    // TokenizedDocument ~ DocumentID * Words
    // Index ~ Document * Score
    // Indices ~ [Index]
    // Weight ~ Word * Indices
    public static Map<String, Integer> countWord(Stream<Word> file) {
        Map<String, Integer> result = new HashMap<>();
        file.forEach((word) -> {
            word.getMatching().forEach((str) -> {
                result.putIfAbsent(str, 0);
                result.put(str, result.get(str) + 1);
            });
        });
        return result;
    }
    public static Map<Integer, Map<String, Double>> calcWeight(Map<Integer,Stream<Word>> fileList) {
        Map<Integer, Map<String, Double>> result = new HashMap<>();
        Map<String, Integer> df = new HashMap<>();
        fileList.forEach((documentID, stream) -> {
            Map<String, Integer> currentFileWordCount = Indexer.countWord(stream);
            currentFileWordCount.forEach((str, integer) -> {
                result.putIfAbsent(documentID, new HashMap<>());
                result.get(documentID).putIfAbsent(str, 0.0);
                result.get(documentID).put(str, result.get(documentID).get(str) + integer);
                df.putIfAbsent(str, 0);
                df.put(str, df.get(str) + 1);
            });
            // now result stores tf(t,d).
            currentFileWordCount.forEach((str, wordcnt) -> {
                result.get(documentID).put(str,
                        1 + Math.log10(result.get(documentID).get(str))
                                * Math.log10(fileList.size() / ((double) df.get(str))));
            });
            // now result stores w(t,d).
        });
        return result;
    }
    public static Map<String, Double> lengthRegularizeSingle(Map<String, Double> vector) {
        double sum = vector.values().stream().reduce(0.0, (a, b) -> a + (b * b));
        vector.keySet().forEach((key) -> vector.put(key, vector.get(key) / sum));
        return vector;
    }
    public static Map<Integer, Map<String, Double>> lengthRegularize(Map<Integer, Map<String, Double>> documentMatrix) {
        documentMatrix.values().forEach((vector) -> Indexer.lengthRegularizeSingle(vector));
        return documentMatrix;
    }
    public static Map<Integer, Stream<Word>> asFileList(Stream<Word> tokenList) {
        Map<Integer, Stream<Word>> res = new HashMap<>();
        res.put(0, tokenList);
        return res;
    }
    public static Map<Integer, Stream<Word>> toFileList(List<File> fileList) {
        final int[] i = {0};
        Map<Integer, Stream<Word>> result = new HashMap<>();
        fileList.forEach((file) -> {
            try {
                result.putIfAbsent(i[0], Indexer.tokenize(file));
                i[0]++;
            } catch (IOException ex) {
                Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        return result;
    }
    public static void main (String[] args) throws IOException {
    }

    static String readFileToString(String fileName) throws FileNotFoundException, IOException {
        StringBuilder resBuilder = new StringBuilder();
        InputStreamReader r = new InputStreamReader(new FileInputStream(fileName));
        int pivot;
        while((pivot = r.read()) != -1) {
            resBuilder.append((char)pivot);
        }
        return resBuilder.toString();
    }
    static String caseInsensitiveWrapAll(String subj, String str, Function<String, String> wrapper) {
        // construct a case insensitive regexp.
        Pattern pattern = Pattern.compile("(?i)"+str);
        Matcher matcher = pattern.matcher(subj);
        StringBuilder resultBuilder = new StringBuilder();
        int lastEndIndex = 0;
        while(matcher.find()) {
            resultBuilder.append(subj.substring(lastEndIndex, matcher.start()));
            resultBuilder.append(wrapper.apply(matcher.group()));
            lastEndIndex = matcher.end();
        }
        resultBuilder.append(subj.substring(lastEndIndex));
        return resultBuilder.toString();
    }
}
