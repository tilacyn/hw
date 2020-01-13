package com.nduginets.ml;

import java.util.Objects;

public class DataComposer {
    private String measure;
    private String kernel;
    private double k;
    private boolean neigh;
    private double fMeasure;

    public DataComposer(String measure, String kernel, double k, boolean neigh, double fMeasure) {
        this.measure = measure;
        this.kernel = kernel;
        this.k = k;
        this.neigh = neigh;
        this.fMeasure = fMeasure;
    }

    public DataComposer() {
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getKernel() {
        return kernel;
    }

    public void setKernel(String kernel) {
        this.kernel = kernel;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public boolean isNeigh() {
        return neigh;
    }

    public void setNeigh(boolean neigh) {
        this.neigh = neigh;
    }

    public double getfMeasure() {
        return fMeasure;
    }

    public void setfMeasure(double fMeasure) {
        this.fMeasure = fMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataComposer that = (DataComposer) o;
        return Double.compare(that.k, k) == 0 &&
                neigh == that.neigh &&
                Double.compare(that.fMeasure, fMeasure) == 0 &&
                Objects.equals(measure, that.measure) &&
                Objects.equals(kernel, that.kernel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(measure, kernel, k, neigh, fMeasure);
    }

    @Override
    public String toString() {
        return "DataComposer{" +
                "measure='" + measure + '\'' +
                ", kernel='" + kernel + '\'' +
                ", k=" + k +
                ", neigh=" + neigh +
                ", fMeasure=" + fMeasure +
                '}';
    }
}
