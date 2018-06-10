package spellcheck;

import java.util.Objects;

/**
 * Generic pair.
 * @author bctnry
 */
public class Pair<T0, T1> {
    private final T0 first;
    private final T1 second;
    public Pair(T0 first, T1 second) {
        this.first = first;
        this.second = second;
    }
    public T0 getFirst() {
        return this.first;
    }
    public T1 getSecond() {
        return this.second;
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Pair)) return false;
        Pair pair = (Pair)o;
        return this.first.equals(pair.getFirst())
                && this.second.equals(pair.getSecond());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.first);
        hash = 97 * hash + Objects.hashCode(this.second);
        return hash;
    }
}
