package org.tilacyn.graph;

import java.util.ArrayList;
import java.util.List;

public class MatrixGraph extends CircleGraph {
    private boolean[][] matrix;

    public MatrixGraph(DrawingAPI drawingAPI, boolean[][] matrix) {
        super(drawingAPI);
        this.matrix = matrix;
        this.n = matrix.length;
    }

    @Override
    protected void calculateEdges() {
        List<Edge> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j]) {
                    result.add(fromVertices(i, j));
                }
            }
        }
        edges = result;
    }

}
