
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * @author bctnry
 */
public class ToRegularCorpus {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        Scanner stdin = new Scanner(System.in);
        System.out.print("input corpus filepath: ");
        String corpusFilename = stdin.nextLine();
        Scanner corpusFile = new Scanner(new File(corpusFilename));
        System.out.println("The regular corpus will be at " + corpusFilename + ".out");
        FileWriter outputFile = new FileWriter(corpusFilename + ".out");
        List<String> afterList = new ArrayList<>();
        while(corpusFile.hasNextLine()) {
            String line = corpusFile.nextLine();
            StringBuilder lineStringBuilder = new StringBuilder();
            Scanner lineScanner = new Scanner(line.trim());
            if(lineScanner.hasNext()) lineScanner.next();
            while(lineScanner.hasNext()) {
                // clear the first component.
                String word = lineScanner.next();
                if(word.charAt(0) == '[') {
                    String delimiter = lineScanner.delimiter().toString();
                    lineScanner.useDelimiter("]");
                    String rest = lineScanner.next();
                    lineScanner.useDelimiter(delimiter);
                    String after = word.substring(1).concat(rest);
                    afterList.add(after);
                    String preWholeWord = word.substring(1).concat(rest);
                    StringBuilder wholeWord = new StringBuilder();
                    for(String component : preWholeWord.split(" ")) {
                        wholeWord.append(component.split("/")[0]);
                    }
                    String type = lineScanner.next().substring(1);
                    lineStringBuilder.append(wholeWord.toString()).append('/').append(type).append(' ');
                } else {
                    lineStringBuilder.append(word).append(' ');
                }
            }
            outputFile.write(lineStringBuilder.toString());
            outputFile.write("\n");
        }
        afterList.forEach((string) -> {
            try {
                outputFile.write(string);
                outputFile.write("\n");
            } catch (IOException ex) {
                Logger.getLogger(ToRegularCorpus.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
