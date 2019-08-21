package WebPages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import TestBase.TestSetup;

public class GoogleResultsPage {

	WebDriver driver;
	private WebDriverWait wait;

	
	public GoogleResultsPage() {
		driver = TestSetup.testcontext.get().getWebDriver();
		wait= new WebDriverWait(driver, 30);
		PageFactory.initElements(driver, this);
	}
	
	
	@FindBy(xpath="(//h3)[1]")
	WebElement links;
	
	public LinkResultPage clickLink() {
		
		try {
			wait.until(ExpectedConditions.visibilityOf(links)).click();
		} catch (Exception e) {			
			Assert.fail("Unable to click link in the google result "+e);
		}
		return new LinkResultPage();
		
	}

	
	
}
