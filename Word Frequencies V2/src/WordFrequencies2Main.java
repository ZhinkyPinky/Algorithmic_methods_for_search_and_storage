import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Scanner;

public class WordFrequencies2Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner((new BufferedReader(new FileReader("Word Frequencies V2/test.txt"))))) {
            scanner.useDelimiter("\\W+");

            HashSet<String> strings = new HashSet<>();
            Trie<Integer> trie = new Trie<>();

            while (scanner.hasNext()) {
                String s = scanner.next().toLowerCase();
                strings.add(s);

                Integer value = trie.get(s);
                if (value == null) {
                    trie.put(s, 1);
                } else {
                    trie.put(s, value + 1);
                }
            }

            strings.stream().sorted().forEach(it -> System.out.println(it + " : " + trie.get(it)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}