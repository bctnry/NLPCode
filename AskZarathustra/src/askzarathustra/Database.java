/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package askzarathustra;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 * @author bctnry
 */
public class Database {
    private Map<Integer, Map<String, Double>> documentMatrix;
    private File[] fileArray;
    private String[] description;
    public Database(File[] fileArray, String[] description) {
        this.documentMatrix = Indexer.lengthRegularize(Indexer.calcWeight(Indexer.toFileList(Arrays.asList(fileArray))));
        this.fileArray = fileArray;
        this.description = description;
    }
    public String getFilenameOf(int documentID) {
        return this.fileArray[documentID].getAbsolutePath();
    }
    public String getDescriptionOf(int documentID) {
        return this.description[documentID];
    }
    public Map<Integer, Map<String, Double>> getDocumentMatrix() {
        return this.documentMatrix;
    }
    public Stream<Integer> ask(String query, int documentCnt) throws IOException {
        Map<String, Double> queryWeight
                = Indexer.calcWeight(Indexer.asFileList(Indexer.tokenize(query))).get(0);
        SimpleEntry<Integer, Double>[] preRes = this.documentMatrix.keySet().stream().map((documentID) -> {
            // the document.
            Map<String, Double> doc = this.documentMatrix.get(documentID);
            return new SimpleEntry<>(documentID, queryWeight.keySet().stream().map(
                    (word) -> (doc.containsKey(word)?doc.get(word):0) * queryWeight.get(word)
            ).reduce(0.0, (a, b) -> a + b));
        }).toArray(SimpleEntry[]::new);
        Arrays.sort(preRes, (Object t, Object t1) -> {
            SimpleEntry<Integer, Double>
                    tt = (SimpleEntry<Integer, Double>)t,
                    tt1 = (SimpleEntry<Integer, Double>)t1;
            return 1 - Double.compare(tt.getValue(), tt1.getValue());
        });
        Stream<SimpleEntry<Integer, Double>> preRes2 = Arrays.asList(preRes).stream().filter((entry -> entry.getValue() > 0.0));
        if(documentCnt > 0) preRes2 = preRes2.limit(documentCnt);
        return preRes2.map((pair) -> pair.getKey());
    }
}
