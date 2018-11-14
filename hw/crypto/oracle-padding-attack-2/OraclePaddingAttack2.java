import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.Random;
import java.util.Scanner;

public class OraclePaddingAttack2 {

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

    private static byte[] yes = new byte[16];
    private static byte[] na = new byte[16];
    private static byte[] b16 = new byte[16];

    static {
        yes[0] = 'Y';
        yes[1] = 'e';
        yes[2] = 's';
        na[0] = 'N';
        na[1] = '/';
        na[2] = 'A';
        for (int i = 3; i < 16; i++) {
            yes[i] = 13;
            na[i] = 13;
        }
        for (int i = 0; i < 16; i++) {
            b16[i] = 16;
        }
    }

    private static int isYes = 0;

    private static void makeQuery() {
        //print("make query");

        byte[] newIS = new byte[16];

        if (isYes == 0) {
            newIS = xor(yes, IV);
        } else {
            newIS = xor(na, IV);
        }

        byte[] newIV = xor(b16, newIS);

        System.out.println(cipher);
        System.out.println(base64(newIV));
    }

    private static byte[] xor(byte[] f, byte[] s) {
        byte[] res = new byte[16];
        for (int i = 0; i < 16; i++) {
            res[i] = (byte) (f[i] ^ s[i]);
        }
        return res;
    }



    private static void processResponse() {
        //print("process response");

        String response = readS();
        //print(response);

        if (response.equals("Ok")) {
            if (isYes == 0) {
                message = "Yes";
                return;
            }
            message = "N/A";
        } else {
            if (isYes == 0) {
                isYes = 1;
            } else {
                message = "No";
            }
        }
     }


    public static void main(String[] args) {
        in = new Scanner(System.in);


        cipher = readS();
        IV = base64(readS());

        for (int i = 0; i < 3; i++) {
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
