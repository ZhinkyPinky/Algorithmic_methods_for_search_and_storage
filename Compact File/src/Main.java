import java.awt.*;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        doStuff(new File("Compact File/abba-1"), 1, 7);
        doStuff(new File("Compact File/abba-2"), 2, 7);
        doStuff(new File("Compact File/tenbits-2"), 2, 10);
    }

    private static void doStuff(File file, int B, int bo) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("Compact File/" + file.getName() + "-output"))
        ) {
            byte[] bytes = fileInputStream.readAllBytes();
            int[] ints = new int[(int) (bytes.length / B)];
            for (int i = 0; i < ints.length; i++) {
                for (int j = 0; j < B; j++) {
                    ints[i] = (ints[i] << (8 * (B - 1 - j))) | (bytes[B * i + j] & 0xFF) << (8 * (B - 1 - j));
                }
            }

            int buffer = 0;
            int bitsInBuffer = 0;
            for (int i : ints) {
                bitsInBuffer += bo;
                buffer = (buffer << bo) | i;

                while (bitsInBuffer >= 8) {
                    bitsInBuffer -= 8;
                    bufferedOutputStream.write((buffer >>> bitsInBuffer) & (-1 >>> (32 - 8)));
                }
            }

            bufferedOutputStream.write((buffer << 8 - bitsInBuffer) & (-1 >>> (32 - 8)));
            bufferedOutputStream.flush();
        }
    }
}

