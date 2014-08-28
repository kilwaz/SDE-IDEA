package application.tester;

import application.DrawableNode;
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
        resultList.add(testResult);
//        TableView<TestResult> tableView = (TableView<TestResult>) Controller.getInstance().getElementById("resultsTable-" + getId());
//        if (tableView != null) {
//            System.out.println("FOUND!");
//            tableView.setItems(resultList);
//        } else {
//            System.out.println("DID NOT FIND!");
//        }
    }

    public ObservableList<TestResult> getResultList() {
        return resultList;
    }
}
