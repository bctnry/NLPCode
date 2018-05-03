
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bctnry
 */
public class TestMain2 {
    public static void main(String[] args) throws IOException {
        Scanner stdin = new Scanner(System.in);
        //System.out.print("input the filename for the corpus: ");
        //String corpusFilename = stdin.nextLine();
        String corpusFilename = "/home/bctnry/Downloads/199801.txt.out.out";
        Scanner corpusFile = new Scanner(new File(corpusFilename));
        CorpusReader cp = new CorpusReader();
        while(corpusFile.hasNextLine()) {
            cp.learnLineFrom(corpusFile.nextLine());
        }
        cp.embellish();

        HMM<String, String> hmm = new HMM<>(
                cp.getWordEnumInstance(),
                cp.getWordTypeEnumInstance()
        );
        hmm.setInitializationMatrixFromFrequency(cp.getInitializationFrequencyAsArray());
        hmm.setProjectionMatrixFromFrequency(cp.getProjetionFrequencyAsArray());
        hmm.setTransitionMatrixFromFrequency(cp.getTransitionFrequencyAsArray());
        List<String> query = new ArrayList<>();
        String test = "这 只是 一个 测试";
        for(String x : test.split(" ")) {
                query.add(x.trim());
        }
        hmm.mostProbableHiddenSequenceOf(query).forEach((type) -> {
            System.out.println(" => " + type);
        });


        /*
        System.out.print("input the prefix filename for the hmm: ");
        String prefix = stdin.nextLine();

        System.out.print("Generating .words.txt...");
        FileWriter wordSet = new FileWriter(prefix + ".words.txt");
        StringBuilder wordSetStringBuilder = new StringBuilder();
        for(String word : cp.getWordEnumInstance().names()) {
            wordSetStringBuilder.append(word).append('\n');
        }
        wordSet.write(wordSetStringBuilder.toString());
        System.out.println("done.");

        System.out.print("Generating .wordtypes.txt...");
        FileWriter wordTypeSet = new FileWriter(prefix + ".wordtypes.txt");
        StringBuilder wordTypeSetStringBuilder = new StringBuilder();
        for(String word : cp.getWordTypeEnumInstance().names()) {
            wordTypeSetStringBuilder.append(word).append('\n');
        }
        wordTypeSet.write(wordTypeSetStringBuilder.toString());
        System.out.println("done.");

        System.out.println("Generating .init.txt...");
        FileWriter initializationMatrix = new FileWriter(prefix + ".init.txt");
        initializationMatrix.write(ToCSV.fromDouble1D(hmm.getInitializationMatrix()));
        System.out.println("done.");

        System.out.println("Generating .tran.txt...");
        FileWriter transitionMatrix = new FileWriter(prefix + ".tran.txt");
        transitionMatrix.write(ToCSV.fromDouble2D(hmm.getTransitionMatrix()));
        System.out.println("done.");

        System.out.println("Generating .proj.txt...");
        FileWriter projectionMatrix = new FileWriter(prefix + ".proj.txt");
        projectionMatrix.write(ToCSV.fromDouble2D(hmm.getProjectionMatrix()));
        System.out.println("done.");
        */
    }
}
