/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceparser;

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
    
}
