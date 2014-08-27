package application.tester;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public static TestCase testAllSelectValues(String id, String frame) {
        TestCase testCase = new TestCase();

        testCase.setElementType("select");
        testCase.setElementFrame(frame);
        testCase.setElementId(id);

        return testCase;
    }

    public static List<String> findEmployeeIDsOnWorksheet(WebDriver driver) {
        List<String> employeeID = new ArrayList<String>();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("content")));
        driver.switchTo().frame("content");

        WebElement scroll = driver.findElement(By.id("scroll"));
        List<WebElement> rows = scroll.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            WebElement comment = row.findElement(By.className("comment"));
            String[] split = comment.getAttribute("id").split("\\.");
            employeeID.add(split[1]);
        }

        driver.switchTo().defaultContent();

        return employeeID;
    }
}
