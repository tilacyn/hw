package com.nduginets.ml;

public final class Utils {

    private Utils() {
    }

    public static double calculateFMeasure(double[][] matrix) {
        double percW = 0;
        double recallW = 0;
        double all = 0;
        for (double[] aMatrix : matrix) {
            for (double anAMatrix : aMatrix) {
                all += anAMatrix;
            }
        }
        for (int i = 0; i < matrix.length; i++) {
            double c = 0;
            double p = 0;
            for (int j = 0; j < matrix[i].length; j++) {
                c += matrix[j][i];
                p += matrix[i][j];
            }
            percW += p == 0 ? 0 : (matrix[i][i] * c / p) / all;
            recallW += matrix[i][i] / all;
        }
        return percW + recallW == 0 ? 0 : 2 * percW * recallW / (percW + recallW);
    }
}
