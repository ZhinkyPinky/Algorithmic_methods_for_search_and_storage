import java.util.Arrays;

public class MSD {
    private static int bla = 0;

    public static <T> T[] sort(T[] a, T[] aux, int R, int low, int high, int d, DigitGetter<T> digitGetter) {
        if (high <= low) return a;

        int[] count = new int[R + 2];

        for (int i = low; i <= high; i++) {
            count[digitGetter.getDigit(a[i], d, 6) + 2]++;
        }

        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }

        for (int i = low; i <= high; i++) {
            aux[count[digitGetter.getDigit(a[i], d, 6) + 1]++] = a[i];
        }

        for (int i = low; i <= high; i++) {
            a[i] = aux[i - low];
        }

        //System.out.println(Arrays.toString(a));
        //System.out.println(Arrays.toString(aux));
        //System.out.println(Arrays.toString(count));

        for (int r = 0; r < R; r++) {
            sort(a, aux, R, low + count[r], low + count[r + 1] - 1, d + 1, digitGetter);
        }

        //System.out.println(Arrays.toString(a));
        return a;
    }
}
