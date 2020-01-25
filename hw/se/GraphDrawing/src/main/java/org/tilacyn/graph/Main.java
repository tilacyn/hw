package org.tilacyn.graph;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static Map<String, DrawingAPI> apiMap = new HashMap<>();

    static {
        JavaAwtAPI awtAPI = new JavaAwtAPI();
        JavaFxDrawingAPI fxAPI = new JavaFxDrawingAPI();
        apiMap.put("awt", awtAPI);
        apiMap.put("fx", fxAPI);
    }

    public static void main(String... args) {
        String drawingApiType = args[0];
        String graphType = args[1];

        DrawingAPI drawingAPI = apiMap.get(drawingApiType);

        Graph graph;
        if (graphType.equals("list")) {
            graph = new ListGraph(drawingAPI, readListGraph());
        } else {
            graph = new MatrixGraph(drawingAPI, readMatrixGraph());
        }
        graph.drawGraph();

        drawingAPI.showGraph();
    }

    private static List<List<Integer>> readListGraph() {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<Integer> neighbors = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                neighbors.add(j);
            }
            result.add(neighbors);
        }
        return result;
    }

    private static boolean[][] readMatrixGraph() {
        int N = 30;
        boolean[][] result = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                result[i][j] = true;
            }
        }
        return result;
    }
}
