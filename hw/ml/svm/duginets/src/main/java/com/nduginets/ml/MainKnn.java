package com.nduginets.ml;

import com.nduginets.ml.algorithm.Knn;
import org.antlr.v4.runtime.misc.Pair;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainKnn {

    private static final List<String> kernels = Arrays.asList("uniform", "triangular", "epanechnikov",
            "quartic", "triweight");

    public static void main(String[] args) {
        CsvReader<Double> aa = new CsvReader<>("/home/nikita/IdeaProjects/ml-algorithms/src/main/resources/cholesterol.csv");
        CsvReader.CsvData<Double> csvData = aa.parseCsv(Double::parseDouble);
        DataHolder data = new DataHolder(12, csvData);
        data.normalize();
        Knn knn = new Knn(data, 150, 4d);
        knn.trainee();

        List<XYChart> charts = new ArrayList<>();
        for (List<DataComposer> composers : knn.getComposers()) {
            charts.add(visualize(composers.get(0).getMeasure(), true, composers));
            charts.add(visualize(composers.get(0).getMeasure(), false, composers));
        }

            new SwingWrapper<XYChart>(charts).displayChartMatrix();
    }

    private static XYChart visualize(String distance, boolean neigh, List<DataComposer> composer) {

        XYChart chart =
                new XYChartBuilder()
                        .width(800)
                        .height(600)
                        .title(distance + (neigh ? " distance depend neighbour index" : " distance depend hyperparameter"))
                        .xAxisTitle("neighbour")
                        .yAxisTitle("")
                        .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        chart.getStyler().setYAxisLabelAlignment(Styler.TextAlignment.Right);
        chart.getStyler().setYAxisDecimalPattern("f #.####");
        chart.getStyler().setPlotMargin(0);
        chart.getStyler().setPlotContentSize(.95);

        for (String kernel : kernels) {
            Pair<List<Double>, List<Double>> identity = new Pair<>(new ArrayList<>(), new ArrayList<>());
            Pair<List<Double>, List<Double>> reduced =
                    composer
                            .stream()
                            .filter(x -> x.getKernel().equals(kernel) && x.isNeigh() == neigh)
                            .reduce(identity, (i, dc) -> {
                                i.a.add(dc.getK());
                                i.b.add(dc.getfMeasure());
                                return i;
                            }, (dc1, dc2) -> {
                                dc1.a.addAll(dc2.a);
                                dc1.b.addAll(dc2.b);
                                return dc1;
                            });
            chart.addSeries(kernel, reduced.a, reduced.b);
        }
        return chart;
    }
}
