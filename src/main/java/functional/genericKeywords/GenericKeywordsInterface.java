package functional.genericKeywords;

import java.io.File;
import java.util.List;

import functional.custom.CustomWebElement;
import org.openqa.selenium.WebElement;

/**
 * Interface defining generic web automation keywords.
 * This interface provides  methods for various web automation tasks
 * such as clicking elements, inputting text, navigating, and verifying conditions.
 */
public interface GenericKeywordsInterface {

	/**
	 * Clicks on a specified custom web element.
	 *
	 * @param customWebElement the custom web element to be clicked
	 */
	void click(CustomWebElement customWebElement);

	/**
	 * Inputs text into a specified custom web element.
	 *
	 * @param customWebElement the custom web element where text is to be inputted
	 * @param value            the text to input
	 */
	void inputText(CustomWebElement customWebElement, String value);

	/**
	 * Inputs text into a custom web element using keyboard actions.
	 *
	 * @param customWebElement the custom web element where text is to be inputted
	 * @param value            the text to input
	 */
	void inputTextWithActions(CustomWebElement customWebElement, String value);

	/**
	 * Opens a web browser. Can be configured for performance testing.
	 *
	 * @param performanceTest if true, configures the browser for performance testing
	 */
	void openBrowser(boolean performanceTest);

	/**
	 * Closes the currently open web browser.
	 */
	void closeBrowser();

	/**
	 * Loads a given URL in the web browser.
	 *
	 * @param URL the URL to load
	 */
	void loadURL(String URL);

	/**
	 * Navigates back in the browser's history.
	 */
	void navigateBack();

	/**
	 * Navigates forward in the browser's history.
	 */
	void navigateForward();

	/**
	 * Refreshes the current page.
	 */
	void refreshPage();

	/**
	 * Retrieves the text from a custom web element.
	 *
	 * @param customWebElement the custom web element from which to retrieve text
	 * @return the text from the specified element
	 */
	String getText(CustomWebElement customWebElement);

	/**
	 * Retrieves text from a standard WebElement, with an associated field name.
	 *
	 * @param element    the WebElement from which to retrieve text
	 * @param fieldName  an associated field name for context
	 * @return the text from the specified element
	 */
	String getText(WebElement element, String fieldName);

	/**
	 * Returns the current URL of the browser.
	 *
	 * @return the current browser URL
	 */
	String getUrl();

	/**
	 * Retrieves a single web element based on a custom web element.
	 *
	 * @param customWebElement the custom web element to find
	 * @return the corresponding WebElement
	 */
	WebElement getElement(CustomWebElement customWebElement);

	/**
	 * Retrieves a list of web elements based on a custom web element.
	 *
	 * @param customWebElement the custom web element to find multiple instances of
	 * @return a list of WebElements matching the custom element
	 */
	List<WebElement> getElements(CustomWebElement customWebElement);

	/**
	 * Performs a double-click action on a custom web element.
	 *
	 * @param customWebElement the custom web element to double-click
	 */
	void doubleClick(CustomWebElement customWebElement);

	/**
	 * Scrolls to a specific custom web element on the page.
	 *
	 * @param customWebElement the custom web element to scroll to
	 */
	void scrollToAnElement(CustomWebElement customWebElement);

	/**
	 * Interacts with a dropdown menu on a custom web element.
	 *
	 * @param customWebElement the custom web element representing a dropdown
	 * @param selectionType    the type of selection action (e.g., "select by value", "select by index")
	 * @param option           the option to select
	 */
	void dropDown(CustomWebElement customWebElement, String selectionType, String option);

	/**
	 * Checks if a custom web element is blank.
	 *
	 * @param customWebElement the custom web element to check
	 * @return true if the element is blank, otherwise false
	 */
	boolean isBlank(CustomWebElement customWebElement);

	/**
	 * Checks if a text value is blank.
	 *
	 * @param textValue the text to check
	 * @return true if the text is blank, otherwise false
	 */
	boolean isBlank(String textValue);

	/**
	 * Adds a specified number to a calendar date in a given format.
	 *
	 * @param entityType    the type of entity (e.g., "day", "month", "year")
	 * @param numbersToAdd  the number of entities to add
	 * @param format        the date format to use
	 * @return the calculated date as a string
	 */
	String addInCalender(String entityType, int numbersToAdd, String format);

	/**
	 * Performs keyboard actions on a custom web element.
	 *
	 * @param customWebElement the custom web element to perform actions on
	 * @param keyValues        an array of key values to be pressed
	 */
	void keyboardActions(CustomWebElement customWebElement, String[] keyValues);

	/**
	 * Generates a random string of a specified length.
	 *
	 * @param stringLength the length of the string to generate
	 * @return a random string of the specified length
	 */
	String getRandomString(int stringLength);

	/**
	 * Waits for a specified duration for a CustomWebElement to become visible on the web page.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element to wait for
	 * @return WebElement         the web element after it becomes visible
	 */
	WebElement waitUntilVisibilityOfAnElement(int timeToWait, CustomWebElement customWebElement);

	/**
	 * Waits for a specified duration for a standard WebElement to become visible on the web page.
	 *
	 * @param timeToWait  the maximum time to wait in seconds
	 * @param locator     the standard web element to wait for
	 * @return WebElement the web element after it becomes visible
	 */
	WebElement waitUntilVisibilityOfAnElement(int timeToWait, WebElement locator);

	/**
	 * Waits for a specified duration for a CustomWebElement to become invisible or not present on the web page.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element to wait for
	 * @return boolean            true if the element becomes invisible within the wait period, otherwise false
	 */
	boolean waitUntilInVisibilityOfAnElement(int timeToWait, CustomWebElement customWebElement);

	/**
	 * Waits for a specified duration for a CustomWebElement to become clickable on the web page.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element to wait for
	 * @return WebElement         the web element after it becomes clickable
	 */
	WebElement waitUntilClickableOfAnElement(int timeToWait, CustomWebElement customWebElement);

	/**
	 * Waits for a specified duration for a standard WebElement to become clickable on the web page.
	 *
	 * @param timeToWait  the maximum time to wait in seconds
	 * @param locator     the standard web element to wait for
	 * @return WebElement the web element after it becomes clickable
	 */
	WebElement waitUntilClickableOfAnElement(int timeToWait, WebElement locator);

	/**
	 * Waits for a specified duration for a CustomWebElement to be present on the web page.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element to wait for
	 * @return WebElement         the web element after it is present on the page
	 */
	WebElement waitUntilPresenceOfAnElement(int timeToWait, CustomWebElement customWebElement);

	/**
	 * Waits for a specified duration for a CustomWebElement's attribute to contain a specific value.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element to check
	 * @param attribute           the attribute to check for
	 * @param value               the value that the attribute should contain
	 * @return boolean            true if the attribute contains the specified value within the wait period, otherwise false
	 */
	boolean waitUntilAttributeContainsInElement(int timeToWait, CustomWebElement customWebElement, String attribute, String value);

	/**
	 * Waits for a specified duration for a CustomWebElement to contain a specific text.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element to check
	 * @param validationText      the text to be present in the element
	 * @return boolean            true if the text is present within the wait period, otherwise false
	 */
	boolean waitUntilPresenceOfText(int timeToWait, CustomWebElement customWebElement, String validationText);

	/**
	 * Waits for a specified duration for a standard WebElement to contain a specific text.
	 *
	 * @param timeToWait  the maximum time to wait in seconds
	 * @param locator     the standard web element to check
	 * @param validationText      the text to be present in the element
	 * @return boolean    true if the text is present within the wait period, otherwise false
	 */
	boolean waitUntilPresenceOfText(int timeToWait, WebElement locator, String validationText);

	/**
	 * Waits for a specified duration for a frame to become available and switch to it.
	 *
	 * @param timeToWait          the maximum time to wait in seconds
	 * @param customWebElement    the custom web element representing the frame
	 */
	void waitUntilFrameAvailability(int timeToWait, CustomWebElement customWebElement);

	/**
	 * Waits until the URL of the browser changes from a specified old URL.
	 *
	 * @param oldUrl      the URL to change from
	 */
	void waitUntilURLChanges(String oldUrl);


	/**
	 * Verifies if an element is present on the web page.
	 *
	 * @param customWebElement the custom web element to check for presence
	 * @return true if the element is present, otherwise false
	 */
	boolean verifyElementPresent(CustomWebElement customWebElement);

	/**
	 * Verifies if an element is not present on the web page.
	 *
	 * @param customWebElement the custom web element to check for absence
	 * @return true if the element is not present, otherwise false
	 */
	boolean verifyElementNotPresent(CustomWebElement customWebElement);

	/**
	 * Verifies if the exact text is present in a custom web element.
	 *
	 * @param customWebElement the custom web element to check
	 * @param validationText   the exact text to verify
	 * @return true if the exact text is present, otherwise false
	 */
	boolean verifyExactTextPresent(CustomWebElement customWebElement, String validationText);

	/**
	 * Verifies if the partial text is present in a custom web element.
	 *
	 * @param customWebElement the custom web element to check
	 * @param validationText   the partial text to verify
	 * @return true if the partial text is present, otherwise false
	 */
	boolean verifyPartialTextPresent(CustomWebElement customWebElement, String validationText);

	/**
	 * Takes a screenshot of a specific custom web element.
	 *
	 * @param customWebElement the custom web element to capture in the screenshot
	 * @return a File object containing the screenshot
	 */
	File takeScreenshotSpecificElement(CustomWebElement customWebElement);

	/**
	 * Loads a property file with a specified status.
	 *
	 * @param status the status to use when loading the property file
	 */
	void loadPropertyFile(String status);

	/**
	 * Retrieves the class name from an exception.
	 *
	 * @param exception the exception to retrieve the class name from
	 * @return the class name of the exception
	 */
	String getClassName(Exception exception);

	/**
	 * Retrieves the method name from an exception.
	 *
	 * @param exception the exception to retrieve the method name from
	 * @return the method name where the exception occurred
	 */
	String getMethodName(Exception exception);

	/**
	 * Retrieves the class name of the current instance.
	 *
	 * @return the class name of the current instance
	 */
	String getClassName();

	/**
	 * Retrieves the method name of the current instance.
	 *
	 * @return the method name of the current instance
	 */
	String getMethodName();


}
