import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

public class Main {

    private static int N = 10000;

    private static int pLen = 2000;


    private static ArrayList<Integer> prefix = new ArrayList<>();




    private static int period() {
        ArrayList<Integer> pi = new ArrayList<>(pLen);
        for (int i = 0; i < pLen; i++) {
            pi.add(0);
        }
        for (int i = 1; i < pLen; i++) {
            int j = pi.get(i - 1);
            while (j > 0 && !prefix.get(i).equals(prefix.get(j))) {
                j = pi.get(j - 1);
            }
            if (prefix.get(i).equals(prefix.get(j))) {
                j++;
            }
            pi.set(i, j);
        }
        return pi.get(pLen - 1);
    }

    private static ArrayList<Integer> array = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);



        //System.out.println(period());

        for (int i = 0; i < pLen; i++) {
            int c = scanner.nextInt();
            System.out.println(0);

            String s = scanner.nextLine();
            s = scanner.nextLine();

            if (s.equals("NO")) {
                c ^= 1;
            }

            //System.out.println("KEK\n" + s);

            prefix.add(c);

            array.add(c);

            System.out.flush();
        }

        int T = period();

        T = pLen - T;

        //System.out.println("T: " + T);

        for (int i = 0; i < N - pLen; i++) {
            int c = scanner.nextInt();
            c ^= array.get(pLen + i - T);
            System.out.println(c);

            String s = scanner.nextLine();
            s = scanner.nextLine();


            array.add(array.get(pLen + i - T));

            System.out.flush();
        }


        scanner.close();
    }
}
