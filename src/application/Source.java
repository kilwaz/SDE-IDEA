package application;

public class Source {
    private Boolean compiled = false;
    private String source;
    private Object compiledInstance;
    private FlowNode parentFlowNode;
    private Integer id = -1;

    Source(FlowNode parentFlowNode) {
        this.parentFlowNode = parentFlowNode;
        this.source = "public void run() {\n" +
                "   System.out.println(\"Sample code\");\n" +
                "}";
    }

    Source(FlowNode parentFlowNode, String source, Integer id) {
        this.parentFlowNode = parentFlowNode;
        this.source = source;
        this.id = id;
    }

    public FlowNode getParentFlowNode() {
        return this.parentFlowNode;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        if (!this.source.equals(source)) {
            this.compiled = false;
            this.source = source;
            DataBank.saveNode(parentFlowNode);
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean isCompiled() {
        return this.compiled;
    }

    public Object getCompiledInstance() {
        return this.compiledInstance;
    }

    public Boolean compile() {
        this.compiledInstance = null;
        this.compiled = false;
        Object instance = CompileCode.compileCode(this);
        if (instance != null) {
            this.compiled = true;
            this.compiledInstance = instance;
            return true;
        }
        return false;
    }

    public void run() {
        if (!this.compiled) {
            compile();
        }
        if (this.compiled) {
            new Thread((Runnable) compiledInstance).start();
        }
    }
}
