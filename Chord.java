/* LazyChord 1.1 is entirely designed and written by Chung Vuong
 * Anyone is free to use it, but there is no warranty, and I am not
 * responsible for any incorrect results from using LazyChord
*/
import java.util.*;

enum Accidental { // either sharp or flat, or natural if no accidental
    SHARP, FLAT, NATURAL;
}
enum Root { // the root note of a chord
    C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B, 
    B_FLAT, A_FLAT, G_FLAT, E_FLAT, D_FLAT;
    static final HashMap<Root,Integer> rootValue =
    new HashMap<Root,Integer>() {
        {
            /* The value is determined by the fret location
             * of the root note of the chord + 1, unless it
             * is an open chord, for which the value is always 1.
            */
            put(Root.C, 1); // open
            put(Root.C_SHARP, 5); // 4th fret
            put(Root.D_FLAT, 5); // 4th fret
            put(Root.D, 1); // open
            put(Root.D_SHARP, 7); // 6th fret
            put(Root.E_FLAT, 7); // 6th fret
            put(Root.E, 1); // open
            put(Root.F, 2); // 1st fret
            put(Root.F_SHARP, 3); // 2nd fret
            put(Root.G_FLAT, 3); // 2nd fret
            put(Root.G, 1); // open
            put(Root.G_SHARP, 5); // 4th fret
            put(Root.A_FLAT, 5); // 4th fret
            put(Root.A, 1); // open
            put(Root.A_SHARP, 2); // 1st, 6th fret
            put(Root.B_FLAT, 2); // 1st, 6th fret
            put(Root.B, 3); // 2nd, 7th fret
        }
    };
    public static int level(Root r) {
        return rootValue.get(r);
    }
}
enum Quality { // the quality of a chord
    MAJOR, MINOR, SEVEN, MAJ, MAJ7, AUG, DIM, SUS;
}
public class Chord { // Represents a musical chord
    private Root r; // root note of chord
    private Quality q; // chord quality
    private Accidental a;
    private int idx; // index identifying chord in the order
    private Chord(Root r, Quality q, Accidental a, int idx) {
        this.r = r;
        this.q = q;
        this.a = a;
        this.idx = idx;
    }
    private Chord(String string) {
        if (Chord.hasRoot(string) && Chord.hasQuality(string)) {
            this.r = parseRoot(string);
            this.q = parseQuality(string);
            this.idx = rootIdx.get(this.r);
            this.a = Accidental.SHARP; // TEMPORARY FOR TESTING
        }
        else {
            throw new RuntimeException("Chord Creation Error: Not a valid chord");
        }
    }
    private static final HashMap<String,Root> stringToRoot = 
    new HashMap<String,Root>() {
        {   
            put("C", Root.C);
            put("C#", Root.C_SHARP);
            put("Db", Root.D_FLAT);
            put("D", Root.D);
            put("D#", Root.D_SHARP);
            put("Eb", Root.E_FLAT);
            put("E", Root.E);
            put("F", Root.F);
            put("F#", Root.F_SHARP);
            put("Gb", Root.G_FLAT);
            put("G", Root.G);
            put("G#", Root.G_SHARP);
            put("Ab", Root.A_FLAT);
            put("A", Root.A);
            put("A#", Root.A_SHARP);
            put("Bb", Root.B_FLAT);
            put("B", Root.B);
        }
    };
    private static final HashMap<String, Quality> stringToQuality = 
    new HashMap<String,Quality>() {
        {
            put("", Quality.MAJOR);
            put("m", Quality.MINOR);
            put("7", Quality.SEVEN);
            put("maj", Quality.MAJ);
            put("maj7", Quality.MAJ7);
            put("aug", Quality.AUG);
            put("dim", Quality.DIM);
            put("sus", Quality.SUS);
        }
    };
    private static final HashMap<Quality,String> qualityVal = 
    new HashMap<Quality,String>() {
        {
            put(Quality.MAJOR, "");
            put(Quality.MINOR, "m");
            put(Quality.SEVEN, "7");
            put(Quality.MAJ, "maj");
            put(Quality.MAJ7, "maj7");
            put(Quality.AUG, "aug");
            put(Quality.DIM, "dim");
            put(Quality.SUS, "sus");
        }
    };
    private static final HashMap<Root,Integer> rootIdx =
    new HashMap<Root,Integer>() {
        {
            put(Root.C, 0);
            put(Root.C_SHARP, 1);
            put(Root.D_FLAT, 1);
            put(Root.D, 2);
            put(Root.D_SHARP, 3);
            put(Root.E_FLAT, 3);
            put(Root.E, 4);
            put(Root.F, 5);
            put(Root.F_SHARP, 6);
            put(Root.G_FLAT, 6);
            put(Root.G, 7);
            put(Root.G_SHARP, 8);
            put(Root.A_FLAT, 8);
            put(Root.A, 9);
            put(Root.A_SHARP, 10);
            put(Root.B_FLAT, 10);
            put(Root.B, 11);
        }
    };
    private static final Vector<Root> orderSharp = new Vector<Root>() {
        {
            add(Root.C);
            add(Root.C_SHARP);
            add(Root.D);
            add(Root.D_SHARP);
            add(Root.E);
            add(Root.F);
            add(Root.F_SHARP);
            add(Root.G);
            add(Root.G_SHARP);
            add(Root.A);
            add(Root.A_SHARP);
            add(Root.B);  
        }
    };
    private static final Vector<Root> orderFlat = new Vector<Root>() {
        {
            add(Root.C);
            add(Root.D_FLAT);
            add(Root.D);
            add(Root.E_FLAT);
            add(Root.E);
            add(Root.F);
            add(Root.G_FLAT);
            add(Root.G);
            add(Root.A_FLAT);
            add(Root.A);
            add(Root.B_FLAT);
            add(Root.B);
        }
    };
    // Client method to construct a chord from an appropriate string
    public static Chord makeChord(String str) {
        return new Chord(str);
    }
    // Parse the string, find the Root Note
    private Root parseRoot(String str) {
        if (str.length() == 1) {
            return stringToRoot.get(str.substring(0, 1));
        }
        else if ((str.substring(1, 2).equals("#")) || (str.substring(1, 2).equals("b"))) {
            return stringToRoot.get(str.substring(0, 2));
        }
        else {
            return stringToRoot.get(str.substring(0, 1));
        }
    }
    // Parse the string, find the chord Quality
    private Quality parseQuality(String str) {
        if (str.length() == 1) {
            return Quality.MAJOR;
        }
        else if ((str.substring(1, 2).equals("#")) || (str.substring(1, 2).equals("b"))) {
            return stringToQuality.get(str.substring(2, str.length()));
        }
        else {
            return stringToQuality.get(str.substring(1, str.length()));
        }
    }
    // Does the string have a valid Chord root?
    public static boolean hasRoot(String str) {
        if (str.length() <= 1) {
            return stringToRoot.containsKey(str);
        }
        else if ((str.substring(1, 2).equals("#")) || (str.substring(1, 2).equals("b"))) {
            return stringToRoot.containsKey(str.substring(0,2));
        }
        else {
            return stringToRoot.containsKey(str.substring(0,1));
        }
    }
    // Does the string have a valid Chord quality?
    public static boolean hasQuality(String str) {
        if (str.length() <= 1) { // C major, etc.
            return stringToRoot.containsKey(str);
        }
        else if ((str.substring(1, 2).equals("#")) || (str.substring(1, 2).equals("b"))) {
            return stringToQuality.containsKey(str.substring(2,str.length()));
        }
        else {
            return stringToQuality.containsKey(str.substring(1,str.length()));
        }
    }
    // Represent this chord as a string
    @Override
    public String toString() {
        String answer = "";
        String s = this.r.toString();
        if (s.length() == 1) { // ex. A, B, C, etc.
            answer = answer + s.substring(0, 1);
        }
        else { // ex. Bb, D#, etc.
            String accidental = s.substring(2, s.length());
            if (accidental.equals("SHARP")) {
                accidental = "#";
            }
            else {
                accidental = "b";
            }
            answer = answer + s.substring(0, 1) + accidental; 
        }
        answer = answer + qualityVal.get(this.q);
        return answer;
    }
    // Transpose this chord by given number of half-steps
    public void transpose(int halfsteps) {
        int n = this.idx + halfsteps;
        if (n > 11) {
            n = n - 12;
        }
        else if (n < 0) {
            n = 12 + n;
        }
        else { }
        if (this.a.equals(Accidental.FLAT)) {
            this.r = orderFlat.get(n);
            this.idx = n;
        }
        else {
            this.r = orderSharp.get(n);
            this.idx = n;
        }
    }
    // Transpose this chord by given number of half-steps
    public Chord transposeChord(int halfsteps) {
        int n = this.idx + halfsteps;
        if (n > 11) {
            n = n - 12;
        }
        else if (n < 0) {
            n = 12 + n;
        }
        else { }
        if (this.a.equals(Accidental.FLAT)) {
            return new Chord(orderFlat.get(n), this.q, this.a, n);
        }
        else {
            return new Chord(orderSharp.get(n), this.q, this.a, n);
        }
    }
    public int difficulty() {
        return Root.level(this.r);
    }
    public static void main(String[] args) {
        String s = "Cb";
        Chord asharp = new Chord("C7");
        asharp.a = Accidental.NATURAL;
        asharp.transpose(-3);
        System.out.println(asharp.toString());
        System.out.println(asharp.idx);
        System.out.println("///////////////////////");
    }
}