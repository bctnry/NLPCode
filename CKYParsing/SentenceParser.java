package sentenceparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * A front end for berkley parser.
 * @author bctnry
 */
public class SentenceParser {
    
    public static ParseTree CKY(PCFG grammar, final List<Token> tokenizedString) {
        ParseTree res = new ParseTree();
        Set<Token> nonterminalSet = grammar.getNonterminal();
        // setup the score array.
        Map<Pair3<Integer, Integer, Token>, Rule> score = new HashMap<>();
        Map<Pair3<Integer, Integer, Token>, Pair<Integer, Integer>[]> back = new HashMap<>();
        
        // phase 1.
        for(int i = 0; i < tokenizedString.size(); i++) {
            // phase 1.1.
            for(Token lhs : nonterminalSet) {
                Token[] rhs = new Token[1]; rhs[0] = tokenizedString.get(i);
                Rule rulePointer = new Rule(lhs, rhs);
                if(grammar.hasRule(rulePointer)) {
                    Pair3<Integer, Integer, Token> pointer = new Pair3<>(i, i+1, lhs);
                    score.putIfAbsent(pointer, null);
                    score.put(pointer, grammar.getRule(rulePointer));
                    back.putIfAbsent(pointer, null);
                    // this is the stub of the parse tree so there's no back pointer
                    // pointing elsewhere.
                }
            }
            
            // phase 1.2.
            // this is for adding in the unary rules, and it's (almost) exactly the
            // same code as in phase 2.2.
            
            // for any rules in the form of lhs -> rhs:
            for(Token lhs : nonterminalSet) {
                for(Token rhs : nonterminalSet) {
                    Pair3<Integer, Integer, Token> scorePointer = new Pair3<>(i, i+1, rhs);
                    // if the rhs is considered
                    if(score.get(scorePointer) != null) {
                        Token[] rhsp = new Token[1]; rhsp[0] = rhs;
                        Rule rulePointer = new Rule(lhs, rhsp);
                        // if the rule exist
                        if(grammar.hasRule(rulePointer)) {
                            Rule prevRule = grammar.getRule(rulePointer);
                            // a new rule object is required to prevent modifying the "original"
                            // rules stored in the grammar when updating the probability.
                            Rule newRule = new Rule(prevRule.getLHS(), prevRule.getRHS(), prevRule.getProbability());
                            Pair3<Integer, Integer, Token> scorePointer1 = new Pair3<>(i, i+1, lhs);
                            double newProb = score.get(scorePointer).getProbability()
                                           * newRule.getProbability();
                            // if the lhs is not considered, or the current probability
                            // of lhs is smaller than the new probability
                            if(score.get(scorePointer1) == null
                                    || newProb > score.get(scorePointer1).getProbability()) {
                                // modify the score array.
                                newRule.setProbability(newProb);
                                score.putIfAbsent(scorePointer1, null);
                                score.put(scorePointer1, newRule);
                                back.putIfAbsent(scorePointer1, new Pair[1]);
                                back.get(scorePointer1)[0] = new Pair<>(i, i+1);
                            }
                        }
                    }
                }
            }
        }
        
        // this is absolutely terrible...
        // phase 2.
        for(int span = 2; span <= tokenizedString.size(); span++) {
            for(int begin = 0; begin <= tokenizedString.size() - span; begin++) {
                int end = begin + span;
                // check for any possible of combinations, namely the combinations
                // of subtree str(begin,split) and str(split,end).
                for(int split = begin + 1; split <= end - 1; split++) {
                    // phase 2.1.
                    // check for all rules that could have lead to this combination.
                    for(Token A : nonterminalSet) {
                        for(Token B : nonterminalSet) {
                            for(Token C : nonterminalSet) {
                                Token[] ruleprhs = {B, C};
                                Rule rule = grammar.getRule(new Rule(A, ruleprhs));
                                // if there ain't such a rule then leave.
                                if(rule != null) {
                                    Pair3<Integer, Integer, Token>
                                            leftPointer = new Pair3<>(begin, split, B),
                                            rightPointer = new Pair3<>(split, end, C),
                                            rootPointer = new Pair3<>(begin, end, A);
                                    // if it's not possible to derive the two subtree then leave.
                                    if(score.get(leftPointer) != null && score.get(rightPointer) != null) {
                                        double newProb =
                                                score.get(leftPointer).getProbability()
                                                * score.get(rightPointer).getProbability()
                                                * rule.getProbability();
                                        // if it's possible to combine such subtrees but such combination
                                        // hasn't been checked yet, or if the new combination's probability
                                        // is greater than the old combination, then modify the combination.
                                        if(score.get(rootPointer) == null
                                                || newProb > score.get(rootPointer).getProbability()) {
                                            Rule modifiedRule = new Rule(rule.getLHS(), rule.getRHS(), newProb);
                                            score.putIfAbsent(rootPointer, null);
                                            score.put(rootPointer, modifiedRule);
                                            back.putIfAbsent(rootPointer, new Pair[2]);
                                            // no... this is absolutely terrible.
                                            back.get(rootPointer)[0] = new Pair<>(begin, split);
                                            back.get(rootPointer)[1] = new Pair<>(split, end);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    
                    // phase 2.2.
                    for(Token lhs : nonterminalSet) {
                        for(Token rhs : nonterminalSet) {
                            Pair3<Integer, Integer, Token> scorePointer = new Pair3<>(begin, end, rhs);
                            if(score.get(scorePointer) != null) {
                                Token[] rhsp = new Token[1]; rhsp[0] = rhs;
                                Rule rulePointer = new Rule(lhs, rhsp);
                                if(grammar.hasRule(rulePointer)) {
                                    Rule prevRule = grammar.getRule(rulePointer);
                                    Rule newRule = new Rule(prevRule.getLHS(), prevRule.getRHS(), prevRule.getProbability());
                                    Pair3<Integer, Integer, Token> scorePointer1 = new Pair3<>(begin, end, lhs);
                                    double newProb = score.get(scorePointer).getProbability()
                                                   * newRule.getProbability();
                                    if(score.get(scorePointer1) == null
                                            || newProb > score.get(scorePointer1).getProbability()) {
                                        newRule.setProbability(newProb);
                                        score.putIfAbsent(scorePointer1, null);
                                        score.put(scorePointer1, newRule);
                                        back.putIfAbsent(scorePointer1, new Pair[1]);
                                        back.get(scorePointer1)[0] = new Pair<>(begin, begin+1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        
        // building the tree from score[0][tokenizedString.size()];
        return buildTree(nonterminalSet, score, back, tokenizedString);
    }

    private static ParseTree buildTree(
            Set<Token> nonterminalTokenSet,
            Map<Pair3<Integer, Integer, Token>, Rule> score,
            Map<Pair3<Integer, Integer, Token>, Pair<Integer, Integer>[]> back,
            List<Token> tokenizedString
    ) {
        // this is expected to be a resource hog for not using back pointer but
        // plain recursion.
        return buildTreeHelper(
                new Token("S", false),
                score,
                back,
                0,
                tokenizedString.size(),
                tokenizedString
        );
    }

    private static ParseTree buildTreeHelper(
            Token requirement,
            Map<Pair3<Integer, Integer, Token>, Rule> score,
            Map<Pair3<Integer, Integer, Token>, Pair<Integer, Integer>[]> back,
            int begin,
            int end,
            List<Token> tokenizedString
    ) {
        ParseTree res = new ParseTree();
        Pair3<Integer, Integer, Token> pointer = new Pair3<>(begin, end, requirement);
        System.out.println("building tree for token " + requirement + " at " + begin + " " + end);
        Rule rule = score.get(pointer);
        System.out.println("rule: " + rule);
        Pair<Integer,Integer>[] childs = back.get(pointer);
        res.setRoot(rule.getLHS());
        if(childs == null) {
            ParseTree wordNode = new ParseTree();
            wordNode.setRoot(rule.getRHS()[0]);
            wordNode.setChild(null);
            res.addChild(wordNode);
        } else {
            for(int i = 0; i < childs.length; i++){
                Pair<Integer,Integer> child = childs[i];
                Token expectation = rule.getRHS()[i];
                System.out.println("child " + child.getFirst() + " " + child.getSecond());
                System.out.println("expectation " + expectation);
                res.addChild(buildTreeHelper(expectation, score, back, child.getFirst(), child.getSecond(), tokenizedString));
            }
        }
        return res;
    }
    
    
        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        // tokens.
        
        Token S = new Token("S", false);
        Token VP = new Token("VP", false);
        Token atVP_V = new Token("@VP_V", false);
        Token NP = new Token("NP", false);
        Token P = new Token("P", false);
        Token PP = new Token("PP", false);
        Token N = new Token("N", false);
        Token V = new Token("V", false);
        Token people = new Token("people", true);
        Token fish = new Token("fish", true);
        Token tanks = new Token("tanks", true);
        Token roads = new Token("roads", true);
        Token with = new Token("with", true);
        
        /*
        // rules.
        Rule[] rules = new Rule[19];
        Token[] rhs1 = {NP, VP}; rules[0] = new Rule(S, rhs1, 0.9);
        Token[] rhs2 = {VP}; rules[1] = new Rule(S, rhs2, 0.1);
        Token[] rhs3 = {V, NP}; rules[2] = new Rule(VP, rhs3, 0.5);
        Token[] rhs4 = {V}; rules[3] = new Rule(VP, rhs4, 0.1);
        Token[] rhs5 = {V, atVP_V}; rules[4] = new Rule(VP, rhs5, 0.3);
        Token[] rhs6 = {V, PP}; rules[5] = new Rule(VP, rhs6, 0.1);
        Token[] rhs7 = {NP, PP}; rules[6] = new Rule(atVP_V, rhs7, 1.0);
        Token[] rhs8 = {NP, NP}; rules[7] = new Rule(NP, rhs8, 0.1);
        Token[] rhs9 = {NP, PP}; rules[8] = new Rule(NP, rhs9, 0.2);
        Token[] rhs10 = {N}; rules[9] = new Rule(NP, rhs10, 0.7);
        Token[] rhs11 = {P, NP}; rules[10] = new Rule(PP, rhs11, 1.0);
        Token[] rhs12 = {people}; rules[11] = new Rule(N, rhs12, 0.5);
        Token[] rhs13 = {fish}; rules[12] = new Rule(N, rhs13, 0.2);
        Token[] rhs14 = {tanks}; rules[13] = new Rule(N, rhs14, 0.2);
        Token[] rhs15 = {roads}; rules[14] = new Rule(N, rhs15, 0.1);
        Token[] rhs16 = {people}; rules[15] = new Rule(V, rhs16, 0.1);
        Token[] rhs17 = {fish}; rules[16] = new Rule(V, rhs17, 0.6);
        Token[] rhs18 = {tanks}; rules[17] = new Rule(V, rhs18, 0.3);
        Token[] rhs19 = {with}; rules[18] = new Rule(V, rhs19, 1.0);
        
        PCFG grammar = new PCFG();
        for(Rule rule : rules) {
            grammar.addRule(rule);
        }
        /**/
        Scanner stdin = new Scanner(System.in);
        System.out.print("please input grammar file name. ");
        String grammarFilename = stdin.nextLine();
        FileInputStream source = new FileInputStream(grammarFilename.trim());
        PCFG grammar = PCFG.readFrom(source);
        /**/
        grammar.display();
        System.out.print("please input your sentence. all words begin with lowercase letters. seperated with space.\n");
        String[] prevTokens = stdin.nextLine().trim().split(" ");
        List<Token> tokenizedString = new ArrayList<>();
        for(String token : prevTokens) {
            tokenizedString.add(new Token(token, Token.isTerminal(token)));
        }
        
        ParseTree res = SentenceParser.CKY(grammar, tokenizedString);
        ParseTree.display(res, "");
    }
    
}
