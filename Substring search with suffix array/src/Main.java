import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Substring search with suffix array/test.txt"))) {
            SuffixArray suffixArray = new SuffixArray(bufferedReader.readLine());
            System.out.println("boop");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}