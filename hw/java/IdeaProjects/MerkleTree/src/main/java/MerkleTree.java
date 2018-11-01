import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Scanner;


public class MerkleTree {

    static int h;
    static String root;

    static String[] neighbours;
    static int id;
    static String data;

    static byte[] acabSrc;



    private class Node {
        String hash;
        Node parent;
        Node l, r;
        Node(Node parent) {
            this.parent = parent;

        }
    }

    static byte[] base64(String s) {
        return Base64.getDecoder().decode(s);
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




            //System.out.println(index + " " + hash.length + " " + second.length);




            if (hash.length == 0 && second.length == 0) {
                hash = new byte[0];
            } else {
                hash = sha256(src);
            }
            //System.out.println("hash: " + Base64.getEncoder().encodeToString(hash));
        }
        return Arrays.equals(hash, base64(root));
    }

    static byte[] sha256(byte[] src) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(src);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {

        /*
        byte[] hash = new byte[0];
        byte[] dataBlock = base64("acab");
        acabSrc = new byte[dataBlock.length + 1];
        acabSrc[0] = 0;
        add(1, acabSrc, dataBlock);
        byte[] second = sha256(acabSrc);

        byte[] src = new byte[hash.length + second.length + 2];

        int index = 0;

        src[index++] = 1;
        add(index, src, hash);
        index += hash.length;
        src[index++] = 2;
        add(index, src, second);
        index += second.length;

        System.out.println(Base64.getEncoder().encodeToString(sha256(src)));

        */

        Scanner in = new Scanner(System.in);

        h = in.nextInt();

        in.nextLine();
        root = in.nextLine();

        int q = in.nextInt();

        for (int i = 0; i < q; i++) {
            neighbours = new String[h];

            id = in.nextInt();

            data = in.next();
            in.nextLine();

            for (int j = 0; j < h; j++) {
                neighbours[j] = in.nextLine();
                //System.out.println(neighbours[j]);
            }
            if (process(id, data, neighbours)) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }
        }
    }
}
