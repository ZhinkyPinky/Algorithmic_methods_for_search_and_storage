import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        sortTest(new File("Radix Sort/bible-lines.txt"));
        //sortTest(new File("Radix Sort/test.txt"));
        //sortTest(new File("Radix Sort/testString.txt"));
        //sortTest(new File("Radix Sort/ints.txt"));
    }

    private static void sortTest(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Radix Sort/testOutput.txt"))) {
            ArrayList<String> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add(line);
            }
            //System.out.println(aTemp);

            String[] a = MSD.sort(aTemp.toArray(new String[0]), new String[aTemp.size()], (-1 >>> (32 - 8)), 0, aTemp.size() - 1, 0, (element, d, bits) -> {
                byte[] bytes = element.getBytes();

                //System.out.println(Arrays.toString(bytes) + " lkuh");
                //System.out.println("element: " + element);
                //System.out.println("digit: " + d);
                //System.out.println("bytes: " + bytes.length);

                int bitsTo = (bytes.length * 8) - (d * bits) - 1;
                if (bitsTo < 0) {
                    return -1;
                }
                //System.out.println("bitsTo: " + bitsTo);
                //System.out.println("bitsTo/8: " + (bitsTo / 8));

                int bitsFrom = (bytes.length * 8) - ((d + 1) * bits);
                if (bitsFrom < 0) {
                    bitsFrom = 0;
                }
                //System.out.println("bitsFrom: " + bitsFrom);
                //System.out.println("bitsFrom/8: " + (bitsFrom / 8));

                int outputBits;
                if ((bitsTo / 8) == (bitsFrom / 8)) {
                    outputBits = ((-1 >>> 32 - bitsTo) & bytes[(bytes.length - 1) - (bitsTo / 8)]) >>> (bitsFrom % 8);
                    //System.out.println();
                    return outputBits;
                }

                outputBits = bytes[bytes.length - (bitsTo / 8)] >>> (8 - (bitsTo % 8));
                //System.out.println("bytes.length - (bitsTo / 8): " + (bytes.length - (bitsTo / 8)));
                //System.out.println("bytes[bytes.length - (bitsTo / 8)]: " + bytes[(bytes.length - 1) - (bitsTo / 8)]);

                for (int i = 1; i < (bitsTo - bitsFrom) / 8; i++) {
                    //System.out.println(outputBits);
                    //System.out.println("i: " + i + " length: " + bytes.length);
                    outputBits = (outputBits << 8) & bytes[(bytes.length - 1) - ((bitsTo / 8) - i)];
                    //System.out.println(outputBits);
                }

                //System.out.println("bytes.length - (bitsFrom / 8): " + (bytes.length - (bitsFrom / 8)));
                //System.out.println("bytes[bytes.length - (bitsFrom / 8)]: " + bytes[bytes.length - (bitsFrom / 8)]);
                //System.out.println("a: " + outputBits);
                outputBits = (outputBits << (8 - (bitsFrom % 8))) | (bytes[(bytes.length - 1) - (bitsFrom / 8)] >>> (bitsFrom % 8));
                //System.out.println("b: " + outputBits);
                //System.out.println();
                return outputBits;
            });

            for (String s : a) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}