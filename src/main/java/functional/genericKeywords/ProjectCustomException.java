package functional.genericKeywords;

/**
 * Custom exception class for project-specific exceptions in a test-driven development framework.
 * This exception is used to handle and report errors specific to the project's testing scenarios.
 */
public class ProjectCustomException extends Exception {

	/**
	 * Default constructor for {@link ProjectCustomException}.
	 * Initializes a new instance of this class with default settings.
	 */
	public ProjectCustomException() {
	}

	/**
	 * Constructs a new {@link ProjectCustomException} with detailed information about the failure.
	 * It logs the failed scenario, closes the browser, and captures the exception details and a custom message.
	 *
	 * @param failedClassName   The name of the class where the failure occurred.
	 * @param failedMethodName  The name of the method where the failure occurred.
	 * @param exceptionDetails  The exception details of the failure.
	 * @param message           The custom message for the exception.
	 */
	public ProjectCustomException(String failedClassName, String failedMethodName, Exception exceptionDetails, String message) {
		super(message); // Includes the custom message in the exception
		// Adding the failed scenario to the list of failed scenarios.
		GenericKeywords.failedScenarios.add(ThreadLocalFunctionalities.getTestData().get(ThreadLocalFunctionalities.getScenarioColumnNumber("Scenario")));
		// Closing the browser upon encountering the exception.
		new GenericKeywords().closeBrowser();
		// Additional handling can be added here if needed
	}
}
