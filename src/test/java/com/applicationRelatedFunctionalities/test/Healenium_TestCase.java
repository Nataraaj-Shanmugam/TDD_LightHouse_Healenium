package com.applicationRelatedFunctionalities.test;

import com.applicationRelatedFunctionalities.pages.ApplicationActions;
import functional.custom.CustomWebElement;
import functional.custom.DataProviderTransformer;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import testExecutionEngine.TestExecutionEngine;

import java.util.List;


@Listeners(DataProviderTransformer.class)
public class Healenium_TestCase extends TestExecutionEngine{
	ApplicationActions applicationActions = new ApplicationActions();

	/**
	 * Test method for searching functionality.
	 * Asserts that the URL changes after performing a search action.
	 *
	 * @param testData List of test data parameters for the search.
	 */
	@Story("This is Healenium story")
	@Test()
	public void search(List<String> testData) {
		loadURL("C:/Users/Others/Desktop/Login.html");
		WebElement uNameElement = getElement(new CustomWebElement(By.xpath("//input[@name='uname']"), "UserName"));
//		((JavascriptExecutor)getDriver()).executeScript("arguments[0].setAttribute('name', 'UserName')", element);
		uNameElement.sendKeys("UserNameTest");
		getElement(new CustomWebElement(By.xpath("//input[@name='psw']"), "Password")).sendKeys("Password");
		getElement(new CustomWebElement(By.tagName("button"), "Password")).click();

	}
}
