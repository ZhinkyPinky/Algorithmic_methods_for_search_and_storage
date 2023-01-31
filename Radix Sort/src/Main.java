import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        //sortTestString(new File("Radix Sort/bible-lines.txt"));
        //sortTestString(new File("Radix Sort/test.txt"));
        //sortTestString(new File("Radix Sort/testString.txt"));
        sortTestInt(new File("Radix Sort/ints.txt"));
        //sortTestInt(new File("Radix Sort/test.txt"));
    }

    private static void sortTestInt(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Radix Sort/testOutput.txt"))) {
            ArrayList<Integer> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add(Integer.valueOf(line));
            }

            Integer[] a = MSD.sort(aTemp.toArray(new Integer[0]), new Integer[aTemp.size()], (-1 >>> (31 - 8)), 0, aTemp.size() - 1, 0, 8, (element, d, bits) -> {
                int bitsTo = 32 - (d * bits);
                if (bitsTo < 0) {
                    return -1;
                }

                int bitsFrom = (32 - ((d + 1) * bits));
                if (bitsFrom < 0) {
                    bitsTo = 0;
                }

                return ((-1 >>> 32 - (bitsTo)) & element) >>> (bitsFrom);
            });

            for (Integer s : a) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sortTestString(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Radix Sort/testOutput.txt"))) {
            ArrayList<String> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add(line);
            }

            String[] a = MSD.sort(aTemp.toArray(new String[0]), new String[aTemp.size()], (-1 >>> (31 - 8)), 0, aTemp.size() - 1, 0, 8, (element, d, bits) -> {
                byte[] bytes = element.getBytes();

                int bitsFrom = d * bits;
                if (bitsFrom >= bytes.length * 8) {
                    return -1;
                }

                int bitsTo = ((d + 1) * bits) - 1;
                if (bitsTo > bytes.length * 8) {
                    bitsTo = (bytes.length * 8) - 1;
                }

                int outputBits = 0;
                for(int i = bitsFrom / 8; i <= bitsTo / 8; i++){
                    if (i == bitsFrom / 8){
                        if (i == bitsTo / 8){
                            return ((-1 >>> 32 - (bitsTo % 8)) & bytes[bitsTo / 8]) >>> (bitsFrom % 8);
                        }

                        outputBits = bytes[bitsFrom / 8] >>> ((bitsFrom % 8));
                    } else if (i == bitsTo / 8){
                        outputBits = (outputBits << (8 - (bitsTo % 8))) | (bytes[bitsTo / 8] >>> (bitsTo % 8));
                    } else {
                        outputBits = (outputBits << 8) | bytes[i];
                    }
                }

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