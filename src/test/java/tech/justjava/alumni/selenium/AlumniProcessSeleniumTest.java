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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        // Start the process
        driver.get("http://localhost:" + port + "/alumni/start");

        WebElement documentType = driver.findElement(By.id("documentType"));
        documentType.sendKeys("transcript");

        WebElement paymentMethod = driver.findElement(By.id("paymentMethod"));
        paymentMethod.sendKeys("remita");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        // Verify the process started
        assertTrue(driver.getCurrentUrl().contains("/alumni/tasks"));

        // Complete the submitRequest task
        WebElement taskLink = driver.findElement(By.linkText("Submit Document Request"));
        taskLink.click();

        WebElement documentTypeForm = driver.findElement(By.id("documentType"));
        documentTypeForm.sendKeys("transcript");

        WebElement paymentMethodForm = driver.findElement(By.id("paymentMethod"));
        paymentMethodForm.sendKeys("remita");

        WebElement submitFormButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitFormButton.click();

        // Verify the task is completed
        assertTrue(driver.getCurrentUrl().contains("/alumni/tasks"));

        // Complete the makePayment task
        WebElement paymentTaskLink = driver.findElement(By.linkText("Make Payment"));
        paymentTaskLink.click();

        WebElement confirmPaymentButton = driver.findElement(By.cssSelector("button[type='submit']"));
        confirmPaymentButton.click();

        // Verify the payment task is completed
        assertTrue(driver.getCurrentUrl().contains("/alumni/tasks"));

        // Approve the request
        WebElement approveTaskLink = driver.findElement(By.linkText("Approve Request"));
        approveTaskLink.click();

        WebElement approveSelect = driver.findElement(By.id("approved"));
        approveSelect.sendKeys("true");

        WebElement approveSubmitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        approveSubmitButton.click();

        // Verify the approval task is completed
        assertTrue(driver.getCurrentUrl().contains("/alumni/tasks"));

        // Verify the process is completed
        driver.get("http://localhost:" + port + "/alumni/processes");
        assertTrue(driver.findElement(By.tagName("body")).getText().contains("Alumni Document Request Process"));
    }
}
