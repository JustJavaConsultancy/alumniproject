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
    public void setUp() {
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
    public void testAlumniProcessFlow() {
        driver.get("http://localhost:" + port + "/alumni/requestForm");

        WebElement documentType = driver.findElement(By.id("documentType"));
        documentType.sendKeys("transcript");

        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        paymentMethod.sendKeys("remita");

        WebElement approver = driver.findElement(By.id("approver"));
        approver.sendKeys("approver1");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("Process started with ID: processInstanceId", driver.findElement(By.tagName("body")).getText());

        driver.get("http://localhost:" + port + "/alumni/tasks?assignee=approver1");

        WebElement task = driver.findElement(By.cssSelector("a.btn-primary"));
        task.click();

        assertEquals("Task completed successfully", driver.findElement(By.tagName("body")).getText());
    }
}
