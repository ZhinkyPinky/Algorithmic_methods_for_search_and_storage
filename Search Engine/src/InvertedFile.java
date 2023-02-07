import java.util.*;
import java.util.stream.Collectors;

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
                return results;
            }

            documents.add(document);
        }

        if (documents.size() == 1) {
            return new ArrayList<>(documents.get(0));
        }

        Integer[] bla = documents.get(0).toArray(new Integer[0]);
        int index = 0;
        int d = bla[index];
        int f;
        while (true) {
            if (index != -1) {
                d = bla[index];
            }
            System.out.println(d);
            int matches = 0;
            for (int i = 0; i < documents.size(); i++) {
                f = binarySearch(documents.get(i).toArray(new Integer[0]), 0, documents.get(i).size(), d);

                if (f == d) {
                    matches++;
                }

                if (f > d) {
                    d = f;
                    index = -1;
                    break;
                }

                if (matches == keys.length) {
                    results.add(d);
                    for (int n = 0; n < bla.length; n++) {
                        if (bla[n] == d) {
                            index = n + 1;
                            System.out.println(index);
                        }
                    }
                    break;
                }
            }

            if (d == 4){
                System.out.println("doop");
            }
            if (d == bla[bla.length - 1]) {
                break;
            }
        }

        return results;
            /*
            Integer[] bla = documents.get(0).toArray(new Integer[0]);


        for (int n = 0; n < bla.length; n++) {
            int matches = 0;
            int d = bla[n];

            for (int i = 0; i < keys.length; i++) {
                Integer[] documentArray = documents.get(i).toArray(new Integer[0]);

                int f = binarySearch(documentArray, 0, documentArray.length, d);
                if (f == d) {
                    matches++;
                }
                if (f > d) {
                    d = f;
                    matches = 0;
                }


                for (int j = 0; j < documentArray.length; j++) {
                    if (documentArray[j] == d) {
                        matches++;
                        break;
                    }
                    if (documentArray[j] > d) {
                        d = documentArray[j];
                        break;
                    }
                }


                if (matches == keys.length) {
                    results.add(d);
                }
            }
            */
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

    private int binarySearch(Integer[] array, int left, int right, int target) {
        if (right >= left) {
            if (right - left <= 1) {
                if (array[left] < target) {
                    return array[right];
                }

                return array[left];
            }

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
