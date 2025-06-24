package org.runner;

import org.base.TestBase;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(features = "src\\test\\resources", 
						glue = { "classpath:org.stepDefinition" }, 
							plugin = { "pretty", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:",
								"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm" }, 
									dryRun = false, 
										monochrome = true, 
											tags = "@run")

public class TestRunner extends AbstractTestNGCucumberTests {

	@BeforeSuite
	public void launchBrowser() {
		TestBase testbase = new TestBase();
		testbase.selectBrowser("chrome");
		testbase.strictPass(true);
	}

	@AfterSuite
	public void tearDown() {
		TestBase testBase = new TestBase();
		testBase.openCucumberHTMLReport();
		testBase.openCucumberPDFReport();
	}
}
