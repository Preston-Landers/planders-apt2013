
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TempConversion extends TestCase {
    private static String LOGIN_URL = "http://adnan.appspot.com/testing-lab-login.html";
    WebDriver driver;
    HashMap<String, String> userPasswordMap;
    HashMap<String, String> userAlternatives;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        userPasswordMap = new HashMap<String, String>();
        userPasswordMap.put("andy", "apple");
        userPasswordMap.put("bob", "bathtub");
        userPasswordMap.put("charley", "china");

        userAlternatives = new HashMap<String, String>();
        userAlternatives.put("andy", " ANDY ");
        userAlternatives.put("andy", " Andy");
        userAlternatives.put("andy", "AnDy  ");

        userAlternatives.put("bob", " BOB ");
        userAlternatives.put("bob", " Bob");
        userAlternatives.put("bob", "BoB  ");

        userAlternatives.put("charley", " CHARLEY ");
        userAlternatives.put("charley", " Charley");
        userAlternatives.put("charley", "CharLEY ");

    }

    @After
    public void tearDown() {
        try {
            driver.quit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testBadLogin() {

        driver.get(LOGIN_URL);

        // Enter the bad login
        WebElement userIdField = driver.findElement(By.name("userId"));
        userIdField.clear();
        userIdField.sendKeys("bob");

        WebElement passwordField = driver.findElement(By.name("userPassword"));
        passwordField.clear();
        passwordField.sendKeys("bad password for sure!");

        passwordField.submit();

        String pageSource = driver.getPageSource();

        assertTrue(
                "Page doesn't contain bad password message.",
                pageSource.contains("Input combination of user id and password is incorrect.")
        );

    }
}