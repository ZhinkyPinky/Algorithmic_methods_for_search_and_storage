import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //sortTest(new File("Radix Sort/bible-lines.txt"));
        //sortTest(new File("Radix Sort/test.txt"));
        sortTest(new File("Radix Sort/testString.txt"));
        //sortTest(new File("Radix Sort/ints.txt"));
    }

    private static void sortTest(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {
            ArrayList<String> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add(line);
            }
            System.out.println(aTemp);

            MSD.sort(aTemp.toArray(new String[0]), new String[aTemp.size()], (-1 >>> (32 - 8)), 0, aTemp.size() - 1, 0, (element, d, bits) -> {
                byte[] bytes = element.getBytes();
                //System.out.println(Arrays.toString(bytes) + " lkuh");

                int bitsTo = (((bytes.length) * 7)) - (d * bits);
                //System.out.println((((bytes.length))));
                //System.out.println((((bytes.length) * 8)));
                //System.out.println(bitsTo);
                //System.out.println();
                if (bitsTo < 0) {
                    return -1;
                }

                int bitsFrom = ((bytes.length) * 7) - ((d + 1) * bits);
                //System.out.println((((bytes.length))));
                //System.out.println((((bytes.length) * 8)));
                //System.out.println(bitsFrom);
                //System.out.println();
                if (bitsFrom < 0) {
                    bitsFrom = 0;
                }

                int outputBits;
                if ((bitsTo / 8) == (bitsFrom / 8)) {
                    outputBits = ((-1 >>> 32 - bitsTo) & bytes[(bitsTo / 8)]) >>> (bitsFrom % 8);
                    return outputBits;
                }

                outputBits = bytes[(bitsTo / 8) - 1] >>> (8 - (bitsTo % 8));

                for (int i = 1; i < (bitsTo - bitsFrom) / 8; i++) {
                    System.out.println(outputBits);
                    System.out.println("i: " + i + " length: " + bytes.length);
                    outputBits = (outputBits << 8) & bytes[(bitsTo / 8) - i];
                    System.out.println(outputBits);
                }
                System.out.println("a" + outputBits);
                outputBits = (outputBits << (8 - (bitsFrom % 8))) | (bytes[(bitsFrom / 8)] >>> (bitsFrom % 8));
                System.out.println("b" + outputBits);
                return outputBits;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}