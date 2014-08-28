package application;

import javafx.scene.paint.Color;

public class DrawableNode {
    private Integer id = -1;
    private Double x;
    private Double y;
    private Double width;
    private Double height;
    private Color color;
    private Double scale = 1.0;
    private String containedText = "";
    private Integer programId = -1;

    public DrawableNode(Double x, Double y, Double width, Double height, Color color, String containedText, Integer programId, Integer id) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.color = color;
        this.containedText = containedText;
        this.programId = programId;
        this.id = id;
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

    public Double getScale() {
        return this.scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public void setContainedText(String containedText) {
        this.containedText = containedText;
    }

    public Integer getProgramId() {
        return this.programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
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
