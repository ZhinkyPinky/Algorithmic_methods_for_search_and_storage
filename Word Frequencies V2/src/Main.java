import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner((new BufferedReader(new FileReader("Word Frequencies V2/test.txt"))))) {
            scanner.useDelimiter("\\W+");
            HashSet<String> strings = new HashSet<>();
            Trie trie = new Trie();
            while (scanner.hasNext()) {
                String s = scanner.next().toLowerCase();
                strings.add(s);

                int value = trie.get(s);
                if (value == -1) {
                    trie.put(s, 1);
                } else {
                    trie.put(s, value + 1);
                }
            }

            for (String s : strings) {
                System.out.println(s + " : " + trie.get(s));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}