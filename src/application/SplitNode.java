package application;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SplitNode extends DrawableNode {
    private List<Split> splits = new ArrayList<Split>();

    public SplitNode(Double x, Double y, String containedText) {
        super(x, y, 50.0, 40.0, Color.BLACK, containedText, -1, -1);
    }

    public SplitNode(Double x, Double y, String containedText, Integer id, Integer programId, String split1, String split2) {
        super(x, y, 50.0, 40.0, Color.BLACK, containedText, programId, id);
        this.splits.add(new Split(split1));
        this.splits.add(new Split(split2));
    }

    public void setSplit(Integer index, String split) {
        this.splits.set(index, new Split(split));
    }

    public List<Split> getSplits() {
        return this.splits;
    }

    public Split getSplit(Integer index) {
        return this.splits.get(index);
    }

    public void toggleSplit(Integer index) {
        this.splits.get(index).toggle();
    }

    public void run(Boolean whileWaiting, HashMap<String, Object> map) {
        for (Split split : splits) {
            if (split.isEnabled()) {
                FlowNode flowNode = FlowController.getSourceFromContainedText(split.getTarget());
                flowNode.getSource().run(whileWaiting, map);
            }
        }
    }

    public String getNodeType() {
        return "SplitNode";
    }
}
