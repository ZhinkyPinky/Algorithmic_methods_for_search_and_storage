import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("Word Frequencies/test.txt")));
        scanner.useDelimiter("\\W+");

        HashMap<String, Integer> frequencies = new HashMap<>();
        while (scanner.hasNext()){
            String s = scanner.next().toLowerCase(Locale.ROOT);

            if (frequencies.containsKey(s)){
                frequencies.put(s, frequencies.get(s) + 1);
            } else {
                frequencies.put(s, 1);
            }
        }

        frequencies.forEach((key, value) -> System.out.println(key + ": " + value));

        scanner.close();
    }
}
