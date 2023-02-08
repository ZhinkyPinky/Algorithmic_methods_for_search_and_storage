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
        ArrayList<LinkedHashSet<Integer>> documentSetList = new ArrayList<>();
        LinkedHashSet<Integer> documentSet;

        ArrayList<Integer> results = new ArrayList<>();

        for (String key : keys) {
            if ((documentSet = trie.get(key)) == null) {
                return results;
            }

            documentSetList.add(documentSet);
        }

        if (documentSetList.size() == 1){
            return new ArrayList<>(documentSetList.get(0));
        }

        Integer[] firstKeyDocumentArray = documentSetList.get(0).toArray(new Integer[0]);
        int documentToMatch = firstKeyDocumentArray[0];
        int currentDocument = -1;
        boolean done;
        do {
            done = (documentToMatch != firstKeyDocumentArray[firstKeyDocumentArray.length - 1]);
            int matches = 0;
            for (LinkedHashSet<Integer> currentDocumentSet : documentSetList) {
                Integer[] currentKeyDocumentArray = currentDocumentSet.toArray(new Integer[0]);

                int index = binarySearch(currentKeyDocumentArray, documentToMatch);
                if (index != -1) {
                    currentDocument = currentKeyDocumentArray[index];
                } else {
                    documentToMatch = getNextDocument(firstKeyDocumentArray, documentToMatch);
                    break;
                }

                if (currentDocument == documentToMatch) {
                    matches++;
                }

                if (currentDocument > documentToMatch) {
                    documentToMatch = currentDocument;
                    break;
                }

                if (matches == keys.length) {
                    results.add(documentToMatch);
                    documentToMatch = getNextDocument(firstKeyDocumentArray, documentToMatch);
                    break;
                }
            }

        } while (done);

        return results;
    }

    private int getNextDocument(Integer[] documents, int document) {
        int index = binarySearch(documents, document);

        if(index != -1) {
            if (index == (documents.length - 1)){
                index--;
            }
            return documents[index + 1];
        } else {
            return documents[documents.length - 1];
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
