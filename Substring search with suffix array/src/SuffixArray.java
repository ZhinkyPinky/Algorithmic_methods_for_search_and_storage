import java.util.Arrays;
import java.util.Comparator;

public class SuffixArray {
    private final Integer[] suffixArray;
    private final int[] lcpArray;

    public SuffixArray(String s) {
        suffixArray = constructSuffixArray(s);
        System.out.println(Arrays.toString(suffixArray));
        lcpArray = constructLCPArray(s, suffixArray);
        System.out.println(Arrays.toString(lcpArray));
    }

    private Integer[] constructSuffixArray(String s) {
        int stringLength = s.length();
        Integer[] suffixes = new Integer[stringLength + 1];
        int[] groupNumbers = new int[stringLength + 1];
        int[] groupLengths = new int[stringLength + 1];
        int h = 0;

        initializeArrays(s, stringLength, suffixes, groupNumbers, groupLengths);

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

    private void initializeArrays(String s, int stringLength, Integer[] suffixes, int[] groupNumbers, int[] groupLengths) {
        for (int i = 0; i < stringLength; i++) {
            suffixes[i] = i;
            groupNumbers[i] = s.charAt(i); //Initialize with chars for first sort, sorting on group comes later.
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

    private int[] constructLCPArray(String s, Integer[] suffixArray) {
        int[] inverse = new int[suffixArray.length];
        int[] lcp = new int[suffixArray.length];

        for (int i = 0; i < suffixArray.length; i++) {
            inverse[suffixArray[i]] = i;
        }
        int h;
        for (int i = 0; i < suffixArray.length; i++) {
            int r = inverse[i];
            h = 0;
            if (r > 0) {
                int j = suffixArray[r - 1];

                if (j != s.length()) {
                    while (s.charAt(i + h) == s.charAt(j + h)) {
                        h++;

                        if ((i + h) == s.length() || (j + h) == s.length()) {
                            break;
                        }
                    }
                }

                lcp[r - 1] = h;
            }
        }

        return lcp;
    }

    public void binarySearch(String target, String s) {
        int left = 0;
        int right = suffixArray.length - 1;
        k, lcp = 0;

        int l = compareToSuffixLCP(target, s, left, 0);
        int r = compareToSuffixLCP(target, s, right, 0);

        while (left <= right) {
            int mid = left + (right - left) / 2;
            int mlr = Math.min(l, r);
            int diff = compareToSuffixLCP(target, s, suffixArray[mid], mlr);
            int lcp = Math.max(lcp, di)
        }

    }

    private int compareToSuffixLCP(String target, String s, int left, int i) {
        int compare = 0;

        while (true) {
            if ()
        }
    }
}
