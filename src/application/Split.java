package application;

public class Split {
    private Boolean enabled = false;
    private String target;

    public Split(String target) {
        this.target = target;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void toggle() {
        enabled = !enabled;
    }
}
