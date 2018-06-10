/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spellcheck;

import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author bctnry
 */
public class ExtStringReader extends Reader {
    private String body;
    private int mark, pointer;
    
    public ExtStringReader(String str) {
        this.body = str;
        this.mark = mark;
        this.pointer = pointer;
    }
    
    public void markCurrentPosition() {
        this.mark = pointer;
    }
    public void rollback() {
        this.pointer = mark;
    }
    
    @Override
    public int read(char[] chars, int i, int i1) throws IOException {
        // i - offset. i1 - len.
        for(int x = 0; x < i1; x++) {
            if(x >= this.body.length()) return -1;
            chars[x + i] = this.body.charAt(i);
        }
        return i1;
    }
    
    public void close() throws IOException {
        // do nothing.
    }
    
}
