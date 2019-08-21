package mobile;

import java.io.File;
import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.remote.MobileCapabilityType;

public class BaseClass {

	AppiumDriver<MobileElement>driver;
	@BeforeTest
	public void setup() {

		try {
			String chromeDriverPath = System.getProperty("user.dir")+File.separator+"Webdrivers"+File.separator+"chromedriver.exe";
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability(MobileCapabilityType.DEVICE_NAME, "Redmi");
			cap.setCapability(MobileCapabilityType.UDID, "355bcc42");
			cap.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
			cap.setCapability(MobileCapabilityType.VERSION, "7.1.2");
			cap.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60);
			cap.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");
			/*cap.setCapability("appPackage", "com.android.chrome");
			cap.setCapability("appActivity", "com.google.android.apps.chrome.Main");
			 */
			System.setProperty("webdriver.chrome.driver",chromeDriverPath);
			URL url = new URL("http://127.0.0.1:4723/wd/hub");
			driver = new AppiumDriver<MobileElement>(url , cap);
			
			//driver = new AndroidDriver<>(url,cap);
			//driver = new IOSDriver<>(url,cap);
			System.out.println("Application started");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Test
	public void sampletest() {
		System.out.println("Test Started");
	}
	
	
	@AfterTest
	public void teardown() {
		
		
		
	}
}
