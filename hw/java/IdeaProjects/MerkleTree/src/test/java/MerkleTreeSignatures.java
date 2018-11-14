import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;


public class MerkleTreeSignatures {

    private static final int h = 8;

    private static Scanner in;

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

    private static boolean verify(int id, String data, String[] neighbours) throws NoSuchAlgorithmException {
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

    private static boolean verify(boolean second, String signature, String publicKey) throws NoSuchAlgorithmException, FileNotFoundException {
        byte[][] signArray = makeByteArray(signature);
        byte[][] keyArray = makeByteArrayFromKey(publicKey, second);

        for (int i = 0; i < 256; i++) {
            if (!Arrays.equals(sha256(signArray[i]), keyArray[i])) {
                return false;
            }
        }
        return true;
    }

    private static byte[] sha256(byte[] src) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(src);
    }


    private static String readS() {
        String kek = in.next();
        in.nextLine();
        return kek;
    }


    // decodes string from base64 and makes byte array 256 x 32
    private static byte[][] makeByteArray(String s) throws FileNotFoundException {
        byte[] array = base64(s);

        byte[][] result = new byte[256][32];


        for (int i = 0; i < 256; i++) {
            System.arraycopy(array, 32 * i, result[i], 0, 32);
        }
        return result;
    }

    // decodes string from base64 and makes byte array 256 x 32
    private static byte[][] makeByteArrayFromKey(String s, boolean second) {
        byte[] array = base64(s);
        byte[][] result = new byte[256][32];

        if (array.length != 2 * 256 * 32) {
            return new byte[0][0];
        }

        if (second) {
            for (int i = 0; i < 256; i++) {
                System.arraycopy(array, 256 * 32 + 32 * i, result[i], 0, 32);
            }
        } else {
            for (int i = 0; i < 256; i++) {
                System.arraycopy(array, 32 * i, result[i], 0, 32);
            }
        }

        return result;
    }

    // print all 1s
    private static void print1() {
        for (int i = 0; i < 256; i++) {
            System.out.print(1);
        }
        System.out.println();
    }

    // print all 0s
    private static void print0() {
        for (int i = 0; i < 256; i++) {
            System.out.print(0);
        }
        System.out.println();
    }

    // read 256 chars
    private static boolean[] readDocHash() throws FileNotFoundException{
        boolean[] result = new boolean[256];
        String input = readS();

        for (int i = 0; i < 256; i++) {
            result[i] = input.charAt(i) == '1';
            if (input.charAt(i) != '1' && input.charAt(i) != '0') {
                throw new NullPointerException("PIZDEC");
            }
        }
        return result;
    }

    // print document signature
    private static void printSignature(boolean[] Q, SecretKey sk) {
        byte[] result = new byte[256 * 32];

        for (int i = 0; i < 256; i++) {
            byte[] from;
            if (Q[i]) {
                from = sk.second[i];
            } else {
                from = sk.first[i];
            }
            add(i * 32, result, from);
        }
        System.out.println(base64(result));
    }

    private static class SecretKey{
        byte[][] first = new byte[256][32];
        byte[][] second = new byte[256][32];
        boolean knowFirst = false;
        boolean knowSecond = false;
    }

    private static SecretKey[] secretKeys = new SecretKey[256];

    static {
        for (int i = 0; i < 256; i++) {
            secretKeys[i] = new SecretKey();
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {

        in = new Scanner(System.in);

        root.hash = base64(readS());


        for (int i = 0; i < 1000 && in.hasNext(); i++) {

            // new signature key
            int k = in.nextInt();

            //PrintWriter pw = new PrintWriter("wowout");
            //pw.println(i);
            //pw.close();



            // make document hash
            SecretKey sk = secretKeys[k];
            if (sk.knowFirst) {
                print1();
            } else {
                print0();
            }

            String signature = readS();

            String publicKey = readS();


            String[] proof = new String[8];

            for (int j = 0; j < 8; j++) {
                proof[j] = readS();
            }

            boolean validProof = verify(k, publicKey, proof);

            boolean validSignature =  verify(sk.knowFirst, signature, publicKey);

            if (validSignature) {
                if (!sk.knowFirst) {
                    sk.first = makeByteArray(signature);
                    sk.knowFirst = true;
                } else if (!sk.knowSecond) {
                    sk.second = makeByteArray(signature);
                    sk.knowSecond = true;
                }
            }

            boolean[] Q;
            Q = readDocHash();

            if (validSignature && validProof) {
                System.out.println("YES");
            } else {
                System.out.println("NO");
            }


            if (sk.knowSecond) {
                System.out.println("YES");
                printSignature(Q, sk);
                in.close();
                return;
            } else {
                System.out.println("NO");
            }


        }
        in.close();

    }
}
