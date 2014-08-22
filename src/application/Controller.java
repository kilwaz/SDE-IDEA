package application;

import application.net.SSHManager;
import application.utils.DataBank;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TextArea console;

    @FXML
    private StackPane stackPane;

    @FXML
    private ListView<Program> programList;

    @FXML
    private AnchorPane programAccordion;

    @FXML
    private AnchorPane leftAccordionAnchorPane;

    @FXML
    private TitledPane programTitlePane;

    @FXML
    private Accordion leftAccordion;

    @FXML
    private MenuItem menuContextNewProgram;

    @FXML
    private SplitPane splitPanePageCentral;

    @FXML
    private javafx.scene.canvas.Canvas canvasFlow;

    @FXML
    private TabPane tabPaneSource;

    private CanvasController canvasController;
    private Boolean skipCanvasClick = false;
    private static Controller controller;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        Controller.controller = this;

        assert console != null : "fx:id=\"console\" was not injected: check your FXML file 'ApplicationScene.fxml'.";
        assert programList != null : "fx:id=\"programList\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        assert programAccordion != null : "fx:id=\"programAccordion\" was not injected: check your FXML file 'ApplicationScene.fxml'.";
        assert leftAccordionAnchorPane != null : "fx:id=\"leftAccordionAnchorPane\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        assert programTitlePane != null : "fx:id=\"programTitlePane\" was not injected: check your FXML file 'ApplicationScene.fxml'.";
        assert leftAccordion != null : "fx:id=\"leftAccordion\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        assert menuContextNewProgram != null : "fx:id=\"menuContextNewProgram\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        assert splitPanePageCentral != null : "fx:id=\"splitPanePageCentral\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        assert canvasFlow != null : "fx:id=\"canvasFlow\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        assert tabPaneSource != null : "fx:id=\"tabPaneSource\" was not injected: check your FXML file 'ApplicationScene.fxml'.";

        SplitPane.setResizableWithParent(leftAccordionAnchorPane, Boolean.FALSE);
        canvasController = new CanvasController(canvasFlow);

        canvasFlow.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                canvasController.canvasDragged(event);
            }
        });

        canvasFlow.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                canvasController.canvasMouseDown(event);
            }
        });

        canvasFlow.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                skipCanvasClick = canvasController.canvasMouseUp(event);
            }
        });

        canvasFlow.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                canvasController.setScale(canvasController.getScale() + event.getDeltaY() / 400);
                canvasController.drawProgram(DataBank.currentlyEditProgram);
            }
        });

        canvasFlow.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (skipCanvasClick) {
                    skipCanvasClick = false;
                } else {
                    Program program = DataBank.currentlyEditProgram;
                    List<FlowNode> clickNodes = program.getFlowController().getClickedNodes(event.getX(), event.getY());
                    if (clickNodes.size() > 0) {
                        FlowNode flowNode = clickNodes.get(0);
                        createOrShowSourceTab(flowNode);
                    }
                }
            }
        });

        canvasFlow.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            @Override
            public void handle(ContextMenuEvent event) {
                Program program = DataBank.currentlyEditProgram;

                List<FlowNode> clickNodes = program.getFlowController().getClickedNodes(event.getX(), event.getY());
                if (clickNodes.size() > 0) {
                    FlowNode flowNode = clickNodes.get(0);

                    MenuItem menuItemFlowAddNode = new MenuItem("Add Node");
                    menuItemFlowAddNode.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Program program = DataBank.currentlyEditProgram;
                            FlowNode flowNode = program.getFlowController().getNodeById(Integer.parseInt(((MenuItem) event.getSource()).getId().replace("AddNode-", "")));

                            SecureRandom random = new SecureRandom();
                            FlowNode newFlowNode = new FlowNode(flowNode.getX() + 90, flowNode.getY(), new BigInteger(40, random).toString(32));
                            program.getFlowController().addSource(newFlowNode);
                            DataBank.saveNode(newFlowNode); // We need to save the node after creating it to assign the ID correctly
                            canvasController.drawProgram(program);
                        }
                    });
                    menuItemFlowAddNode.setId("AddNode-" + flowNode.getId());

                    MenuItem menuItemFlowStartNode = new MenuItem("Set Start Node");
                    menuItemFlowStartNode.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Program program = DataBank.currentlyEditProgram;
                            FlowNode flowNode = program.getFlowController().getNodeById(Integer.parseInt(((MenuItem) event.getSource()).getId().replace("StartNode-", "")));
                            program.getFlowController().setStartNode(flowNode);
                        }
                    });
                    menuItemFlowStartNode.setId("StartNode-" + flowNode.getId());

                    MenuItem menuItemFlowRemoveNode = new MenuItem("Remove Node");
                    menuItemFlowRemoveNode.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Program program = DataBank.currentlyEditProgram;
                            FlowNode flowNode = program.getFlowController().getNodeById(Integer.parseInt(((MenuItem) event.getSource()).getId().replace("RemoveNode-", "")));

                            canvasController.drawProgram(program);

                            Tab tabToRemove = null;
                            for (Tab loopTab : tabPaneSource.getTabs()) {
                                if (loopTab.getId() != null) {
                                    if (loopTab.getId().equals(flowNode.getId().toString())) {
                                        tabToRemove = loopTab;
                                    }
                                }
                            }

                            if (tabToRemove != null) {
                                EventHandler<Event> handler = tabToRemove.getOnClosed();
                                if (null != handler) {
                                    handler.handle(null);
                                } else {
                                    tabToRemove.getTabPane().getTabs().remove(tabToRemove);
                                }
                            }
                        }
                    });
                    menuItemFlowRemoveNode.setId("RemoveNode-" + flowNode.getId());

                    ContextMenu contextMenu = new ContextMenu();
                    contextMenu.getItems().add(menuItemFlowAddNode);
                    contextMenu.getItems().add(menuItemFlowRemoveNode);
                    contextMenu.getItems().add(menuItemFlowStartNode);
                    contextMenu.show(canvasFlow, event.getScreenX(), event.getScreenY());
                }
            }
        });

        tabPaneSource.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> arg0, Tab oldTab, Tab newTab) {
                SwingTextArea sourceCodeTextArea = (SwingTextArea) stackPane.lookup("#textArea-" + newTab.getId());
                if (sourceCodeTextArea != null) {
                    sourceCodeTextArea.requestFocus();
                }
            }
        });

        programList.getItems().addAll(DataBank.getPrograms());
        programList.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
            private String clickedName = "";

            @Override
            public void handle(ContextMenuEvent event) {
                clickedName = programList.getSelectionModel().getSelectedItem().getProgramName();

                MenuItem menuItemNewProgram = new MenuItem("New Program");
                menuItemNewProgram.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Program program = DataBank.createNewProgram("New program");

                        programList.getItems().add(program);
                    }
                });

                MenuItem menuItemCompile = new MenuItem("Compile...");
                menuItemCompile.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Program program = DataBank.currentlyEditProgram;
                        program.compile();
//                        FlowNode flowNode = program.getFlowController().getNodeByName(compileButton.getId().replace("compileButton-", ""));
//                        flowNode.getSource().compile();
                    }
                });

                MenuItem menuItemRun = new MenuItem("Run...");
                menuItemRun.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Program program = DataBank.currentlyEditProgram;
                        program.run();
//                        FlowNode flowNode = program.getFlowController().getNodeByName(runButton.getId().replace("runButton-", ""));
//                        flowNode.getSource().run();
                    }
                });

                ContextMenu contextMenu = new ContextMenu();

                if (clickedName == null) {
                    contextMenu.getItems().add(menuItemNewProgram);
                } else {
                    contextMenu.getItems().add(menuItemNewProgram);
                    contextMenu.getItems().add(menuItemCompile);
                    contextMenu.getItems().add(menuItemRun);
                }

                contextMenu.show(programList, event.getScreenX(), event.getScreenY());

                clickedName = null;
            }
        });

        programList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Program>() {

                    public void changed(ObservableValue<? extends Program> ov, Program oldProgram, Program newProgram) {
                        DataBank.currentlyEditProgram = newProgram;
                        newProgram.getFlowController().checkConnections();
                        canvasController.drawProgram(newProgram);
                    }
                });

        console.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    String consoleText = console.getText();
                    String commandText = consoleText.substring(consoleText.lastIndexOf("$") + 1, consoleText.length());
                    System.out.println("Sending -> " + commandText);
                    SSHManager sshManager = (SSHManager) DataBank.loadVariable("ssh", "s1");
                    sshManager.sendShellCommand(commandText);
                }
            }
        });

        tabPaneSource.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
    }

    public void createOrShowSourceTab(FlowNode flowNode) {
        Tab tab = new Tab();
        for (Tab loopTab : tabPaneSource.getTabs()) {
            if (loopTab.getId() != null) {
                if (loopTab.getId().equals(flowNode.getId().toString())) {
                    SingleSelectionModel<Tab> selectionModel = tabPaneSource.getSelectionModel();
                    selectionModel.select(loopTab);

                    SwingTextArea sourceCodeTextArea = (SwingTextArea) stackPane.lookup("#textArea-" + flowNode.getId());
                    if (sourceCodeTextArea != null) {
                        sourceCodeTextArea.setSource(flowNode.getSource());
                        sourceCodeTextArea.setEnabled(true);
                    }

                    TextField textField = (TextField) stackPane.lookup("#fieldName-" + flowNode.getId());
                    textField.setText(flowNode.getContainedText());
//                    buttonRunProgram.setDisable(!program.isCompiled());
//                    buttonCompileProgram.setDisable(false);
//                    fieldProgramName.setDisable(false);
                    return;
                }
            }
        }

        tab.setText(flowNode.getContainedText());
        tab.setId(flowNode.getId().toString());

        AnchorPane tabAnchorPane = new AnchorPane();

        TextField nameField = new TextField();
        nameField.setLayoutX(57);
        nameField.setLayoutY(13);
        nameField.setId("fieldName-" + flowNode.getId());
        tabAnchorPane.getChildren().add(nameField);

        nameField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TextField nameField = (TextField) event.getSource();
                if (!nameField.getText().isEmpty()) {
                    Program program = DataBank.currentlyEditProgram;
                    FlowNode flowNode = program.getFlowController().getNodeById(Integer.parseInt(nameField.getId().replace("fieldName-", "")));
                    flowNode.setContainedText(nameField.getText());

                    for (Tab loopTab : tabPaneSource.getTabs()) {
                        if (loopTab.getId() != null) {
                            if (loopTab.getId().equals(flowNode.getId().toString())) {
                                loopTab.setText(nameField.getText());
                            }
                        }
                    }

                    canvasController.drawProgram(program);
                }
            }
        });

        Label nameFieldLabel = new Label();
        nameFieldLabel.setText("Name:");
        nameFieldLabel.setLayoutX(11);
        nameFieldLabel.setLayoutY(17);
        tabAnchorPane.getChildren().add(nameFieldLabel);

        SwingTextArea tabTextArea = new SwingTextArea();
        tabTextArea.setLayoutX(11);
        tabTextArea.setLayoutY(50);
        tabTextArea.setId("textArea-" + flowNode.getId());

        // Setup the text area
        SwingUtilities.invokeLater(tabTextArea);
        tabAnchorPane.getChildren().add(tabTextArea);
        tab.setContent(tabAnchorPane);

        tabPaneSource.getTabs().add(tab);

        createOrShowSourceTab(flowNode);
    }

    public void writeNewLineToConsole(String text) {
        console.appendText(System.getProperty("line.separator"));
        console.appendText(text);
    }

    public void writeToConsole(String text) {
        class OneShotTask implements Runnable {
            String str;

            OneShotTask(String s) {
                str = s;
            }

            public void run() {
                console.appendText(str);
            }
        }

        Platform.runLater(new OneShotTask(text));
    }

    // Use this one when on GUI thread
    public void updateCanvasControllerNow() {
        canvasController.drawProgram(DataBank.currentlyEditProgram);
    }

    // Use this one when not on GUI thread
    public void updateCanvasControllerLater() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                canvasController.drawProgram(DataBank.currentlyEditProgram);
            }
        });
    }

    public static Controller getInstance() {
        return Controller.controller;
    }
}


