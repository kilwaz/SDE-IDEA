package application;

import application.utils.DataBank;

import java.util.ArrayList;
import java.util.List;

public class FlowController {
    private FlowNode startNode;
    private List<FlowNode> sources = new ArrayList<FlowNode>();
    private String referenceID = "test";
    private Program parentProgram;

    public FlowController(Program parentProgram) {
        this.parentProgram = parentProgram;
        startNode = new FlowNode(30.0, 30.0, "Start");
        startNode.setId(-1);
    }

    public void createNewNode(Integer id, Integer programId, String containedText, String source, String referenceID, Boolean isStartNode) {
        FlowNode newNode = new FlowNode(30.0, 30.0, containedText, source, id, programId);
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

    public List<FlowNode> getSources() {
        return this.sources;
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

    public Program getParentProgram() {
        return this.parentProgram;
    }

    public void loadInstances(FlowNode flowNode, String referenceID) {
        DataBank.saveInstanceObject(referenceID, flowNode.getContainedText(), flowNode.getSource());

        for (FlowNode loopFlowNode : flowNode.getChildren()) {
            loadInstances(loopFlowNode, referenceID);
        }
    }

    private Boolean compile(FlowNode flowNode) {
        System.out.println("Compiling " + flowNode.getId() + " -> " + flowNode.getContainedText());
        Boolean result = flowNode.getSource().compile();

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
}
