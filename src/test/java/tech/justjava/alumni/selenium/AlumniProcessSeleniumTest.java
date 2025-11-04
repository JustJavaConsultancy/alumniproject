package tech.justjava.alumni.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlumniProcessSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSubmitDocumentRequest() {
        driver.get("http://localhost:" + port + "/alumni/requestForm");

        WebElement documentType = driver.findElement(By.id("documentType"));
        documentType.sendKeys("transcript");

        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        paymentMethod.sendKeys("remita");

        WebElement approver = driver.findElement(By.id("approver"));
        approver.sendKeys("approver");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:" + port + "/api/alumni/start", driver.getCurrentUrl());
    }

    @Test
    public void testMakePayment() {
        driver.get("http://localhost:" + port + "/alumni/paymentForm?taskId=task1&paymentAmount=1000");

        WebElement paymentReference = driver.findElement(By.id("paymentReference"));
        paymentReference.sendKeys("ref123");

        WebElement completeButton = driver.findElement(By.cssSelector("button[type='submit']"));
        completeButton.click();

        assertEquals("http://localhost:" + port + "/api/alumni/complete?taskId=task1", driver.getCurrentUrl());
    }

    @Test
    public void testCompleteTask() {
        driver.get("http://localhost:" + port + "/alumni/tasks?assignee=approver");

        WebElement completeButton = driver.findElement(By.cssSelector("a.btn-primary"));
        completeButton.click();

        assertEquals("http://localhost:" + port + "/api/alumni/complete?taskId=task1", driver.getCurrentUrl());
    }
}
