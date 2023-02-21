import java.io.*;

public class Main {
    public static void main(String[] args) {
        doStuff(new File("Huffman coding/abra.txt"));
        //doStuff(new File("Huffman coding/bible-en.txt"));
    }

    private static void doStuff(File file) {
        Huffman huffman = new Huffman(256);
        huffman.encode(file);
    }
}