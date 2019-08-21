package TestNgUtilities;
	
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import TestBase.TestSetup;
				
public class ScreenShotListeners extends TestListenerAdapter {

	
	@Override
	public void onTestFailure(ITestResult result) {
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
		String methodName = result.getName();
	//	String timeStamp = new SimpleDateFormat("yyyy"+File.separator+"MMM"+File.separator+"dd hh_mm_aa").format(new Date());
		if(!result.isSuccess()){	
			
			
			WebDriver webDriverAttribute = (WebDriver)TestSetup.testcontext.get().getWebDriver();
			
			
			File scrFile = ((TakesScreenshot)webDriverAttribute).getScreenshotAs(OutputType.FILE);
			try {
			//	String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()+File.separator+ "ResultOutput"+File.separator+timeStamp.split(" ")[0]+File.separator+timeStamp.split(" ")[1];
				String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()+"/target/surefire-reports/failure_screenshots";
				
				File destFile = new File((String) reportDirectory+"/failure_screenshots/"+methodName+"_"+formater.format(calendar.getTime())+".png");
				FileUtils.copyFile(scrFile, destFile);
				Reporter.log("<a href='"+ destFile.getAbsolutePath() + "'> <img src='"+ destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}			
	}
}
			
		
	
		