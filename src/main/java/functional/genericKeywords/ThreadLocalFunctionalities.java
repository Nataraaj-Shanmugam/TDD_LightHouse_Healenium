package functional.genericKeywords;

import java.util.HashMap;
import java.util.List;

import com.epam.healenium.SelfHealingDriver;
import org.openqa.selenium.WebDriver;

/**
 * Provides thread-local functionalities to handle WebDriver instances
 * and other test-related data. This ensures thread safety when running
 * tests in parallel, allowing each thread to have its own instance of
 * the data.
 */
public class ThreadLocalFunctionalities {

	/** Thread Local WebDriver instance */
	protected static ThreadLocal<WebDriver> threadDriverInstance = new ThreadLocal<>();

	/**
	 * Retrieves the WebDriver instance specific to the current thread.
	 *
	 * @return The WebDriver instance for the current thread.
	 */
	public static WebDriver getDriver() {
		return threadDriverInstance.get();
	}

	/** Thread Local validation data */
	protected ThreadLocal<HashMap<String, String>> validationData = new ThreadLocal<>();

	/**
	 * Retrieves the validation data map specific to the current thread.
	 *
	 * @return The validation data HashMap for the current thread.
	 */
	protected HashMap<String, String> getValidationData() {
		return validationData.get();
	}

	/** Thread Local test data */
	public static ThreadLocal<List<String>> scenarioTestData = new ThreadLocal<>();

	/**
	 * Retrieves the test data list specific to the current thread.
	 *
	 * @return The test data List for the current thread.
	 */
	public static List<String> getTestData() {
		return scenarioTestData.get();
	}

	/** Thread Local scenario column numbers */
	public static ThreadLocal<HashMap<String, Integer>> scenarioColumnNumber = new ThreadLocal<>();

	/**
	 * Retrieves the column number for a specific column name
	 * in the scenario data, specific to the current thread.
	 *
	 * @param column The name of the column.
	 * @return The column number for the specified column.
	 */
	public static int getScenarioColumnNumber(String column) {
		return scenarioColumnNumber.get().get(column);
	}

	/**
	 * Thread-local variable to store the performance port for each thread.
	 */
	protected static ThreadLocal<Integer> performancePort = new ThreadLocal<>();

	/**
	 * Retrieves the performance port specific to the current thread.
	 *
	 * @return The performance port number for the current thread.
	 *         If the port has not been set for the current thread, this method returns null.
	 */
	protected static int getPerformancePort(){ return performancePort.get();}

}
