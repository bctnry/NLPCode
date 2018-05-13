package sentenceparser;

/**
 *
 * @author bctnry
 */
public class Rule {
    private Token lhs;
    private Token[] rhs;
    private double prob;
    public Rule(Token lhs, Token[] rhs) {
        this(lhs, rhs, 1);
    }
    public Rule(Token lhs, Token[] rhs, double prob) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.prob = prob;
    }
    public Token getLHS() {
        return this.lhs;
    }
    public Token[] getRHS() {
        return this.rhs;
    }
    public double getProbability() {
        return this.prob;
    }

    void setProbability(double newProb) {
        this.prob = newProb;
    }
    
    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append(lhs.toString()).append(" -> ");
        for(Token token : rhs) {
            res.append(token.toString()).append(' ');
        }
        res.append(prob);
        return res.toString();
    }
}
