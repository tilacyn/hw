package org.tilacyn.graph;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public abstract class CircleGraph extends Graph {

    protected int n;

    protected List<Vertex> vertices;
    protected List<Edge> edges;

    public CircleGraph(DrawingAPI drawingAPI) {
        super(drawingAPI);
    }


    @Override
    public void drawGraph() {
        calculateVertices();
        drawingAPI.setVertices(vertices);
        calculateEdges();
        drawingAPI.setEdges(edges);
    }

    protected abstract void calculateEdges();

    private void calculateVertices() {
        double width = drawingAPI.getCanvasWidth();
        double radius = width * 9 / 20;
        Vertex center = Vertex.of(width / 2.0, width / 2.0);

        List<Vertex> result = new ArrayList<>();

        double atomAngle = (double) 2 * Math.PI / n;
        for (int i = 0; i < n; i++) {
            double angle = atomAngle * i;
            Vertex v = Vertex.of(
                    center.x + radius * Math.cos(angle),
                    center.y + radius * Math.sin(angle),
                    xoffset(radius, angle),
                    yoffset(radius, angle),
                    i + 1);
            result.add(v);
        }

        vertices = result;
    }

    protected Edge fromVertices(int i, int j) {
        Vertex v1 = vertices.get(i);
        Vertex v2 = vertices.get(j);
        return Edge.of(v1.x, v1.y, v2.x, v2.y);
    }

    private double xoffset(double radius, double angle) {
        return -radius * (1 - Math.cos(angle)) * 0.033;
    }

    private double yoffset(double radius, double angle) {
        return -radius * (1 - Math.sin(angle)) * 0.033;
    }

}
