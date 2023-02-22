import java.io.File;

public class Main {
    public static void main(String[] args) {
        //doStuff(new File("BWT/abra0.txt"));
        //doStuff(new File("BWT/banana.txt"));
        //doStuff(new File("BWT/bible-en.txt"));
        doStuff(new File("BWT/test.txt"));
    }

    private static void doStuff(File file) {
        BWT bwt = new BWT();
        bwt.transform(file);
    }
}