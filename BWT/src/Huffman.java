import java.io.*;
import java.util.PriorityQueue;

public class Huffman {
    private final int R;

    public Huffman(int R) {
        this.R = R;
    }

    public void encode(int originalStringIndex, byte[] bytes) {
        System.out.println(bytes.length);
        for (byte b : bytes) {
            System.out.print(b + " ");
        }
        System.out.println();

        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("BWT/output.bwt"))) {
            Node root = constructTrie(bytes);
            String[] codes = constructCodes(root, new String[R], "");
            String output = constructOutputString(originalStringIndex, bytes, codes);
            System.out.println(output);

            for (int i = 0; i < (output.length() / 8); i++) {
                bufferedOutputStream.write((byte) Integer.parseInt(output.substring((i * 8), ((i * 8) + 8)), 2));
            }

            bufferedOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Node constructTrie(byte[] bytes) {
        int[] frequencies = new int[R];
        for (byte b : bytes) {
            frequencies[b] = ++frequencies[b];
        }

        PriorityQueue<Node> q = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                q.add(new Node(frequencies[i], (char) i, null, null, true));
            }
        }

        while (q.size() > 1) {
            Node left = q.poll();
            Node right = q.poll();

            Node parent = new Node(left.frequency + right.frequency, '\0', left, right, false);
            q.add(parent);
        }

        return q.poll();
    }

    private String[] constructCodes(Node node, String[] codes, String code) {
        if (node.isLeaf) {
            System.out.println(node.key + " : " + code);
            codes[node.key] = code;
            return codes;
        }

        constructCodes(node.left, codes, code + "0");
        constructCodes(node.right, codes, code + "1");

        return codes;
    }

    private String constructOutputString(int originalStringIndex, byte[] bytes, String[] codes) {
        StringBuilder output = new StringBuilder(String.format("%32s", Integer.toBinaryString(bytes.length)).replaceAll(" ", "0"));
        output.append(String.format("%32s", Integer.toBinaryString(originalStringIndex)).replaceAll(" ", "0"));

        for (String code : codes) {
            if (code != null) {
                output.append("1".repeat(code.length()));
                output.append("0");
                output.append(code);
            } else {
                output.append("0");
            }
        }

        for (byte b : bytes) {
            System.out.println(codes[b]);
            output.append(codes[b]);
        }

        System.out.println((8 - output.length() % 8) % 8);
        output.append("0".repeat(Math.max(0, (8 - output.length() % 8) % 8)));
        return output.toString();
    }

    private record Node(int frequency, char key, Node left, Node right, boolean isLeaf) implements Comparable<Node> {
        @Override
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

}
