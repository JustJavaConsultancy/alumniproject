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

class AlumniProcessSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testAlumniDocumentRequestProcess() {
        // Deploy the process
        driver.get("http://localhost:8080/api/alumni/deploy");
        assertEquals("Process deployed successfully", driver.findElement(By.tagName("body")).getText());

        // Start the process
        driver.get("http://localhost:8080/alumni/requestForm");
        WebElement documentType = driver.findElement(By.id("documentType"));
        Select documentTypeSelect = new Select(documentType);
        documentTypeSelect.selectByValue("transcript");

        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        Select paymentMethodSelect = new Select(paymentMethod);
        paymentMethodSelect.selectByValue("remita");

        WebElement approver = driver.findElement(By.id("approver"));
        approver.sendKeys("admin");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify the process started
        assertEquals("Process started successfully", driver.findElement(By.tagName("body")).getText());

        // Approve the request
        driver.get("http://localhost:8080/alumni/approvalForm?taskId=taskId");
        WebElement approvalStatus = driver.findElement(By.id("approvalStatus"));
        Select approvalStatusSelect = new Select(approvalStatus);
        approvalStatusSelect.selectByValue("approved");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        // Verify the request was approved
        assertEquals("Request approved successfully", driver.findElement(By.tagName("body")).getText());
    }
}
