import java.util.HashMap;
import java.util.Objects;

public class Trie<T> {
    private final Node<T> root = new Node<>();
    private final HashMap<ParentChildConnection<T>, Node<T>> childMap = new HashMap<>();

    public Trie() {
    }

    public T get(String key) {
        Node<T> node = get(root, key, 0);
        if (node == null) return null;
        return node.value;
    }

    private Node<T> get(Node<T> node, String key, int d) {
        if (node == null) return null;
        if (node.label == null) return null;
        if (!key.startsWith(node.label, d)) return null;
        if (key.length() == d + node.label.length()) return node;

        return get(getChild(node, key.charAt(d + node.label.length())), key, d + node.label.length());
    }

    private Node<T> getChild(Node<T> parent, char childLabelFirstChar) {
        return childMap.get(new ParentChildConnection<>(parent, childLabelFirstChar));
    }

    public void put(String key, T value) {
        Node<T> parentNode = root;
        int keyPos = 0;
        int depth = 0;

        while (true) {
            if (keyPos == (depth + parentNode.label.length())) { //End of parent-node label reached.
                if (keyPos == key.length()) { //Update value if key already exists.
                    updateValue(parentNode, value);
                    break;
                }

                Node<T> childNode = getChild(parentNode, key.charAt(keyPos));
                if (childNode == null) { //Create new node with value if key doesn't already exist.
                    addNewNode(parentNode, key, keyPos, value);
                    break;
                } else { //Move to new parent-node if child with continuation of key exists.
                    depth += parentNode.label.length();
                    parentNode = childNode;
                }
            } else if (keyPos == key.length()) { //Key is substring of existing substring.
                splitNode(parentNode, keyPos, depth);
                parentNode.value = value;
                break;
            } else if (key.charAt(keyPos) != parentNode.label.charAt(keyPos - depth)) { //Parent label doesn't match key.
                splitNode(parentNode, keyPos, depth);
                addNewNode(parentNode, key, keyPos, value);
                break;
            }

            keyPos++;
        }
    }

    private void addNewNode(Node<T> parentNode, String key, int keyPos, T value) {
        childMap.put(new ParentChildConnection<>(parentNode, key.charAt(keyPos)), new Node<>(key.substring(keyPos), value));
    }

    private void splitNode(Node<T> parentNode, int keyPos, int depth) {
        childMap.put( //New node to split the parent node.
                new ParentChildConnection<>(parentNode, parentNode.label.charAt(keyPos - depth)),
                new Node<>(parentNode.label.substring(keyPos - depth), parentNode.value));

        parentNode.label = parentNode.label.substring(0, keyPos - depth);
        parentNode.value = null;
    }


    private void updateValue(Node<T> node, T value) {
        node.value = value;
    }

    private static class Node<T> {
        private String label = "";
        private T value;

        public Node() {
        }

        public Node(String label) {
            this.label = label;
        }

        public Node(String label, T value) {
            this.label = label;
            this.value = value;
        }
    }

    private record ParentChildConnection<T>(Node<T> parent, char childLabelFirstChar) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParentChildConnection<T> that = (ParentChildConnection<T>) o;
            return parent.equals(that.parent) && childLabelFirstChar == that.childLabelFirstChar;
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, childLabelFirstChar);
        }
    }
}
