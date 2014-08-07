package application;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FlowNode {
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Color color;
    private Double scale = 1.0;
    private List<FlowNode> children = new ArrayList<FlowNode>();
    private String containedText = "";
    private Integer id = -1;
    private Source source = null;
    private FlowNode parent = null;
    private FlowController parentController;

    public FlowNode(Double x, Double y) {
        this.x = x;
        this.y = y;
        this.height = 40.0;
        this.width = 40.0;
        this.color = Color.BLACK;
        this.source = new Source(this);
    }

    public FlowNode(Double x, Double y, String containedText) {
        this.x = x;
        this.y = y;
        this.height = 40.0;
        this.width = 40.0;
        this.color = Color.BLACK;
        this.containedText = containedText;
        this.source = new Source(this);
    }

    public FlowNode(Double x, Double y, String containedText, String source, Integer id) {
        this.x = x;
        this.y = y;
        this.height = 40.0;
        this.width = 40.0;
        this.color = Color.BLACK;
        this.containedText = containedText;
        this.id = id;
        this.source = new Source(this, source, id);
    }

    public void addChild(FlowNode flowNode) {
        flowNode.setId(getId() + children.size());
        flowNode.setParent(this);
        children.add(flowNode);
    }

    public FlowNode getParent() {
        return this.parent;
    }

    public void setParent(FlowNode parent) {
        this.parent = parent;
    }

    public List<FlowNode> getChildren() {
        return children;
    }

    public Double getCenterX() {
        return this.x + (this.width / 2);
    }

    public Double getCenterY() {
        return this.y + (this.height / 2);
    }

    public Double getX() {
        return this.x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return this.y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getWidth() {
        return this.width;
    }

    public Double getHeight() {
        return this.height;
    }

    public Double getScaledCenterX() {
        return (this.x * scale) + ((this.width * scale) / 2);
    }

    public Double getScaledCenterY() {
        return (this.y * scale) + ((this.height * scale) / 2);
    }

    public Double getScaledX() {
        return this.x * scale;
    }

    public Double getScaledY() {
        return this.y * scale;
    }

    public Double getScaledWidth() {
        return this.width * scale;
    }

    public Double getScaledHeight() {
        return this.height * scale;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getContainedText() {
        return containedText;
    }

    public void setContainedText(String containedText) {
        this.containedText = containedText;
        DataBank.saveNode(this);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
        this.source.setId(id);
    }

    public Source getSource() {
        return this.source;
    }

    public Double getScale() {
        return this.scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public void run() {
        this.source.run();
    }

    public Boolean isCoordInside(Double x, Double y) {
        if (x > this.x * scale && x < this.x * scale + this.width * scale) {
            if (y > this.y * scale && y < this.y * scale + this.height * scale) {
                return true;
            }
        }

        return false;
    }

    public void delete() {
        children = null;
        if (parent != null) {
            parent.getChildren().remove(this);
        }
    }
}
