package application;

import application.utils.ConnectionManager;
import application.utils.DataBank;
import application.utils.ThreadManager;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new MySQLConnection();
        DataBank.loadFromDatabase();
        new ConnectionManager();
        new ThreadManager();
//        WebDriver driver = new FirefoxDriver();
//        driver.get("http://www.google.com");
//        // Find the text input element by its name
//        WebElement element = driver.findElement(By.name("q"));
//        // Enter something to search for
//        element.sendKeys("Spiralinks");
//        // Now submit the form. WebDriver will find the form for us from the element
//        element.submit();
//        // Google's search is rendered dynamically with JavaScript.
//        // Wait for the page to load, timeout after 10 seconds
//        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver d) {
//                return d.getTitle().toLowerCase().startsWith("s");
//            }
//        });
//
//        // Should see: "cheese! - Google Search"
//        System.out.println("Page title is: " + driver.getTitle());
//
//        //Close the browser
//        driver.quit();


        Parent root = FXMLLoader.load(getClass().getResource("ApplicationScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Spiralinks Development Engine V0.1");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                ConnectionManager.getInstance().closeConnections();
                ThreadManager.getInstance().closeThreads();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
