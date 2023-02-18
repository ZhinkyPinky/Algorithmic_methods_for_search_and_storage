import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("Substring search with suffix array/test.txt"))) {
            SuffixArray suffixArray = new SuffixArray(bufferedReader.readLine());
            doStuff(suffixArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doStuff(SuffixArray suffixArray) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Search: ");
            String target = scanner.nextLine();

            System.out.println("**Matches***************************************");
            System.out.println(suffixArray.finalAllOccurrences(target).toString());
            System.out.println(suffixArray.binarySearch(target));
            System.out.println("************************************************");
            System.out.println();
        }
    }
}