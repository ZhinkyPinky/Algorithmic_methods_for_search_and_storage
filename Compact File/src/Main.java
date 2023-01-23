import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        compact(new File("Compact File/abba-1"), 1, 7);
        compact(new File("Compact File/abba-2"), 2, 7);
        compact(new File("Compact File/tenbits-2"), 2, 10);
    }

    private static void compact(File file, int B, int bo) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("Compact File/" + file.getName() + "-output"))
        ) {
            int buffer = 0;
            int bitsInBuffer = 0;
            for (int i = 0; i < (file.length() / B); i++) {
                int integer = 0;
                for (int j = 0; j < B; j++) {
                    integer = (integer << (8 * (B - 1 - j))) | (bufferedInputStream.read() & 0xFF) << (8 * (B - 1 - j));
                }

                bitsInBuffer += bo;
                buffer = (buffer << bo) | integer;

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

