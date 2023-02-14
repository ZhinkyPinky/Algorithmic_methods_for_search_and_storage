import java.util.Arrays;

public class SuffixArray {
    private int[] a;

    public SuffixArray(String s) {
        a = sort(s);
    }

    private int[] sort(String s) {
        int N = s.length();
        int[] suffixes = new int[N + 1];
        int[] groupNumbers = new int[N + 1];
        int[] groupLengths = new int[N + 1];
        int h = 0;

        for (int i = 0; i < N; i++) {
            suffixes[i] = i;
            groupNumbers[i] = s.charAt(i); //Initialize with chars for first sort, sorting on group comes later.
        }
        suffixes[N] = N;

        groupLengths[0] = N + 1; //We only have one unsorted group at this point.
        while (groupLengths[0] > (-N - 1)) {
            int sl = 0;
            int k;
            for (int i = 0; i <= N; i = k) {
                if (groupLengths[i] < 0) {
                    k = i - groupLengths[i];
                    sl += groupLengths[i];
                } else {
                    k = i + groupLengths[i];

                    if (sl < 0) {
                        groupLengths[i + sl] = sl;
                        sl = 0;
                    }

                    Arrays.sort(suffixes, i, k);

                    for (int f = i, g; f < k; f = g + 1) {
                        g = f;

                        while (g + 1 < k && compareSuffixes(suffixes[g + 1], suffixes[f], h, N, groupNumbers) == 0) {
                            g++;
                        }

                        if (f == g) { //Set group length to -1 if size is 1 (sorted).
                            groupLengths[f] = -1;
                        } else { //Else set group length.
                            groupLengths[f] = g - f + 1;
                        }
                    }

                    for (int f = i, g; f < k; f = g + 1) {
                        g = f + Math.abs(groupLengths[f]) - 1;

                        for (int j = f; j <= g; j++) {
                            groupNumbers[suffixes[j]] = g;
                        }
                    }
                }

                if (sl < 0) {
                    if ((i  + sl) == -1){
                        System.out.println("beep");
                    }
                    groupLengths[i + sl] = sl;
                }

                if (h == 0) {
                    h = 1;
                } else {
                    h = 2 * h;
                }
            }
        }

        return suffixes;
    }

    private int compareSuffixes(int a, int b, int h, int N, int[] groupNumbers) {
        int skey = (a + h) < N ? groupNumbers[a + h] : -1;
        int bkey = (b + h) < N ? groupNumbers[b + h] : -1;
        return skey - bkey;
    }
}
