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

            for (int i = 0; i < (output.length() / 8); i++) { //Output one byte at a time.
                bufferedOutputStream.write((byte) Integer.parseInt(output.substring((i * 8), ((i * 8) + 8)), 2));
            }

            bufferedOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Node constructTrie(byte[] bytes) {
        int[] frequencies = new int[R];
        for (byte b : bytes) { //Find frequencies of the bytes.
            frequencies[b] = ++frequencies[b];
        }

        PriorityQueue<Node> q = new PriorityQueue<>();
        for (int i = 0; i < frequencies.length; i++) { //Add nodes for the leafs to the priority queue.
            if (frequencies[i] > 0) {
                q.add(new Node(frequencies[i], (char) i, null, null, true));
            }
        }

        while (q.size() > 1) { //Smash together the two lowest frequency nodes into a single node and add it to the queue.
            Node left = q.poll();
            Node right = q.poll();

            Node parent = new Node(left.frequency + right.frequency, '\0', left, right, false);
            q.add(parent);
        }

        return q.poll(); //Return the root.
    }

    private String[] constructCodes(Node node, String[] codes, String code) {
        if (node.isLeaf) { //Add code if a leaf is reached.
            codes[node.key] = code;
            return codes;
        }

        constructCodes(node.left, codes, code + "0"); //Go left.
        constructCodes(node.right, codes, code + "1"); //Go right.

        return codes;
    }

    private String constructOutputString(byte[] bytes, String[] codes) {
        StringBuilder output = new StringBuilder(String.format("%32s", Integer.toBinaryString(bytes.length)).replaceAll(" ", "0"));  //Size of file
        for (String code : codes) { //Add codeword lengths to the output.
            if (code != null) { //Length of codeword in unary.
                output.append("1".repeat(code.length()));
                output.append("0");
                output.append(code);
            } else { //0:s if codeword is missing.
                output.append("0");
            }
        }

        for (byte b : bytes) { //Add codewords to the output.
            output.append(codes[b]);
        }

        //Make output divisible by 8.
        output.append("0".repeat(Math.max(0, (8 - output.length() % 8) % 8)));
        //output.append("0".repeat(Math.max(0, (output.length() % 8))));

        return output.toString();
    }

    private record Node(int frequency, char key, Node left, Node right, boolean isLeaf) implements Comparable<Node> {
        @Override
        public int compareTo(Node that) {
            return this.frequency - that.frequency;
        }
    }

}
