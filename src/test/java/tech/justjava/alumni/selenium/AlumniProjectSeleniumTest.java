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
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlumniProjectSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/alumni");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void deployProcess() {
        driver.findElement(By.linkText("Deploy Process")).click();
        WebElement message = driver.findElement(By.id("message"));
        assertEquals("Process deployed successfully", message.getText());
    }

    @Test
    void startProcess() {
        driver.findElement(By.linkText("Start Process")).click();

        Select documentType = new Select(driver.findElement(By.id("documentType")));
        documentType.selectByValue("transcript");

        Select paymentMethod = new Select(driver.findElement(By.id("paymentMethod")));
        paymentMethod.selectByValue("remita");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement message = driver.findElement(By.id("message"));
        assertEquals("Process started successfully", message.getText());
    }

    @Test
    void completeTask() {
        driver.findElement(By.linkText("Task List")).click();
        driver.findElement(By.linkText("Complete Task")).click();

        Select documentType = new Select(driver.findElement(By.id("documentType")));
        documentType.selectByValue("certificate");

        Select paymentMethod = new Select(driver.findElement(By.id("paymentMethod")));
        paymentMethod.selectByValue("paystack");

        driver.findElement(By.id("paymentVerified")).click();
        driver.findElement(By.id("requestApproved")).click();

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        WebElement message = driver.findElement(By.id("message"));
        assertEquals("Task completed successfully", message.getText());
    }

    @Test
    void viewProcessHistory() {
        driver.findElement(By.linkText("Process History")).click();
        WebElement historyTable = driver.findElement(By.tagName("table"));
        assertTrue(historyTable.isDisplayed());
    }
}
