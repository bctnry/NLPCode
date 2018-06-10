/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellcheck;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author bctnry
 */
public class Preprocess {
    public static String readTillWhitespaces(BufferedReader reader) throws IOException {
        StringBuilder strbuilder = new StringBuilder();
        int pivot;
        while((pivot = reader.read()) != -1 && Character.isWhitespace(pivot));
        strbuilder.append((char)pivot);
        while((pivot = reader.read()) != -1 && !Character.isWhitespace(pivot)) {
            strbuilder.append((char)pivot);
        }
        return strbuilder.toString();
    }

    enum RuleType { PREFIX, SUFFIX; }
    static class HalfRule {
        public String matchRegexp;
        public String removeStr;
        public String affix;
        public HalfRule(){}
        public HalfRule(String removeStr, String affix, String matchRegexp) {
            this.removeStr = ".*" + removeStr + "$";
            this.affix = affix;
            this.matchRegexp = ".*" + matchRegexp + "$";
        }
        private void readRule(BufferedReader reader) throws IOException {
            readTillWhitespaces(reader); // type.
            readTillWhitespaces(reader); // name.
            this.removeStr = readTillWhitespaces(reader);
            this.removeStr = (this.removeStr.contentEquals("0")? "" : this.removeStr) + "$";
            this.affix = readTillWhitespaces(reader);
            this.matchRegexp = ".*" + readTillWhitespaces(reader) + "$";
        }
        @Override 
        public String toString() {
            return "HalfRule{" + this.removeStr + "," + this.affix + "," + this.matchRegexp + "}";
        }
    }
    static class Rule {
        private RuleType type;
        private String ruleName;
        private boolean isCombinable;
        private List<HalfRule> halfRuleList;
        public void readRule(BufferedReader reader) throws IOException {
            this.type = readTillWhitespaces(reader).contentEquals("PFX")? RuleType.PREFIX : RuleType.SUFFIX;
            this.ruleName = readTillWhitespaces(reader).trim();
            this.isCombinable = readTillWhitespaces(reader).trim().contentEquals("Y");
            this.halfRuleList = new ArrayList<HalfRule>();
            int n = Integer.parseInt(readTillWhitespaces(reader).trim());
            for(int i = 0; i < n; i++) {
                HalfRule halfrule = new HalfRule();
                halfrule.readRule(reader);
                this.halfRuleList.add(halfrule);
            }        
        }
        public String apply(String subject) {
            String[] subjectArray = {subject};
            return Arrays.asList(subjectArray).stream().flatMap((str) -> 
                this.halfRuleList.stream().map((halfrule) -> {
                    if(str.matches(halfrule.matchRegexp)) {
                        if(this.type == RuleType.PREFIX) {
                            return halfrule.affix + str;
                        } else {
                            return str.replace(halfrule.removeStr, "") + halfrule.affix;
                        }
                    } else { return ""; }
                })
            ).filter((str) -> !str.contentEquals("")).findFirst().orElse(subject);
        }
        @Override
        public String toString() {
            StringBuilder res = new StringBuilder();
            res.append("Rule{").append(this.ruleName).append(this.type == RuleType.PREFIX? "*-":"-*");
            this.halfRuleList.forEach((halfrule) -> {
                res.append("<").append(halfrule).append(">");
            });
            return res.toString();
        }
    }
    public static String fileToString(String filename) throws FileNotFoundException, IOException {
        StringBuilder res = new StringBuilder();
        Reader r = new InputStreamReader(new FileInputStream(new File(filename)));
        int pivot;
        while((pivot = r.read()) != -1) {
            res.append((char)pivot);
        }
        return res.toString();
    }
    public static List<String> apply(
            Map<String,Rule> P0, Map<String,Rule> P1, Map<String,Rule> S0, Map<String,Rule> S1,
            String str, String trans
    ) {
        List<String> res = new ArrayList<>();
        char[] transArray = trans.toCharArray();
        Set<String> prefixSet0 = new HashSet<>(), prefixSet1 = new HashSet<>(),
                suffixSet0 = new HashSet<>(), suffixSet1 = new HashSet<>();
        for(char t : transArray) {
            String tranT = String.valueOf(t);
            if(P0.containsKey(tranT)) prefixSet0.add(tranT);
            if(P1.containsKey(tranT)) prefixSet1.add(tranT);
            if(S0.containsKey(tranT)) suffixSet0.add(tranT);
            if(S1.containsKey(tranT)) suffixSet1.add(tranT);
        }
        prefixSet0.forEach((prefix0) -> {
            suffixSet0.forEach((suffix0) -> {
                res.add(P0.get(prefix0).apply(S0.get(suffix0).apply(str)));
            });
        });
        prefixSet0.forEach((prefix) -> {
            res.add(P0.get(prefix).apply(str));
        });
        suffixSet0.forEach((suffix) -> {
            res.add(S0.get(suffix).apply(str));
        });
        prefixSet1.forEach((prefix) -> {
            res.add(P1.get(prefix).apply(str));
        });
        suffixSet1.forEach((suffix) -> {
            res.add(S1.get(suffix).apply(str));
        });
        return res;
    }
    public static void main (String[] args) throws IOException {
        Map<String, Rule>
                P0 = new HashMap<>(),
                P1 = new HashMap<>(),
                S0 = new HashMap<>(),
                S1 = new HashMap<>();
        String ruleListFilename = "/home/bctnry/Correcting/trans_rules.txt";
        BufferedReader ruleReader = new BufferedReader(new StringReader(" " + Preprocess.fileToString(ruleListFilename)));
        int pivot;
        while((pivot = ruleReader.read())!= -1) {
            Rule newRule = new Rule();
            newRule.readRule(ruleReader);
            System.out.println(newRule);
            (newRule.type == RuleType.PREFIX?
                    (newRule.isCombinable? P0 : P1)
                    :(newRule.isCombinable? S0 : S1))
                    .putIfAbsent(newRule.ruleName, newRule);
        };
        String dictFilename = "/home/bctnry/Correcting/words.txt";
        String dictOutputFilename = dictFilename + ".out";
        BufferedReader wordReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(dictFilename))));
        PrintWriter outputWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(dictOutputFilename))));
        
        String wordLine;
        while((wordLine = wordReader.readLine()) != null) {
            String[] splitted = wordLine.split("/");
            if(splitted.length <= 1) {
                outputWriter.println(wordLine);
            } else {
                outputWriter.println(splitted[0]);
                Preprocess.apply(P0, P1, S0, S1, splitted[0], splitted[1]).forEach((str) -> {
                    outputWriter.println(str);
                });
                outputWriter.flush();
            }
        }
    }
}
