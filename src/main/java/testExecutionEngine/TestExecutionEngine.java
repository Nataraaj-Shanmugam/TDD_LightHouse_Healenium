package testExecutionEngine;

import functional.annotations.TestDataAnnotation;
import functional.utilities.DataProviderUtility;
import functional.utilities.ReporterUtilities;
import functional.genericKeywords.GenericKeywords;
import functional.genericKeywords.ThreadLocalFunctionalities;
import io.qameta.allure.model.Status;
import nonFunctional.PerformanceTest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Main engine for executing TestNG tests, extending GenericKeywords for web automation functionalities.
 * This class provides custom setup and teardown methods for TestNG tests, along with data provisioning capabilities.
 */
public class TestExecutionEngine extends GenericKeywords {

	/**
	 * Default constructor for {@link TestExecutionEngine}.
	 * Initializes a new instance of this class with default settings.
	 */
	public TestExecutionEngine() {
	}

	/**
	 * A globally accessible instance of the GenericKeywords class.
	 * This instance is used to access functionalities provided by the GenericKeywords class.
	 */
	public static final GenericKeywords genericKeywords = new GenericKeywords();

	/**
	 * Method executed before each TestNG test method. Initializes thread-local test data and opens a browser session.
	 * If the test method is annotated with {@link PerformanceTest}, it sets up specific browser capabilities.
	 *
	 * @param testMethod The test method to be executed.
	 * @param testData   The test data to be used in the test method.
	 */
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method testMethod, Object[] testData) {
		ThreadLocalFunctionalities.scenarioTestData.set((List<String>) testData[0]);
		if(testMethod.isAnnotationPresent(TestDataAnnotation.class) && !testMethod.getAnnotation(TestDataAnnotation.class).isNoDataProvider()){
			TestDataAnnotation testDataAnnotation = testMethod.getAnnotation(TestDataAnnotation.class);
			ThreadLocalFunctionalities.scenarioColumnNumber.set(DataProviderUtility.getSheetHeader(Objects.equals(testDataAnnotation.sheetId(), "") ? getPropertyValue("sheetId") : testDataAnnotation.sheetId() , testDataAnnotation.sheetName()));
		}
		genericKeywords.openBrowser(testMethod.isAnnotationPresent(PerformanceTest.class));
	}

	/**
	 * Method executed after each TestNG test method. Updates the test status in the report and closes the browser session.
	 *
	 * @param result The result of the test execution, providing the status of the test.
	 */
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestResult result) {
		if (result.getStatus() == ITestResult.SUCCESS)
			ReporterUtilities.updateTestStatus(Status.PASSED);
		else if (result.getStatus() == ITestResult.FAILURE)
			ReporterUtilities.updateTestStatus(Status.FAILED);
		else if (result.getStatus() == ITestResult.SKIP)
			ReporterUtilities.updateTestStatus(Status.SKIPPED);

		genericKeywords.closeBrowser();
	}

	/**
	 * Custom data provider for TestNG tests, based on {@link TestDataAnnotation}.
	 * Retrieves test data from specified sheet and combines it with browser details for comprehensive test coverage.
	 *
	 * @param testMethod The test method requesting the data.
	 * @return An array of test data and browser combinations.
	 */
	@DataProvider(name = "customDataProvider", parallel = true)
	public Object[] customDataProvider(Method testMethod) {
		TestDataAnnotation testDataAnnotation = testMethod.getAnnotation(TestDataAnnotation.class);
		String sheetId;
		if (testDataAnnotation.sheetId().isEmpty()) sheetId = getPropertyValue("sheetId");
		else sheetId = testDataAnnotation.sheetId();
		List<List<Object>> testData = new ArrayList<>();
		List<Object> temp;
		HashSet<List<String>> browserCombination = DataProviderUtility.getBrowserDetails();
		for (List<Object> each : DataProviderUtility.getData(sheetId, testDataAnnotation.sheetName())) {
			for (List<String> eachBrowser : browserCombination) {
				temp = new ArrayList<>(each);
				temp.addAll(eachBrowser);
				testData.add(temp);
			}
		}
		return testData.stream().toArray(Object[]::new);
	}

	/**
	 * Data provider that provides only browser details for tests not requiring additional data.
	 * This is useful for tests that are browser-specific but do not depend on variable test data.
	 *
	 * @return An iterator over an array of browser details.
	 */
	@DataProvider(name = "noDataProvider", parallel = true)
	public Iterator<Object[]> noDataProvider() {
		return DataProviderUtility.getBrowserDetails().stream().map(array -> new Object[]{array}).iterator();
	}
}