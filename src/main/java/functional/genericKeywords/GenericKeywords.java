package functional.genericKeywords;

import functional.utilities.ReporterUtilities;
import functional.custom.CustomWebElement;
import io.qameta.allure.Attachment;
import nonFunctional.LighthouseUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

/**
 * This class represents a set of generic keywords and utility methods used in automated testing.
 * It provides common functionalities such as interacting with web elements, browser navigation, and logging.
 * This class is designed to be extended and used in test automation frameworks.
 */

public class GenericKeywords extends ThreadLocalFunctionalities implements GenericKeywordsInterface {

	/**
	 * Default constructor for {@link GenericKeywords}.
	 * Initializes a new instance of this class with default settings.
	 */
	public GenericKeywords() {
	}

	/**
	 * A list that stores the names of test scenarios that have failed during test execution.
	 * It is used to keep track of failed scenarios for reporting and analysis.
	 */
	protected static ArrayList<String> failedScenarios = new ArrayList<String>();

	/**
	 * The file path to the "Constants.properties" file, which contains configuration settings for the test framework.
	 * It is a private variable and should not be accessed directly outside this class.
	 */
	private static String propertyFilePath = "./Constants.properties";

	/**
	 * The path where the test execution reports will be generated.
	 * It is a public variable and can be accessed and set from other parts of the code.
	 */
	public static String reportPath;

	/**
	 * An instance of the Properties class used to store properties loaded from the "Constants.properties" file.
	 * It is a private variable and should not be accessed directly outside this class.
	 */
	private static Properties propFile;

	/**
	 * Static initialization block that runs when the class is loaded.
	 * It is used to perform one-time setup tasks, such as loading properties and constructing the reportPath.
	 */
	static {
		// Load properties from the Constants.properties file.
		new GenericKeywords().loadPropertyFile(propertyFilePath);

		// Construct the reportPath based on the current timestamp and properties.
		reportPath = System.getProperty("user.dir") + "\\" + propFile.getProperty("reportLocation")
				+ "\\" + "Test Execution Report_" + Calendar.getInstance().getTime().toString().replace(" ", "_").replace(":", "_");
	}

	/**
	 * Performs a click action on the specified CustomWebElement and logs the action.
	 * It handles exceptions and attempts alternative methods for clicking if necessary.
	 *
	 * @param customWebElement The CustomWebElement to click.
	 */
	public void click(CustomWebElement customWebElement) {
		ReporterUtilities.log("Clicking " + customWebElement.getName(), () -> {
			try {
				// Attempt to click the element directly.
				getElement(customWebElement).click();
			} catch (Exception e) {
				try {
					// If direct click fails, execute a click using JavaScript.
					((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", customWebElement.getByElement());
				} catch (Exception e1) {
					try {
						// If JavaScript click fails, use Actions class to perform the click.
						new Actions(getDriver()).moveToElement(getElement(customWebElement)).click(getElement(customWebElement)).perform();
					} catch (Exception e2) {
						// Handle the exception and log an error message.
						new ProjectCustomException(getClassName(), getMethodName(), e2, "Unable to click on element " + customWebElement.getName());
					}
				}
			}
		});
	}

	/**
	 * Enters text into the specified CustomWebElement and logs the action.
	 * It handles exceptions and logs errors if text input fails.
	 *
	 * @param customWebElement The CustomWebElement where text will be entered.
	 * @param value            The text to be entered.
	 */
	public void inputText(CustomWebElement customWebElement, String value) {
		ReporterUtilities.log("Entering " + value + " in " + customWebElement.getName(), () -> {
			try {
				// Enter the specified text into the element.
				getElement(customWebElement).sendKeys(value);
			} catch (Exception e) {
				// Handle the exception and log an error message.
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to enter '" + value + "' value in " + customWebElement.getName());
			}
		});
	}

	/**
	 * Enters text into the specified CustomWebElement using Actions class and logs the action.
	 * It handles exceptions and logs errors if text input fails.
	 *
	 * @param customWebElement The CustomWebElement where text will be entered.
	 * @param value            The text to be entered.
	 */
	@Override
	public void inputTextWithActions(CustomWebElement customWebElement, String value) {
		ReporterUtilities.log("Entering " + value + " in " + customWebElement.getName(), () -> {
			try {
				// Use Actions class to enter text with clearing the field first.
				new Actions(getDriver()).moveToElement(getElement(customWebElement)).sendKeys(getElement(customWebElement), Keys.CLEAR, value).perform();
			} catch (Exception e) {
				// Handle the exception and log an error message.
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to enter '" + value + "' value to " + customWebElement.getName());
			}
		});
	}

	/**
	 * Opens a web browser for automated testing.
	 *
	 * @param performance A boolean flag indicating whether to enable performance testing features.
	 *                   If true, additional performance-related configurations may be applied.
	 */
	@Override
	public void openBrowser(boolean performance) {
		WebDriver webDriver = null;

		// Get the browser name and version from test data.
		String browserName = getTestData().get(getTestData().size() - 3);
		String browserVersion = getTestData().get(getTestData().size() - 1);

		// Determine if a specific port should be used for performance testing.
		int port = performance ? new LighthouseUtil().getFreePort() : 0;

		// Define the default download path for downloaded files.
		String defaultDownloadPath = System.getProperty("user.dir") + "\\" + getPropertyValue("defaultDownloadPath").replace("/", "\\");

		MutableCapabilities capabilities;

		switch (browserName) {
			case "Chrome":
				if (getPropertyValue("executionPlatform").equals("Local")) {
					// Configure ChromeOptions for local execution.
					capabilities = new ChromeOptions();
					HashMap<String, Object> capPref = new HashMap<>();
					capPref.put("download.default_directory", defaultDownloadPath);
					capPref.put("browser.set_download_behavior", "{ behavior: 'allow' , downloadPath: '" + defaultDownloadPath + "'}");
					((ChromeOptions) capabilities).addArguments("start-maximized");
					((ChromeOptions) capabilities).addArguments("--incognito");

					// Apply performance-related settings if enabled.
					if (performance) {
						performancePort.set(port);
						((ChromeOptions) capabilities).addArguments("--remote-debugging-port=" + port);
						((ChromeOptions) capabilities).addArguments("--user-data-dir=" + Paths.get("").toAbsolutePath() + File.separator + "ChromeProfile");
					}

					((ChromeOptions) capabilities).addArguments("--headless");
					((ChromeOptions) capabilities).setExperimentalOption("prefs", capPref);

					// Create a ChromeDriver instance with the configured capabilities.
					webDriver = new ChromeDriver(((ChromeOptions) capabilities));
				} else {
					// Code for remote execution
				}
				break;
			case "Firefox":
				if (getPropertyValue("executionPlatform").equals("Local")) {
					// Configure FirefoxOptions for local execution.
					capabilities = new FirefoxOptions();
					FirefoxProfile profile = new FirefoxProfile();
					profile.setPreference("browser.download.folderList", 2);
					profile.setPreference("browser.download.dir", defaultDownloadPath);
					profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv,application/java-archive, application/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/x-msexcel,application/excel,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/x-excel,application/vnd.ms-excel,image/png,image/jpeg,text/html,text/plain,application/msword,application/xml,application/vnd.microsoft.portable-executable");
					((FirefoxOptions) capabilities).addArguments("start-maximized");
					((FirefoxOptions) capabilities).setProfile(profile);

					// Create a FirefoxDriver instance with the configured capabilities.
					webDriver = new FirefoxDriver(((FirefoxOptions) capabilities));
				} else {
					// Code for remote execution
				}
				break;
			case "Edge":
				if (getPropertyValue("executionPlatform").equals("Local")) {
					// Create an EdgeDriver instance for local execution.
					webDriver = new EdgeDriver();
				} else {
					// Code for remote execution
				}
				break;
			case "Safari":
				if (getPropertyValue("executionPlatform").equals("Local")) {
					// Create a SafariDriver instance for local execution.
					webDriver = new SafariDriver();
				} else {
					// Code for remote execution
				}
				break;
		}

		// Set the WebDriver instance in a thread-safe manner for parallel testing.
		threadDriverInstance.set(webDriver);

		if (webDriver != null) {
			if (isBlank(browserVersion)) {
				// Log the browser opening with version information (if available).
				ReporterUtilities.log("Opened " + browserName + " with version " + browserVersion);
			} else {
				// Log the browser opening without version information.
				ReporterUtilities.log("Opened " + browserName);
			}
		} else {
			// Handle the case where the WebDriver creation failed and log an error.
			Exception exceptionObject = new Exception();
			new ProjectCustomException(getClassName(exceptionObject), getMethodName(exceptionObject), exceptionObject, "Unable to recognize " + browserName + " browser details");
		}

		// Load the specified URL in the opened browser.
		loadURL(getPropertyValue("URL"));
	}

	/**
	 * Closes the web browser after automated testing is completed.
	 */
	@Override
	public void closeBrowser() {
		// Log the action of closing the browser and quit the WebDriver instance.
		ReporterUtilities.log("Closing browser", () -> getDriver().quit());
	}

	/**
	 * Loads the specified URL in the current browser window.
	 *
	 * @param URL The URL to load.
	 */
	@Override
	public void loadURL(String URL) {
		ReporterUtilities.log("Loaded URL: " + URL, () -> {
			try {
				getDriver().navigate().to(URL);
			} catch (Exception e) {
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to load URL '" + URL + "'");
			}
		});
	}

	/**
	 * Navigates to the previous page in the browser's history.
	 */
	@Override
	public void navigateBack() {
		ReporterUtilities.log("Navigating to previous page", () -> {
			try {
				getDriver().navigate().back();
			} catch (Exception e) {
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to navigate back to the previous page");
			}
		});
	}

	/**
	 * Navigates to the next page in the browser's history.
	 */
	@Override
	public void navigateForward() {
		ReporterUtilities.log("Navigated to the next page", () -> {
			try {
				getDriver().navigate().forward();
			} catch (Exception e) {
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to navigate to the next page");
			}
		});
	}

	/**
	 * Refreshes the current webpage.
	 */
	@Override
	public void refreshPage() {
		ReporterUtilities.log("Refresh Webpage", () -> getDriver().navigate().refresh());
	}

	/**
	 * Retrieves the text from the specified CustomWebElement.
	 *
	 * @param customWebElement The CustomWebElement to get text from.
	 * @return The text of the CustomWebElement.
	 */
	@Override
	public String getText(CustomWebElement customWebElement) {
		return ReporterUtilities.log("Getting text for " + customWebElement.getName(), () -> getText(getElement(customWebElement), customWebElement.getName()));
	}

	/**
	 * Retrieves the text from the specified WebElement and provides a field name for logging.
	 *
	 * @param element    The WebElement to get text from.
	 * @param fieldName  The field name for logging.
	 * @return The text of the WebElement.
	 */
	@Override
	public String getText(WebElement element, String fieldName) {
		return ReporterUtilities.log("Getting text for " + fieldName, () -> waitUntilVisibilityOfAnElement(NumberUtils.toInt(getPropertyValue("defaultWaitTime")), element).getText());
	}

	/**
	 * Retrieves the current URL of the browser.
	 *
	 * @return The current URL.
	 */
	@Override
	public String getUrl() {
		String currentUrl = getDriver().getCurrentUrl();
		ReporterUtilities.log("Getting current URL: " + currentUrl);
		return currentUrl;
	}

	/**
	 * Retrieves the WebElement corresponding to the given CustomWebElement after waiting for it to be visible.
	 *
	 * @param customWebElement The CustomWebElement to retrieve as a WebElement.
	 * @return The WebElement of the CustomWebElement.
	 */
	@Override
	public WebElement getElement(CustomWebElement customWebElement) {
		return waitUntilVisibilityOfAnElement(NumberUtils.toInt(getPropertyValue("defaultWaitTime")), customWebElement);
	}

	/**
	 * Retrieves a list of WebElements that match the given CustomWebElement's locator.
	 *
	 * @param customWebElement The CustomWebElement to retrieve multiple elements for.
	 * @return A list of WebElements matching the locator of the CustomWebElement.
	 */
	@Override
	public List<WebElement> getElements(CustomWebElement customWebElement) {
		return getDriver().findElements(customWebElement.getByElement());
	}

	/**
	 * Performs a double click action on the specified CustomWebElement.
	 *
	 * @param customWebElement The CustomWebElement to double click.
	 */
	@Override
	public void doubleClick(CustomWebElement customWebElement) {
		ReporterUtilities.log("Double click on " + customWebElement.getName(), () -> {
			try {
				new Actions(getDriver()).moveToElement(getElement(customWebElement)).doubleClick(getElement(customWebElement)).perform();
			} catch (Exception e) {
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to perform Actions double click for an element: " + customWebElement.getName());
			}
		});
	}

	/**
	 * Scrolls to the specified CustomWebElement on the page.
	 *
	 * @param customWebElement The CustomWebElement to scroll to.
	 */
	@Override
	public void scrollToAnElement(CustomWebElement customWebElement) {
		ReporterUtilities.log("Scroll to " + customWebElement.getName(), () -> {
			try {
				((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", customWebElement.getElement());
			} catch (Exception e) {
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to perform Scroll to an element: " + customWebElement.getName());
			}
		});
	}

	/**
	 * Takes a screenshot of the current page.
	 *
	 * @return A byte array representing the screenshot in PNG format.
	 */
	@Attachment(type = "image/png")
	public static byte[] takeScreenshot() {
		return ((TakesScreenshot) getDriver()).getScreenshotAs(OutputType.BYTES);
	}

	/**
	 * Takes a screenshot of a specific CustomWebElement.
	 *
	 * @param customWebElement The CustomWebElement to take a screenshot of.
	 * @return A File object representing the screenshot file.
	 */
	@Attachment(type = "image/png")
	@Override
	public File takeScreenshotSpecificElement(CustomWebElement customWebElement) {
		return getElement(customWebElement).getScreenshotAs(OutputType.FILE);
	}

	/**
	 * Selects an option from a dropdown element based on the specified selectionType and option.
	 *
	 * @param customWebElement The CustomWebElement representing the dropdown element.
	 * @param selectionType    The type of selection (e.g., "index", "value", "text").
	 * @param option           The option to select.
	 */
	@Override
	public void dropDown(CustomWebElement customWebElement, String selectionType, String option) {
		ReporterUtilities.log("Selected " + option + " " + selectionType + " in the dropdown element: " + customWebElement.getName(), () -> {
			boolean flag = false;
			try {
				if (customWebElement.getByElement().toString().contains("select")) {
					Select dropdown = new Select(getElement(customWebElement));
					switch (selectionType.toLowerCase()) {
						case "index":
							dropdown.selectByIndex(NumberUtils.toInt(option));
							flag = true;
							break;
						case "value":
							dropdown.selectByValue(option);
							flag = true;
							break;
						case "text":
							dropdown.selectByVisibleText(option);
							flag = true;
							break;
						default:
							break;
					}
				} else {
					for (WebElement eachElement : getElements(customWebElement)) {
						if (eachElement.getAttribute(selectionType).equals(option)) {
							eachElement.click();
							flag = true;
							break;
						}
					}
				}
				if (!flag)
					throw new Exception("Unable to choose any element in the dropdown");
			} catch (Exception e) {
				new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to find the " + option + " " + selectionType + " in the dropdown element: " + customWebElement.getName());
			}
		});
	}

	/**
	 * Checks if the text of the given CustomWebElement is blank or empty.
	 *
	 * @param customWebElement The CustomWebElement to check.
	 * @return True if the text is blank; otherwise, false.
	 */
	@Override
	public boolean isBlank(CustomWebElement customWebElement) {
		return ReporterUtilities.log("Validating if " + customWebElement.getName() + " is blank", () -> isBlank(getText(customWebElement)));
	}

	/**
	 * Checks if the provided string value is blank or empty.
	 *
	 * @param stringValue The string value to check.
	 * @return True if the string is blank; otherwise, false.
	 */
	@Override
	public boolean isBlank(String stringValue) {
		return stringValue.trim().isEmpty();
	}

	/**
	 * Adds or subtracts a specified number of units (e.g., years, months, hours) to a calendar and returns the result as a formatted string.
	 *
	 * @param entityType    The type of unit to add or subtract (e.g., "year", "month").
	 * @param numbersToAdd  The number of units to add (use negative values to subtract).
	 * @param format        The desired date format for the result.
	 * @return A formatted string representing the modified calendar date.
	 */
	@Override
	public String addInCalender(String entityType, int numbersToAdd, String format) {
		Calendar calObj = Calendar.getInstance();
		SimpleDateFormat simpleDateFormatObject = new SimpleDateFormat(format);
		try {
			if (!isBlank(getPropertyValue("TimeZone")))
				simpleDateFormatObject.setTimeZone(TimeZone.getTimeZone(getPropertyValue("TimeZone")));
			else
				simpleDateFormatObject.setTimeZone(TimeZone.getTimeZone("EST"));

			switch (entityType.toLowerCase()) {
				case "year":
					calObj.set(Calendar.YEAR, calObj.get(Calendar.YEAR) + numbersToAdd);
					break;
				case "month":
					calObj.set(Calendar.MONTH, calObj.get(Calendar.MONTH) + numbersToAdd);
					break;
				case "date":
					calObj.set(Calendar.DATE, calObj.get(Calendar.DATE) + numbersToAdd);
					break;
				case "hour":
					calObj.set(Calendar.HOUR_OF_DAY, calObj.get(Calendar.HOUR_OF_DAY) + numbersToAdd);
					break;
				case "minute":
					calObj.set(Calendar.MINUTE, calObj.get(Calendar.MINUTE) + numbersToAdd);
					break;
				case "second":
					calObj.set(Calendar.SECOND, calObj.get(Calendar.SECOND) + numbersToAdd);
					break;
				default:
					break;
			}
		} catch (Exception e) {
			new ProjectCustomException(getClassName(), getMethodName(), e, "Unable to process Calendar operations for " + entityType + " in the specified '" + format + "' format ");
		}
		return simpleDateFormatObject.format(calObj.getTime());
	}

	/**
	 * Performs keyboard actions by sending key combinations to the given CustomWebElement.
	 *
	 * @param customWebElement The CustomWebElement to perform keyboard actions on.
	 * @param keyValues        An array of key values (e.g., "Ctrl", "Shift", "A").
	 */
	@Override
	public void keyboardActions(CustomWebElement customWebElement, String[] keyValues) {
		ReporterUtilities.log("Key actions: " + Arrays.toString(keyValues) + " in " + customWebElement.getName(), () -> {
			new Actions(getDriver()).sendKeys(getElement(customWebElement), Keys.CLEAR, Keys.chord(keyValues)).perform();
		});
	}

	/**
	 * Generates a random alphanumeric string of the specified length.
	 *
	 * @param stringLength The desired length of the random string.
	 * @return A random alphanumeric string of the specified length.
	 */
	@Override
	public String getRandomString(int stringLength) {
		return RandomStringUtils.randomAlphabetic(stringLength);
	}

	/**
	 * Waits until the given CustomWebElement is visible on the page.
	 *
	 * @param timeToWait     The maximum time to wait for the element to become visible, in seconds.
	 * @param customWebElement The CustomWebElement to wait for.
	 * @return The WebElement once it becomes visible.
	 */
	@Override
	public WebElement waitUntilVisibilityOfAnElement(int timeToWait, CustomWebElement customWebElement) {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.visibilityOfElementLocated(customWebElement.getByElement()));
	}

	/**
	 * Waits until the given WebElement is visible on the page.
	 *
	 * @param timeToWait The maximum time to wait for the element to become visible, in seconds.
	 * @param locator    The WebElement to wait for.
	 * @return The WebElement once it becomes visible.
	 */
	@Override
	public WebElement waitUntilVisibilityOfAnElement(int timeToWait, WebElement locator) {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.visibilityOf(locator));
	}

	/**
	 * Waits until the given CustomWebElement is not visible on the page (invisibility).
	 *
	 * @param timeToWait     The maximum time to wait for the element to become invisible, in seconds.
	 * @param customWebElement The CustomWebElement to wait for.
	 * @return True if the element becomes invisible within the specified time; otherwise, false.
	 */
	@Override
	public boolean waitUntilInVisibilityOfAnElement(int timeToWait, CustomWebElement customWebElement) {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.invisibilityOfElementLocated(customWebElement.getByElement()));
	}

	/**
	 * Waits until the given CustomWebElement becomes clickable on the page.
	 *
	 * @param timeToWait     The maximum time to wait for the element to become clickable, in seconds.
	 * @param customWebElement The CustomWebElement to wait for.
	 * @return The WebElement once it becomes clickable.
	 */
	@Override
	public WebElement waitUntilClickableOfAnElement(int timeToWait, CustomWebElement customWebElement) {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.elementToBeClickable(customWebElement.getByElement()));
	}


	/**
	 * Waits until the given WebElement becomes clickable on the page.
	 *
	 * @param timeToWait The maximum time to wait for the element to become clickable, in seconds.
	 * @param locator    The WebElement to wait for.
	 * @return The WebElement once it becomes clickable.
	 */
	@Override
	public WebElement waitUntilClickableOfAnElement(int timeToWait, WebElement locator) {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.elementToBeClickable(locator));
	}

	/**
	 * Waits until the presence of the specified CustomWebElement is detected on the page.
	 *
	 * @param timeToWait     The maximum time to wait for the element's presence, in seconds.
	 * @param customWebElement The CustomWebElement to wait for.
	 * @return The WebElement once its presence is detected.
	 */
	@Override
	public WebElement waitUntilPresenceOfAnElement(int timeToWait, CustomWebElement customWebElement) {
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.presenceOfElementLocated(customWebElement.getByElement()));
	}

	/**
	 * Waits until the specified attribute of the given CustomWebElement contains the specified validation text.
	 *
	 * @param timeToWait     The maximum time to wait for the condition to be met, in seconds.
	 * @param customWebElement The CustomWebElement to wait for.
	 * @param attribute       The attribute to check for validation text.
	 * @param validationText  The expected validation text.
	 * @return True if the attribute contains the validation text within the specified time; otherwise, false.
	 */
	@Override
	public boolean waitUntilAttributeContainsInElement(int timeToWait, CustomWebElement customWebElement, String attribute, String validationText) {
		waitUntilPresenceOfAnElement(timeToWait, customWebElement);
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.attributeContains(customWebElement.getByElement(), attribute, validationText));
	}

	/**
	 * Waits until the specified validation text is present in the text of the given CustomWebElement.
	 *
	 * @param timeToWait     The maximum time to wait for the condition to be met, in seconds.
	 * @param customWebElement The CustomWebElement to wait for.
	 * @param validationText  The expected validation text.
	 * @return True if the validation text is present within the specified time; otherwise, false.
	 */
	@Override
	public boolean waitUntilPresenceOfText(int timeToWait, CustomWebElement customWebElement, String validationText) {
		waitUntilPresenceOfAnElement(timeToWait, customWebElement);
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.textToBePresentInElementLocated(customWebElement.getByElement(), validationText));
	}

	/**
	 * Waits until the specified validation text is present in the text of the given WebElement.
	 *
	 * @param timeToWait     The maximum time to wait for the condition to be met, in seconds.
	 * @param locator        The WebElement to wait for.
	 * @param validationText  The expected validation text.
	 * @return True if the validation text is present within the specified time; otherwise, false.
	 */
	@Override
	public boolean waitUntilPresenceOfText(int timeToWait, WebElement locator, String validationText) {
		waitUntilVisibilityOfAnElement(timeToWait, locator);
		return new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.textToBePresentInElement(locator, validationText));
	}

	/**
	 * Waits until a frame is available and switches to it based on the given CustomWebElement.
	 *
	 * @param timeToWait     The maximum time to wait for the frame to be available, in seconds.
	 * @param customWebElement The CustomWebElement representing the frame.
	 */
	@Override
	public void waitUntilFrameAvailability(int timeToWait, CustomWebElement customWebElement) {
		new WebDriverWait(getDriver(), Duration.ofSeconds(timeToWait)).until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(customWebElement.getByElement()));
	}

	/**
	 * Waits until the URL changes from the old URL to a different URL.
	 *
	 * @param oldUrl The old URL to compare against.
	 */
	public void waitUntilURLChanges(String oldUrl) {
		new WebDriverWait(getDriver(), Duration.ofSeconds(Long.parseLong(getPropertyValue("defaultWaitTime")))).until((ExpectedCondition<Boolean>) d -> !getUrl().equals(oldUrl));
	}

	/**
	 * Verifies if the given CustomWebElement is present on the page.
	 *
	 * @param customWebElement The CustomWebElement to verify.
	 * @return True if the element is present; otherwise, false.
	 */
	@Override
	public boolean verifyElementPresent(CustomWebElement customWebElement) {
		return !getElements(customWebElement).isEmpty();
	}

	/**
	 * Verifies if the given CustomWebElement is not present on the page.
	 *
	 * @param customWebElement The CustomWebElement to verify.
	 * @return True if the element is not present; otherwise, false.
	 */
	@Override
	public boolean verifyElementNotPresent(CustomWebElement customWebElement) {
		return !verifyElementPresent(customWebElement);
	}

	/**
	 * Verifies if the text of the given CustomWebElement matches the specified validation text exactly.
	 *
	 * @param customWebElement The CustomWebElement to verify.
	 * @param validationText  The expected validation text.
	 * @return True if the text matches exactly; otherwise, false.
	 */
	@Override
	public boolean verifyExactTextPresent(CustomWebElement customWebElement, String validationText) {
		return getText(customWebElement).equals(validationText);
	}

	/**
	 * Verifies if the text of the given CustomWebElement contains the specified validation text partially.
	 *
	 * @param customWebElement The CustomWebElement to verify.
	 * @param validationText  The expected partial validation text.
	 * @return True if the text contains the partial validation text; otherwise, false.
	 */
	@Override
	public boolean verifyPartialTextPresent(CustomWebElement customWebElement, String validationText) {
		return getText(customWebElement).contains(validationText);
	}

	/**
	 * Loads a property file from the specified file path and populates the 'propFile' object.
	 *
	 * @param filePath The path to the property file to load.
	 */
	public void loadPropertyFile(String filePath) {
		try {
			propFile = new Properties();
			propFile.load(GenericKeywords.class.getClassLoader().getResourceAsStream(filePath));
		} catch (Exception e) {
			new ProjectCustomException(getClassName(e), getMethodName(e), e, "Unable to load property File in the location '" + filePath + "'");
		}
	}

	/**
	 * Retrieves the value of a property from the loaded property file.
	 *
	 * @param property The name of the property to retrieve.
	 * @return The value of the specified property, or null if it doesn't exist.
	 */
	public String getPropertyValue(String property) {
		return propFile.getProperty(property);
	}

	/**
	 * Retrieves the class name from the provided exception's stack trace.
	 *
	 * @param exceptionMessage The exception from which to extract the class name.
	 * @return The name of the class that caused the exception.
	 */
	@Override
	public String getClassName(Exception exceptionMessage) {
		return exceptionMessage.getStackTrace()[0].getClassName();
	}

	/**
	 * Retrieves the method name from the provided exception's stack trace.
	 *
	 * @param exceptionMessage The exception from which to extract the method name.
	 * @return The name of the method that caused the exception.
	 */
	@Override
	public String getMethodName(Exception exceptionMessage) {
		return exceptionMessage.getStackTrace()[0].getMethodName();
	}

	/**
	 * Retrieves the current class name from the current thread's stack trace.
	 *
	 * @return The name of the class where this method is called from.
	 */
	@Override
	public String getClassName() {
		return Thread.currentThread().getStackTrace()[1].getClassName();
	}

	/**
	 * Retrieves the current method name from the current thread's stack trace.
	 *
	 * @return The name of the method where this method is called from.
	 */
	@Override
	public String getMethodName() {
		return Thread.currentThread().getStackTrace()[1].getMethodName();
	}
}
