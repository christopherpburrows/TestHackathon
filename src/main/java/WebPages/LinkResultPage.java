package WebPages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import Helper.AppConstants;
import TestBase.TestSetup;

public class LinkResultPage {

	WebDriver driver;
	private WebDriverWait wait;
	
	
	public LinkResultPage() {
		driver = TestSetup.testcontext.get().getWebDriver();
		wait= new WebDriverWait(driver, 30);
		PageFactory.initElements(driver, this);
	}
	
	@FindAll(value = { @FindBy(xpath="//div[@id='main']//h3") })
	List<WebElement> movies;
	@FindAll(value = { @FindBy(xpath="//div[@id='main']//h3/../p[contains(.,'Director')]/a[1]") })
	List<WebElement> director;
	@FindAll(value= {@FindBy(xpath="//h3/a")})
	List<WebElement> links;
	
	public void addmoviesDetails() {

		try {
			for(WebElement movie : movies) {
				
				AppConstants.movies.add(movie.getText().split("\\.")[1].split("\\(")[0].trim());

			}

			for(WebElement directors : director) {

				AppConstants.directors.add(directors.getText());

			}
			for(WebElement link : links) {

				AppConstants.links.add(link.getAttribute("href"));

			}
		} catch (Exception e) {
			Assert.fail("Unable to add movie details "+e);
		}

	}


	public moviePage selectLinkOpenNewTab() {
		
		try {
			String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN); 
			String xpaths = "(//h3[contains(.,'movieName')]/a)[1]".replace("movieName", TestSetup.testcontext.get().getFilmNames());
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(xpaths)))).sendKeys(selectLinkOpeninNewTab);
			List<String> tabs = new ArrayList<String> (driver.getWindowHandles());
			TestSetup.testcontext.get().setWindow(tabs.get(tabs.size()-1));
			TestSetup.testcontext.get().setParentWindow(tabs.get(0));
		} catch (Exception e) {
			Assert.fail("Unable to select link and open new tab "+e);
		}
		return new moviePage();
	}
	
}
