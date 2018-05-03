

import java.io.*;
import java.util.Scanner;

// convert default Windows-1252 to UTF-8
// to solve the coding issue.
public class ToUTF8 {
    public static void main (String[] args) throws IOException {
        Scanner stdin = new Scanner(System.in);
        System.out.print("input filename:");
        String inputFileName = stdin.nextLine();
        System.out.println("output filename will be " + inputFileName + ".out");
        String outputFileName = inputFileName.concat(".out");
        File inputFile = new File(inputFileName);
        BufferedReader fileInput = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(inputFileName),
                        "GBK"
                )
        );
        String temp;
        PrintWriter fileOutput = new PrintWriter(
                new OutputStreamWriter(
                        new FileOutputStream(outputFileName),
                        "UTF-8"
                )
        );
        while((temp = fileInput.readLine()) != null){
            fileOutput.println(temp);
        }
        System.out.println("done.");
    }
}
