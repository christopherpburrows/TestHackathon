package WebPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import TestBase.TestSetup;

public class Google {

	WebDriver driver;
	private WebDriverWait wait;
	
	@FindBy(xpath="//input[@title='Search']")
	WebElement searchItem;

	@FindBy(xpath="//input[@value='Google Search']")
	WebElement googleSearchButton;


	public  Google() {

		this.driver = TestSetup.testcontext.get().getWebDriver();
		wait= new WebDriverWait(driver, 30);
		PageFactory.initElements(driver, this);

	}

	public Google enterSearchItem(String searchItems) {

		 try {
			wait.until(ExpectedConditions.visibilityOf(searchItem)).sendKeys(searchItems);
		} catch (Exception e) {
			Assert.fail("Unable to enter search item"+e);
		}
		 return this;
		

	}

	public GoogleResultsPage clickGoogleSearch() {

		try {
			wait.until(ExpectedConditions.visibilityOf(googleSearchButton)).click();
		} catch (Exception e) {
			Assert.fail("Unable to click Google search "+e);
		}
		return new GoogleResultsPage();
		
	}

	

}
