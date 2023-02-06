import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //doStuff(new File("Search Engine/oldhouse.txt"));
        doStuff(new File("Search Engine/bible-washed.txt"));
    }

    private static void doStuff(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            InvertedFile invertedFile = new InvertedFile();

            String line;
            int document = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] words = line.split(" ");
                document++;
                for (String word : words) {
                    invertedFile.add(document, word);
                }
            }

            doMoreStuff(invertedFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void doMoreStuff(InvertedFile invertedFile) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Search: ");
            String query = scanner.nextLine();

            System.out.println("**Matches***************************************");
            for (String s : query.split(" ")) {
                System.out.println(s + ": " + invertedFile.getDocuments(s));
            }
            System.out.println("************************************************");
        }
    }
}