package application;

import application.utils.DataBank;

import java.util.ArrayList;
import java.util.List;

public class FlowController {
    private FlowNode startNode;
    private String referenceID = "test";

    public FlowController() {
        startNode = new FlowNode(30.0, 30.0, "Start");
        startNode.setId(-1);
//        FlowNode node = new FlowNode(100.0, 120.0, "One");
//        startNode.addChild(node);
//        startNode.addChild(new FlowNode(100.0, 30.0, "Two"));
//        node.addChild(new FlowNode(200.0, 120.0, "Three"));
    }

    public void createNewNode(Integer id, String containedText, String source, String referenceID, Boolean isStartNode) {
        if (isStartNode) {
            startNode = new FlowNode(30.0, 30.0, containedText, source, id);
        }

        this.referenceID = referenceID;
    }

    public FlowNode getStartNode() {
        return startNode;
    }

    public String getReferenceID() {
        return this.referenceID;
    }

    public Boolean checkIfTreeIsCompiled() {
        return checkIfTreeIsCompiled(startNode);
    }

    public Boolean compile() {
        return compile(startNode);
    }

    public void loadInstances() {
        loadInstances(startNode, referenceID);
    }

    public void loadInstances(FlowNode flowNode, String referenceID) {
        DataBank.saveInstanceObject(referenceID, flowNode.getContainedText(), flowNode.getSource());

        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            loadInstances(loopFlowNode, referenceID);
        }
    }

    private Boolean compile(FlowNode flowNode) {
        Boolean result = true;

        System.out.println("Compiling " + flowNode.getId() + " -> " + flowNode.getContainedText());
        Boolean compileResult = flowNode.getSource().compile();
        if (result) {
            result = compileResult;
        }

        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            compile(loopFlowNode);
        }

        return result;
    }

    private Boolean checkIfTreeIsCompiled(FlowNode flowNode) {
        Boolean result = true;

        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            result = checkIfTreeIsCompiled(loopFlowNode);
            if (!result) {
                break;
            }
        }

        return result;
    }

    public List<FlowNode> getClickedNodes(Double x, Double y) {
        List<FlowNode> nodeList = new ArrayList<FlowNode>();
        return checkNodeCoords(x, y, startNode, nodeList);
    }

    private List<FlowNode> checkNodeCoords(Double x, Double y, FlowNode flowNode, List<FlowNode> nodeList) {
        if (flowNode.isCoordInside(x, y)) {
            nodeList.add(flowNode);
        }

        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            checkNodeCoords(x, y, loopFlowNode, nodeList);
        }
        return nodeList;
    }

    public FlowNode getNodeById(Integer id) {
        return getNodeById(startNode, id);
    }

    private FlowNode getNodeById(FlowNode flowNode, Integer id) {
        FlowNode result = null;
        if (flowNode.getId().equals(id)) {
            return flowNode;
        } else {
            for (FlowNode loopFlowNode : flowNode.getChildren()) {
                result = getNodeById(loopFlowNode, id);
                if (result != null) {
                    break;
                }
            }
        }

        return result;
    }

    public static FlowController getFlowControllerFromSource(Source source) {
        FlowController foundController = null;
        for (Program program : DataBank.getPrograms()) {
            Boolean found = findSource(program.getFlowController().getStartNode(), source);
            if (found) {
                foundController = program.getFlowController();
                break;
            }

        }

        return foundController;
    }

    private static Boolean findSource(FlowNode flowNode, Source source) {
        if (flowNode.getSource() == source) {
            return true;
        } else {
            for (FlowNode loopNode : flowNode.getChildren()) {
                findSource(loopNode, source);
            }
        }
        return false;
    }
}
