package com.nduginets.ml.kernels;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RadialFactory implements Iterable<Kernel> {
    private final double low;
    private final double up;
    private final double step;

    public RadialFactory(double low, double up, double step) {
        this.low = low;
        this.up = up;
        this.step = step;
    }

    @NotNull
    @Override
    public Iterator<Kernel> iterator() {
        return new LinearIterator();
    }

    private class LinearIterator implements Iterator<Kernel> {

        double cur = low;

        @Override
        public boolean hasNext() {
            return cur < up;
        }

        @Override
        public Kernel next() {
            cur += step;
            return new RadialKernel(cur);
        }
    }

    public static class RadialKernel implements Kernel {

        private final double gamma;

        public RadialKernel(double gamma) {
            this.gamma = gamma;
        }

        @Override
        public double evaluate(List<Double> x1, List<Double> x2) {
            double r = 0;
            for (int i = 0; i < x1.size(); i++) {
                r += Math.pow(x1.get(i) - x2.get(i), 2);
            }
            return Math.exp(-gamma * r);
        }

        @Override
        public String name() {
            return "radial";
        }

        @Override
        public String toString() {
            return "radial gamma=" + gamma;
        }
    }
}
