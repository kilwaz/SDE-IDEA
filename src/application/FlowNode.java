package application;

import application.utils.DataBank;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class FlowNode extends DrawableNode {
    private Source source = null;
    private FlowController parentController;

    public FlowNode(Double x, Double y, String containedText) {
        super(x, y, 50.0, 40.0, Color.BLACK, containedText, -1, -1);
        this.source = new Source(this);
    }

    public FlowNode(Double x, Double y, String containedText, String source, Integer id, Integer programId) {
        super(x, y, 50.0, 40.0, Color.BLACK, containedText, programId, id);
        this.source = new Source(this, source, id);
    }

    public void setContainedText(String containedText) {
        super.setContainedText(containedText);
        DataBank.saveNode(this);
    }

    public void setId(Integer id) {
        super.setId(id);
        this.source.setId(id);
    }

    public String getNodeType() {
        return "FlowNode";
    }

    public Source getSource() {
        return this.source;
    }

    public void run() {
        this.source.run(false, new HashMap<String, Object>());
    }
}
