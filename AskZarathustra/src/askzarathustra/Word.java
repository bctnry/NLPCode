/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package askzarathustra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author bctnry
 */
public class Word {
    private String word;
    private Set<String> matching;
    
    public String getWord() {
        return word;
    }
    public Set<String> getMatching() {
        return matching;
    }
    public Word(String word) {
        this.word = word;
        this.matching = new HashSet<>();
        this.matching.add(word);
    }
    public void addMatching(String newMatching) {
        this.matching.add(newMatching);
    }
    public boolean match(String matchedWord) {
        return this.matching.contains(matchedWord);
    }
    @Override
    public String toString() {
        StringBuilder toStringResultBuilder = new StringBuilder();
        toStringResultBuilder.append("Word{").append(this.word).append(":");
        this.matching.forEach((str) -> {
            toStringResultBuilder.append('<').append(str).append('>');
        });
        toStringResultBuilder.append('}');
        return toStringResultBuilder.toString();
    }
    @Override
    public boolean equals(Object o) {
        if(o instanceof Word) {
            Word w2 = (Word) o;
            return w2.word.contentEquals(this.word);
        } else return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.word);
        hash = 53 * hash + Objects.hashCode(this.matching);
        return hash;
    }
}
