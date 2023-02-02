import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        long timeBefore = System.currentTimeMillis();
        sortTestString(new File("Radix Sort/bible-lines.txt"));
        //sortTestString(new File("Radix Sort/test.txt"));
        //sortTestString(new File("Radix Sort/strangestrings.txt"));
        //sortTestInt(new File("Radix Sort/ints.txt"));
        //sortTestInt(new File("Radix Sort/someones.txt"));
        //sortTestInt(new File("Radix Sort/test.txt"));
        long timeAfter = System.currentTimeMillis();
        System.out.println((timeAfter - timeBefore) / 1000.0);
    }

    private static void sortTestInt(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Radix Sort/testOutput.txt"))) {
            ArrayList<Integer> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add((int) Long.parseLong(line));
            }

            Integer[] a = MSD.sort(aTemp.toArray(new Integer[0]), new Integer[aTemp.size()], (1024), 0, aTemp.size() - 1, 0, 10, (element, d, bits) -> {
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

            String[] a = aTemp.toArray(new String[0]);
            String[] aux = new String[a.length];
            int b = 16;
            int R = (-1 >>> (32 - b));
            int low = 0;
            int high = a.length - 1;

            String[] aSorted =
                    MSD.sort(a, aux, R, low, high, 0, b, (element, d, bits) -> {
                        int bitsFrom = d * bits;
                        if (bitsFrom >= element.length() * 8) {
                            return -1;
                        }

                        int bitsTo = ((d + 1) * bits) - 1;
                        if (bitsTo > element.length() * 8) {
                            bitsTo = (element.length() * 8) - 1;
                        }

                        int outputBits = 0;
                        for (int i = bitsFrom / 8; i <= bitsTo / 8; i++) {
                            if (i == bitsFrom / 8) {
                                if (i == bitsTo / 8) {
                                    return ((-1 >>> 32 - (bitsTo % 8)) & element.charAt(bitsTo / 8)) >>> (bitsFrom % 8);
                                }
                                outputBits = element.charAt(bitsFrom / 8) >>> ((bitsFrom % 8));
                            } else if (i == bitsTo / 8) {
                                outputBits = (outputBits << ((bitsTo % 8))) | (element.charAt(bitsTo / 8) >>> (7 - (bitsTo % 8)));
                            } else {
                                outputBits = (outputBits << 8) | element.charAt(i);
                            }
                        }

                        return outputBits;
                    });

            for (String s : aSorted) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sortTestStringOld(File file) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Radix Sort/testOutput.txt"))) {
            ArrayList<String> aTemp = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                aTemp.add(line);
            }

            String[] a = aTemp.toArray(new String[0]);
            String[] aux = new String[a.length];
            int b = 16;
            int R = (-1 >>> (32 - b));
            int low = 0;
            int high = a.length - 1;

            String[] aSorted =
                    MSD.sort(a, aux, R, low, high, 0, b, (element, d, bits) -> {
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
                        for (int i = bitsFrom / 8; i <= bitsTo / 8; i++) {
                            if (i == bitsFrom / 8) {
                                if (i == bitsTo / 8) {
                                    return ((-1 >>> 32 - (bitsTo % 8)) & bytes[bitsTo / 8]) >>> (bitsFrom % 8);
                                }
                                outputBits = bytes[bitsFrom / 8] >>> ((bitsFrom % 8));
                            } else if (i == bitsTo / 8) {
                                outputBits = (outputBits << ((bitsTo % 8))) | (bytes[bitsTo / 8] >>> (7 - (bitsTo % 8)));
                            } else {
                                outputBits = (outputBits << 8) | bytes[i];
                            }
                        }

                        return outputBits;
                    });

            for (String s : aSorted) {
                bufferedWriter.write(s + "\n");
            }
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}