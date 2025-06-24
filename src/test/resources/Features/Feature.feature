Feature: verifying the user able to launch a practice application to automate

@run
Scenario: verifying user able to launch the practice application
Given user able to launch practice application URL "https://practice.expandtesting.com/"

@run
Scenario: verifying user able to validate the error message without username and password
Given user able to click on the Web inputs hyperlink
When user able to enter the Number as input
When user able to enter the user name as text


