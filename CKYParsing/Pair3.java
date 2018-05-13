/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sentenceparser;

import java.util.Objects;

/**
 *
 * @author bctnry
 */
class Pair3<T0, T1, T2> {
    private final T0 first;
    private final T1 second;
    private final T2 third;
    public Pair3(T0 first, T1 second, T2 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
    public T0 getFirst() {
        return this.first;
    }
    public T1 getSecond() {
        return this.second;
    }
    public T2 getThird() {
        return this.third;
    }
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Pair3)) return false;
        Pair3 pair = (Pair3)o;
        return this.first.equals(pair.getFirst())
                && this.second.equals(pair.getSecond())
                && this.third.equals(pair.getThird());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.first);
        hash = 43 * hash + Objects.hashCode(this.second);
        hash = 43 * hash + Objects.hashCode(this.third);
        return hash;
    }
}
