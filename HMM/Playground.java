
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bctnry
 */
public class Playground {
    public static void main (String[] args) {
        Scanner stdin = new Scanner(System.in);
        System.out.println(stdin.delimiter());
        String str = stdin.delimiter().toString();
        stdin.useDelimiter("]");
        String strx = stdin.next();
        stdin.useDelimiter(str);
        System.out.println(strx);
    }
}
