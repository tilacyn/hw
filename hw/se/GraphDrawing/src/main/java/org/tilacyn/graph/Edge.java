package org.tilacyn.graph;

public class Edge {
    double xstart;
    double ystart;
    double xend;
    double yend;

    private Edge() {
    }

    public static Edge of(double xstart, double ystart, double xend, double yend) {
        Edge e = new Edge();
        e.xstart = xstart;
        e.xend = xend;
        e.ystart = ystart;
        e.yend = yend;
        return e;
    }

}
