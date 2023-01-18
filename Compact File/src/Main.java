import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        doStuff("abba-1", 1, 3);
        System.out.println();

        //doStuff("abba-7-output", 1, 3);
    }

    private static void doStuff(String file, int B, int bo) throws FileNotFoundException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
        while (scanner.hasNext()){
            String s = scanner.next();
            char[] cA = s.toCharArray();
            System.out.println(s);

            for (int i = 0; i < cA.length - 1; i++){
                System.out.println("i: " + i);
                System.out.println(cA[i] + " " + (int) cA[i]);
                System.out.println(cA[i + 1] + " " + (int) cA[i + 1]);

                cA[i] = (char) ((cA[i] << ((i % 6) + 1)) | (cA[i + 1] >>> (6 - (i % 6))));
                System.out.println(cA[i] + " " + (int) cA[i]);

                cA[i + 1] = (char) (cA[i + 1] & (-1 >>> 32 - 6 + (i % 6)));
                System.out.println(cA[i + 1] + " " + (int) cA[i + 1]);

                System.out.println();
            }
            System.out.println();

            for (char c : cA){
                System.out.println(c + " " + (int) c);
            }
            System.out.println();
        }


        System.out.println();
        scanner.close();
    }
}
