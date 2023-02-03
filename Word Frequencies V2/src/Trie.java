import java.util.HashMap;
import java.util.Objects;

public class Trie {
    private Node root;
    private final HashMap<ParentChildConnection, Node> childMap = new HashMap<>();

    private Node getChild(Node parent, char childLabelFirstChar) {
        return childMap.get(new ParentChildConnection(parent, childLabelFirstChar));
    }

    public int get(String key) {
        Node node = get(root, key, 0);
        if (node == null) return -1;
        return node.value;
    }

    private Node get(Node node, String key, int d) {
        if (node == null) return null;
        if (!key.startsWith(node.label, d)) return null;
        if (key.length() == d + node.label.length()) return node;

        return get(getChild(node, key.charAt(d + node.label.length())), key, d + node.label.length());
    }

    public void put(String key){

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
