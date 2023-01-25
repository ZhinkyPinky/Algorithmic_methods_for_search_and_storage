import com.sun.jdi.InconsistentDebugInfoException;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        sortTest();
    }

    private static void sortTest(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
        ) {
            ArrayList<String> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add(line);
            }

            MSD.sort(aTemp.toArray(new String[0]), new String[aTemp.size()], 2, 0, 0, 0, new DigitGetter<String>() {

                @Override
                public int getDigit(String element, int i, int bits) {
                    byte[] bytes = element.getBytes();
                    if (i > element.length()){
                        return -1;
                    }

                    int integer = 0;
                    for (int j = i; j < bits; j++) {
                        integer = (integer << (8 * j) | (bytes[bytes.length - 1 - j] & 0xFF));
                    }

                    return integer;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}