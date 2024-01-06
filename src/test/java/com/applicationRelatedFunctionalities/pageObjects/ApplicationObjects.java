package com.applicationRelatedFunctionalities.pageObjects;

import org.openqa.selenium.By;
import functional.custom.CustomWebElement;
/**
 * This class contains the locators for various web elements in the application.
 * It uses Selenium's By class to define the locators.
 */
public class ApplicationObjects {

	public CustomWebElement searchBox_input = new CustomWebElement(By.xpath("//textarea[@type]"), "Search input box");
	public CustomWebElement search_button = new CustomWebElement(By.xpath("(//input[@value='Google Search'])[last()]"), "Search button");
	public CustomWebElement feelingLucky_button = new CustomWebElement(By.xpath("(//input[@value=\"I'm Feeling Lucky\"])[last()]"), "`I'm Feeling Lucky` button");
	public CustomWebElement about_button = new CustomWebElement(By.linkText("About"), "About link in the footer");
}
