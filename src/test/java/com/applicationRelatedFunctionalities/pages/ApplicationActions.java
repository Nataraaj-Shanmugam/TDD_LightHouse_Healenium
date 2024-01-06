package com.applicationRelatedFunctionalities.pages;

import com.applicationRelatedFunctionalities.pageObjects.ApplicationObjects;
import functional.genericKeywords.GenericKeywords;
import functional.genericKeywords.ProjectCustomException;

/**
 * Class containing actions that can be performed within the application.
 * Extends GenericKeywords to leverage common web interaction methods.
 */
public class ApplicationActions extends GenericKeywords {
	ApplicationObjects applicationObjectsObj = new ApplicationObjects();

	/**
	 * Enters a value into the search box.
	 * The value is retrieved based on the 'SearchText' column from the scenario data.
	 */
	public void enterValueInSearch() {
		try {
			inputText(applicationObjectsObj.searchBox_input, getTestData().get(getScenarioColumnNumber("SearchText")));
		}catch(Exception e) {
			new ProjectCustomException(getClassName(e), getMethodName(e), e, "");
		}
	}

	/**
	 * Clicks the search button in the application.
	 */
	public void clickSearchButton() {
		try {
//			waitUntilClickableOfAnElement(10, applicationObjectsObj.search_button);
			click(applicationObjectsObj.search_button);
		}catch(Exception e) {
			new ProjectCustomException(getClassName(e), getMethodName(e), e, "");
		}
	}

	/**
	 * Clicks the 'Feeling Lucky' button in the application.
	 */
	public void clickFeelingLuckButton() {
		try {
//			waitUntilClickableOfAnElement(10, applicationObjectsObj.feelingLucky_button);
			click(applicationObjectsObj.feelingLucky_button);
		}catch(Exception e) {
			new ProjectCustomException(getClassName(e), getMethodName(e), e, "");
		}
	}

	/**
	 * Clicks the 'About' button in the application.
	 */
	public void clickAbout() {
		try {
//			waitUntilClickableOfAnElement(10, applicationObjectsObj.about_button);
			click(applicationObjectsObj.about_button);
		}catch(Exception e) {
			new ProjectCustomException(getClassName(e), getMethodName(e), e, "");
		}
	}
}
