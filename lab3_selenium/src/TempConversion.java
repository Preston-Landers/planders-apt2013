
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TempConversion extends TestCase {
    private static String LOGIN_URL = "http://adnan.appspot.com/testing-lab-login.html";
    private static String BAD_PASSWORD_MSG = "Input combination of user id and password is incorrect.";
    private static String WAIT_FOR_LOGIN_MSG = "Wait for 10 seconds before trying to login again";

    // User that we use for certain tests
    private static String STANDARD_USER = "bob";

    // Can only log in this often
    private static int WAIT_BETWEEN_LOGINS = 10 * 1000;

    private static int LOCKOUT_PERIOD_MS = 60 * 1000;

    // This regex implicitly tests that numbers are not returned in exponential notation
    // e.g. 9.73E2 because any decimal digits must be followed by " Celsius"
    private static final Pattern findConversion =
            Pattern.compile("Farenheit = ([-]?\\d+?[.]?(\\d*?)) Celsius", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);


    WebDriver driver;
    HashMap<String, String> userPasswordMap;
    HashMap<String, List<String>> userAlternatives;

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        userPasswordMap = new HashMap<String, String>();
        userPasswordMap.put("andy", "apple");
        userPasswordMap.put("bob", "bathtub");
        userPasswordMap.put("charley", "china");

        userAlternatives = new HashMap<String, List<String>>();
        userAlternatives.put("andy",
                Arrays.asList(" ANDY ", " Andy", "AnDy  "));

        userAlternatives.put("bob",
                Arrays.asList(" BOB ", " Bob", "BoB  "));

        userAlternatives.put("charley",
                Arrays.asList(" CHARLEY ", " Charley", "CharLEY  "));

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

        doSleep(WAIT_BETWEEN_LOGINS);

        // 'bathtub' is a valid password, so make sure this variant isn't accepted
        doLogin(driver, "bob", "Bathtub");

        String pageSource = driver.getPageSource();

        assertTrue(
                "Page doesn't contain bad password message.",
                pageSource.contains(BAD_PASSWORD_MSG)
        );
        System.out.println("Found expected bad password msg: " + BAD_PASSWORD_MSG);

    }

    /**
     * Make sure the 1 minute lockout timer applies to bad password attempts.
     * Try a bad password 3 times, then try a good one - it shouldn't work.
     * Then wait another minute and ensure it does work.
     */
    @Test
    public void testBadLoginTimer() {
        String userName = "bob";
        String realPassword = userPasswordMap.get(userName);
        String pageSource;

        // In theory, we should wait the entire lockout timer FIRST
        // in case we were locked out from another test...
        // doSleep(LOCKOUT_PERIOD_MS);
        doSleep(WAIT_BETWEEN_LOGINS);

        // Try 4 bad passwords within 10 seconds.
        // First 2 should have "bad password", subsequent should say we're locked out
        for (int i = 0; i < 4; i++) {
            doSleep(1000);
            doLogin(driver, userName, "definitely a bad password! " + i);
            pageSource = driver.getPageSource();

            if (i < 2) {
                assertTrue(
                        "Page doesn't contain bad password message: " + pageSource,
                        pageSource.contains(BAD_PASSWORD_MSG)
                );
                System.out.println("Passed check for bad password msg on good login.");
            } else {
                assertTrue(
                        "Page doesn't contain expected lockout message." + pageSource,
                        pageSource.contains(WAIT_FOR_LOGIN_MSG)
                );
                System.out.println("Passed check for lockout msg after " + i + " logins.");
            }
        }

        // sleep long enough to reset the timer
        doSleep(LOCKOUT_PERIOD_MS);
        doLogin(driver, userName, realPassword);
        pageSource = driver.getPageSource();

        assertFalse(
                "Page contains lockout period message unexpectedly: " + pageSource,
                pageSource.contains(WAIT_FOR_LOGIN_MSG)
        );
        System.out.println("Passed check for lockout msg after good login.");

        WebElement farenheitTempInput = driver.findElement(By.name("farenheitTemperature"));
        assertNotNull("Can't find farenheitTemperature on result page after login", farenheitTempInput);

        System.out.println("Passed check to find temp input after login.");

    }

    /**
     * Helper to perform the actual login
     *
     * @param driver
     * @param userName
     * @param password
     * @return
     */
    private static WebDriver doLogin(WebDriver driver, String userName, String password) {
        System.out.println("Loading login page at: " + LOGIN_URL);
        driver.get(LOGIN_URL);
        System.out.println("Filling in username and password: " + userName + " " + password);
        WebElement userIdField = driver.findElement(By.name("userId"));
        userIdField.clear();
        userIdField.sendKeys(userName);

        WebElement passwordField = driver.findElement(By.name("userPassword"));
        passwordField.clear();
        passwordField.sendKeys(password);

        System.out.println("Submitting login form.");
        passwordField.submit();
        return driver;
    }

    // Precision Tests and expected digits of precision
    // A: -393.38934    -> 1
    // B: -0.00001      -> 1
    // C: 0.0           -> 2
    // D: 93.389321     -> 2
    // E: 212.0         -> 2
    // F: 212.000001    -> 1
    // G: 245.38342     -> 1
    // H: 0.01          -> 2
    // I: 211.99999     -> 2
    @Test
    public void testPrecisionA() {
        String inputString = "-393.38934";
        final String expectedOutput = "-236.3";
        final int expectedPrecision = 1;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionB() {
        String inputString = "-0.00001";
        final String expectedOutput = "-17.8";
        final int expectedPrecision = 1;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionC() {
        String inputString = "0.0";
        final String expectedOutput = "-17.78";
        final int expectedPrecision = 2;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionD() {
        String inputString = "93.389321";
        final String expectedOutput = "34.11";
        final int expectedPrecision = 2;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionE() {
        String inputString = "212.0";
        final String expectedOutput = "100";
        final int expectedPrecision = 2;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionF() {
        String inputString = "212.1";
        final String expectedOutput = "100.1";
        final int expectedPrecision = 1;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionG() {
        String inputString = "245.38342";
        final String expectedOutput = "118.5";
        final int expectedPrecision = 1;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionH() {
        String inputString = "0.01";
        final String expectedOutput = "-17.77";
        final int expectedPrecision = 2;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    public void testPrecisionI() {
        String inputString = "211.99";
        final String expectedOutput = "99.99";
        final int expectedPrecision = 2;
        testPrecisionHelper(inputString, expectedOutput, expectedPrecision);
    }

    // Getting a Null assertion failure can also mean the server returned a weird number format.
    private void testPrecisionHelper(
            String inputString,
            String expectedOutput,
            final int expectedPrecision) {
        System.out.println("Testing conversion of " + inputString + " F ");
        Matcher matcher = getConversionString(driver, STANDARD_USER, userPasswordMap.get(STANDARD_USER), inputString);
        assertNotNull("Could not obtain converted string from site", matcher);
        String cResult = matcher.group(1);
        String cPrecision = matcher.group(2);
        if (cPrecision.length() > expectedPrecision) {
            fail("Precision fail. Expected < " + expectedPrecision + " >  actual < " + cPrecision.length() + " >  Raw result: " + inputString + " F -> " + cResult + " C");
        }
        assertEquals(expectedOutput, cResult);
        System.out.println("Passed conversion test. Expected: " + expectedOutput + " actual: " + cResult);
    }


    public static Matcher getConversionString(WebDriver driver, String username, String password, String inputText) {
        doSleep(WAIT_BETWEEN_LOGINS);
        doLogin(driver, username, password);
        WebElement farenheitTempInput = assertGoodLogin(driver);
        farenheitTempInput.clear();
        farenheitTempInput.sendKeys(inputText);
        farenheitTempInput.submit();
        String pageSource = driver.getPageSource();
        Matcher matcher = findConversion.matcher(pageSource);
        if (matcher.find()) {
            return matcher;
        }
        return null;
    }

    @Test
    public void testBadNumberFormatA() {
        String inputString = "-534.043.23";  // NOT a valid number
        testBadNumberFormatHelper(inputString);
    }

    @Test
    public void testBadNumberFormatB() {
        String inputString = "Tony";  // NOT a valid number
        testBadNumberFormatHelper(inputString);
    }

    /**
     * Ensure that a malformatted number input gives an appropriate error.
     */
    private void testBadNumberFormatHelper(String inputString) {
        String expectedError = "Got a NumberFormatException on " + inputString;
        Matcher matcher = getConversionString(driver, STANDARD_USER, userPasswordMap.get(STANDARD_USER), inputString);
        String pageSource = driver.getPageSource();
        assertNull(
                "Got unexpected result from site from bad number! source: " + pageSource,
                matcher);
        assertTrue(
                "Page doesn't have expected number format error! source: " + pageSource,
                pageSource.contains(expectedError)
        );
        System.out.println("Passed bad number test for input: " + inputString);
    }

    @Test
    public void testInputCaseSensitivity() {
        String inputString = "93.389321";
        final String expectedOutput = "34.11";
        final int expectedPrecision = 2;
        final String newInputName = "FarenheitTemperature";

        doSleep(WAIT_BETWEEN_LOGINS);
        doLogin(driver, STANDARD_USER, userPasswordMap.get(STANDARD_USER));
        WebElement farenheitTempInput = assertGoodLogin(driver);
        System.out.println("Setting input for case-sensitive form check");
        farenheitTempInput.clear();
        farenheitTempInput.sendKeys(inputString);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        System.out.println("Running Javascript to change form input name from farenheitTemperature to " + newInputName);
        js.executeScript("document.getElementsByName('farenheitTemperature')[0].setAttribute('name', '" + newInputName + "');");
        // js.executeScript("window.alert(document.getElementsByName('" + newInputName + "')[0]);", farenheitTempInput);

        System.out.println("Submitting new form with new input name " + newInputName);
        farenheitTempInput.submit();
        String pageSource = driver.getPageSource();
        assertFalse(
                "Page says we need to enter a temp after changing case of input element to " + newInputName,
                pageSource.contains("Need to enter a temperature!")
        );
        System.out.println("It seemed to accept our new input!");
        Matcher matcher = findConversion.matcher(pageSource);
        if (!matcher.find()) {
            fail("Failed to retrieve expected result from source: " + pageSource);
        }
        String actualResult = matcher.group(1);
        String actualFrac = matcher.group(2);
        assertEquals(
                expectedOutput,
                actualResult);
        System.out.println("Passed testInputCaseSensitivity when I got the expected result " + expectedOutput);
    }

    /**
     * Make sure that variations or alternatives to the login names work.
     */
    @Test
    public void testAlternateLogins() {

        for (String realUsername : userAlternatives.keySet()) {

            for (String altUsername : userAlternatives.get(realUsername)) {
                // Add some leading/trailing whitespace to password (should be ok)
                String password = "  " + userPasswordMap.get(realUsername) + " ";

                // Sleep 10 seconds to ensure login timer has cleared
                doSleep(WAIT_BETWEEN_LOGINS);

                System.out.println("Testing alternate login: " + altUsername + " pw: <" + password + ">");

                doLogin(driver, altUsername, password);
                assertGoodLogin(driver);
                System.out.println("Passed alternate login check for: " + altUsername + " pw: <" + password + ">");

            }

        }
    }

    private static WebElement assertGoodLogin(WebDriver driver) {
        String pageSource = driver.getPageSource();
        WebElement farenheitTempInput = driver.findElement(By.name("farenheitTemperature"));
        assertNotNull("Can't find farenheitTemperature on result page after login", farenheitTempInput);

        assertFalse(
                "Page had the bad password message.",
                pageSource.contains(BAD_PASSWORD_MSG)
        );
        assertFalse(
                "Page had the lockout message.",
                pageSource.contains(WAIT_FOR_LOGIN_MSG)
        );
        return farenheitTempInput;
    }

    private static void doSleep(int timeMS) {
        try {
            Thread.sleep(timeMS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}