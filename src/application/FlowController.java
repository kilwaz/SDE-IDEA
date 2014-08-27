package application;

import application.utils.DataBank;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FlowController {
    private FlowNode startNode;
    private List<FlowNode> sources = new ArrayList<FlowNode>();
    private List<SourceConnection> connections = new ArrayList<SourceConnection>();
    private String referenceID = "test";
    private Program parentProgram;

    public FlowController(Program parentProgram) {
        this.parentProgram = parentProgram;
        startNode = new FlowNode(30.0, 30.0, "Start");
        startNode.setId(-1);
    }

    public void createNewNode(Integer id, Integer programId, String containedText, String source, String referenceID, Double sourceX, Double sourceY, Boolean isStartNode) {
        FlowNode newNode = new FlowNode(sourceX, sourceY, containedText, source, id, programId);
        sources.add(newNode);
        if (isStartNode) {
            startNode = newNode;
        }

        this.referenceID = referenceID;
    }

    public void addSource(FlowNode flowNode) {
        flowNode.setProgramId(parentProgram.getId());
        sources.add(flowNode);
    }

    public void addConnection(SourceConnection connection) {
        connections.add(connection);
    }

    public List<SourceConnection> getConnections() {
        return this.connections;
    }

    public List<FlowNode> getSources() {
        return this.sources;
    }

    public FlowNode getStartNode() {
        return startNode;
    }

    public void setStartNode(FlowNode startNode) {
        this.startNode = startNode;
    }

    public String getReferenceID() {
        return this.referenceID;
    }

    public Boolean checkIfTreeIsCompiled() {
        Boolean result = true;

        for (FlowNode flowNode : sources) {
            result = flowNode.getSource().isCompiled();
            if (!result) {
                break;
            }
        }

        return result;
    }

    public Boolean compile() {
        for (FlowNode node : sources) {
            node.setColor(Color.DARKRED);
        }

        Controller.getInstance().updateCanvasControllerLater();

        for (FlowNode node : sources) {
            Boolean result = node.getSource().compile();
            if (result) {
                node.setColor(Color.LIMEGREEN);
            } else {
                node.setColor(Color.ORANGE);
            }
            Controller.getInstance().updateCanvasControllerLater();
        }

        return true; // This should return what the actual compile method returns..
    }

    public void loadInstances() {
        for (FlowNode node : sources) {
            DataBank.saveInstanceObject(referenceID, node.getContainedText(), node.getSource());
        }
    }

    public Program getParentProgram() {
        return this.parentProgram;
    }

    public List<FlowNode> getClickedNodes(Double x, Double y) {
        List<FlowNode> nodeList = new ArrayList<FlowNode>();

        for (FlowNode node : sources) {
            if (node.isCoordInside(x, y)) {
                nodeList.add(node);
            }
        }

        return nodeList;
    }

    public FlowNode getNodeById(Integer id) {
        for (FlowNode node : sources) {
            if (node.getId().equals(id)) {
                return node;
            }
        }

        return null;
    }

    public void checkConnections() {
        Boolean updateCanvas = false;

        // Find new connections and creates them
        for (FlowNode startNode : sources) {
            String src = startNode.getSource().getSource();

            for (FlowNode endNode : getSources()) {
                if (src.contains("run(\"" + endNode.getContainedText())) {
                    if (!connectionExists(startNode, endNode)) {
                        SourceConnection newConnection = new SourceConnection(startNode, endNode);
                        connections.add(newConnection);
                        updateCanvas = true;
                    }
                }
            }
        }

        // Checks old connections and removes ones that don't exist
        List<SourceConnection> listToRemove = new ArrayList<SourceConnection>();
        for (SourceConnection sourceConnection : connections) {
            if (!sourceConnection.getConnectionStart().getSource().getSource().contains("run(\"" + sourceConnection.getConnectionEnd().getContainedText())) {
                listToRemove.add(sourceConnection);
                updateCanvas = true;
            }
        }

        connections.removeAll(listToRemove);
        if (updateCanvas) {
            Controller.getInstance().updateCanvasControllerLater();
        }
    }

    public Boolean connectionExists(FlowNode start, FlowNode end) {
        for (SourceConnection sourceConnection : connections) {
            if (sourceConnection.getConnectionStart() == start && sourceConnection.getConnectionEnd() == end) {
                return true;
            }
        }

        return false;
    }

    public static FlowController getFlowControllerFromSource(Source source) {
        for (Program program : DataBank.getPrograms()) {
            for (FlowNode node : program.getFlowController().getSources()) {
                if (node.getSource() == source) {
                    return program.getFlowController();
                }
            }
        }

        return null;
    }

    public static void sourceStarted(String reference) {
        FlowNode flowNode = FlowController.getSourceFromReference(reference);
        flowNode.setColor(Color.RED);
        Controller.getInstance().updateCanvasControllerLater();
    }

    public static void sourceFinished(String reference) {
        FlowNode flowNode = FlowController.getSourceFromReference(reference);
        flowNode.setColor(Color.BLACK);
        Controller.getInstance().updateCanvasControllerLater();
    }

    public void setSourceToBlack() {
        for (FlowNode node : sources) {
            node.setColor(Color.BLACK);
        }
        Controller.getInstance().updateCanvasControllerLater();
    }

    public static FlowNode getSourceFromReference(String reference) {
        for (Program program : DataBank.getPrograms()) {
            for (FlowNode node : program.getFlowController().getSources()) {
                if (node.getId().toString().equals(reference)) {
                    return node;
                }
            }
        }

        return null;
    }
}
