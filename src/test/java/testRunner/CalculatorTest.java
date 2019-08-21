package testRunner;

import java.net.URL;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

public class CalculatorTest {

	public static AppiumDriver<MobileElement> driver;
	
	
	public static void main(String[] args) {
		
		try {
			openCalculator();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	public static void openCalculator() throws Exception {
		
		DesiredCapabilities cap = new DesiredCapabilities();
		
		cap.setCapability("deviceName", "Redmi");
		cap.setCapability("udid", "355bcc42");
		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "7.1.2");
		cap.setCapability("appPackage", "com.android.chrome");
		cap.setCapability("appActivity", "com.google.android.apps.chrome.Main");
		/*cap.setCapability("autoGrantPermissions", true);
		cap.setCapability("noReset", true);
		cap.setCapability("android:exported", true);*/
		URL url = new URL("http://127.0.0.1:4723/wd/hub");
		driver = new AppiumDriver<MobileElement>(url , cap);
		
		System.out.println("Application started");
		
	}
	
}
