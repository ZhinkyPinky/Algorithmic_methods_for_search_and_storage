import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File("Substring search with suffix array/test.txt");
        //File file = new File("Substring search with suffix array/bible-oneline.txt");
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
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
            System.out.println(suffixArray.findAllOccurrences(target).toString());
            System.out.println(suffixArray.binarySearch(target));
            System.out.println("************************************************");
            System.out.println();
        }
    }
}