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

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new MySQLConnectionManager();
        new SSHConnectionManager();
        new ThreadManager();
        new BrowserManager();

        DataBank.loadFromDatabase();

        //WebDriver driver = new FirefoxDriver();
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        System.setProperty("webdriver.ie.driver", "C:\\Program Files\\Internet Explorer\\iexplore.exe\"");

        //ChromeOptions options = new ChromeOptions();
        //options.setBinary("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
//        DesiredCapabilities capabilities = DesiredCapabilities.firefox();
//
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("test-type");
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//
//        WebDriver driver = new ChromeDriver(capabilities);
//        driver.get("http://www.spiralinks.com");
//
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.elementToBeClickable(By.id("menu-item-370")));
//
//        // Find the text input element by its name
//        WebElement menuItem = driver.findElement(By.id("menu-item-370"));
//        WebElement aLink = menuItem.findElement(By.tagName("a"));
//
//        aLink.click();

        // Enter something to search for
        //element.sendKeys("Spiralinks");
        // Now submit the form. WebDriver will find the form for us from the element
        //element.submit();
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
//        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver d) {
//                return d.getTitle().toLowerCase().startsWith("s");
//            }
//        });

        // Should see: "cheese! - Google Search"
        //System.out.println("Page title is: " + driver.getTitle());

        //Close the browser
        //driver.quit();


        Parent root = FXMLLoader.load(getClass().getResource("ApplicationScene.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Spiralinks Development Engine V0.1");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                SSHConnectionManager.getInstance().closeConnections();
                ThreadManager.getInstance().closeThreads();
                BrowserManager.getInstance().closeBrowsers();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
