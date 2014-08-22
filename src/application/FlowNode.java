package application;

import application.utils.DataBank;
import javafx.scene.paint.Color;

import java.util.HashMap;

public class FlowNode {
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Color color;
    private Double scale = 1.0;
    private String containedText = "";
    private Integer id = -1;
    private Source source = null;
    private FlowController parentController;
    private Integer programId = -1;

    public FlowNode(Double x, Double y, String containedText) {
        this.x = x;
        this.y = y;
        this.height = 40.0;
        this.width = 50.0;
        this.color = Color.BLACK;
        this.containedText = containedText;
        this.source = new Source(this);
    }

    public FlowNode(Double x, Double y, String containedText, String source, Integer id, Integer programId) {
        this.x = x;
        this.y = y;
        this.height = 40.0;
        this.width = 50.0;
        this.color = Color.BLACK;
        this.containedText = containedText;
        this.id = id;
        this.programId = programId;
        this.source = new Source(this, source, id);
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

    public Integer getProgramId() {
        return this.programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public void run() {
        this.source.run(false, new HashMap<String, Object>());
    }

    public Boolean isCoordInside(Double x, Double y) {
        if (x > this.x * scale && x < this.x * scale + this.width * scale) {
            if (y > this.y * scale && y < this.y * scale + this.height * scale) {
                return true;
            }
        }

        return false;
    }
}
