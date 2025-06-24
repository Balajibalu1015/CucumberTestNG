package org.pageObjects;

import java.io.Console;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.util.Base64;

import javax.crypto.Cipher;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.base.TestBase;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends TestBase {

	public WebDriver driver;
	private WebDriverWait wait;
	private FluentWait<WebDriver> fluentwait;

	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
		wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		fluentwait = new FluentWait<WebDriver>(driver).withTimeout(Duration.ofSeconds(30))
				.pollingEvery(Duration.ofSeconds(1)).ignoring(Throwable.class);
	}

	// user able to click on the Web inputs hyperlink
	@FindBy(xpath = "//a[text()='Web inputs']")
	private WebElement webInputs;

	public void clickOnWebInputs() throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		fluentwait.until(ExpectedConditions.visibilityOf(webInputs));
		clickOn(webInputs);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}

	// user able to enter the Number as input
	@FindBy(id = "input-number")
	private WebElement number;

	public void enterNumber(String value) throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		fluentwait.until(ExpectedConditions.visibilityOf(number));
		sendKeys(number, value);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}

	// user able to enter the user name as text
	@FindBy(name = "input-text")
	private WebElement userName;

	public void enterUserName(String value) throws InterruptedException {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		fluentwait.until(ExpectedConditions.visibilityOf(userName));
		sendKeys(userName, value);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
	}

}
