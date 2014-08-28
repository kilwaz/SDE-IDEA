package application.tester;

public class TestResult {
    private String outcome;
    private String expected;

    public TestResult() {
    }

    public String getOutcome() {
        return this.outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getExpected() {
        return this.expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }
}
