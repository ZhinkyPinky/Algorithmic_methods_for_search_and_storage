import java.util.HashMap;
import java.util.Objects;

public class Trie {
    private Node root;
    private final HashMap<ParentChildConnection, Node> childMap = new HashMap<>();

    public Trie() {
        root = new Node("");
    }

    public int get(String key) {
        Node node = get(root, key, 0);
        if (node == null) return -1;
        return node.value;
    }

    private Node get(Node node, String key, int d) {
        if (node == null) return null;
        if (node.label == null) return null;
        if (!key.startsWith(node.label, d)) return null;
        if (key.length() == d + node.label.length()) return node;

        return get(getChild(node, key.charAt(d + node.label.length())), key, d + node.label.length());
    }

    private Node getChild(Node parent, char childLabelFirstChar) {
        return childMap.get(new ParentChildConnection(parent, childLabelFirstChar));
    }

    public void put(String key, int value) {
        Node parentNode = root;
        int keyPos = 0;
        int depth = 0;

        while (true) {
            if (parentNode != root && key.charAt(keyPos) == parentNode.label.charAt(keyPos - depth)) { //Parent label match key.
                if (keyPos == (depth + parentNode.label.length() - 1)) { //End of parent-node label reached.
                    depth += parentNode.label.length();

                    if (keyPos == (key.length() - 1)) { //Update value if key already exists.
                        parentNode.value = value;
                        return;
                    }

                    Node childNode = getChild(parentNode, key.charAt(keyPos));
                    if (childNode == null) { //Create new node with value if key doesn't already exist.
                        childMap.put(new ParentChildConnection(parentNode, key.charAt(keyPos)), new Node(key.substring(keyPos), value));
                        return;
                    } else { //Move to new parent-node if child with continuation of key exists.
                        parentNode = childNode;
                    }
                }
            } else { //Parent label doesn't match key.
                if (parentNode != root) {
                    childMap.put( //New node to split the parent node.
                            new ParentChildConnection(parentNode, parentNode.label.charAt(keyPos - depth)),
                            new Node(parentNode.label.substring(keyPos - depth)));

                    //Update the parentNode.
                    parentNode.label = parentNode.label.substring(0, keyPos - depth);
                    parentNode.value = -1;
                }

                childMap.put( //New node to add the new key.
                        new ParentChildConnection(parentNode, key.charAt(keyPos)),
                        new Node(key.substring(keyPos), value));
            }

            keyPos++;
        }

    }

    private static class Node {
        private String label;
        private int value;

        public Node(String label) {
            this.label = label;
            this.value = -1;
        }

        public Node(String label, int value) {
            this.label = label;
            this.value = value;
        }
    }

    private record ParentChildConnection(Node parent, char childLabelFirstChar) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ParentChildConnection that = (ParentChildConnection) o;
            return parent.equals(that.parent) && childLabelFirstChar == that.childLabelFirstChar;
        }

        @Override
        public int hashCode() {
            return Objects.hash(parent, childLabelFirstChar);
        }
    }
}
