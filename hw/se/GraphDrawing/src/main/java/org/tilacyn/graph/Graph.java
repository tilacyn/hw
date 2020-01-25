package org.tilacyn.graph;

public abstract class Graph {
    protected DrawingAPI drawingAPI;

    public Graph(DrawingAPI drawingAPI) {
        this.drawingAPI = drawingAPI;
    }

    public abstract void drawGraph();
}
