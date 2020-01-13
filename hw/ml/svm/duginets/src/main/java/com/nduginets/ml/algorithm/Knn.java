package com.nduginets.ml.algorithm;

import com.nduginets.ml.DataComposer;
import com.nduginets.ml.DataHolder;
import com.nduginets.ml.Utils;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import static java.lang.Math.abs;

public final class Knn {

    private static final List<String> metrics = Arrays.asList("manhattan", "euclidean", "maxbtw");

    private static final List<String> kernels = Arrays.asList("uniform", "triangular", "epanechnikov",
            "quartic", "triweight");
    private final DataHolder pureData;

    private final int knnValue;
    private final double maxDistance;


    private List<List<DataComposer>> composers = null;

    public Knn(DataHolder data, int knnValue, double maxDistance) {
        this.pureData = data;
        this.knnValue = knnValue;
        this.maxDistance = maxDistance;
    }


    public void trainee() {
        ExecutorService service = Executors.newFixedThreadPool(3);
        List<Future<List<DataComposer>>> futures = new ArrayList<>();
        composers = new ArrayList<>(3);
        for (String m : metrics) {
            Future<List<DataComposer>> f = service.submit(() -> traineeInFile(m, m + "_report.txt"));
            futures.add(f);
        }
        for (Future<List<DataComposer>> f : futures) {
            try {
                composers.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();

    }

    public List<List<DataComposer>> getComposers() {
        return composers;
    }

    private List<DataComposer> traineeInFile(String metric, String fileName) {
        File file = new File(fileName);
        List<DataComposer> composers = new ArrayList<>();
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String k : kernels) {
                for (int i = 1; i < knnValue; i++) {
                    double result = trainee(pureData, metric, k, i, true);
                    //String report = String.format("metric: %s, kernel: %s, #nei: %d, neigh, measure: %.10f\n", metric, k, i, result);
                    //writer.append(report);
                    composers.add(new DataComposer(metric, k, i, true, result));
                }
                for (double d = 0.1; d <= maxDistance; d += 0.1d) {
                    double result = trainee(pureData, metric, k, d, false);
                    //String report = String.format("metric: %s, kernel: %s, dist: %.2f, dist, measure: %.10f\n", metric, k, d, result);
                    //writer.append(report);
                    composers.add(new DataComposer(metric, k, d, false, result));
                }
                System.err.println(String.format("%s, %s", fileName, k));
                //writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return composers;
    }

    private double trainee(DataHolder dataCopy, String metricValue, String kernelValue, double distValue, boolean isNeighbour) {
        BiFunction<List<Double>, List<Double>, Double> metricFunction = distance(metricValue);
        BiFunction<Double, Double, Double> kernelFunction = kernel(kernelValue);

        int uniqueSize = dataCopy.getUniqueLabel().size();
        double[][] matrix = new double[uniqueSize][uniqueSize];

        for (int testIndex = 0; testIndex < dataCopy.getRow(); testIndex++) {
            List<Double> testPoint = dataCopy.getAttributes().get(testIndex);
            double testLabel = dataCopy.getLabel().get(testIndex);
            int[] sortedData = dataCopy.indexSort(testPoint, metricFunction);

            Map<Double, Double> answers = new HashMap<>();
            // choose most likely
            for (Double unique : dataCopy.getUniqueLabel()) {
                answers.put(unique, 0d);
            }

            for (int trainIndex = 1; trainIndex < sortedData.length; trainIndex++) {
                //if (sortedData[trainIndex] == testIndex) {
                // the same point
                //    continue;
                //}
                List<Double> trainPoint = dataCopy.getAttributes().get(sortedData[trainIndex]);
                double trainLabel = dataCopy.getLabel().get(sortedData[trainIndex]);
                double normalize;
                if (isNeighbour) {
                    int neighbourIndex = sortedData[(int) distValue];
                    normalize = metricFunction.apply(testPoint, dataCopy.getAttributes().get(neighbourIndex));
                } else {
                    normalize = distValue;
                }
                double weight =
                        kernelFunction.apply(metricFunction.apply(testPoint, trainPoint), normalize);
                answers.put(trainLabel, answers.get(trainLabel) + weight);
            }

            // set matrix field
            int mostLikely = (int) getMostLikelyLabel(answers);
            matrix[(int) testLabel][mostLikely]++;
        }

        return Utils.calculateFMeasure(matrix);
    }



    private double getMostLikelyLabel(Map<Double, Double> answers) {
        return answers
                .entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .get()
                .getKey();
    }

    private BiFunction<List<Double>, List<Double>, Double> distance(String distance) {
        int m = pureData.getAttributes().get(0).size();

        switch (distance) {
            case "manhattan":
                return (x, y) -> {
                    double d = 0;
                    for (int i = 0; i < m; i++) {
                        d += Math.abs(x.get(i) - y.get(i));

                    }
                    return (double) d;
                };
            case "euclidean":
                return (x, y) -> {
                    double d = 0;
                    for (int i = 0; i < m; i++) {
                        d += Math.abs(x.get(i) - y.get(i)) * Math.abs(x.get(i) - y.get(i));
                    }
                    return Math.sqrt(d);
                };
            default:
                return (x, y) -> {
                    double d = Double.MIN_VALUE;
                    for (int i = 0; i < m; i++) {
                        d = Math.max(d, Math.abs(x.get(i) - y.get(i)));
                    }
                    return (double) d;
                };
        }
    }

    private BiFunction<Double, Double, Double> kernel(final String kernel) {
        return (cur, normalizeDistance) -> {
            double u = (double) ((double) cur / (double) normalizeDistance);
            boolean predicate = (double) abs(u) >= (double) 1;
            switch (kernel) {
                case "uniform":
                    return predicate ? 0 : 0.5;
                case "triangular":
                    return predicate ? 0 : (double) 1 - u;
                case "epanechnikov":
                    return predicate ? 0 : (double) 0.75 * ((double) 1 - (double) (u * u));
                case "quartic":
                    return predicate ? 0 : (((double) 15 / (double) 16) * (((double) 1 - ((double) u * u)) * ((double) 1 - (double) (u * u))));
                default:
                    return predicate ? 0 : (double) ((double) 35 / (double) 32) * ((double) Math.pow(((double) 1 - (u * u)), 3));
            }

        };
    }


}
