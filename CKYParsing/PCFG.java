/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * PCFG.
 * @author bctnry
 */
public class PCFG {
    private final Map<Token, Set<Rule>> grammar;
    public PCFG() {
        grammar = new HashMap<>();
    }
    public void addRule(Rule rule) {
        grammar.putIfAbsent(rule.getLHS(), new HashSet<>());
        grammar.get(rule.getLHS()).add(rule);
        isNonterminalSetGenerated = false;
    }
    public void addRule(Token lhs, Token[] rhs, double prob) {
        grammar.putIfAbsent(lhs, new HashSet<>());
        grammar.get(lhs).add(new Rule(lhs, rhs, prob));
        // stuff that needs to be generated again.
        isNonterminalSetGenerated = false;
    }
    public Set<Rule> derive(Token lhs) {
        return grammar.get(lhs);
    }
    
    // aux methods.
    private Set<Token> nonterminalSet;
    private boolean isNonterminalSetGenerated = false;
    public Set<Token> getNonterminal() {
        if(!isNonterminalSetGenerated) {
            nonterminalSet = new HashSet<>();
            this.grammar.forEach((token, rhs) -> {
                if(!token.isTerminal()) {
                    nonterminalSet.add(token);
                }
            });
        }
        return this.nonterminalSet;
    }

    Rule getRule(Rule rulePointer) {
        Rule[] result = {null};
        this.grammar.get(rulePointer.getLHS()).forEach((containedRule) -> {
            Token[] containedRuleRHS = containedRule.getRHS();
            Token[] queriedRuleRHS = rulePointer.getRHS();
            if(containedRuleRHS.length == queriedRuleRHS.length) {
                boolean ruleMismatch = false;
                int i = 0; for(; i < containedRuleRHS.length && i < queriedRuleRHS.length; i++) {
                    if(!Token.equal(containedRuleRHS[i], queriedRuleRHS[i])) {
                        ruleMismatch = true;
                        break;
                    }
                }
                if(!ruleMismatch) {
                    result[0] = containedRule;
                }
            }
        });
        return result[0];
    }

    boolean hasRule(Rule rulePointer) {
        boolean[] res = {false};
        this.grammar.get(rulePointer.getLHS()).forEach((containedRule) -> {
            Token[] containedRuleRHS = containedRule.getRHS();
            Token[] queriedRuleRHS = rulePointer.getRHS();
            if(containedRuleRHS.length == queriedRuleRHS.length) {
                boolean ruleMismatch = false;
                int i = 0; for(; i < containedRuleRHS.length && i < queriedRuleRHS.length; i++) {
                    if(!Token.equal(containedRuleRHS[i], queriedRuleRHS[i])) {
                        ruleMismatch = true;
                        break;
                    }
                }
                if(!ruleMismatch) {
                    res[0] = true;
                }
            }
        });
        return res[0];
    }
    
    /**
     * Generate a grammar from specified source.
     *      Grammar ::= Rule | Rule Grammar
     *      Rule    ::= Nonterminal space rightarrow space ID+ space Number
     *      ID      ::= Nonterminal | Terminal
     *      Nonterminal ::= [@A-Z][a-zA-Z0-9_]*
     *      Terminal ::= [^@A-Z][a-zA-Z0-9_]*
     * @param source
     * @return 
     */
    public static PCFG readFrom(InputStream source) throws IOException {
        BufferedReader sourceReader = new BufferedReader(new InputStreamReader(source));
        boolean sourceValid = true;
        PCFG res = new PCFG();
        String inl;
        while((inl = sourceReader.readLine()) != null) {
            inl = inl.trim();
            if("".contentEquals(inl)) {
                continue;
            }
            String[] prevTokens = inl.split(" ");
            if(prevTokens.length < 4) {
                // that must not be the case.
                System.err.println("Wrong format. Leaving.");
                sourceValid = false;
                break;
            }
            String lhs = prevTokens[0].trim();
            double prob = Double.parseDouble(prevTokens[prevTokens.length - 1].trim());
            Token lhstoken = new Token(
                    lhs,
                    Token.isTerminal(lhs)
            );
            Token[] rhstokens = new Token[prevTokens.length - 3];
            for(int i = 0; i < rhstokens.length; i++) {
                String token = prevTokens[i + 2].trim();
                rhstokens[i] = new Token(token, Token.isTerminal(token));
            }
            res.addRule(lhstoken, rhstokens, prob);
        }
        return sourceValid? res : null;
    }
    public void display() {
        grammar.forEach((token, ruleset) -> {
            System.out.println("Rules for token " + token + ":");
            ruleset.forEach((rule) -> {
                System.out.println("    " + rule);
            });
        });
    }
}
