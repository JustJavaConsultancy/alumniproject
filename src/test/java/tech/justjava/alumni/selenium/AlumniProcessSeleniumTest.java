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
    public void testStartProcess() {
        driver.get("http://localhost:" + port + "/alumni-process/start");

        WebElement documentType = driver.findElement(By.id("documentType"));
        documentType.sendKeys("transcript");

        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        paymentMethod.sendKeys("remita");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:" + port + "/alumni-process/tasks", driver.getCurrentUrl());
    }

    @Test
    public void testCompleteTask() {
        driver.get("http://localhost:" + port + "/alumni-process/tasks");

        WebElement taskLink = driver.findElement(By.linkText("Complete"));
        taskLink.click();

        WebElement approval = driver.findElement(By.id("approval"));
        approval.sendKeys("approved");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:" + port + "/alumni-process/tasks", driver.getCurrentUrl());
    }
}
