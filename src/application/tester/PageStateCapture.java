package application.tester;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PageStateCapture {
    private String elementFrame;
    private String rawSource;
    private HashMap<String, String> selectValues = new HashMap<String, String>();
    private Document doc;
    private Elements allElements;

    public PageStateCapture(String elementFrame) {
        this.elementFrame = elementFrame;
    }

    public HashMap<String, String> getSelectValues() {
        return this.selectValues;
    }

    public Elements getAllElements() {
        return this.allElements;
    }

    public void capturePage(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(elementFrame)));
        driver.switchTo().frame(elementFrame);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        rawSource = driver.getPageSource();
        doc = Jsoup.parse(rawSource);
        allElements = doc.getAllElements();

        for (Element tag : allElements) {
            if ("select".equals(tag.tagName())) {
                String selectResult = (String) js.executeScript("return document.getElementById('" + tag.attr("id") + "').options[document.getElementById('" + tag.attr("id") + "').selectedIndex].text");
                selectValues.put(tag.attr("id"), selectResult);
            }
        }

        driver.switchTo().defaultContent();
    }

    public ChangedElements compare(PageStateCapture pageStateCapture) {
        ChangedElements changedElements = new ChangedElements();

        List<Element> textElementChanges = new ArrayList<Element>();
        List<Element> textElementChanges2 = new ArrayList<Element>();
        List<Element> textElementChangesFinal = new ArrayList<Element>();
        List<Element> textElementChangesFinal2 = new ArrayList<Element>();

        Elements compareAllElements = pageStateCapture.getAllElements();
        HashMap<String, String> compareSelectValues = pageStateCapture.getSelectValues();

        for (int n = 0; n < allElements.size(); n++) {
            if (allElements.size() > n && compareAllElements.size() > n) {
                Element tag = allElements.get(n);
                Element tag2 = compareAllElements.get(n);
                String output = "<" + tag.tagName() + " " + tag.attributes().toString() + "></" + tag.tagName() + ">";
                String output2 = "<" + tag2.tagName() + " " + tag2.attributes().toString() + "></" + tag2.tagName() + ">";
                if ("select".equals(tag.tagName())) {
                    String selectValue = selectValues.get(tag.attr("id"));
                    String compareSelectValue = compareSelectValues.get(tag2.attr("id"));

                    if (!selectValue.equals(compareSelectValue)) {
                        changedElements.addElement(new ChangedElement(tag, selectValue, tag2, compareSelectValue, "select"));
                    }
                }

                if (!output.equals(output2)) {
                    for (Attribute att : tag.attributes()) {
                        if (!att.getValue().equals(tag2.attr(att.getKey()))) {
                            changedElements.addElement(new ChangedElement(tag, att.getValue(), tag2, tag2.attr(att.getKey()), "attribute"));
                        }
                    }
                }

                if (!tag.text().equals(tag2.text())) {
                    textElementChanges.add(tag);
                    textElementChanges2.add(tag2);
                }
            }
        }

        textElementChangesFinal.addAll(textElementChanges);
        textElementChangesFinal2.addAll(textElementChanges2);
        for (Element element : textElementChanges) {
            Element parent = element.parent();
            while (parent != null) {
                if (textElementChangesFinal.contains(parent)) {
                    textElementChangesFinal.remove(parent);
                }
                parent = parent.parent();
            }
        }

        for (Element element : textElementChanges2) {
            Element parent = element.parent();
            while (parent != null) {
                if (textElementChangesFinal2.contains(parent)) {
                    textElementChangesFinal2.remove(parent);
                }
                parent = parent.parent();
            }
        }

        for (int i = 0; i < textElementChangesFinal.size(); i++) {
            changedElements.addElement(new ChangedElement(textElementChangesFinal.get(i), textElementChangesFinal.get(i).text(), textElementChangesFinal.get(i), textElementChangesFinal2.get(i).text(), "text"));
        }

        return changedElements;
    }
}
