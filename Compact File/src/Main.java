import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        doStuff(new File("Compact File/abba-1"), 1, 7);
        //System.out.println();
        //doStuff(new File("Compact File/abba-2"), 2, 7);
        //System.out.println();
        //doStuff(new File("Compact File/tenbits-2"), 2, 10);

    }

    private static void doStuff(File file, int B, int bo) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("Compact File/" + file.getName() + "-output"))
        ) {
            int[] arr = new int[(int) file.length()];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = fileInputStream.read();
            }

            fileInputStream.close();

            int[] intArray = new int[(int) (file.length() / B)];
            for (int i = 0; i < intArray.length; i++) {
                for (int j = 0; j < B; j++) {
                    int temp = arr[B * i + j] << 8 * (B - 1 - j);
                    intArray[i] = (intArray[i] << 8 * (B - 1 - j)) | temp;
                }
            }
            System.out.println();

            for (int i : intArray) {
                //System.out.print(i + "\t");
            }

            int buffer = 0;
            int bitsInBuffer = 0;
            for (int i = 0; i < intArray.length; i++){
                if (bitsInBuffer < B*8){
                    bitsInBuffer += bo;
                    buffer = (buffer << bo) | intArray[i];
                } else {
                    bitsInBuffer -= B*8;
                    System.out.println((buffer >>> bitsInBuffer) & (-1 >>> 32-B*8));
                    bufferedOutputStream.write((buffer >>> bitsInBuffer) & (-1 >>> 32-B*8));
                }
            }

            /*
            int i = 0;
            while (i + (i / bo) < intArray.length) {
                int bitBuffer = 0;
                int remainingCapacity = B * 8;
                while (remainingCapacity > 0 && i + (i / bo) < intArray.length) {
                    if (remainingCapacity >= bo) {
                        //System.out.println(remainingCapacity + " : "  + (remainingCapacity - (bo - (i % bo))));
                        int h = remainingCapacity - (bo - (i % bo));
                        bitBuffer = bitBuffer | intArray[i + ((i - 1) / (bo))] << remainingCapacity - (bo - (i % bo));
                        //System.out.println(bitBuffer);
                        remainingCapacity -= (bo - (i % bo));
                        i++;
                    } else {
                        System.out.println("a: " + bitBuffer + " : " + (bo - remainingCapacity));
                        bitBuffer = bitBuffer | intArray[i + ((i - 1) / (bo))] >>> (bo - remainingCapacity);
                        System.out.println("b: " + bitBuffer + " : " + (intArray[i + (i / bo)] >>> ((bo - 1 - remainingCapacity) % bo)));
                        //(cA[i + (i / 7) + 1] >>> (6 - i % 7)))
                        //System.out.println("before:" + intArray[i + (i / bo)]);
                        intArray[i + (i / bo)] = (intArray[i + ((i - 1) / (bo))] & (-1 >>> 32 - bo + remainingCapacity));
                        //System.out.println("remaining capacity: " + remainingCapacity + " : " + (-1 >>> 32 - bo + remainingCapacity));
                        //System.out.println("after:" + intArray[i + (i / bo)]);
                        remainingCapacity -= remainingCapacity;
                    }
                }
                if (bitBuffer == 98){
                    System.out.println("dflkjgh");
                }

                //System.out.println("buffer:" + bitBuffer + " remaining capacity: " + remainingCapacity);
                //System.out.println("ölkj" +remainingCapacity);
                System.out.println(bitBuffer);
                bufferedOutputStream.write(bitBuffer);
            }
             */

        /*
        while (scanner.hasNext()) {
            char[] cA = scanner.next().toCharArray();
            for (char c : cA) {
                System.out.print(c + ":" + (int) c + "   ");
            }
            System.out.println();

            ArrayList<Integer> list = new ArrayList<>();

            int bitBuffer;
            int bitsInBuffer = 0;
            for (int i = 0; i + (i / bo) < cA.length - 1; i++) {
                bitBuffer = cA[i];
                if (bitsInBuffer == 8) {
                    bitBuffer = bitBuffer << 8;
                } else {
                    bitBuffer = bitBuffer << B * 8 - bo;
                    bitsInBuffer += B * 8 - bo;
                }

                System.out.println(bitBuffer);
                System.out.println(bitsInBuffer);
                //bitBuffer = cA[i] << (((B * 8) - bo + (i % bo)) - 1);
                //bitBuffer = (cA[i] << (((B * 8) - bo + (i % bo))) | (cA[i + (i / bo) + 1] >>> ((bo - 1) - i % bo))) & (-1 >>> 32 - B * 8);
            }


            for (int i = 0; (i + (i / bo)) < cA.length - 1; i++) {
                bitBuffer = (cA[i + (i / bo)] << (((B * 8) - bo + (i % bo) - 1)));
                System.out.println(bitBuffer);
                //| (cA[i + (i / bo) + 1] >>> (6 - i % 7))) & (-1 >>> 32 - 8);

                if ((i + (i / 7)) == cA.length - 2) {
                    bufferedOutputStream.write((cA[cA.length - 1] << 4) & (-1 >>> 32 - 8));
                }
            }


            bufferedOutputStream.flush();
            */
        }
    }


    private static int bitsNeeded(int input) {
        int bits = 0;
        while ((input >> bits) > 0) bits++;
        return bits;
    }
}

    /*
    private static void doStuff(String file, int B, int bo) throws IOException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("Compact File/" + file)));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("Compact File/" + file + "-output"));

        while (scanner.hasNext()) {
            char[] cA = scanner.next().toCharArray();
            for (char c : cA) {
                System.out.print(c + ":" + (int) c + "   ");
            }
            System.out.println();

            ArrayList<Integer> list = new ArrayList<>();

            for (int i = 0; (i + (i / 7)) < cA.length - 1; i++) {
                bufferedOutputStream.write(((cA[i + (i / 7)] << ((i % 7) + 1)) | (cA[i + (i / 7) + 1] >>> (6 - i % 7))) & (-1 >>> 32 - 8));
                System.out.println(cA[i] + " " + (int) cA[i]);

                if ((i + (i / 7)) == cA.length - 2) {
                    bufferedOutputStream.write((cA[cA.length - 1] << 4) & (-1 >>> 32 - 8));
                }
            }

            bufferedOutputStream.flush();

            System.out.println();
        }


        scanner = new Scanner(new BufferedReader(new FileReader("Compact File/" + file + "-output")));

        while (scanner.hasNext()) {
            String s = scanner.next();
            System.out.println(s);
        }


        scanner = new Scanner(new BufferedReader(new FileReader("Compact File/" + "abba-7-output")));

        while (scanner.hasNext()) {
            String s = scanner.next();
            for (char c : s.toCharArray()) {
                System.out.print(c + " " + (int) c + "\t");
            }
        }


        System.out.println();
        scanner.close();
    }
    */

