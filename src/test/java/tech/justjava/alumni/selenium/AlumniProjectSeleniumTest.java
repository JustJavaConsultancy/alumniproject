package tech.justjava.alumni.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlumniProjectSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/alumni/deploy");
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAlumniDocumentRequestProcess() {
        // Start the process
        driver.get("http://localhost:8080/alumni/start");

        WebElement documentType = driver.findElement(By.id("documentType"));
        Select documentTypeSelect = new Select(documentType);
        documentTypeSelect.selectByValue("transcript");

        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        Select paymentMethodSelect = new Select(paymentMethod);
        paymentMethodSelect.selectByValue("remita");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Complete the Submit Document Request task
        driver.get("http://localhost:8080/alumni/tasks");

        WebElement completeLink = driver.findElement(By.linkText("Complete"));
        completeLink.click();

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Complete the Make Payment task
        driver.get("http://localhost:8080/alumni/tasks");

        completeLink = driver.findElement(By.linkText("Complete"));
        completeLink.click();

        WebElement paymentStatus = driver.findElement(By.id("paymentStatus"));
        Select paymentStatusSelect = new Select(paymentStatus);
        paymentStatusSelect.selectByValue("success");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Complete the Approve Request task
        driver.get("http://localhost:8080/alumni/tasks");

        completeLink = driver.findElement(By.linkText("Complete"));
        completeLink.click();

        WebElement approvalStatus = driver.findElement(By.id("approvalStatus"));
        Select approvalStatusSelect = new Select(approvalStatus);
        approvalStatusSelect.selectByValue("approved");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify the process history
        driver.get("http://localhost:8080/alumni/history");

        WebElement processInstanceId = driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]"));
        assertEquals("1", processInstanceId.getText());

        WebElement status = driver.findElement(By.xpath("//table/tbody/tr[1]/td[2]"));
        assertEquals("Completed", status.getText());
    }
}
