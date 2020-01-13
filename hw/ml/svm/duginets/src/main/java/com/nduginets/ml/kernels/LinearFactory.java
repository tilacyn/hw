package com.nduginets.ml.kernels;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinearFactory implements Iterable<Kernel> {

    private final double low;
    private final double up;
    private final double step;

    public LinearFactory(double low, double up, double step) {

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


        private double st = low;

        @Override
        public boolean hasNext() {
            return st <= up;
        }

        @Override
        public Kernel next() {
            st += step;
            return new LinearKernel(st);
        }
    }

    private class LinearKernel implements Kernel {

        private final double w;

        LinearKernel(double w) {
            this.w = w;
        }

        @Override
        public double evaluate(List<Double> x1, List<Double> x2) {
            double r = 0;
            for (int i = 0; i < x1.size(); i++) {
                r += w * x1.get(i) * x2.get(i);
            }
            return r;
        }

        @Override
        public String name() {
            return "linear";
        }

        @Override
        public String toString() {
            return "linear weights=" + w;
        }
    }
}
