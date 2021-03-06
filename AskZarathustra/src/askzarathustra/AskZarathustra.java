/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package askzarathustra;

import java.io.File;
import java.util.Arrays;

/**
 *
 * @author bctnry
 */
public class AskZarathustra {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainJFrame jf = new MainJFrame();
        jf.setVisible(true);
    }

    public static Database getDatabase() {
        String[] titleArray = {
            "Zarathustra's Prologue, sec. 1",
            "Zarathustra's Prologue, sec. 2",
            "Zarathustra's Prologue, sec. 3",
            "Zarathustra's Prologue, sec. 4",
            "Zarathustra's Prologue, sec. 5",
            "Zarathustra's Prologue, sec. 6",
            "Zarathustra's Prologue, sec. 7",
            "Zarathustra's Prologue, sec. 8",
            "Zarathustra's Prologue, sec. 9",
            "Zarathustra's Prologue, sec. 10",
            "Zarathustra's Disclosure, I. THE THREE METAMORPHOSES.",
"Zarathustra's Disclosure, II. THE ACADEMIC CHAIRS OF VIRTUE.",
"Zarathustra's Disclosure, III. BACKWORLDSMEN.",
"Zarathustra's Disclosure, IV. THE DESPISERS OF THE BODY.",
"Zarathustra's Disclosure, V. JOYS AND PASSIONS.",
"Zarathustra's Disclosure, VI. THE PALE CRIMINAL.",
"Zarathustra's Disclosure, VII. READING AND WRITING.",
"Zarathustra's Disclosure, VIII. THE TREE ON THE HILL.",
"Zarathustra's Disclosure, IX. THE PREACHERS OF DEATH.",
"Zarathustra's Disclosure, X. WAR AND WARRIORS.",
"Zarathustra's Disclosure, XI. THE NEW IDOL.",
"Zarathustra's Disclosure, XII. THE FLIES IN THE MARKET-PLACE.",
"Zarathustra's Disclosure, XIII. CHASTITY.",
"Zarathustra's Disclosure, XIV. THE FRIEND.",
"Zarathustra's Disclosure, XV. THE THOUSAND AND ONE GOALS.",
"Zarathustra's Disclosure, XVI. NEIGHBOUR-LOVE.",
"Zarathustra's Disclosure, XVII. THE WAY OF THE CREATING ONE.",
"Zarathustra's Disclosure, XVIII. OLD AND YOUNG WOMEN.",
"Zarathustra's Disclosure, XIX. THE BITE OF THE ADDER.",
"Zarathustra's Disclosure, XX. CHILD AND MARRIAGE.",
"Zarathustra's Disclosure, XXI. VOLUNTARY DEATH.",
"Zarathustra's Disclosure, XXII. THE BESTOWING VIRTUE.",
"Zarathustra's Disclosure, Part II Prologue",
"Zarathustra's Disclosure, XXIII. THE CHILD WITH THE MIRROR.",
"Zarathustra's Disclosure, XXIV. IN THE HAPPY ISLES.",
"Zarathustra's Disclosure, XXV. THE PITIFUL.",
"Zarathustra's Disclosure, XXVI. THE PRIESTS.",
"Zarathustra's Disclosure, XXVII. THE VIRTUOUS.",
"Zarathustra's Disclosure, XXVIII. THE RABBLE.",
"Zarathustra's Disclosure, XXIX. THE TARANTULAS.",
"Zarathustra's Disclosure, XXX. THE FAMOUS WISE ONES.",
"Zarathustra's Disclosure, XXXI. THE NIGHT-SONG.",
"Zarathustra's Disclosure, XXXII. THE DANCE-SONG.",
"Zarathustra's Disclosure, XXXIII. THE GRAVE-SONG.",
"Zarathustra's Disclosure, XXXIV. SELF-SURPASSING.",
"Zarathustra's Disclosure, XXXV. THE SUBLIME ONES.",
"Zarathustra's Disclosure, XXXVI. THE LAND OF CULTURE.",
"Zarathustra's Disclosure, XXXVII. IMMACULATE PERCEPTION.",
"Zarathustra's Disclosure, XXXVIII. SCHOLARS.",
"Zarathustra's Disclosure, XXXIX. POETS.",
"Zarathustra's Disclosure, XL. GREAT EVENTS.",
"Zarathustra's Disclosure, XLI. THE SOOTHSAYER.",
"Zarathustra's Disclosure, XLII. REDEMPTION.",
"Zarathustra's Disclosure, XLIII. MANLY PRUDENCE.",
"Zarathustra's Disclosure, XLIV. THE STILLEST HOUR.",
"Zarathustra's Disclosure, Part III Prologue",
"Zarathustra's Disclosure, XLV. THE WANDERER.",
"Zarathustra's Disclosure, XLVI. THE VISION AND THE ENIGMA.",
"Zarathustra's Disclosure, XLVII. INVOLUNTARY BLISS.",
"Zarathustra's Disclosure, XLVIII. BEFORE SUNRISE.",
"Zarathustra's Disclosure, XLIX. THE BEDWARFING VIRTUE.",
"Zarathustra's Disclosure, L. ON THE OLIVE-MOUNT.",
"Zarathustra's Disclosure, LI. ON PASSING-BY.",
"Zarathustra's Disclosure, LII. THE APOSTATES.",
"Zarathustra's Disclosure, LIII. THE RETURN HOME.",
"Zarathustra's Disclosure, LIV. THE THREE EVIL THINGS.",
"Zarathustra's Disclosure, LV. THE SPIRIT OF GRAVITY.",
"Zarathustra's Disclosure, LVI. OLD AND NEW TABLES.",
"Zarathustra's Disclosure, LVII. THE CONVALESCENT.",
"Zarathustra's Disclosure, LVIII. THE GREAT LONGING.",
"Zarathustra's Disclosure, LIX. THE SECOND DANCE-SONG.",
"Zarathustra's Disclosure, LX. THE SEVEN SEALS.",
"Zarathustra's Disclosure, Part IV Prologue",
"Zarathustra's Disclosure, LXI. THE HONEY SACRIFICE.",
"Zarathustra's Disclosure, LXII. THE CRY OF DISTRESS.",
"Zarathustra's Disclosure, LXIII. TALK WITH THE KINGS.",
"Zarathustra's Disclosure, LXIV. THE LEECH.",
"Zarathustra's Disclosure, LXV. THE MAGICIAN.",
"Zarathustra's Disclosure, LXVI. OUT OF SERVICE.",
"Zarathustra's Disclosure, LXVII. THE UGLIEST MAN.",
"Zarathustra's Disclosure, LXVIII. THE VOLUNTARY BEGGAR.",
"Zarathustra's Disclosure, LXIX. THE SHADOW.",
"Zarathustra's Disclosure, LXX. NOONTIDE.",
"Zarathustra's Disclosure, LXXI. THE GREETING.",
"Zarathustra's Disclosure, LXXII. THE SUPPER.",
"Zarathustra's Disclosure, LXXIII. THE HIGHER MAN.",
"Zarathustra's Disclosure, LXXIV. THE SONG OF MELANCHOLY.",
"Zarathustra's Disclosure, LXXV. SCIENCE.",
"Zarathustra's Disclosure, LXXVI. AMONG DAUGHTERS OF THE DESERT.",
"Zarathustra's Disclosure, LXXVII. THE AWAKENING.",
"Zarathustra's Disclosure, LXXVIII. THE ASS-FESTIVAL.",
"Zarathustra's Disclosure, LXXIX. THE DRUNKEN SONG.",
"Zarathustra's Disclosure, LXXX. THE SIGN.",
        };
        String[] filenameArray = {
"./book/part-1/sec-1.txt",
"./book/part-1/sec-2.txt",
"./book/part-1/sec-3.txt",
"./book/part-1/sec-4.txt",
"./book/part-1/sec-5.txt",
"./book/part-1/sec-6.txt",
"./book/part-1/sec-7.txt",
"./book/part-1/sec-8.txt",
"./book/part-1/sec-9.txt",
"./book/part-1/sec-10.txt",
"./book/part-2/sec-1.txt",
"./book/part-2/sec-2.txt",
"./book/part-2/sec-3.txt",
"./book/part-2/sec-4.txt",
"./book/part-2/sec-5.txt",
"./book/part-2/sec-6.txt",
"./book/part-2/sec-7.txt",
"./book/part-2/sec-8.txt",
"./book/part-2/sec-9.txt",
"./book/part-2/sec-10.txt",
"./book/part-2/sec-11.txt",
"./book/part-2/sec-12.txt",
"./book/part-2/sec-13.txt",
"./book/part-2/sec-14.txt",
"./book/part-2/sec-15.txt",
"./book/part-2/sec-16.txt",
"./book/part-2/sec-17.txt",
"./book/part-2/sec-18.txt",
"./book/part-2/sec-19.txt",
"./book/part-2/sec-20.txt",
"./book/part-2/sec-21.txt",
"./book/part-2/sec-22.txt",
"./book/part-3/sec-1.txt",
"./book/part-3/sec-2.txt",
"./book/part-3/sec-3.txt",
"./book/part-3/sec-4.txt",
"./book/part-3/sec-5.txt",
"./book/part-3/sec-6.txt",
"./book/part-3/sec-7.txt",
"./book/part-3/sec-8.txt",
"./book/part-3/sec-9.txt",
"./book/part-3/sec-10.txt",
"./book/part-3/sec-11.txt",
"./book/part-3/sec-12.txt",
"./book/part-3/sec-13.txt",
"./book/part-3/sec-14.txt",
"./book/part-3/sec-15.txt",
"./book/part-3/sec-16.txt",
"./book/part-3/sec-17.txt",
"./book/part-3/sec-18.txt",
"./book/part-3/sec-19.txt",
"./book/part-3/sec-20.txt",
"./book/part-3/sec-21.txt",
"./book/part-3/sec-22.txt",
"./book/part-3/sec-23.txt",
"./book/part-4/sec-1.txt",
"./book/part-4/sec-2.txt",
"./book/part-4/sec-3.txt",
"./book/part-4/sec-4.txt",
"./book/part-4/sec-5.txt",
"./book/part-4/sec-6.txt",
"./book/part-4/sec-7.txt",
"./book/part-4/sec-8.txt",
"./book/part-4/sec-9.txt",
"./book/part-4/sec-10.txt",
"./book/part-4/sec-11.txt",
"./book/part-4/sec-12.txt",
"./book/part-4/sec-13.txt",
"./book/part-4/sec-14.txt",
"./book/part-4/sec-15.txt",
"./book/part-4/sec-16.txt",
"./book/part-4/sec-17.txt",
"./book/part-5/sec-1.txt",
"./book/part-5/sec-2.txt",
"./book/part-5/sec-3.txt",
"./book/part-5/sec-4.txt",
"./book/part-5/sec-5.txt",
"./book/part-5/sec-6.txt",
"./book/part-5/sec-7.txt",
"./book/part-5/sec-8.txt",
"./book/part-5/sec-9.txt",
"./book/part-5/sec-10.txt",
"./book/part-5/sec-11.txt",
"./book/part-5/sec-12.txt",
"./book/part-5/sec-13.txt",
"./book/part-5/sec-14.txt",
"./book/part-5/sec-15.txt",
"./book/part-5/sec-16.txt",
"./book/part-5/sec-17.txt",
"./book/part-5/sec-18.txt",
"./book/part-5/sec-19.txt",
"./book/part-5/sec-20.txt",
"./book/part-5/sec-21.txt"
        };
        return new Database(Arrays.asList(filenameArray).stream().map(File::new).toArray(File[]::new), titleArray);
    }

    
}
