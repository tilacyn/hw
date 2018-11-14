import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class OraclePaddingAttack1 {

    private static String cipher;

    private static byte[] IV;

    private static Scanner in;

    private static String message = "";


    // intermediate state
    private static byte[] IS = new byte[16];

    // plain text
    private static byte[] plainText = new byte[16];

    private static byte p = -1;

    // known bytes of IS
    private static int k = 0;

    private static byte[] base64(String s) {
        return Base64.getDecoder().decode(s);
    }

    private static String base64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }

    private static String readS() {
        String kek = in.nextLine();
        return kek;
    }

    private static void makeQuery() {
        //print("make query");

        byte[] newIV = new byte[16];

        for (int i = 0; i < k; i++) {
            int lol = IS[15 - i] ^ (k + 1);
            newIV[15 - i] = (byte) lol;
        }

        Random rand = new Random(43);

        for (int i = 0; i < 15 - k; i++) {
            newIV[i] = (byte) rand.nextInt();
        }

        newIV[15 - k] = ++p;

        System.out.println(cipher);
        System.out.println(base64(newIV));
    }



    private static void processResponse() {
        //print("process response");

        String response = readS();
        //print(response);
        if (response.equals("Ok") && k == 0) {
            IS[15 - k] = (byte) ((k + 1) ^ p);
            //print(Byte.toString((byte) (IS[15 - k] ^ IV[15 - k])));
            if ((IS[15 - k] ^ IV[15 - k]) == 13) {
                p = -1;
                k = 13;
                for (int i = 0; i < 13; i++) {
                    IS[15 - i] = (byte) (13 ^ IV[15 - i]);
                }
            } else {
                message = "No";
                return;
            }
        } else if (response.equals("Ok") && k == 13) {
            IS[15 - k] = (byte) ((k + 1) ^ p);
            //print(Character.toString((char) (IS[15 - k] ^ IV[15 - k])));
            if ((char) (IS[15 - k] ^ IV[15 - k]) == 's') {
                message = "Yes";
                return;
            } else {
                message = "N/A";
                return;
            }
        }
    }


    public static void main(String[] args) {
        in = new Scanner(System.in);


        cipher = readS();
        IV = base64(readS());

        boolean able = false;

        for (int i = 0; i < 1000; i++) {
            if (!message.equals("")) {
                System.out.println("YES");
                System.out.println(message);
                in.close();
                return;
            } else {
                System.out.println("NO");
            }


            makeQuery();

            processResponse();
        }
    }
}
