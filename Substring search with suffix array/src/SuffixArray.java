import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

public class SuffixArray {
    private final String S;
    private final Integer[] SUFFIX_ARRAY;
    private final int[] LCP;

    public SuffixArray(String s) {
        this.S = s;

        SUFFIX_ARRAY = constructSuffixArray(s);
        System.out.println(Arrays.toString(SUFFIX_ARRAY));

        LCP = constructLCP(s, SUFFIX_ARRAY);
        System.out.println(Arrays.toString(LCP));
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

        int k = 0;
        for (int i = 0; i < n; i++) {
            /*
            if (inverse[i] == n - 1){
                k = 0;
                continue;
            }

            int j = suffixArray[inverse[i] + 1];
            while ((((i + k) < n - 1) && ((j + k) < n - 1)) && s.charAt(i + k) == s.charAt(j + k)){
                k++;
            }

            lcp[inverse[i]] = k;

            if (k > 0){
                k--;
            }
             */

            int r = inverse[i];
            int h = 0;
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

                lcp[r] = h;
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

    public LinkedList<Integer> finalAllOccurrences(String target) {
        LinkedList<Integer> suffixes = new LinkedList<>();

        int i = binarySearch(target);
        suffixes.addFirst(i);

        int j = 1;
        while (i + j < LCP.length && LCP[i + j] != 0) {
            suffixes.addLast(i + j++);
        }

        /*
        if(LCP[i] != 0) {
            j = 1;
            while (LCP[i - j] != 0) {
                suffixes.addFirst(i - j++);
            }

            suffixes.addFirst(i - j);
        }
         */

        return suffixes;
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
