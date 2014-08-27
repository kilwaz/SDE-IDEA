package application.tester;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestCase {
    private String elementType;
    private String elementId;
    private String elementFrame;

    public TestCase() {

    }

    public String getElementId() {
        return this.elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getElementFrame() {
        return this.elementFrame;
    }

    public void setElementFrame(String elementFrame) {
        this.elementFrame = elementFrame;
    }

    public String getElementType() {
        return this.elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public TestResult evaluate(WebDriver driver) {
        TestResult testResult = new TestResult();

        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementFrame)));
        driver.switchTo().frame(elementFrame);

        if ("select".equals(elementType)) {
            Select select = new Select(driver.findElement(By.id(elementId)));
            for (WebElement option : select.getOptions()) {
                System.out.println("Testing - " + option.getText().trim());
                System.out.println("Result - " + testSelectCase(driver, option.getAttribute("value")));
            }
        }

        driver.switchTo().defaultContent();
        return testResult;
    }


    private String testSelectCase(WebDriver driver, String value) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));

        Select select = new Select(driver.findElement(By.id(elementId)));
        try {
            select.selectByValue(value);
        } catch (NoSuchElementException ex) {
            System.out.println("Cannot find " + value + " within select box");
            return "FAIL";
        }

        return wait.until(new Function<WebDriver, String>() {
            public String apply(WebDriver driver) {
                Select ajaxSelect = new Select(driver.findElement(By.id(elementId)));
                WebElement ajaxElement = driver.findElement(By.id(elementId));
                String className = ajaxElement.getAttribute("className");

                if (className.contains("inputChangeComplete")) {
                    return ajaxSelect.getFirstSelectedOption().getText().trim();
                } else {
                    return null;
                }
            }
        });
    }
}
