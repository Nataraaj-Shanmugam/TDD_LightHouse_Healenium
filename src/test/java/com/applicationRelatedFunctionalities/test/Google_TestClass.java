package com.applicationRelatedFunctionalities.test;

import java.util.List;

import functional.custom.DataProviderTransformer;
import functional.annotations.TestDataAnnotation;
import com.applicationRelatedFunctionalities.pages.ApplicationActions;
import io.qameta.allure.Story;
import nonFunctional.LighthouseUtil;
import nonFunctional.PerformanceTest;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import testExecutionEngine.TestExecutionEngine;
import org.testng.annotations.Test;


/**
 * Test class for various application functionalities.
 * It extends the TestExecutionEngine to leverage common test capabilities.
 */
@Listeners(DataProviderTransformer.class)
public class Google_TestClass extends TestExecutionEngine{
	ApplicationActions applicationActions = new ApplicationActions();

	/**
	 * Test method for searching functionality.
	 * Asserts that the URL changes after performing a search action.
	 *
	 * @param testData List of test data parameters for the search.
	 */
	@Story("This is Search story")
	@TestDataAnnotation(sheetName = "Search")
	@Test(groups = "search")
	@PerformanceTest
	public void search(List<String> testData) {
		applicationActions.enterValueInSearch();
		applicationActions.clickSearchButton();
		Assert.assertNotEquals(getPropertyValue("URL"), getUrl());
		new LighthouseUtil().performanceTest(getDriver().getCurrentUrl() , "LighHouseReport_Testing",getPerformancePort());
	}

	/**
	 * Test method for the 'Feeling Lucky' button functionality.
	 * Asserts that the URL is as expected after clicking the button.
	 *
	 * @param testData List of test data parameters, not used in this test.
	 */
	@TestDataAnnotation(isNoDataProvider = true)
	@Test()
	public void feelingLuck(List<String> testData) {
		applicationActions.clickFeelingLuckButton();
		Assert.assertEquals("https://doodles.google/", getUrl());
	}

	/**
	 * Test method for the 'About' page functionality.
	 * Asserts that the URL starts with the expected string.
	 *
	 * @param testData List of test data parameters for the about page.
	 */
	@Test()
	public void about(List<String> testData) {
		applicationActions.clickAbout();
		waitUntilURLChanges(getPropertyValue("URL"));
		Assert.assertTrue(getUrl().startsWith("https://about.google/"));
	}
}
