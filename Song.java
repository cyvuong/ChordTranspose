/* LazyChord 1.1 is entirely designed and written by Chung Vuong
 * Anyone is free to use it, but there is no warranty, and I am not
 * responsible for any incorrect results from using LazyChord
*/
import java.util.*;

public class Song {
    private Vector<Chord> chords; // sequence of chords in song
    private int capo; // the position of the capo the chords are relative to
    private int difficulty; // the difficulty of the song
    private Song() {
        this.chords = new Vector<Chord>();
        this.capo = 0;
        this.difficulty = 0;
    }
    private Song(Vector<Chord> chords) {
        this.chords = chords;
        this.capo = 0;
        this.difficulty = this.precomputeDifficulty();
    }
    // Memoize song transpositions to prevent recomputation
    private static Vector<Song> transposed = new Vector<Song>();
    // Memoize transposed song difficulties
    private static Vector<Integer> difficulties = new Vector<Integer>();

    // Make an empty song
    public static Song makeSong() {
        return new Song();
    }
    // Make a song with the given chords
    public static Song makeSong(Vector<Chord> chords) {
        return new Song(chords);
    }
    // Adds given chord to end of this song
    public void add(Chord c) {
        this.chords.add(c);
        this.difficulty = this.difficulty + c.difficulty();
    }
    // Computes number of chords in this song
    public int size() {
        return this.chords.size();
    }
    // Takes string of chords spaced out, and adds them to song
    public void addChords(String str) {
        String[] arr = str.trim().split(" ");
        for (String s : arr) {
            try {
                Chord c = Chord.makeChord(s);
                this.add(c);
            }
            catch (Exception e) {
                throw new RuntimeException("Sequence could not be computed because " 
                    + s + " is not a valid chord");
            }
        }
    }
    // Transpose all chords in this song up given amount of half-steps
    public void transposeAll(int halfsteps) {
        for (Chord c : this.chords) {
            c.transpose(halfsteps);
        }
    }
    public Song transposeSong(int halfsteps) {
        Song answer = Song.makeSong();
        for (Chord c : this.chords) {
            answer.add(c.transposeChord(halfsteps));
        }
        return answer;
    }
    // Computes difficulty of song based on chords
    private int precomputeDifficulty() {
        int answer = 0;
        for (Chord c : this.chords) {
            answer = answer + c.difficulty();
        }
        return answer;
    }
    public int songDifficulty() {
        return this.difficulty;
    }
    //
    public Song withCapo() {
        Song song = this;
        int orig = this.songDifficulty();
        Pair lowest = Pair.makePair(0, orig);
        Song.transposed.add(this); // original song at index 0
        Song.difficulties.add(orig);
        for (int i = 1; i < 12; i++) {
            // capo is i
            Song compare = this.transposeSong(-i);
            compare.capo = i;
            int current = compare.songDifficulty();
            // add transposed song at index == capo posn == i
            Song.transposed.add(compare); 
            // add its difficulty at same index
            Song.difficulties.add(current);
            if (current < lowest.getValue()) {
                lowest.setValue(current);
                lowest.setIndex(i);
                song = compare;
            }
            else { 
            }
        }
        song.capo = lowest.getIndex();
        return song;
    }
    // Clears the cached tranpositions of the song
    public static void clearMem() {
        Song.transposed.clear();
        Song.difficulties.clear();
    }
    // Return the song by i halfsteps
    public Song rememberSong(int i) {
        return Song.transposed.get(i);
    }
    // Return the difficulty of the song by i halfsteps
    public int rememberDiff(int i) {
        return Song.difficulties.get(i);
    }
    // Finds next easiest way to play the song after given difficulty
    public Song nextEasiest(int diff) {
        int ans = 100;
        int i = 0;
        for (int j = 0; j < 12; j++) {
            int d = Song.difficulties.get(j);
            if ((d > diff) && (d <= ans)) {
                ans = d;
                i = j;
            }
            else {
            }
        }/*
        for (Integer d: this.difficulties) {
            if ((d >= diff) && (d <= ans)) {
                ans = d;
            }
            else {
            }
        }
        */
        return Song.transposed.get(i);

    }
    public static void main(String[] args) {
        /*
        String s = "   Hello  World Yay!           ";
        String[] a = s.trim().split(" ");
        for (String str: a) {
            System.out.println(str);
        }
        */
        String set = " Gbm E D Db F# Dbm Gb A ";
        Song song = Song.makeSong();
        song.addChords(set);
        /*
        song.transposeAll(-2);
        int i = song.difficulty();
        */
        Song x = song.withCapo();
        System.out.println("Difficulty: " + x.songDifficulty());
        System.out.println("Capo: " + x.capo);
        for (Chord c : x.chords) {
            System.out.println(c.toString());
        }
        System.out.println("/////////");
        Song x2 = song.rememberSong(2);
        System.out.println("Difficulty: " + song.rememberDiff(2));
        System.out.println("Capo: " + x2.capo);
        for (Chord c : x2.chords) {
            System.out.println(c.toString());
        }
        System.out.println("/////////");
        Song x3 = song.nextEasiest(12);
        System.out.println("Difficulty: " + x3.songDifficulty());
        System.out.println("Capo: " + x3.capo);
        for (Chord c : x3.chords) {
            System.out.println(c.toString());
        }
        Song.clearMem();
    }
}
class Pair {
    private int i; // idx
    private int v; // value
    private Pair(int i, int v) {
        this.i = i;
        this.v = v;
    }
    public static Pair makePair(int i, int v) {
        return new Pair(i, v);
    }
    public int getIndex() {
        return this.i;
    }
    public int getValue() {
        return this.v;
    }
    public void setIndex(int newi) {
        this.i = newi;
    }
    public void setValue(int newv) {
        this.v = newv;
    }
}