
package sentenceparser;

import java.util.Objects;

/**
 *
 * @author bctnry
 */
public class Token {

    static boolean equal(Token token0, Token token1) {
        return token0.toString().contentEquals(token1.toString())
                && token0.isTerminal() == token1.isTerminal();
    }
    private final boolean isTerminal;
    private final String name;
    public Token(String name, boolean isTerminal) {
        this.isTerminal = isTerminal;
        this.name = name;
    }
    public boolean isTerminal() {
        return this.isTerminal;
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Token)) return false;
        else return Token.equal(this, (Token)o);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.isTerminal ? 1 : 0);
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }
    @Override
    public String toString() {
        return this.name;
    }
}
