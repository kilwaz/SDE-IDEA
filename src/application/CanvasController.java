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
    private FlowNode draggedNode = null;
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
            drawProgram(DataBank.currentlyEditProgram);
            isDraggingNode = true;
        }
    }

    public void canvasMouseDown(MouseEvent event) {
        if (event.isPrimaryButtonDown()) {
            Program program = DataBank.currentlyEditProgram;

            List<FlowNode> clickedNodes = program.getFlowController().getClickedNodes(event.getX(), event.getY());
            if (clickedNodes.size() > 0) {
                draggedNode = clickedNodes.get(0);
                dragXOffset = draggedNode.getX() - event.getX();
                dragYOffset = draggedNode.getY() - event.getY();
            }
        }
    }

    public Boolean canvasMouseUp(MouseEvent event) {
        if (isDraggingNode) {
            draggedNode = null;
            isDraggingNode = false;
            drawProgram(DataBank.currentlyEditProgram);
            return true;
        } else {
            return false;
        }
    }

    public void setFlowNodeScale(FlowNode flowNode, Double scale) {
        flowNode.setScale(scale);
        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            setFlowNodeScale(loopFlowNode, scale);
        }
    }

    public void drawProgram(Program program) {
        setFlowNodeScale(program.getFlowController().getStartNode(), this.scale);
        gc.clearRect(0, 0, canvasFlow.getWidth(), canvasFlow.getHeight());

        drawNode(program.getFlowController().getStartNode());
    }

    public void drawNode(FlowNode flowNode) {
        gc.setStroke(flowNode.getColor());
        gc.setFill(Color.WHITE);
        gc.fillRect(flowNode.getScaledX(), flowNode.getScaledY(), flowNode.getScaledWidth(), flowNode.getScaledHeight());
        gc.strokeRect(flowNode.getScaledX(), flowNode.getScaledY(), flowNode.getScaledWidth(), flowNode.getScaledHeight());

        gc.setFill(Color.GRAY);
        int offsetThing = 0;
        double innerLoop = 0;
        //colourCounter

        for (double j = 0; j < 10; j++) {
            for (innerLoop = colourCounter; innerLoop < 0.7; innerLoop = innerLoop + 0.05) {
                gc.setFill(new Color(innerLoop, innerLoop, innerLoop, 0.75));
                gc.fillRect(flowNode.getScaledX() + 20 + offsetThing, flowNode.getScaledY() + 42, 2, 2);
                offsetThing++;
            }
        }
        colourCounter = colourCounter + 0.05;
        if (colourCounter > 0.7) {
            colourCounter = 0.0;
        }

        drawContainedText(flowNode);

        Integer offset = 0;
        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            drawConnectingLine(flowNode, loopFlowNode, offset);
            drawNode(loopFlowNode);
            offset++;
        }
    }

    public void drawContainedText(FlowNode flowNode) {
        if (flowNode.getContainedText() != null) {
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(VPos.CENTER);
            gc.setFill(Color.BLACK);
            gc.fillText(flowNode.getContainedText(), flowNode.getScaledCenterX(), flowNode.getScaledCenterY());

        }
    }

    public void drawConnectingLine(FlowNode startNode, FlowNode endNode, Integer offset) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);

        Double nodePadding = (30 + (offset * 5)) * startNode.getScale();

        gc.strokeLine(startNode.getScaledCenterX() + startNode.getScaledWidth() / 2, startNode.getScaledCenterY(), startNode.getScaledCenterX() + nodePadding, startNode.getScaledCenterY());
        gc.strokeLine(startNode.getScaledCenterX() + nodePadding, startNode.getScaledCenterY(), startNode.getScaledCenterX() + nodePadding, endNode.getScaledCenterY());
        gc.strokeLine(startNode.getScaledCenterX() + nodePadding, endNode.getScaledCenterY(), endNode.getScaledCenterX() - nodePadding, endNode.getScaledCenterY());
        gc.strokeLine(endNode.getScaledCenterX() - nodePadding, endNode.getScaledCenterY(), endNode.getScaledCenterX() - endNode.getScaledWidth() / 2, endNode.getScaledCenterY());
    }
}