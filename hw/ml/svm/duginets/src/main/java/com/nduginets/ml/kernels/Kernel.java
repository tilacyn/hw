package com.nduginets.ml.kernels;

import java.util.List;

public interface Kernel {

    double evaluate(List<Double> x1, List<Double> x2);

    String name();
}
