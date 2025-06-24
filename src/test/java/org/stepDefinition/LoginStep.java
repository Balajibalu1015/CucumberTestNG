package org.stepDefinition;

import java.io.IOException;
import java.util.Map;

import org.base.TestBase;
import org.json.simple.parser.ParseException;
import org.pageObjects.LoginPage;

import dataProvider.DataProvider;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class LoginStep extends TestBase {

	LoginPage lp;
	Map<String, String> map;
	DataProvider data;

	public LoginStep() throws IOException, ParseException {
		data = new DataProvider();
		map = data.dataProvider();
		data.jsonFileReader();
	}

	@Given("user able to launch practice application URL {string}")
	public void user_able_to_launch_practice_application_url(String string) {
		reportTestCaseName("user able to launch practice application URL " + string);
		launchApplication(string);
	}

	@Given("user able to click on the Web inputs hyperlink")
	public void user_able_to_click_on_the_web_inputs_hyperlink() throws InterruptedException {
		reportTestCaseName("user able to click on the Web inputs hyperlink");
		lp.clickOnWebInputs();
	}

	@When("user able to enter the Number as input")
	public void user_able_to_enter_the_number_as_input() throws InterruptedException {
		reportTestCaseName("user able to enter the Number as input");
		lp.enterNumber(data.getJsonMapValue("Number"));
	}

	@When("user able to enter the user name as text")
	public void user_able_to_enter_the_user_name_as_text() throws InterruptedException {
		reportTestCaseName("user able to enter the user name as text");
		lp.enterUserName(map.get("UserName"));
	}

	@Before
	public void before() {
		lp = new LoginPage(driver);
	}
}
