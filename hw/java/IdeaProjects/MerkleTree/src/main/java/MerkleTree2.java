import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;


public class MerkleTree2 {

    private static int h;

    private static String cdata;

    private static class Node {
        byte[] hash;
        Node parent;
        Node l, r;
        String data;
        Node(Node parent, boolean ld) {
            this.parent = parent;
            if (parent == null) {
                return;
            }
            if (ld) {
                this.parent.r = this;
            } else {
                this.parent.l = this;
            }
        }
    }

    private static Node root = new Node(null, true);

    private static byte[] base64(String s) {
        return Base64.getDecoder().decode(s);
    }

    private static String base64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    private static void add(int index, byte[] src, byte[] array) {
        for (byte anArray : array) {
            src[index++] = anArray;
        }
    }

    private static boolean process(int id, String data, String[] neighbours) throws NoSuchAlgorithmException {
        byte[] dataArray = base64(data);
        byte[] hash = new byte[dataArray.length + 1];
        hash[0] = 0;
        add(1, hash, dataArray);
        hash = sha256(hash);

        if (data.equals("null")) {
            hash = new byte[0];
        }



        for (int i = 0; i < h; i++) {
            byte[] second = new byte[0];
            if (!neighbours[i].equals("null")) {
                second = base64(neighbours[i]);
            }

            if (id % 2 == 1) {
                byte[] buf = hash;
                hash = second;
                second = buf;
            }

            id /= 2;


            byte[] src = new byte[2 + hash.length + second.length];
            int index = 0;

            src[index++] = 1;
            add(index, src, hash);
            index += hash.length;
            src[index++] = 2;
            add(index, src, second);

            if (hash.length == 0 && second.length == 0) {
                hash = new byte[0];
            } else {
                hash = sha256(src);
            }
        }
        return Arrays.equals(hash, base64(Base64.getEncoder().encodeToString(root.hash)));
    }

    private static byte[] sha256(byte[] src) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(src);
    }

    /* Tree methods */

    private static int[] p = new int[30];

    static {
        p[0] = 1;
        for (int i = 1; i < 30; i++) {
            p[i] = p[i - 1] * 2;
        }
    }

    private static void add(int id, String data) throws NoSuchAlgorithmException {
        Node node = root;
        Node parent = null;
        int i = 0;
        boolean ld = false;

        for (; node != null; i++) {
            parent = node;
            if (id >= p[h - i - 1]) {
                id -= p[h - i - 1];
                node = node.r;
                ld = true;
            } else {
                node = node.l;
                ld = false;
            }
        }
        for (; i < h; i++) {
            node = new Node(parent, ld);
            parent = node;
            if (id >= p[h - i - 1]) {
                id -= p[h - i - 1];
                ld = true;
            } else {
                ld = false;
            }
        }

        node = new Node(parent, ld);

        byte[] dataSrc = new byte[base64(data).length + 1];
        dataSrc[0] = 0;
        add(1, dataSrc, base64(data));
        node.hash = sha256(dataSrc);
        //System.out.println("\nLEAF HASH: " + base64(node.hash));
        node.data = data;
    }

    private static String[] prove(int id) {
        String[] result = new String[h];
        Node node = root;
        int i = 0;

        for (; node != null && i < h; i++) {
            if (id >= p[h - i - 1]) {
                id -= p[h - i - 1];
                if (node.l == null) {
                    result[i] = "null";
                } else {
                    result[i] = base64(node.l.hash);
                }

                node = node.r;

            } else {
                if (node.r == null) {
                    result[i] = "null";
                } else {
                    result[i] = base64(node.r.hash);
                }

                node = node.l;
            }
        }
        if (i == h && node != null) {
            cdata = node.data;
        } else {
            cdata = "null";
        }

        for (; i < h; i++) {
            result[i] = "null";
        }
        return result;
    }

    private static byte[] countHashes(Node node) throws NoSuchAlgorithmException {
        if (node == null) {
            return new byte[0];
        }
        byte[] f = countHashes(node.l);
        byte[] s = countHashes(node.r);
        if (f.length == 0 && s.length == 0) {
            if (node.data != null) {
                return node.hash;
            }
            node.hash = new byte[0];
            return node.hash;
        }

        byte[] res = new byte[f.length + s.length + 2];
        res[0] = 1;
        add(1, res, f);
        res[f.length + 1] = 2;
        add(f.length + 2, res, s);
        node.hash = sha256(res);
        return node.hash;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        Scanner in = new Scanner(System.in);

        h = in.nextInt();

        int n = in.nextInt();

        for (int i = 0; i < n; i++) {
            int id = in.nextInt();
            String data = in.next();
            add(id, data);
            in.nextLine();
        }

        countHashes(root);

        int q = in.nextInt();

        for (int i = 0; i < q; i++) {
            int id = in.nextInt();
            String[] res = prove(id);
            System.out.println(id + " " + cdata);
            for (int e = h - 1; e >= 0; e--) {
                System.out.println(res[e]);
            }
        }
    }
}
