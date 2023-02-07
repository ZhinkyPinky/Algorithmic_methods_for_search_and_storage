import java.util.*;

public class InvertedFile {
    Trie<LinkedHashSet<Integer>> trie = new Trie<>();

    public void add(int document, String key) {
        LinkedHashSet<Integer> hashSet = trie.get(key);
        if (trie.get(key) == null) {
            trie.put(key, new LinkedHashSet<>(List.of(document)));
        } else {
            hashSet.add(document);
        }
    }

    public LinkedHashSet<Integer> getDocuments(String keys) {
        return trie.get(keys);
    }

    public ArrayList<Integer> getDocumentsWithAll(String[] keys) {
        ArrayList<Integer> results = new ArrayList<>();
        ArrayList<LinkedHashSet<Integer>> documents = new ArrayList<>();
        LinkedHashSet<Integer> document;
        for (String key : keys) {
            if ((document = trie.get(key)) == null) {
                return results;
            }

            documents.add(document);
        }

        Integer[] bla = documents.get(0).toArray(new Integer[0]);

        int d = bla[0];
        int f;
        do {
            int matches = 0;
            for (int i = 0; i < documents.size(); i++) {
                Integer[] a = documents.get(i).toArray(new Integer[0]);

                int index = binarySearch(a, d);
                if (index != -1) {
                    f = a[index];
                } else {
                    int newIndex = binarySearch(bla, d) + 1;
                    if (newIndex < bla.length) {
                        d = bla[newIndex];
                    }
                    break;
                }

                if (f == d) {
                    matches++;
                }

                if (f > d) {
                    d = f;
                    break;
                }

                if (matches == keys.length) {
                    results.add(d);

                    int newIndex = binarySearch(bla, d) + 1;
                    if (newIndex < bla.length) {
                        d = bla[newIndex];
                    }
                    break;
                }
            }

        } while (d != bla[bla.length - 1]);

        return results;
    }

    private int thingSearch(Integer[] array, int i, int target) {
        int j = 1;
        while (true) {
            if (array[i].compareTo(target) == 0) {
                return i;
            }

            if (array[i].compareTo(target) < 0) {
                i += j;
                j *= 2;
            }

            if (array[i].compareTo(target) > 0) {
                return thingSearch(array, i - (j / 2), target);
            }
        }
    }

    private int binarySearch(Integer[] array, int target) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (array[mid] == target) {
                return mid;
            }

            if (array[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return (left == array.length) ? -1 : left;
    }
}
