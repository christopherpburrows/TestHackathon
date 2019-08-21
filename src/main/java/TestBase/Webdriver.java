package TestBase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import TestNgUtilities.Asserter;

public class Webdriver {

	public static enum BROWSERS {CHROME,FIREFOX,INTERNET_EXPLORER};
	/**
	 * To Launch the browser for execution.
	 * 
	 * 
	 */	
	public static WebDriver setBrowsers(BROWSERS browser){

		
		try {
			
			switch(browser) {

			case CHROME:
				return initialiseChromeDriver();

			case FIREFOX:
				
				break;

			case INTERNET_EXPLORER:
				return initialiseIEDriver();
				
				
			default:
				Asserter.verifyFail(" Unable to navigate to the requried url");
				return null;
			}
			Thread.sleep(Constants.sleepHigh);
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return null;
	}
	
	public static void navigatetoUrl(WebDriver driver,String url){

		try {

			driver.get(url);
		}catch(Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * To refresh the current browser.
	 * 
	 *
	 * @author Deepak.Shivakumar
	 * 
	 */
	protected static void refresh(WebDriver driver){
		
		driver.navigate().refresh();		
		try 
		{ 
			Alert alert = driver.switchTo().alert();
			alert.accept();	  
		}   
		catch (NoAlertPresentException Ex) 
		{ 
			//do nothing
		}  
		finally{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				//do nothing
			}
		}
	}

	
	/**
	 * To close the current driver browsing and launch the new browser for execution.
	 * 
	 * @return returns the current driver in use for execution.
	 * 
	 * @author Deepak.Shivakumar
	 * 
	 */	
	public static WebDriver closeNLaunchBrowser(WebDriver driver,BROWSERS browser) {
		driver.close();
		setBrowsers(browser);
		return driver;
	}
	
	
	public static void maximizeWindow(WebDriver driver) {
		driver.manage().window().maximize();
	}
	
	public static void closeDriver(WebDriver driver) {
		TestSetup.testcontext.get().setWebDriver(null);
		driver.close();
	}
	
	public static void quit() {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * To initialize the Chrome browser for execution.
	 * 
	 * @author Deepak.Shivakumar
	 * 
	 */	
	@SuppressWarnings("deprecation")
	private static WebDriver initialiseChromeDriver() {
		
		WebDriver driver = null; 
		 try{

			 String chromeDriverPath = System.getProperty("user.dir")+File.separator+"Webdrivers"+File.separator+"chromedriver.exe";
			 System.setProperty("webdriver.chrome.driver", chromeDriverPath);
			 DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			 ChromeOptions options = new ChromeOptions();
			 options.addArguments("--disable-extensions");
			 options.addArguments("--disbale-notifications");
			 options.addArguments("--enable-automation");
			 options.addArguments("disable-infobars");
			 options.addArguments("--disable-save-password-buble");
			 options.addArguments("test-type");
			 Map<String, Object> prefs = new HashMap<String, Object>();
			 prefs.put("credentials_enable_service", false);
			 prefs.put("profile.password_manager_enabled", false);
			 options.setExperimentalOption("prefs", prefs);
			 capabilities.setCapability("chrome.binary",chromeDriverPath);
			 capabilities.setCapability(ChromeOptions.CAPABILITY,options);						
			 driver = new ChromeDriver(capabilities);
			 Asserter.verifyTrue(driver!=null,"Unable to intitalize the driver");			
		 } catch (Exception e) {
			Asserter.verifyFail("Unable to Launch the browser",e);
		}
		return driver;
	}

	/**
	 * To initialize the IE browser for execution.
	 * 
	 * @author Deepak.Shivakumar
	 * 
	 */	
	@SuppressWarnings("deprecation")
	private static WebDriver initialiseIEDriver() {

		WebDriver driver = null;

		try {
			String	ieDriverPath = 	System.getProperty("user.dir")+File.separator+"IEServer.exe";
			System.setProperty("webdriver.ie.driver",ieDriverPath);
			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
			capabilities.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
			capabilities.setCapability("ie.binary", "C://Program Files (x86)\\Internet Explorer\\iexplore.exe");
			capabilities.setCapability("requireWindowFocus", false);				
			driver = new InternetExplorerDriver(capabilities);
			Asserter.verifyTrue(driver!=null,"Unable to intitalize the driver");			
		} catch (Exception e) {
			Asserter.verifyFail("Unable to Launch the browser",e);
		}		
		return driver;
	}

	public static WebDriver getDriver() {
		
		
		return TestSetup.testcontext.get().getWebDriver();
	}
	
	/**
	 * To refresh the current browser.
	 * 
	 *
	 * @author Deepak.Shivakumar
	 * 
	 */
	public static void refresh(){
		
		
		
		getDriver().navigate().refresh();		
		try 
		{ 
			Alert alert = getDriver().switchTo().alert();
			alert.accept();	  
		}   
		catch (NoAlertPresentException Ex) 
		{ 
			//do nothing
		}  
		finally{
			try {
				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
			} catch (InterruptedException e) {
				//do nothing
			}
		}
	}

	

}
