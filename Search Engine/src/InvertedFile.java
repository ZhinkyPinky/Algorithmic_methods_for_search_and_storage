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

    public ArrayList<Integer> getDocumensWithAll(String[] keys) {
        ArrayList<Integer> results = new ArrayList<>();
        ArrayList<LinkedHashSet<Integer>> documents = new ArrayList<>();
        LinkedHashSet<Integer> document;
        for (String key : keys) {
            if ((document = trie.get(key)) == null) {
                return null;
            }

            documents.add(document);
        }

        document = documents.get(0);

        document.forEach(d -> {
            int matches = 0;
            for (int i = 1; i < keys.length; i++) {
                Integer[] documentArray = documents.get(i).toArray(new Integer[0]);
                int j = 1;
                while (true) {
                    if (documentArray[j - 1].compareTo(d) > 0) {
                        j *= 2;
                    }

                    if (documentArray[j - 1].compareTo(d) == 0) {
                        break;
                    }
                }

                int f = binarySearch(documentArray, 0, documentArray.length, d);
            }

            if (matches == keys.length) {
                results.add(d);
            }
        });

        return results;
    }

    private int binarySearch(Integer[] array, int left, int right, int target) {
        if (right >= left) {
            int mid = left + (right - left) / 2;

            if (array[mid] == target) {
                return mid;
            }

            if (array[mid] > target) {
                return binarySearch(array, left, mid - 1, target);
            }

            return binarySearch(array, mid + 1, right, target);
        }

        return -1;
    }
}
