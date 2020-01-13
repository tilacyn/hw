package com.nduginets.ml;

import org.javatuples.Quartet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSeparator implements Iterator<Quartet<List<List<Double>>, List<Integer>, List<List<Double>>, List<Integer>>> {

    private final List<List<Double>> x;
    private final List<Integer> y;
    private final int kSize;
    private int idx = 0;

    public DataSeparator(List<List<Double>> x, List<Integer> y, int kSize) {
        this.x = x;
        this.y = y;
        this.kSize = kSize;

    }

    @Override
    public boolean hasNext() {
        return idx + kSize < x.size();
    }

    @Override
    public Quartet<List<List<Double>>, List<Integer>, List<List<Double>>, List<Integer>> next() {
        List<List<Double>> trainX = new ArrayList<>();
        List<Integer> trainY = new ArrayList<>();
        List<List<Double>> testX = new ArrayList<>(x.subList(idx, idx + kSize));
        List<Integer> testY = new ArrayList<>(y.subList(idx, idx + kSize));
        if (idx > 0) {
            trainX.addAll(x.subList(0, idx - 1));
            trainY.addAll(y.subList(0, idx - 1));
        }
        if (idx + kSize < x.size()) {
            trainX.addAll(x.subList(idx + kSize, x.size()));
            trainY.addAll(y.subList(idx + kSize, x.size()));
        }
        idx += kSize;
        return new Quartet<>(trainX, trainY, testX, testY);
    }
}
