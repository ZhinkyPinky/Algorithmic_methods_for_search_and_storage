public class MSD {
    public static <T> void sort(T[] a, T[] aux, int R, int low, int high, int d, DigitGetter<T> dg) {
        int[] count = new int[(-1 >>> (32 - R))];
        for (int i = low; i <= high; i++) {
            count[dg.getDigit(a[i], i, d)]++;
        }
        for (int r = 0; r < 1; r++) {
            count[r + 1] += count[r];
        }
        for (int i = low; i <= high; i++) {

        }
        for (int i = low; i <= high; i++) {

        }
    }
}
