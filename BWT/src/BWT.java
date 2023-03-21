import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;

public class BWT {
    public BWT() {
    }

    public void transform(File file) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] bytes = new byte[(int) Files.size(file.toPath()) + 1];

            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) bufferedInputStream.read();
            }
            bytes[bytes.length - 1] = Character.MIN_VALUE;

            Integer[] suffixArray = constructSuffixArray(bytes);

            /*
            String[] sA = new String[bytes.length];
            Arrays.fill(sA, "");
            for (int i = 0; i < sA.length; i++) {
                for (int j = 0; j < sA.length; j++) {
                    sA[j] += (char) bytes[(i - j + bytes.length) % bytes.length];
                    if (j % 10000 == 0) {
                        System.out.println(j);
                    }
                }
                if (i % 100 == 0) {
                    System.out.println(i);
                }
            }

            Arrays.sort(suffixArray);

            for (int i = 0; i < suffixArray.length; i++) {
                if (suffixArray[i] < bytes.length) {
                    System.out.println(suffixArray[i] + " : " + (char) bytes[suffixArray[i]] + " : " + bytes[suffixArray[i]]);
                }
            }
            System.out.println(bytes.length);


            for (int i = 0; i < suffixArray.length; i++) {
                if (suffixArray[i] < bytes.length) {
                    System.out.println(suffixArray[i] + " : " + (char) bytes[((suffixArray[i] - 1) + bytes.length) % bytes.length] + " : " + bytes[((suffixArray[i] - 1) + bytes.length) % bytes.length]);
                }
            }

            System.out.println();

            byte[][] m = new byte[bytes.length][bytes.length];
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[i].length; j++) {
                    m[i][j] = bytes[(j - i + bytes.length) % bytes.length];
                    // System.out.print((i + j) % bytes.length + " ");
                    System.out.print((j - i + bytes.length) % bytes.length + " ");
                }
                System.out.println();
            }
            System.out.println();



            for (byte[] ba : m) {
                for (byte b : ba) {
                    System.out.print(b + " ");
                }
                System.out.println();
            }
            System.out.println();

            Arrays.sort(m, Comparator.comparingInt(o -> o[0]));

            for (byte[] ba : m) {
                for (byte b : ba) {
                    System.out.print(b + " ");
                }
                System.out.println();
            }
            System.out.println();

            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = m[i][m[i].length - 1];
            }
            */

            int originalStringIndex = -1;
            int j = 0;
            byte[] bwtOutput = new byte[bytes.length - 1];
            for (int i = 1; i < suffixArray.length; i++) { //Find the bwt output string based on the suffix array.
                if (suffixArray[i] == 0) {
                    originalStringIndex = i - 1;
                    j = 1;
                } else {
                    bwtOutput[i - 1 - j] = bytes[((suffixArray[i] - 1) + bytes.length) % bytes.length];
                    //System.out.println(suffixArray[i] + " : " + (char) bytes[((suffixArray[i] - 1) + bytes.length) % bytes.length] + " : " + bytes[((suffixArray[i] - 1) + bytes.length) % bytes.length]);
                }
            }

            /*
            int originalStringIndex = -1;
            int j = 0;
            byte[] bwtOutput = new byte[sA.length - 1];
            for (int i = 0; i < bytes.length; i++) {
                char c = sA[i].charAt(sA[i].length() - 1);

                if (c == '$') {
                    originalStringIndex = i;
                    j++;
                } else {
                    bwtOutput[i - j] = (byte) c;
                }
            }
            */

            new Huffman(256).encode(originalStringIndex, moveToFront(bwtOutput));
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] moveToFront(byte[] bytes) {
        int[] chars = new int[256];
        for (int i = 0; i < 256; i++) { //Construct alphabet representation.
            chars[i] = i;
        }

        for (int i = 0; i < bytes.length; i++) { //For each character in the bwt output.
            int index = -1;
            for (int j = 0; j < chars.length; j++) { //For each character in the alphabet representation.
                if (chars[j] == bytes[i]) { //If character in bwt output found in the alphabet representation.
                    index = j;
                    for (int n = j; n > 0; n--) { //Move everything to the left of the found character one step to the right.
                        chars[n] = chars[n - 1];
                    }

                    chars[0] = bytes[i]; //Set first character to the one that was found.
                }
            }

            bytes[i] = (byte) index;
        }

        return bytes;
    }

    private Integer[] constructSuffixArray(byte[] bytes) {
        int stringLength = bytes.length;
        Integer[] suffixes = new Integer[stringLength + 1];
        int[] groupNumbers = new int[stringLength + 1];
        int[] groupLengths = new int[stringLength + 1];
        int h = 0;

        initializeArrays(bytes, stringLength, suffixes, groupNumbers, groupLengths);

        //While there's unsorted groups left.
        while (groupLengths[0] > (-stringLength - 1)) {
            int sortedLength = 0;

            int startA, endA;
            for (startA = 0; startA <= stringLength; startA = endA) {
                if (groupLengths[startA] < 0) { //If sorted found.
                    endA = startA - groupLengths[startA]; //Skip past sorted group.
                    sortedLength += groupLengths[startA]; //Count sorted group length.
                } else { //Else if unsorted found.
                    endA = startA + groupLengths[startA]; //Set end interval of group.

                    if (sortedLength < 0) { //If we have found sorted.
                        groupLengths[startA + sortedLength] = sortedLength; //Set length of sorted group.
                        sortedLength = 0;
                    }

                    Arrays.sort(suffixes, startA, endA, new SuffixComparator(h, stringLength, groupNumbers));

                    for (int startB = startA, endB; startB < endA; startB = endB + 1) {
                        endB = startB; //Set end interval of group to look at.

                        while (endB + 1 < endA && compareSuffixes(suffixes[endB + 1], suffixes[startB], h, stringLength, groupNumbers) == 0) {
                            endB++;
                        }

                        if (startB == endB) { //Set group length to -1 if size is 1 (sorted).
                            groupLengths[startB] = -1;
                        } else { //Else set group length.
                            groupLengths[startB] = endB - startB + 1;
                        }
                    }

                    //Update group numbers.
                    for (int startC = startA, endC; startC < endA; startC = endC + 1) {
                        endC = startC + Math.abs(groupLengths[startC]) - 1;

                        for (int j = startC; j <= endC; j++) {
                            groupNumbers[suffixes[j]] = endC;
                        }
                    }
                }
            }

            if (sortedLength < 0) { //If sorted found.
                if (startA + sortedLength == -1) {
                    System.out.println("boop");
                }
                groupLengths[startA + sortedLength] = sortedLength; //Set length of sorted group. Start goes up while sortedLength goes down, 1:1.
            }

            if (h == 0) { //If on first phase (sort on first char).
                h = 1;
            } else { //Else double h each phase.
                h = 2 * h;
            }
        }

        return suffixes;
    }

    private void initializeArrays(byte[] bytes, int stringLength, Integer[] suffixes, int[] groupNumbers, int[] groupLengths) {
        for (int i = 0; i < stringLength; i++) {
            suffixes[i] = i;
            groupNumbers[i] = bytes[i]; //Initialize with chars for first sort, sorting on group comes later.
        }
        suffixes[stringLength] = stringLength;
        groupLengths[0] = stringLength + 1; //We only have one unsorted group at this point.
    }

    private int compareSuffixes(int a, int b, int h, int N, int[] groupNumbers) {
        int aKey = (a + h) < N ? groupNumbers[a + h] : -1;
        int bKey = (b + h) < N ? groupNumbers[b + h] : -1;
        return aKey - bKey;
    }

    private static class SuffixComparator implements Comparator<Integer> {
        int h;
        int N;
        int[] groupNumbers;

        public SuffixComparator(int h, int N, int[] groupNumbers) {
            this.h = h;
            this.N = N;
            this.groupNumbers = groupNumbers;
        }

        @Override
        public int compare(Integer a, Integer b) {
            int aKey = (a + h) < N ? groupNumbers[a + h] : -1;
            int bKey = (b + h) < N ? groupNumbers[b + h] : -1;
            return aKey - bKey;
        }
    }
}
