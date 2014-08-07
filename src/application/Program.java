package application;

public class Program {
    private String programName;
    private FlowController flowController;
    private Integer id = -1;

    public Program(String programName) {
        this.programName = programName;
        flowController = new FlowController();
    }

    public Program(String programName, Integer id) {
        this.programName = programName;
        this.id = id;
        flowController = new FlowController();
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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
        this.flowController.loadInstances();
        this.flowController.getStartNode().run();
    }

    public static void runHelper(String name, String referenceID) {
        Source source = (Source) DataBank.loadInstanceObject(referenceID, name);
        source.run();
    }
}
