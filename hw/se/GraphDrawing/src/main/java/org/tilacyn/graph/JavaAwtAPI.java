package org.tilacyn.graph;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;


public class JavaAwtAPI extends Frame implements DrawingAPI {
    private static final int POINT_WIDTH = 30;
    private static final int CANVAS_WIDTH = 1000;
    private static final int CANVAS_HEIGHT = 1000;


    private List<Vertex> vertices;
    private List<Edge> edges;

    public JavaAwtAPI() {
        setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        setTitle("Circle Graph");
    }

    @Override
    public int getCanvasWidth() {
        return CANVAS_WIDTH;
    }

    @Override
    public void setVertices(List<Vertex> vertices) {
        this.vertices = vertices;
    }

    @Override
    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        System.out.println("Drawing " + vertices.size() + " vertices");

        vertices.stream()
                .map(this::drawVertex)
                .forEach(g2::draw);

        System.out.println("Drawing " + edges.size() + " edges");

        edges.stream()
                .map(this::drawEdge)
                .forEach(g2::draw);

    }

    private Ellipse2D.Double drawVertex(Vertex v) {
        return new Ellipse2D.Double(v.x + v.xoffset, v.y + v.yoffset, POINT_WIDTH, POINT_WIDTH);
    }

    private Line2D.Double drawEdge(Edge e) {
        return new Line2D.Double(e.xstart, e.ystart, e.xend, e.yend);
    }

    @Override
    public void showGraph() {
        setVisible(true);
    }
}
