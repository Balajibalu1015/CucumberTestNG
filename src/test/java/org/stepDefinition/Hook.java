package org.stepDefinition;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.base.TestBase;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.testng.Assert;
import org.testng.SkipException;

import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;

public class Hook extends TestBase {

	@Before
	public void beforeScenario(Scenario scenario) {
		if (strictPass == true) {
			try {
				if (Hook.scenario.isFailed()) {
					throw new SkipException("Scenario : " + Hook.scenario.getName()
							+ " is failed and the script execution is going to end because of the previous scenarion is failed");
				} else {
					this.scenario = scenario;
				}
			} catch (NullPointerException e) {
				this.scenario = scenario;
			}
		}
	}

	@BeforeStep
	public void beforeStep() {
		reportTestCaseName = null;
		ifTestCasesFailsExceptionMsg = null;
		try {
			driver.getTitle();
		} catch (NoSuchWindowException e) {
			Assert.fail(e.getMessage());
		}
	}

	@AfterStep
	public void addScreenshot(Scenario scenario) throws IOException {
		try {
			File screenshotAs = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			byte[] byteArray = FileUtils.readFileToByteArray(screenshotAs);
			result = verifyAssert();
			if (result == 0) {
				scenario.attach(byteArray, "image/png", reportTestCaseName);
			} else if (result == 1) {
				scenario.attach(byteArray, "image/png", ifTestCasesFailsExceptionMsg);
				Assert.fail(ifTestCasesFailsExceptionMsg);
			} else {
				throw new SkipException("Skipping the execution because of the assertion is failed");
			}
		} catch (NoSuchWindowException e) {
			Assert.fail(e.getMessage());
		} catch (UnhandledAlertException e) {
		}
	}
}
