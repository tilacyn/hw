package org.tilacyn.graph;

import java.util.List;

public interface DrawingAPI {
    int getCanvasWidth();
    void setVertices(List<Vertex> vertices);
    void setEdges(List<Edge> edges);
    void showGraph();
}
