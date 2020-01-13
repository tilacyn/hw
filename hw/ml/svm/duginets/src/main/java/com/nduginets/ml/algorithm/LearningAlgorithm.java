package com.nduginets.ml.algorithm;

import java.util.List;

public interface LearningAlgorithm<X, Y> {

    void train(List<X> trainSet, List<Y> trainLabelSet);

    Y predict(X predictValue);

    String name();
}
