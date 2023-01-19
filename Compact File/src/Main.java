import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        doStuff("abba-1", 1, 3);
        System.out.println();

        //doStuff("abba-7-output", 1, 3);
    }

    private static void doStuff(String file, int B, int bo) throws FileNotFoundException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("Compact File/" + file)));
        while (scanner.hasNext()) {
            String s = scanner.next();
            char[] cA = s.toCharArray();
            ArrayList<Integer> list = new ArrayList<>();
            System.out.println(s);

            for (int i = 0; (i + (i / 7)) < cA.length - 1; i++) {
                list.add(((cA[i + (i / 7)] << ((i % 7) + 1)) | (cA[i + (i / 7) + 1] >>> (6 - i % 7))) & (-1 >>> 32 - 8));
                //cA[i] = (char) (((cA[i + (i / 7)] << ((i % 7) + 1)) | (cA[i + (i / 7) + 1] >>> (6 - i % 7))) & (-1 >>> 32 - 8));
                System.out.println(cA[i] + " " + (int) cA[i]);

                if ((i + (i / 7)) == cA.length - 2) {
                    list.add((cA[cA.length - 1] << 4) & (-1 >>> 32 - 8));
                }
            }

            //cA[cA.length - 1] = (char) ((cA[cA.length - 1] << 4) & (-1 >>> 32 - 8));

            for (char c : cA) {
                System.out.print((int) c + "\t");
            }
            System.out.println();

            for (int i : list) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }


        System.out.println();
        scanner.close();
    }
}
