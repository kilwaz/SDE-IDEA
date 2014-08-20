package application;

public class SourceConnection {
    private FlowNode connectionStart;
    private FlowNode connectionEnd;

    public SourceConnection(FlowNode connectionStart, FlowNode connectionEnd) {
        this.connectionEnd = connectionEnd;
        this.connectionStart = connectionStart;
    }

    public FlowNode getConnectionStart() {
        return this.connectionStart;
    }

    public FlowNode getConnectionEnd() {
        return this.connectionEnd;
    }
}
