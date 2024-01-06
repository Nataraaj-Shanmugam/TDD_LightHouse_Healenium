package functional.custom;

import functional.genericKeywords.ThreadLocalFunctionalities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
/**
 * Custom wrapper for Selenium's WebElement.
 * It enhances the WebElement by adding custom functionalities and state management.
 */
public class CustomWebElement {

    WebElement element;
    String name;
    By byElement;


    /**
     * Constructs a CustomWebElement with an existing WebElement and a name.
     *
     * @param element The WebElement to be wrapped.
     * @param name    The name for this element, for easier identification and logging.
     */
    public CustomWebElement(WebElement element, String name){
        this.element = element;
        this.name = name;
    }

    /**
     * Constructs a CustomWebElement with a locator (By) and a name.
     * The actual WebElement will be located when getElement() is called.
     *
     * @param byElement The locator to find the WebElement.
     * @param name      The name for this element.
     */
    public CustomWebElement(By byElement, String name){
        this.byElement = byElement;
        this.name = name;
    }

    /**
     * Retrieves the WebElement, finding it by its locator if not already done so.
     * If the element is not currently displayed, it will be located again.
     *
     * @return The WebElement wrapped by this custom object.
     */
    public WebElement getElement(){
        if(this.element == null || !this.element.isDisplayed())
            this.element = ThreadLocalFunctionalities.getDriver().findElement(this.byElement);
        return this.element;
    }
    /**
     * Gets the name of this CustomWebElement.
     *
     * @return The name assigned to this element.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the locator (By object) used to find this WebElement, if any.
     *
     * @return The By locator for this element, or null if a WebElement was directly provided.
     */
    public By getByElement(){
        return this.byElement;
    }
}
