package functional.genericKeywords;

import java.io.File;
import java.util.List;

import functional.custom.CustomWebElement;
import org.openqa.selenium.WebElement;

/**
 * Interface defining generic web automation keywords.
 * This interface provides abstract methods for various web automation tasks
 * such as clicking elements, inputting text, navigating, and verifying conditions.
 */
public interface GenericKeywordsInterface {

	abstract void click(CustomWebElement customWebElement);

	abstract void inputText(CustomWebElement customWebElement, String value);

	abstract void inputTextWithActions(CustomWebElement customWebElement, String value);

	abstract void openBrowser(boolean performanceTest);

	abstract void closeBrowser();

	abstract void loadURL(String URL);

	abstract void navigateBack();

	abstract void navigateForward();

	abstract void refreshPage();

	abstract String getText(CustomWebElement customWebElement);

	abstract String getText(WebElement element, String fieldName);

	abstract String getUrl();

	abstract WebElement getElement(CustomWebElement customWebElement);

	abstract List<WebElement> getElements(CustomWebElement customWebElement);

	abstract void doubleClick(CustomWebElement customWebElement);

	abstract void scrollToAnElement(CustomWebElement customWebElement);

	abstract void dropDown(CustomWebElement customWebElement, String selectionType, String option);

	abstract boolean isBlank(CustomWebElement customWebElement);

	abstract boolean isBlank(String textValue);

	abstract String addInCalender(String entityType, int numbersToAdd, String format);

	abstract void keyboardActions(CustomWebElement customWebElement,String[] keyValues);

	abstract String getRandomString(int stringLength);

	//Wait functionalities
	abstract WebElement waitUntilVisibilityOfAnElement(int timeToWait, CustomWebElement customWebElement);

	abstract WebElement waitUntilVisibilityOfAnElement(int timeToWait, WebElement locator);

	abstract boolean waitUntilInVisibilityOfAnElement(int timeToWait, CustomWebElement customWebElement);

	abstract WebElement waitUntilClickableOfAnElement(int timeToWait, CustomWebElement customWebElement);

	abstract WebElement waitUntilClickableOfAnElement(int timeToWait, WebElement locator);

	abstract WebElement waitUntilPresenceOfAnElement(int timeToWait, CustomWebElement customWebElement);

	abstract boolean waitUntilAttributeContainsInElement(int timeToWait, CustomWebElement customWebElement, String attribute, String value);

	abstract boolean waitUntilPresenceOfText(int timeToWait, CustomWebElement customWebElement,String validationText);

	abstract boolean waitUntilPresenceOfText(int timeToWait, WebElement locator,String validationText);

	abstract void waitUntilFrameAvailability(int timeToWait, CustomWebElement customWebElement);

	abstract void waitUntilURLChanges(String oldUrl);

	//Verifications
	abstract boolean verifyElementPresent(CustomWebElement customWebElement);

	abstract boolean verifyElementNotPresent(CustomWebElement customWebElement);

	abstract boolean verifyExactTextPresent(CustomWebElement customWebElement,String validationText);

	abstract boolean verifyPartialTextPresent(CustomWebElement customWebElement,String validationText);

	abstract File takeScreenshotSpecificElement(CustomWebElement customWebElement);
	//property file

	abstract void loadPropertyFile(String status);

	abstract String getClassName(Exception exception);

	abstract String getMethodName(Exception exception);

	abstract String getClassName();

	abstract String getMethodName();

}
