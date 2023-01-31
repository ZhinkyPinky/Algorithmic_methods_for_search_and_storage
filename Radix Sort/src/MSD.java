public class MSD {
    public static <T> T[] sort(T[] a, T[] aux, int R, int low, int high, int d, int bits, DigitGetter<T> digitGetter) {
        if (high <= low) return a;

        int[] count = new int[R + 2];

        for (int i = low; i <= high; i++) {
            count[digitGetter.getDigit(a[i], d, bits) + 2]++;
        }

        for (int r = 0; r < R + 1; r++) {
            count[r + 1] += count[r];
        }

        for (int i = low; i <= high; i++) {
            aux[count[digitGetter.getDigit(a[i], d, bits) + 1]++] = a[i];
        }

        for (int i = low; i <= high; i++) {
            a[i] = aux[i - low];
        }

        for (int r = 0; r < R; r++) {
            sort(a, aux, R, low + count[r], low + count[r + 1] - 1, d + 1, bits, digitGetter);
        }

        return a;
    }
}
