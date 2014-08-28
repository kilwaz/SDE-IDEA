package application;

import application.utils.BrowserManager;
import application.utils.DataBank;
import application.utils.SSHConnectionManager;
import application.utils.ThreadManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new MySQLConnectionManager();
        new SSHConnectionManager();
        new ThreadManager();
        new BrowserManager();

        DataBank.loadFromDatabase();

        // Removes any class or java files previously compiled.
        String userHome = System.getProperty("user.home");
        File dir = new File(userHome, "/SDE/programs");
        if (dir.exists()) {
            for (File file : dir.listFiles()) file.delete();
        }

        // System specific
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\Program Files\\Internet Explorer\\iexplore.exe\"");

        // Sets up interface
        Parent root = FXMLLoader.load(getClass().getResource("ApplicationScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Spiralinks Development Engine V0.1");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                // On Application Close
                SSHConnectionManager.getInstance().closeConnections();
                ThreadManager.getInstance().closeThreads();
                BrowserManager.getInstance().closeBrowsers();

                // Cleans up any class or java files previously compiled.
                String userHome = System.getProperty("user.home");
                File dir = new File(userHome, "/SDE/programs");
                if (dir.exists()) {
                    for (File file : dir.listFiles()) file.delete();
                }
            }
        });
        primaryStage.show();
        Controller.getInstance().setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
