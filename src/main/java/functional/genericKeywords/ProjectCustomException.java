package functional.genericKeywords;


public class ProjectCustomException extends Exception {

	public ProjectCustomException(String failedClassName , String failedMethodName , Exception exceptionDetails,String message) {
		super();
		GenericKeywords.failedScenarios.add(ThreadLocalFunctionalities.getTestData().get(ThreadLocalFunctionalities.getScenarioColumnNumber("Scenario")));
		new GenericKeywords().closeBrowser();
	}
}
