import java.io.*;

public class Main {
    public static void main(String[] args) {
        doStuff(new File("Huffman coding/abra.txt"));
    }

    private static void doStuff(File file){
            Huffman huffman = new Huffman();
            huffman.encode(file);
    }
}