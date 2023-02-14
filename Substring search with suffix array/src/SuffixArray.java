import java.util.Arrays;
import java.util.Comparator;

public class SuffixArray {
    private Integer[] a;

    public SuffixArray(String s) {
        a = sort(s);
    }

    private Integer[] sort(String s) {
        int stringLength = s.length();
        Integer[] suffixes = new Integer[stringLength + 1];
        int[] groupNumbers = new int[stringLength + 1];
        int[] groupLengths = new int[stringLength + 1];
        int h = 0;

        initializeArrays(s, stringLength, suffixes, groupNumbers, groupLengths);

        //While there's unsorted groups left.
        while (groupLengths[0] > (-stringLength - 1)) {
            int sortedLength = 0;
            for (int startA = 0, endA; startA <= stringLength; startA = endA) {
                if (groupLengths[startA] < 0) { //If sorted found.
                    endA = startA - groupLengths[startA]; //Skip end past known sorted group.
                    sortedLength += groupLengths[startA]; //Count sorted.
                } else //Else if unsorted.
                {
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
                    for (int f = startA, g; f < endA; f = g + 1) {
                        g = f + Math.abs(groupLengths[f]) - 1;

                        for (int j = f; j <= g; j++) {
                            groupNumbers[suffixes[j]] = g;
                        }
                    }
                }

                if (sortedLength < 0) { //If sorted found.
                    groupLengths[startA + sortedLength] = sortedLength; //Set length of sorted group. Start goes up while sortedLength goes down, 1:1.
                }

                if (h == 0) { //If on first phase (sort on first char).
                    h = 1;
                } else { //Else double h each phase.
                    h = 2 * h;
                }
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
}
