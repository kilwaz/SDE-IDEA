package application.tester;

import application.utils.DataBank;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    public static List<TestCase> testAllSelectValues(WebDriver driver, String elementId, String elementFrame) {
        List<TestCase> testCaseList = new ArrayList<TestCase>();

//        WebDriverWait wait = new WebDriverWait(driver, 10);
//        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementFrame)));
//        driver.switchTo().frame(elementFrame);
//
//
//        Select select = new Select(driver.findElement(By.id(elementId)));
//        for (WebElement option : select.getOptions()) {
//
//            System.out.println("Testing - " + option.getText().trim());
//            System.out.println("Result - " + testSelectCase(driver, option.getAttribute("value")));
//        }
//
//        driver.switchTo().defaultContent();
//
//
//        TestCase testCase = new TestCase();
//
//        testCase.setElementType("select");
//        testCase.setElementFrame(elementFrame);
//        testCase.setElementId(elementId);

        return testCaseList;
    }

    public static TestCase createSelectTestCase(String elementId, String elementFrame, String inputValue, String expectedOutputValue) {
        TestCase testCase = new TestCase();

        testCase.setElementType("select");
        testCase.setElementFrame(elementFrame);
        testCase.setElementId(elementId);
        testCase.setExpectedOutputValue(expectedOutputValue);
        testCase.setInputValue(inputValue);

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

    public static TestResultSet getResultSet(String resultSet, String referenceID) {
        return (TestResultSet) DataBank.getInstanceObject(referenceID, resultSet);
    }
}
