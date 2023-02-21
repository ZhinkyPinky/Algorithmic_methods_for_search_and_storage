import java.io.*;
import java.util.PriorityQueue;

public class Huffman {
    private final int R;

    public Huffman(int R) {
        this.R = R;
    }

    public void encode(File file) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("Huffman coding/huffman-output.huff"))
        ) {
            byte[] bytes = bufferedInputStream.readAllBytes();
            Node root = constructTrie(bytes);
            String[] codes = constructCodes(root, new String[R], "");
            String output = constructOutputString(bytes, codes);

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

    private String[] constructCodes(Node parent, String[] codes, String code) {
        if (parent.isLeaf) {
            codes[parent.key] = code;
            return codes;
        }

        constructCodes(parent.left, codes, code + "0");
        constructCodes(parent.right, codes, code + "1");

        return codes;
    }

    private String constructOutputString(byte[] bytes, String[] codes) {
        StringBuilder output = new StringBuilder(String.format("%32s", Integer.toBinaryString(bytes.length)).replaceAll(" ", "0"));
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
            output.append(codes[b]);
        }

        output.append("0".repeat(Math.max(0, (output.length() % 8))));

        return output.toString();
    }

    private record Node(int frequency, char key, Node left, Node right, boolean isLeaf) implements Comparable<Node> {
        @Override
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

}
