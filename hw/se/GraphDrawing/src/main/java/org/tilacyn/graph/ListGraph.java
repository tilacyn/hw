package org.tilacyn.graph;

import java.util.ArrayList;
import java.util.List;

public class ListGraph extends CircleGraph {
    private List<List<Integer>> neighbors;

    public ListGraph(DrawingAPI drawingAPI, List<List<Integer>> neighbors) {
        super(drawingAPI);
        this.neighbors = neighbors;
        this.n = neighbors.size();

    }

    @Override
    protected void calculateEdges() {
        List<Edge> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            final int fi = i;
            neighbors.get(i).forEach(j -> result.add(fromVertices(fi, j)));
        }
        edges = result;
    }
}
