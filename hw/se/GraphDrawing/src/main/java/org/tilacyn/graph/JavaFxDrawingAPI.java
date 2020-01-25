package org.tilacyn.graph;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;


public class JavaFxDrawingAPI extends Application implements DrawingAPI {

    private static final int POINT_WIDTH = 30;
    private static final int CANVAS_WIDTH = 1000;
    private static final int CANVAS_HEIGHT = 1000;
    private static final int LINE_WIDTH = 1;
    private static final int NUMBER_MAX_WIDTH = 3;


    private static GraphicsContext gc;
    private static List<Vertex> verticesToDraw;
    private static List<Edge> edges;


    public void drawEdge(Edge e) {
        gc.strokeLine(e.xstart, e.ystart, e.xend, e.yend);
    }

    public int getCanvasWidth() {
        return CANVAS_WIDTH;
    }



    public void drawVertex(Vertex v) {
        double x = v.x + v.xoffset;
        double y = v.y + v.yoffset;
        gc.strokeOval(x, y, POINT_WIDTH, POINT_WIDTH);
        gc.fillText(String.valueOf(v.number), x + 12, y + 18);
    }

    private void drawGraph() {
        System.out.println("Drawing " + verticesToDraw.size() + " vertices");
        verticesToDraw.forEach(this::drawVertex);

        System.out.println("Drawing " + edges.size() + " edges");
        edges.forEach(this::drawEdge);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Circle Graph");
        Group root = new Group();
        Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.GREEN);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(LINE_WIDTH);
        drawGraph();
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void setVertices(List<Vertex> verticesToDraw) {
        JavaFxDrawingAPI.verticesToDraw = verticesToDraw;
    }

    public void setEdges(List<Edge> edges) {
        JavaFxDrawingAPI.edges = edges;
    }

    @Override
    public void showGraph() {
        Application.launch(JavaFxDrawingAPI.class);
    }
}