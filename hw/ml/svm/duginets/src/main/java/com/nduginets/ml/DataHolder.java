package com.nduginets.ml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public class DataHolder {

    private final List<List<Double>> attributes;
    private final List<Double> label;
    private final Set<Double> uniqueLabel;
    private final int row;
    private final int column;

    public DataHolder(int labelIndex, CsvReader.CsvData<Double> csvData) {
        List<List<Double>> data = csvData.getRows();
        this.attributes = new ArrayList<>(data.size());
        this.label = new ArrayList<>(data.size());
        this.row = data.size();
        this.column = data.get(0).size() - 1;
        this.uniqueLabel = new HashSet<>();

        for (List<Double> column : data) {
            List<Double> attribute = new ArrayList<>(column.size() - 1);
            for (int c = 0; c < column.size(); c++) {
                if (c == labelIndex) {
                    double value = column.get(c);
                    label.add(value);
                    uniqueLabel.add(value);
                } else {
                    attribute.add(column.get(c));
                }
            }
            attributes.add(attribute);
        }
    }

    private DataHolder(List<List<Double>> attributesCopy, List<Double> labelCopy, DataHolder holder) {
        this.attributes = attributesCopy;
        this.label = labelCopy;
        this.row = attributesCopy.size();
        this.column = attributesCopy.get(0).size();
        this.uniqueLabel = holder.uniqueLabel;
    }

    public List<List<Double>> getAttributes() {
        return attributes;
    }

    public List<Double> getLabel() {
        return label;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Set<Double> getUniqueLabel() {
        return uniqueLabel;
    }

    public DataHolder createCopy() {
        List<List<Double>> attributesCopy = new ArrayList<>(this.attributes.size());
        List<Double> labelCopy = new ArrayList<>(this.attributes.size());
        for (List<Double> column : attributes) {
            List<Double> colCopy = new ArrayList<>(column.size());
            colCopy.addAll(column);
            attributesCopy.add(colCopy);
        }
        labelCopy.addAll(label);
        return new DataHolder(attributesCopy, labelCopy, this);
    }

    public void normalize() {
        for (int i = 0; i < column; i++) {
            double mean = 0;
            for (int j = 0; j < row; j++) {
                mean += attributes.get(j).get(i);
            }
            mean = mean / (double) row;
            double derivation = 0;
            for (int j = 0; j < row; j++) {
                derivation += Math.pow(attributes.get(j).get(i) - mean, 2);
            }
            derivation = Math.sqrt(derivation) / (double) (row - 1);
            for (int j = 0; j < row; j++) {
                double attribute = attributes.get(j).get(i);
                attributes.get(j).set(i, (attribute - mean) / derivation);
            }
        }
    }

    public int[] indexSort(List<Double> point, BiFunction<List<Double>, List<Double>, Double> metric) {
        return IntStream.range(0, row)
                .boxed()
                .sorted((i, j) -> {
                    double distI = metric.apply(point, attributes.get(i));
                    double distJ = metric.apply(point, attributes.get(j));
                    return Double.compare(distI, distJ);

                })
                .mapToInt(a -> a)
                .toArray();
    }

}
