package org.base;

import java.awt.Desktop;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.awt.event.KeyEvent;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;

public class TestBase {

	public static WebDriver driver;
	public static String reportTestCaseName;
	public static String ifTestCasesFailsExceptionMsg;
	public static Scenario scenario;
	public static boolean strictPass;
	public static int result = 0;

	public WebDriver selectBrowser(String browser) {
		if (browser.equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.setAcceptInsecureCerts(true);
			// Accept insecure SSL certificates
			driver = new ChromeDriver(options);
		} else if (browser.equalsIgnoreCase("edge")) {
			EdgeOptions options = new EdgeOptions();
			options.setAcceptInsecureCerts(true);
			// Accept insecure SSL certificates
			driver = new EdgeDriver();
		} else {
			FirefoxOptions options = new FirefoxOptions();
			options.setAcceptInsecureCerts(true);
			// Accept insecure SSL certificates
			driver = new FirefoxDriver();
		}

		driver.manage().window().maximize();
		return driver;
	}

	public void strictPass(boolean result) {
		strictPass = result;
	}

	public void launchApplication(String url) {
		try {
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
			driver.get(url);
			driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reportTestCaseName(String name) {
		reportTestCaseName = name;
	}

	public String reportTestCaseName() {
		return reportTestCaseName;
	}

	public void ifTestCasesFailsExceptionMsg(String name) {
		ifTestCasesFailsExceptionMsg = name;
	}

	public String ifTestCasesFailsExceptionMsg() {
		return ifTestCasesFailsExceptionMsg;
	}

	public int verifyAssert() {
		if (result == 1) {
			result = 1;
		} else if (result == 0) {
		} else {
			result = 2;
		}
		return result;
	}

	public void attachScenarioStepScreenshotInReport(String reportTestCaseName) throws IOException {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		byte[] byteArray = FileUtils.readFileToByteArray(screenshot);
		scenario.attach(byteArray, "image/png", reportTestCaseName);
	}

	public void getScreenshot(String result) {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File("./screenshot//" + result + "screenshot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void clickOn(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			try {
				fluentwait.until(ExpectedConditions.visibilityOf(element));
				element.click();
			} catch (ElementClickInterceptedException e) {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
				wait.until(ExpectedConditions.visibilityOf(element));
				element.click();
			} catch (ElementNotInteractableException e) {
				WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
				wait.until(ExpectedConditions.visibilityOf(element));
				js.executeScript("arguments[0].scrollIntoView();", element);
				element.click();
			} catch (StaleElementReferenceException e) {
				Thread.sleep(2000);
				element.click();
			}
		} catch (ElementClickInterceptedException e) {
			js.executeScript("arguments[0].click();", element);
		}
	}

	public void jsClickOn(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].click();", element);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			js.executeScript("arguments[0].click();", element);
		}
	}

	public void actionsClickOn(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			actions.click(element).build().perform();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			actions.click(element).build().perform();
		} catch (ElementClickInterceptedException e) {
			js.executeScript("arguments[0].click();", element);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].scrollIntoView();", element);
			actions.click(element).build().perform();
		}
	}

	public void actionsDoubleClickOn(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			actions.doubleClick(element).build().perform();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			actions.doubleClick(element).build().perform();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].scrollIntoView();", element);
			actions.doubleClick(element).build().perform();
		}
	}

	public boolean elementIsDisplayed(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		boolean result = false;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			result = element.isDisplayed();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			result = element.isDisplayed();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			result = element.isDisplayed();
		}
		return result;
	}

	public boolean elementIsEnabled(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		boolean result = false;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			result = element.isEnabled();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			result = element.isEnabled();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			result = element.isEnabled();
		}
		return result;
	}

	public boolean checkBoxIsSelected(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		boolean selected = false;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			selected = element.isSelected();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			selected = element.isSelected();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			selected = element.isSelected();
		}
		return selected;
	}

	public String getElementText(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		String text = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			text = element.getText();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			text = element.getText();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			text = element.getText();
		}
		return text;
	}

	public String getElementAttribute(WebElement element, String attributeKey) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		String text = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			text = element.getDomAttribute(attributeKey);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			text = element.getDomAttribute(attributeKey);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			text = element.getDomAttribute(attributeKey);
		}
		return text;
	}

	public void sendKeys(WebElement element, String value) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			element.sendKeys(value);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			element.sendKeys(value);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].scrollIntoView();", element);
			js.executeScript("arguments[0].click();", element);
			js.executeScript("arguments[0].value='" + value + "';", element);
		}
	}

	public void actionsSendKeys(WebElement element, String value) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			actions.sendKeys(element, value).build().perform();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			actions.sendKeys(element, value).build().perform();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].scrollIntoView();", element);
			js.executeScript("arguments[0].click();", element);
			js.executeScript("arguments[0].value='" + value + "';", element);
		}
	}

	public void jsSendKeys(WebElement element, String value) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].click();", element);
			js.executeScript("arguments[0].value='" + value + "';", element);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			js.executeScript("arguments[0].click();", element);
			js.executeScript("arguments[0].value='" + value + "';", element);
		}
	}

	public void keysSend(WebElement element, Keys value) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		JavascriptExecutor js = (JavascriptExecutor) driver;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			element.sendKeys(value);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			element.sendKeys(value);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			js.executeScript("arguments[0].scrollIntoView();", element);
			js.executeScript("arguments[0].click();", element);
			js.executeScript("arguments[0].value='" + value + "';", element);
		}
	}

	public void actionsKeysSend(WebElement element, Keys value) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		Actions actions = new Actions(driver);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			actions.keyDown(element, value).keyUp(element, value).build().perform();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			actions.keyDown(element, value).keyUp(element, value).build().perform();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			actions.keyDown(element, value).keyUp(element, value).build().perform();
		}
	}

	public void webElementClear(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			element.clear();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			element.clear();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			element.clear();
		}
	}

	public void selectByText(WebElement element, String text) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			select.selectByVisibleText(text);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			select.selectByVisibleText(text);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			select.selectByVisibleText(text);
		}
	}

	public void selectByValue(WebElement element, String value) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			select.selectByValue(value);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			select.selectByValue(value);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			select.selectByValue(value);
		}
	}

	public void selectByIndex(WebElement element, int index) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			select.selectByIndex(index);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			select.selectByIndex(index);
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			select.selectByIndex(index);
		}
	}

	public String getSelectedText(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		String text = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			text = select.getFirstSelectedOption().getText();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			text = select.getFirstSelectedOption().getText();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			text = select.getFirstSelectedOption().getText();
		}
		return text;
	}

	public WebElement getSelectedElement(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		WebElement selectedElement;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			selectedElement = select.getFirstSelectedOption();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			selectedElement = select.getFirstSelectedOption();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			selectedElement = select.getFirstSelectedOption();
		}
		return selectedElement;
	}

	public List<String> getSelectedAllValuesText(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		List<String> list = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			List<WebElement> elements = select.getAllSelectedOptions();
			for (int i = 0; i < elements.size(); i++) {
				list.add(elements.get(i).getText());
			}
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			List<WebElement> elements = select.getAllSelectedOptions();
			for (int i = 0; i < elements.size(); i++) {
				list.add(elements.get(i).getText());
			}
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			List<WebElement> elements = select.getAllSelectedOptions();
			for (int i = 0; i < elements.size(); i++) {
				list.add(elements.get(i).getText());
			}
		}
		return list;
	}

	public List<WebElement> getSelectedAllElement(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		List<WebElement> elements = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			elements = select.getAllSelectedOptions();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			elements = select.getAllSelectedOptions();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			elements = select.getAllSelectedOptions();
		}
		return elements;
	}

	@SuppressWarnings("null")
	public List<String> getAllAvailableOptionsTextInSelect(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		List<String> list = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			List<WebElement> elements = select.getOptions();
			for (int i = 0; i < elements.size(); i++) {
				list.add(elements.get(i).getText());
			}
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			List<WebElement> elements = select.getOptions();
			for (int i = 0; i < elements.size(); i++) {
				list.add(elements.get(i).getText());
			}
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			List<WebElement> elements = select.getOptions();
			for (int i = 0; i < elements.size(); i++) {
				list.add(elements.get(i).getText());
			}
		}
		return list;
	}

	public List<WebElement> getAllAvailableOptionsElementInSelect(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		List<WebElement> elements = null;
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			Select select = new Select(element);
			elements = select.getOptions();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			Select select = new Select(element);
			elements = select.getOptions();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			Select select = new Select(element);
			elements = select.getOptions();
		}
		return elements;
	}

	public void elementMouseMove(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		Actions actions = new Actions(driver);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			actions.moveToElement(element).build().perform();
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			actions.moveToElement(element).build().perform();
		} catch (ElementNotInteractableException e) {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
			actions.moveToElement(element).build().perform();
		}
	}

	public void scrollToElement(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
		}
	}

	public void scrollToWebPageEnd() throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
	}

	public void scrollToUp() throws InterruptedException {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -250);");
	}

	public void highLightElement(WebElement element) throws InterruptedException {
		FluentWait<WebDriver> fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(3))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
		try {
			fluentwait.until(ExpectedConditions.visibilityOf(element));
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].setAttribute('style','background:yellow;border:5px solid green');", element);
		} catch (StaleElementReferenceException e) {
			Thread.sleep(2000);
			((JavascriptExecutor) driver).executeScript(
					"arguments[0].setAttribute('style','background:yellow;border:5px solid green');", element);
		}
	}

	public String getCurrentDate(String pattern) {
		String localDate = LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
		return localDate;
	}

	public String propertyFileReader(String key) throws IOException {
		Properties properties = new Properties();
		File file = new File(System.getProperty("user.dir") + "\\src\\test\\resources\\extent.properties");
		FileReader filereader = new FileReader(file);
		properties.load(filereader);
		String value = properties.getProperty(key);
		return value;
	}

	public void openCucumberHTMLReport() {
		try {
			String htmlPath = propertyFileReader("extent.reporter.spark.out");
			String[] split = htmlPath.split("/");
			File file = new File(System.getProperty("user.dir") + "\\" + split[0] + "\\" + split[1]);
			Desktop desktop = Desktop.getDesktop();
			if (file.exists()) {
				desktop.open(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openCucumberPDFReport() {
		try {
			String htmlPath = propertyFileReader("extent.reporter.pdf.out");
			String[] split = htmlPath.split("/");
			File file = new File(System.getProperty("user.dir") + "\\" + split[0] + "\\" + split[1]);
			Desktop desktop = Desktop.getDesktop();
			if (file.exists()) {
				desktop.open(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fileUpload(String filePath) {
		try {
			Robot robot = new Robot();
			StringSelection stringSelection = new StringSelection(filePath);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
			Thread.sleep(4000);
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_V);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.keyRelease(KeyEvent.VK_V);
			Thread.sleep(2000);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getParentWindowID() {
		String parentWindowID = driver.getWindowHandle();
		return parentWindowID;
	}

	public void moveToChildWindow(String parentWindowID) {
		Set<String> windowHandles = driver.getWindowHandles();
		for (String string : windowHandles) {
			if (!parentWindowID.equals(string)) {
				driver.switchTo().window(string);
			}
		}
	}

	public void moveToParentWindow(String parentWindowID) {
		driver.switchTo().window(parentWindowID);
	}

}
