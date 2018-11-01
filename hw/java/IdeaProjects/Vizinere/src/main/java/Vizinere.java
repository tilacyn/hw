import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Vizinere {

    private static char[] text = new char[1000000];
    private static int textLength;

    private static double countCoincidenceIndex(ArrayList<Character> arrayList) {
        int[] letters = new int[26];
        for (Character anArrayList : arrayList) {
            letters[anArrayList - 'a']++;
        }

        int sum = 0;

        for (int i = 0; i < 26; i++) {
            sum += letters[i] * (letters[i] - 1);
        }

        return sum / (arrayList.size() * (arrayList.size() - 1));
    }

    private static int findKeyLength() {
        ArrayList<Double> keys = new ArrayList<>(5);

        for (int i = 6; i <= 10; i++) {
            ArrayList<Character> arrayList = new ArrayList<>();
            for (int j = 0; j < textLength; j++) {
                if (j % i == 0) {
                    arrayList.add(text[j]);
                }
            }

            double coincidenceKey = countCoincidenceIndex(arrayList);
            keys.set(i - 6, coincidenceKey);
        }


        int maxInd = 0;
        double max = 0;


        for (int i = 0; i < 5; i++) {
            if (keys.get(i) > max) {
                max = keys.get(i);
                maxInd = i;
            }
        }

        return maxInd + 6;

    }

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("incorrect input");

        }

        File f = new File(args[0]);

        File output = new File("deciphered-text");

        if (output.exists()) {
            output.delete();
        }

        output.createNewFile();

        FileReader fileReader = new FileReader(f);

        textLength = fileReader.read(text);

        System.out.println(text[0]);

    }
}
