package application;

import application.utils.DataBank;

import java.util.HashMap;

public class Program {
    private String programName;
    private FlowController flowController;
    private Integer id = -1;

    public Program(String programName) {
        this.programName = programName;
        flowController = new FlowController(this);
    }

    public Program(String programName, Integer id) {
        this.programName = programName;
        this.id = id;
        flowController = new FlowController(this);
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getId() {
        return this.id;
    }

    public FlowController getFlowController() {
        return this.flowController;
    }

    public Boolean isCompiled() {
        return this.flowController.checkIfTreeIsCompiled();
    }

    public Boolean compile() {
        return this.flowController.compile();
    }

    public void run() {
        getFlowController().setSourceToBlack();
        this.flowController.loadInstances();
        this.flowController.getStartNode().run();
    }

    public static void runHelper(String name, String referenceID, Boolean whileWaiting, HashMap<String, Object> map) {
        Source source = (Source) DataBank.getInstanceObject(referenceID, name);
        source.run(whileWaiting, map);
    }

    public String toString() {
        return "" + this.programName;
    }
}
