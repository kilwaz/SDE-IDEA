package application.tester;

import application.DrawableNode;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class TestResultSet extends DrawableNode {
    private ObservableList<TestResult> resultList = FXCollections.observableArrayList();

    public TestResultSet(Double x, Double y, String containedText) {
        super(x, y, 50.0, 40.0, Color.BLACK, containedText, -1, -1);
    }

    public TestResultSet(Double x, Double y, String containedText, Integer id, Integer programId) {
        super(x, y, 50.0, 40.0, Color.BLACK, containedText, programId, id);
    }

    public void addResult(TestResult testResult) {
        class OneShotTask implements Runnable {
            private TestResult testResult;

            OneShotTask(TestResult testResult) {
                this.testResult = testResult;
            }

            public void run() {
                resultList.add(testResult);
            }
        }

        Platform.runLater(new OneShotTask(testResult));
    }

    public ObservableList<TestResult> getResultList() {
        return resultList;
    }
}
