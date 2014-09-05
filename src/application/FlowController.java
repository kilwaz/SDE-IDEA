package application;

import application.tester.TestResultSet;
import application.utils.DataBank;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FlowController {
    private DrawableNode startNode;
    private List<DrawableNode> nodes = new ArrayList<DrawableNode>();
    private List<NodeConnection> connections = new ArrayList<NodeConnection>();
    private String referenceID = "test";
    private Program parentProgram;

    public FlowController(Program parentProgram) {
        this.parentProgram = parentProgram;
        startNode = new FlowNode(30.0, 30.0, "Start");
        startNode.setId(-1);
    }

    public void createNewNode(Integer id, Integer programId, String containedText, String source, String referenceID, Double sourceX, Double sourceY, Boolean isStartNode) {
        DrawableNode newNode;
        if ("".equals(source)) {
            newNode = new TestResultSet(sourceX, sourceY, containedText, id, programId);
        } else {
            newNode = new FlowNode(sourceX, sourceY, containedText, source, id, programId);
        }

        nodes.add(newNode);
        if (isStartNode && newNode instanceof FlowNode) {
            startNode = newNode;
        }

        this.referenceID = referenceID;
    }

    public void addNode(DrawableNode drawableNode) {
        drawableNode.setProgramId(parentProgram.getId());
        nodes.add(drawableNode);
    }

    public void removeNode(DrawableNode drawableNode) {
        nodes.remove(drawableNode);
        ArrayList<NodeConnection> connectionsToRemove = new ArrayList<NodeConnection>();
        for (NodeConnection connection : connections) {
            if (connection.getConnectionStart() == drawableNode || connection.getConnectionEnd() == drawableNode) {
                connectionsToRemove.add(connection);
            }
        }

        for (NodeConnection connection : connectionsToRemove) {
            connections.remove(connection);
        }
    }

    public void addConnection(NodeConnection connection) {
        connections.add(connection);
    }

    public List<NodeConnection> getConnections() {
        return this.connections;
    }

    public List<DrawableNode> getNodes() {
        return this.nodes;
    }

    public DrawableNode getStartNode() {
        return startNode;
    }

    public void setStartNode(DrawableNode startNode) {
        this.startNode = startNode;
    }

    public String getReferenceID() {
        return this.referenceID;
    }

    public Boolean checkIfTreeIsCompiled() {
        Boolean result = true;

        for (DrawableNode node : nodes) {
            if (node instanceof FlowNode) {
                result = ((FlowNode) node).getSource().isCompiled();
                if (!result) {
                    break;
                }
            }
        }

        return result;
    }

    public Boolean compile() {
        for (DrawableNode node : nodes) {
            node.setColor(Color.DARKRED);
        }

        Controller.getInstance().updateCanvasControllerLater();

        for (DrawableNode node : nodes) {
            if (node instanceof FlowNode) {
                Boolean result = ((FlowNode) node).getSource().compile();
                if (result) {
                    node.setColor(Color.LIMEGREEN);
                } else {
                    node.setColor(Color.ORANGE);
                }
                Controller.getInstance().updateCanvasControllerLater();
            }
        }

        return true; // This should return what the actual compile method returns..
    }

    public void loadInstances() {
        for (DrawableNode node : nodes) {
            if (node instanceof FlowNode) {
                DataBank.saveInstanceObject(referenceID, node.getContainedText(), ((FlowNode) node).getSource());
            } else if (node instanceof TestResultSet) {
                DataBank.saveInstanceObject(referenceID, node.getContainedText(), node);
            }
        }
    }

    public Program getParentProgram() {
        return this.parentProgram;
    }

    public List<DrawableNode> getClickedNodes(Double x, Double y) {
        List<DrawableNode> nodeList = new ArrayList<DrawableNode>();

        for (DrawableNode node : nodes) {
            if (node.isCoordInside(x, y)) {
                nodeList.add(node);
            }
        }

        return nodeList;
    }

    public DrawableNode getNodeById(Integer id) {
        for (DrawableNode node : nodes) {
            if (node.getId().equals(id)) {
                return node;
            }
        }

        return null;
    }

    public void checkConnections() {
        Boolean updateCanvas = false;

        // Find new connections and creates them
        for (DrawableNode startNode : nodes) {
            if (startNode instanceof FlowNode) {
                String src = ((FlowNode) startNode).getSource().getSource();

                for (DrawableNode endNode : getNodes()) {
                    if (src.contains("run(\"" + endNode.getContainedText())) {
                        if (!connectionExists(startNode, endNode)) {
                            NodeConnection newConnection = new NodeConnection(startNode, endNode);
                            connections.add(newConnection);
                            updateCanvas = true;
                        }
                    }
                }
            }
        }

        // Checks old connections and removes ones that don't exist
        List<NodeConnection> listToRemove = new ArrayList<NodeConnection>();
        for (NodeConnection nodeConnection : connections) {
            if (startNode instanceof FlowNode) {
                if (!((FlowNode) nodeConnection.getConnectionStart()).getSource().getSource().contains("run(\"" + nodeConnection.getConnectionEnd().getContainedText())) {
                    listToRemove.add(nodeConnection);
                    updateCanvas = true;
                }
            }

        }

        connections.removeAll(listToRemove);
        if (updateCanvas) {
            Controller.getInstance().updateCanvasControllerLater();
        }
    }

    public Boolean connectionExists(DrawableNode start, DrawableNode end) {
        for (NodeConnection nodeConnection : connections) {
            if (nodeConnection.getConnectionStart() == start && nodeConnection.getConnectionEnd() == end) {
                return true;
            }
        }

        return false;
    }

    public static FlowController getFlowControllerFromSource(Source source) {
        for (Program program : DataBank.getPrograms()) {
            for (DrawableNode node : program.getFlowController().getNodes()) {
                if (node instanceof FlowNode) {
                    if (((FlowNode) node).getSource() == source) {
                        return program.getFlowController();
                    }
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
        for (DrawableNode node : nodes) {
            node.setColor(Color.BLACK);
        }
        Controller.getInstance().updateCanvasControllerLater();
    }

    public static FlowNode getSourceFromReference(String reference) {
        for (Program program : DataBank.getPrograms()) {
            for (DrawableNode node : program.getFlowController().getNodes()) {
                if (node.getId().toString().equals(reference)) {
                    return (FlowNode) node;
                }
            }
        }

        return null;
    }
}
