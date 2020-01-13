package com.nduginets.ml;


import java.util.*;
import java.util.stream.IntStream;

public class AAAAA {
    public static void main(String[] args) {
        String[] a = new String[]{
                "1110100",
                "0111010",
                "0011101"


        };
        Set<Integer> cw = buildCodeWords(a);
        System.err.println(cw);
        System.err.println(getDist(cw, matrixN(a)));
        /*String[] a = new String[]{
                "001011",
                "010110",
                "101100"
        };*/

    }

    private static int G(String[] arg) {
        Set<Integer> cw = buildCodeWords(arg);
        System.err.println(cw);
        System.err.println(getDist(cw, matrixN(arg)));
        return 0;
    }

    private static int H(String[] arg) {
        return 0;
    }

    private static Set<Integer> buildCodeWords(String[] arg) {
        int k = arg.length;
        Set<Integer> cc = new HashSet<>();
        int[] m = buildMatrix(arg);

        for (int w = 0; w < (1 << k); w++) {
            cc.add(matrixCross(k, matrixN(arg), w, m));
        }
        return cc;
    }

    private static int findLinearDepend(String[] arg) {
        int K = arg.length;
        int n = matrixN(arg);
        int[] m = buildMatrix(arg);

        for (int used = 1; used < (1 << n); used++) {
            int ones = aaa(used, n, K, m);
            if (ones != 0) {
                continue;
            }
            System.err.println(used + " " + calcOne(used));
        }
        return 0;
    }

    private static int aaa(int used, int N, int K, int[] m) {
        int v = 0;
        for (int n = 0; n < N; n++) {
            if (((1 << n) & used) == 0) {
                continue;
            }
            for (int k = 0; k < K; k++) {
                int qm = (m[k] >> n) & 1;
                int qv = (v >> n) & 1;
                int r = (qv ^ qm) << n;
                v = (((1 << N) - 1) - (1 << n)) & v;
                v = v | r;
            }
        }
        return v;
    }

    private static int[] buildMatrix(String[] arg) {
        int m[] = new int[arg.length];
        for (int i = 0; i < arg.length; i++) {
            String str = arg[i].trim();
            int res = 0;
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == '1') {
                    res = res | (1 << j);
                }
            }
            m[i] = res;
        }
        return m;
    }

    private static int matrixN(String[] arg) {
        return arg[0].trim().length();
    }

    private static int getDist(Set<Integer> used, int n) {
        int v[] = new int[used.size()];
        List<Integer> words = new ArrayList<>(used);
        IntStream.range(0, v.length)
                .forEach(x -> v[x] = words.get(x));

        int size = Integer.MAX_VALUE;
        for (int aV : v) {
            if (aV == 0) {
                continue;
            }
            int r = 0;
            for (int k = 0; k < n; k++) {
                int shift = (1 << k);
                r += ((aV & shift) > 0 ? 1 : 0);
            }
            size = Math.min(size, r);
        }
        return size;
    }


    private static int matrixCross(int K, int N, int a, int... b) {
        int res = 0;
        for (int n = 0; n < N; n++) {
            int sm = 0;
            for (int k = 0; k < K; k++) {
                int ak = (a >> k) & 1;
                int bkn = (b[k] >> n) & 1;
                sm = sm ^ (ak * bkn);
            }
            res += (sm << n);
        }
        return res;
    }

    private static String intToBin(int v, int sz) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sz; i++) {
            sb.append((v >> i) & 1);
        }
        return sb.toString();
    }

    private static int calcOne(int x) {
        int z = 0;
        for (int i = 0; i < 15; i++) {
            if ((x & (1 << i)) > 0) {
                z++;
            }
        }
        return z;
    }
}