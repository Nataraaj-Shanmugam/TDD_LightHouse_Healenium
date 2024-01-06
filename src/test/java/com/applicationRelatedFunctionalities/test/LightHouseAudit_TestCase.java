package com.applicationRelatedFunctionalities.test;

import testExecutionEngine.TestExecutionEngine;
import nonFunctional.LighthouseUtil;
import nonFunctional.PerformanceTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;


public class LightHouseAudit_TestCase extends TestExecutionEngine {



	//	@Story("This is Healenium story")
	@Test
	@PerformanceTest
	public void performanceAudit(List<String> testData){
//	public void performanceAudit(){

		getDriver().get("https://ghoshasish99.medium.com/google-lighthouse-with-selenium-ac767bfa5eb8");


		new LighthouseUtil().performanceTest(getDriver().getCurrentUrl() , "LighHouseReport_Testing",getPerformancePort());

	}
}
