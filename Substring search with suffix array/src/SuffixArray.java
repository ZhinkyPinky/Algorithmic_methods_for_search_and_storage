import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class SuffixArray {
    private final String S;
    private final Integer[] SUFFIX_ARRAY;
    private final int[] LCP;

    public SuffixArray(String s) {
        this.S = s;

        SUFFIX_ARRAY = constructSuffixArray(s);
        //System.out.println(Arrays.toString(SUFFIX_ARRAY));

        LCP = constructLCP(s, SUFFIX_ARRAY);
        //System.out.println(Arrays.toString(LCP));
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

    private int[] constructLCP(String s, Integer[] suffixArray) {
        int n = suffixArray.length;
        int[] inverse = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) {
            inverse[suffixArray[i]] = i;
        }
        int h = 0;

        for (int i = 0; i < n; i++) {
            int r = inverse[i];

            if (r > 0) {
                int j = suffixArray[r - 1];
                while ((i + h < S.length() && j + h < S.length()) && S.charAt(i + h) == S.charAt(j + h)) {
                    h++;
                }

                lcp[r] = h;
                if (h > 0) {
                    h--;
                }
            }
        }

        return lcp;
    }

    /*
    private void constructLCPLR(int index, int left, int right) {
        System.out.println("Index: " + index + " Left: " + left + " Right:" + right);
        if (left == right) {
            LCP_LR[index] = LCP[left];
            return;
        }

        int mid = (left + right) / 2;

        constructLCPLR(2 * index, left, mid);
        constructLCPLR(2 * index + 1, mid + 1, right);

        LCP_LR[index] = Math.min(LCP_LR[2 * index], LCP_LR[2 * index + 1]);
    }

    private void binarySearch(String target) {
        int left = 0;
        int right = SUFFIX_ARRAY.length - 1;
        int mid = (left + right) / 2;

        int k = 0;
        String suffix = S.substring(SUFFIX_ARRAY[mid]);
        for (int i = 0; i < target.length(); i++) {
            if (target.charAt(i) == suffix.charAt(i)) {
                k++;
            } else if (target.charAt(i) < suffix.charAt(i)){

            } else {

            }
        }
    }
     */

    public HashMap<Integer, String> findAllOccurrences(String target) {
        HashMap<Integer, String> occurrences = new HashMap<>();

        int i = binarySearch(target);
        int j = 0;
        while (true) {
            int index = i - j;
            if (index < 0 || index >= LCP.length - 1) {
                break;
            }

            if (LCP[index] == 0) {
                addOccurrence(occurrences, target, index);
                break;
            }

            if (target.length() <= LCP[i - j]) {
                addOccurrence(occurrences, target, index);
                j++;
            } else {
                addOccurrence(occurrences, target, index);
                break;
            }
        }

        j = 1;
        while (true) {
            int index = i + j;
            if (index >= LCP.length - 1) {
                break;
            }

            if (LCP[index] < target.length()) {
                break;
            }

            addOccurrence(occurrences, target, index);
            j++;
        }

        return occurrences;
    }

    private void addOccurrence(HashMap<Integer, String> occurrences, String target, int index) {
        if ((target.length() + SUFFIX_ARRAY[index]) + 10 >= S.length() - 1) {
            occurrences.put(SUFFIX_ARRAY[index], S.substring(SUFFIX_ARRAY[index]));
        } else {
            occurrences.put(SUFFIX_ARRAY[index], target + S.substring(SUFFIX_ARRAY[index] + target.length(), target.length() + SUFFIX_ARRAY[index] + 10));
        }
    }

    public int binarySearch(String target) {
        int left = 0;
        int right = SUFFIX_ARRAY.length - 1;
        int lcp = 0;

        int l = compareToSuffixLCP(target, left, 0)[1];
        int r = compareToSuffixLCP(target, right, 0)[1];

        while (left <= right) {
            int mid = (left + right) / 2;
            int mlr = Math.min(l, r);
            int[] diff = compareToSuffixLCP(target, SUFFIX_ARRAY[mid], mlr);
            lcp = Math.max(lcp, diff[1]);

            if (diff[0] < 0) {
                right = mid - 1;
                r = diff[1];
            } else if (diff[0] > 0) {
                left = mid + 1;
                l = diff[1];
            } else {
                left = mid;
                break;
            }
        }

        return left;
    }

    private int[] compareToSuffixLCP(String target, int i, int j) {
        int compare = 0;

        while (true) {
            if (j >= target.length()) {
                break;
            }

            if (i + j >= S.length()) {
                compare = 1;
                break;
            }

            compare = target.charAt(j) - S.charAt(i + j);
            if (compare != 0) {
                break;
            }

            j++;
        }

        return new int[]{compare, j};
    }
}
