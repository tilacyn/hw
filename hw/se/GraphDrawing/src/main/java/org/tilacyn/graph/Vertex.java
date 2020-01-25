package org.tilacyn.graph;

public class Vertex {
    int number;
    double x;
    double y;

    double xoffset;
    double yoffset;

    private Vertex() {
    }

    public static Vertex of(double x, double y) {
        Vertex v = new Vertex();
        v.x = x;
        v.y = y;
        return v;
    }

    public static Vertex of(double x, double y, double xoffset, double yoffset, int number) {
        Vertex v = Vertex.of(x, y);
        v.number = number;
        v.xoffset = xoffset;
        v.yoffset = yoffset;
        return v;
    }

}
