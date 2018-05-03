
import java.util.ArrayList;
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
public class TestMain {
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        String[] visibleTokens = {"bear","is","move","on","president","progress","the","."};
        String[] hiddenTokens = {"AT","BEZ","IN","NN","VB","PERIOD"};
        HMM<String,String> hmm = new HMM<>(
                Enumerable.fromStrings(visibleTokens),
                Enumerable.fromStrings(hiddenTokens)
        );
        double[] initMatrix = {0.2, 0.1, 0.1, 0.2, 0.3, 0.1};
        // NOTE: needs fixing.
        int[][] transitionMatrix = {
            {0,0,0,48636,0,19},
            {1973,0,426,187,0,38},
            {43322,0,1325,17314,0,185},
            {1067,3720,42470,11773,614,21392},
            {6072,42,4758,1476,129,1522},
            {8016,75,4656,1329,954,0}
        };
        int[][] projectionMatrix = {
            {0,0,0,0,0,0,69016,0},
            {0,10065,0,0,0,0,0,0},
            {0,0,0,5484,0,0,0,0},
            {10,0,36,0,382,108,0,0},
            {43,0,133,0,0,4,0,0},
            {0,0,0,0,0,0,0,48809}
        };
        hmm.setInitializationMatrix(initMatrix);
        hmm.setProjectionMatrixFromFrequency(projectionMatrix);
        hmm.setTransitionMatrixFromFrequency(transitionMatrix);
        ArrayList<String> query = new ArrayList();
        query.add("the"); query.add("bear"); query.add("is"); query.add("on");
        query.add("the"); query.add("move");
        query.add(".");
        System.out.println(hmm.mostProbableHiddenSequenceOf(query));
    }
}
