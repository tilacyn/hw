package com.nduginets.ml;

import com.nduginets.ml.algorithm.SVM;
import com.nduginets.ml.kernels.Kernel;
import com.nduginets.ml.kernels.RadialFactory;
import org.javatuples.Pair;
import org.javatuples.Quartet;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class MainSVM {

    private static final Random RANDOM = new Random();

    public static void main(String[] args) {


        String chips = MainSVM.class.getClassLoader().getResource("svm/chips.csv").getFile();
        CsvReader<String> csv = new CsvReader<>(chips);
        List<List<String>> values = csv.readToStringList();

        SwingWrapper<XYChart> chars = drawChars(values);
//        chars.displayChartMatrix();
    }


    private static SwingWrapper<XYChart> drawChars(List<List<String>> values) {

        //values.sort((x, y) -> RANDOM.nextInt(523) % 2 == 0 ? -1 : 1);

        List<List<Double>> x = new ArrayList<>();
        List<Integer> y = new ArrayList<>();

        for (List<String> s : values) {
            List<Double> parsedData = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                parsedData.add(Double.parseDouble(s.get(i).trim()));
            }
            x.add(parsedData);
            int label = 1;
            if (s.get(2).trim().equals("N")) {
                label = -1;
            }
            y.add(label);
        }


        RadialFactory.RadialKernel kernel = new RadialFactory.RadialKernel(0.6);

        SVM svm = new SVM(kernel, 5_000, 100);
        svm.train(x, y);
        int acc = 0;
        for (int i = 0; i < x.size(); i++) {
            List<Double> o = x.get(i);
            int ry = y.get(i);
            if (svm.predict(o) == ry) {
                acc++;
            }
        }
        System.out.println("acc: " + acc);

        //List<Pair<Double, SVM>> bestModels = new ArrayList<>();

//        for (double c : Collections.singletonList(100.0)) {
////            LinearFactory linearFactory = new LinearFactory(1, 4, 0.5);
//            RadialFactory radialFactory = new RadialFactory(0.1, 1.1, 0.5);
//
////            for (Kernel k : linearFactory) {
////                bestModels.add(trainSVM(k, c, x, y));
////            }
//
//            for (Kernel k : radialFactory) {
//                bestModels.add(trainSVM(k, c, x, y));
//            }
//
//        }

//        List<XYChart> charts = new ArrayList<>();
//        for (String kernelName : Arrays.asList("linear", "radial")) {
//
//            Pair<Double, SVM> best = bestModels
//                    .stream()
//                    .filter(v -> v.getValue1().name().equals(kernelName))
//                    .max(Comparator.comparingDouble(Pair::getValue0))
//                    .get();
//            best.getValue1().train(x, y);
//            charts.add(markSurface(getBoundaries(x), best.getValue1()));
//            System.err.println("fmesaure=" + best.getValue0());
//            System.err.println(best);
//        }
//
//
//        return new SwingWrapper<XYChart>(charts);
        return null;
    }

    private static Pair<Double, SVM> trainSVM(Kernel k, double c, List<List<Double>> x, List<Integer> y) {
        SVM svm = new SVM(k, 5_000, c);
        double measure[][] = new double[2][2];
        DataSeparator separator = new DataSeparator(x, y, 10);
        for (; separator.hasNext(); ) {
            Quartet<List<List<Double>>, List<Integer>, List<List<Double>>, List<Integer>> cur = separator.next();
            svm.train(cur.getValue0(), cur.getValue1());
            for (int i = 0; i < cur.getValue2().size(); i++) {
                int actual = cur.getValue3().get(i);
                int predict = svm.predict(cur.getValue2().get(i));
                actual = actual == -1 ? 0 : actual;
                predict = predict == -1 ? 0 : predict;
                measure[actual][predict] += 0.5;
            }
        }
        double m = Utils.calculateFMeasure(measure);
        return new Pair<>(m, svm);
    }

    public static XYChart markSurface(Quartet<Double, Double, Double, Double> bounds, SVM svm) {
        double minX = bounds.getValue0();
        double minY = bounds.getValue1();
        double maxX = bounds.getValue2();
        double maxY = bounds.getValue3();
        double stepX = abs(maxX - minX) / 50;
        double stepY = abs(maxY - minY) / 50;

        XYChart chart = new XYChartBuilder().width(800).height(600).build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(10);

        List<Double> fx = new ArrayList<>();
        List<Double> fy = new ArrayList<>();
        List<Double> sx = new ArrayList<>();
        List<Double> sy = new ArrayList<>();


        for (double x = minX; x <= maxX; x += stepX) {
            for (double y = minY; y <= maxY; y += stepY) {
                List<Double> v = new ArrayList<>();
                v.add(x);
                v.add(y);
                int result = svm.predict(v);
                if (result > 0) {
                    fx.add(x);
                    fy.add(y);
                } else {
                    sx.add(x);
                    sy.add(y);
                }

            }
        }
        if (!fx.isEmpty()) {
            chart.addSeries("POSITIVE", fx, fy);
        }
        if (!sx.isEmpty()) {
            chart.addSeries("NEGATIVE", sx, sy);
        }
        return chart;
    }

    public static Quartet<Double, Double, Double, Double> getBoundaries(List<List<Double>> set) {
        double xMin = Double.MAX_VALUE;
        double yMin = Double.MAX_VALUE;

        double xMax = Double.MIN_VALUE;
        double yMax = Double.MIN_VALUE;

        for (List<Double> v : set) {
            Double x = v.get(0);
            Double y = v.get(1);

            xMin = min(xMin, x);
            yMin = min(yMin, y);

            xMax = max(xMax, x);
            yMax = max(yMax, y);
        }
        return new Quartet<>(xMin, yMin, xMax, yMax);
    }
}
