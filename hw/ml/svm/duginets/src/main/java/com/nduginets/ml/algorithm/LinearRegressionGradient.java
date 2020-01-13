package com.nduginets.ml.algorithm;

public final class LinearRegressionGradient {

    private final double x[][];
    private final double y[];

    private final double w[];
    private final double q[];

    private final int n;
    private final int m;

    private int iterate = 0;

    private static final double tow = 0.5;

    public LinearRegressionGradient(double x[][], double y[]) {
        this.x = x;
        this.y = y;
        this.w = new double[x[0].length];
        this.q = new double[x[0].length];
        this.n = x.length;
        this.m = x[0].length - 1;
    }


    private double scalarDot(int xIndex) {
        double result = 0;
        for (int i = 0; i <= m; i++) {
            result += x[xIndex][i] * w[i];
        }
        return result;
    }

    private void calculateGradQ(int xIndex) {
        double dot = scalarDot(xIndex);
        for (int i = 0; i <= m; i++) {
            q[i] = -2 * x[xIndex][i] * (y[xIndex] - dot) + w[i] * tow;
        }
    }

    private double findLambda(int xIndex) {
        double xw = 0;
        double xq = 0;
        double xwt = 0;
        double xqt = 0;
        for (int i = 0; i <= m; i++) {
            xw += x[xIndex][i] * w[i];
            xq += x[xIndex][i] * q[i];
            xwt += tow * w[i];
            xqt += tow * q[i];
        }
        return (y[xIndex] - xw - xwt) / (xq + xqt);
    }

    public double[] solve(int upperIterate) {

        if (upperIterate <= iterate) {
            throw new IllegalStateException("upper bound can't be less than current bound");
        }

        for (; iterate <= upperIterate; iterate++) {
            int idx = iterate % n;
            calculateGradQ(idx);
            double lambda = findLambda(idx);
            for (int i = 0; i <= m; i++) {
                w[i] = w[i] * (1 - lambda * tow) + lambda * q[i];
            }
        }
        return w;
    }


    public double errorValidate(double[][] xTest, double[] yTest) {
        double yMax = Integer.MIN_VALUE;
        double yMin = Integer.MAX_VALUE;
        double rmsd = 0;
        for (int i = 0; i < x.length; i++) {
            double yTrain = 0;
            for (int j = 0; j < x[i].length; j++) {
                yTrain += x[i][j] * w[j];
            }
            yMax = Math.max(y[i], yMax);
            yMin = Math.min(y[i], yMin);
            rmsd += Math.pow(y[i] - yTrain, 2);
        }
        rmsd = Math.sqrt(rmsd / (double) x.length);
        return rmsd / (yMax - yMin);

    }
}
