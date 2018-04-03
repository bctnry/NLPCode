
// consist of a single word.
public class Word {
    private String word;
    private String[] wordClass;

    public String getWord() {
        return word;
    }
    public String[] getWordClass() {
        return wordClass;
    }
    public Word(String word, String[] wordClassStrings) {
        this.word = word;
        this.wordClass = wordClassStrings;
    }
    public Word(String commaSeperatedString) {
        String[] strings = commaSeperatedString.split(",");
        this.wordClass = new String[strings.length - 1];
        for(int i = 1; i < strings.length; i++) {
            this.wordClass[i - 1] = strings[i];
        }
        this.word = strings[0];
    }
}
