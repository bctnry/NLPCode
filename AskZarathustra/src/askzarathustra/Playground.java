/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package askzarathustra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 *
 * @author bctnry
 */
public class Playground {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File input = new File("/home/bctnry/AskNietzsche/thus-spake-zarathustra/part-5-t.txt");
        int x = 1;
        BufferedReader inputr = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
        String inl;
        String output = "/home/bctnry/AskNietzsche/thus-spake-zarathustra/sec-";
        File outputFile = null;
        PrintWriter pw = null;
        while((inl = inputr.readLine()) != null) {
            if(inl.matches("^[IVXL]*\\.[ ]*[A-Z'.]*.*")) {
                if(pw != null) { pw.flush(); pw.close(); }
                x++;
                System.out.println("\"./part-5/sec-" + x + ".txt\",");
                outputFile = new File(output + x + ".txt");
                pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
                pw.println(inl);
            } else {
                pw.println(inl);
            }
        }
        if(pw != null) pw.flush();
        System.out.println("I. ZARATHUSTRA'S DISCLOSURE.".matches("^[IXV]*\\.[ ]*[A-Z'.]*.*"));
    }
}
