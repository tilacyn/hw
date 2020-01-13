package com.nduginets.ml.algorithm;

import com.nduginets.ml.kernels.Kernel;

import java.util.*;

import static java.lang.Math.*;

public final class SVM implements LearningAlgorithm<List<Double>, Integer> {

    private final double c;
    private final int steps;
    private final Kernel kernel;
    private static final Random RANDOM = new Random();

    public SVM(Kernel kernel, int steps, double c) {
        this.c = c;
        this.steps = steps;
        this.kernel = kernel;
    }


    private List<Integer> y;
    private List<List<Double>> x;
    private List<Double> errors;
    private List<Double> lambdas;
    private double b = 0;
    private int n = 0;

    private double objective() {
        double suma = lambdas.stream().reduce((double) 0, (x, y) -> x + y);
        double second = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                second += lambdas.get(i) * lambdas.get(j) * kernel.evaluate(x.get(i), x.get(j)) * y.get(i) * y.get(j);
            }
        }
        return suma - 0.5 * second;
    }

    @Override
    public void train(List<List<Double>> trainSet, List<Integer> trainLabelSet) {

        y = trainLabelSet;
        x = trainSet;
        n = trainLabelSet.size();
        errors = new ArrayList<>(n);
        lambdas = new ArrayList<>(n);
        b = 0;

        for (int i = 0; i < n; i++) {
            errors.add(-1.0 * y.get(i));
            lambdas.add(0.0);
        }

        List<List<Double>> products = kernelProduct();

        System.out.println(n);
        System.out.println(kernel);
        System.out.println();

        for (int step = 0; step < steps; step++) {
            for (int i = 0; i < n; i++) {
                int j = getNextRand(i);
//                int j = (i + step + 1) % n;
                takeStep(products, i, j);
            }
            System.out.println(objective());
        }
        System.out.println(toString());
    }

    private int getNextRand(int cant) {
        int j = RANDOM.nextInt(n);
        while (j == cant) {
            j = RANDOM.nextInt(n);
        }
        return j;
    }

    @Override
    public Integer predict(List<Double> predictValue) {
        double result = 0;
        for (int i = 0; i < n; i++) {
            result += y.get(i) * lambdas.get(i) * kernel.evaluate(x.get(i), predictValue);
        }
        result -= b;
        return result > 0 ? 1 : -1;
    }

    @Override
    public String name() {
        return kernel.name();
    }


    @Override
    public String toString() {
        return String.format("kernel=%s, c=%.10f, lambda=%s, b=%.10f", kernel, c, lambdas, b);
    }

    private List<List<Double>> kernelProduct() {
        List<List<Double>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Double> r = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                r.add(kernel.evaluate(x.get(i), x.get(j)));
            }
            result.add(r);
        }
        return result;
    }

    private void takeStep(List<List<Double>> kernelProduct, int i, int j) {
        double alpha1 = lambdas.get(i);
        double alpha2 = lambdas.get(j);

        double y1 = y.get(i);
        double y2 = y.get(j);

        double E1 = errors.get(i);
        double E2 = errors.get(j);

        double L;
        double H;

        if (y1 != y2) {
            L = max(0.0, alpha2 - alpha1);
            H = min(c, c + alpha2 - alpha1);
        } else {
            L = max(0.0, alpha1 + alpha2 - c);
            H = min(c, alpha1 + alpha2);
        }

        if (L == H) {
            return;
        }

        double kij = kernelProduct.get(i).get(j);
        double kii = kernelProduct.get(i).get(i);
        double kjj = kernelProduct.get(j).get(j);
        double eta = 2 * kij - kii - kjj;
        double a1, a2;

        if (eta >= 0) {
            return;
        }

        a2 = alpha2 - y2 * (E1 - E2) / eta;
        if (a2 <= L) {
            a2 = L;
        } else if (a2 >= H) {
            a2 = H;
        }

//        System.out.println("eta: " + eta);

        if (a2 < 1e-4) {
            a2 = 0;
        } else if (a2 > c - 1e-4) {
            a2 = c;
        }

        if (abs(a2 - alpha2) < 1e-2 * (a2 + alpha2 + 1e-2)) {
            return;
        }

        a1 = alpha1 + y1 * y2 * (alpha2 - a2);

        double b1 = E1 + y1 * (a1 - alpha1) * kii + y2 * (a2 - alpha2) * kij + b;
        double b2 = E2 + y1 * (a1 - alpha1) * kij + y2 * (a2 - alpha2) * kjj + b;

        double newb;
        if (0.0 < a1 && a1 < c) {
            newb = b1;
        } else if (0.0 < a2 && a2 < c) {
            newb = b2;
        } else {
            newb = (b1 + b2) / (double) 2;
        }

        lambdas.set(i, a1);
        lambdas.set(j, a2);

        if (0.0 < a1 && a1 < c) {
            errors.set(i, 0.0);
        }
        if (0.0 < a2 && a2 < c) {
            errors.set(j, 0.0);
        }

        for (int k = 0; k < n; k++) {
            if (k == i || k == j) {
                continue;
            }
            double newError = errors.get(k) +
                    y1 * (a1 - alpha1) * kernelProduct.get(i).get(k) +
                    y2 * (a2 - alpha2) * kernelProduct.get(j).get(k) + b - newb;
            errors.set(k, newError);
        }
        b = newb;



//        System.out.println(lambdas.get(i) + " " + lambdas.get(j));
//        //errors.forEach(e -> System.out.print(e + " "));
//        //System.out.println();
//        System.out.println("b: " + b);
//        System.out.println("objective: " + objective());
//        System.out.println("optimized: " + i + " " + j);
//        System.out.println();
//        System.out.println();
    }
}