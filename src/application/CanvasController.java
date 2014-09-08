package application;

import application.utils.DataBank;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.List;

public class CanvasController {
    private Canvas canvasFlow;
    private GraphicsContext gc;
    private PixelWriter pw;

    private Boolean isDraggingNode = false;
    private DrawableNode draggedNode = null;
    private Double dragXOffset = 0.0;
    private Double dragYOffset = 0.0;
    private Double scale = 1.0;
    private Double colourCounter = 0.0;

    public CanvasController(Canvas canvasFlow) {
        this.canvasFlow = canvasFlow;
        gc = canvasFlow.getGraphicsContext2D();
        pw = gc.getPixelWriter();
    }

    public Double getScale() {
        return this.scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public void canvasDragged(MouseEvent event) {
        if (event.isPrimaryButtonDown() && draggedNode != null) {
            draggedNode.setX(event.getX() + dragXOffset);
            draggedNode.setY(event.getY() + dragYOffset);
            drawProgram();
            isDraggingNode = true;
        }
    }

    public void canvasMouseDown(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            Program program = DataBank.currentlyEditProgram;

            List<DrawableNode> clickedNodes = program.getFlowController().getClickedNodes(event.getX(), event.getY());
            if (clickedNodes.size() > 0) {
                draggedNode = clickedNodes.get(0);
                dragXOffset = draggedNode.getX() - event.getX();
                dragYOffset = draggedNode.getY() - event.getY();
            }
        }
    }

    public Boolean canvasMouseUp(MouseEvent event) {
        if (isDraggingNode) {
            DataBank.saveNode(draggedNode);
            draggedNode = null;
            isDraggingNode = false;
            drawProgram();
            return true;
        } else {
            return false;
        }
    }

    public void setFlowNodeScale(DrawableNode flowNode, Double scale) {
//        flowNode.setScale(scale);
//        for (FlowNode loopFlowNode : flowNode.getChildren()) {
//            setFlowNodeScale(loopFlowNode, scale);
//        }
    }

    public void drawProgram() {
        Program program = DataBank.currentlyEditProgram;

        setFlowNodeScale(program.getFlowController().getStartNode(), this.scale);
        gc.clearRect(0, 0, canvasFlow.getWidth(), canvasFlow.getHeight()); // Clears the screen

        // Draw the bottom layer first and build up
        // Connections
        Integer offset = 0;
        for (NodeConnection connection : program.getFlowController().getConnections()) {
            drawConnectingLine(connection.getConnectionStart(), connection.getConnectionEnd(), offset);
            offset++;
        }

        // Nodes
        for (DrawableNode node : program.getFlowController().getNodes()) {
            drawNode(node);
        }
    }

    public void drawNode(DrawableNode drawableNode) {
        gc.setStroke(drawableNode.getColor());
        gc.setFill(Color.WHITE);
        gc.fillRect(drawableNode.getScaledX(), drawableNode.getScaledY(), drawableNode.getScaledWidth(), drawableNode.getScaledHeight());
        gc.strokeRect(drawableNode.getScaledX(), drawableNode.getScaledY(), drawableNode.getScaledWidth(), drawableNode.getScaledHeight());

        gc.setFill(Color.GRAY);
        int offsetThing = 0;
        double innerLoop = 0;
        //colourCounter

//        for (double j = 0; j < 10; j++) {
//            for (innerLoop = colourCounter; innerLoop < 0.7; innerLoop = innerLoop + 0.05) {
//                gc.setFill(new Color(innerLoop, innerLoop, innerLoop, 0.75));
//                gc.fillRect(flowNode.getScaledX() + 20 + offsetThing, flowNode.getScaledY() + 42, 2, 2);
//                offsetThing++;
//            }
//        }
//        colourCounter = colourCounter + 0.05;
//        if (colourCounter > 0.7) {
//            colourCounter = 0.0;
//        }

        drawContainedText(drawableNode);
    }

    public void drawContainedText(DrawableNode drawableNode) {
        if (drawableNode.getContainedText() != null) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.BLACK);
            gc.fillText(drawableNode.getContainedText(), drawableNode.getScaledCenterX(), drawableNode.getScaledCenterY());

        }
    }

    public void drawConnectingLine(DrawableNode startNode, DrawableNode endNode, Integer offset) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        Double nodePadding = (30 + (offset * 5)) * startNode.getScale();

        gc.strokeLine(startNode.getScaledCenterX() + startNode.getScaledWidth() / 2, startNode.getScaledCenterY(), startNode.getScaledCenterX() + nodePadding, startNode.getScaledCenterY());
        gc.strokeLine(startNode.getScaledCenterX() + nodePadding, startNode.getScaledCenterY(), startNode.getScaledCenterX() + nodePadding, endNode.getScaledCenterY());
        gc.strokeLine(startNode.getScaledCenterX() + nodePadding, endNode.getScaledCenterY(), endNode.getScaledCenterX() - nodePadding, endNode.getScaledCenterY());
        gc.strokeLine(endNode.getScaledCenterX() - nodePadding, endNode.getScaledCenterY(), endNode.getScaledCenterX() - endNode.getScaledWidth() / 2, endNode.getScaledCenterY());
    }
}