import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class Huffman {
    private final int[] frequencies = new int[256];

    public Huffman() {
    }

    public void encode(File file) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file.getName() + "-output"))
        ) {
            byte[] bytes = bufferedInputStream.readAllBytes();
            bufferedOutputStream.write(bytes.length);

            for (byte b : bytes){
                frequencies[b] = ++frequencies[b];
            }

            PriorityQueue<Node> q = new PriorityQueue<>();
            for (int i = 0; i < frequencies.length; i++){
                if (frequencies[i] > 0) {
                    q.add(new Node(frequencies[i], (char) i, null, null));
                }
            }

            while (q.size() > 1){
                Node left = q.poll();
                Node right = q.poll();

                Node parent = new Node(left.frequency + right.frequency, '\0', left, right);
                q.add(parent);
            }

            Node root = q.poll();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private record Node(int frequency, char key, Node left, Node right) implements Comparable<Node> {
        @Override
            public int compareTo(Node that) {
                return this.frequency - that.frequency;
            }
        }

}
