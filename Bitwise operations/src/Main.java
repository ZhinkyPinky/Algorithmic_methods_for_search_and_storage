import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(new BufferedReader(new FileReader("Bitwise Operations/test.txt")));

        int testCases = scanner.nextInt();

        System.out.println("bits0To2");
        for (int i = 0; i < testCases; i++){
            int input = scanner.nextInt();
            int expectedOutput = scanner.nextInt();
            int output = bits0To2(input);

            if(output == expectedOutput) System.out.println("{" + input + "} : " + output + " == " + expectedOutput);
            else System.out.println("{"+ input + "} : " + output + " != " + expectedOutput);
        }
        System.out.println();

        testCases = scanner.nextInt();

        System.out.println("bits2To5");
        for (int i = 0; i < testCases; i++){
            int input = scanner.nextInt();
            int expectedOutput = scanner.nextInt();
            int output = bits2To5(input);

            if(output == expectedOutput) System.out.println("{" + input + "} : " + output + " == " + expectedOutput);
            else System.out.println("{"+ input + "} : " + output + " != " + expectedOutput);
        }
        System.out.println();

        testCases = scanner.nextInt();

        System.out.println("bitsFromTo");
        for (int i = 0; i < testCases; i++){
            int input1 = scanner.nextInt(), input2 = scanner.nextInt(), input3 = scanner.nextInt();
            int expectedOutput = scanner.nextInt();
            int output = bitsFromTo(input1, input2, input3);

            if(output == expectedOutput) System.out.println("{" + input1 + ", " + input2 + ", " + input3 + "} : " + output + " == " + expectedOutput);
            else System.out.println("{" + input1 + ", " + input2 + ", " + input3 + "} : " + output + " != " + expectedOutput);
        }
        System.out.println();

        testCases = scanner.nextInt();

        System.out.println("combine4Bits");
        for (int i = 0; i < testCases; i++){
            int input1 = scanner.nextInt(), input2 = scanner.nextInt();
            int expectedOutput = scanner.nextInt();
            int output = combine4Bits(input1, input2);

            if(output == expectedOutput) System.out.println("{" + input1 + ", " + input2 + "} : " + output + " == " + expectedOutput);
            else System.out.println("{" + input1 + ", " + input2 + "} : " + output + " != " + expectedOutput);
        }
        System.out.println();

        testCases = scanner.nextInt();

        System.out.println("combineLow4Bits");
        for (int i = 0; i < testCases; i++){
            int input1 = scanner.nextInt(), input2 = scanner.nextInt();
            int expectedOutput = scanner.nextInt();
            int output = combineLow4Bits(input1, input2);

            if(output == expectedOutput) System.out.println("{" + input1 + ", " + input2 + "} : " + output + " == " + expectedOutput);
            else System.out.println("{" + input1 + ", " + input2 + "} : " + output + " != " + expectedOutput);
        }
        System.out.println();

        testCases = scanner.nextInt();

        System.out.println("bitsNeeded");
        for (int i = 0; i < testCases; i++){
            int input = scanner.nextInt();
            int expectedOutput = scanner.nextInt();
            int output = bitsNeeded(input);

            if(output == expectedOutput) System.out.println("{" + input + "} : " + output + " == " + expectedOutput);
            else System.out.println("{"+ input + "} : " + output + " != " + expectedOutput);
        }

        scanner.close();
    }

    private static int bits0To2(int input) {
        return input & 7;
    }

    private static int bits2To5(int input) {
        return (input >> 2) & 15;
    }

    private static int bitsFromTo(int input, int from, int to) {
        return ((-1 >>> 31 - to) & input) >> from;
    }

    private static int combine4Bits(int input1, int input2) {
        return (input1 << 4) | input2;
    }

    private static int combineLow4Bits(int input1, int input2) {
        return ((input1 & 15) << 4) | (input2 & 15);
    }

    private static int bitsNeeded(int input) {
        int bits = 0;
        while ((input >> bits) > 0) bits++;
        return bits;
    }
}
