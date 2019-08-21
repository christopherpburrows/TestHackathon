package TestBase;

import java.lang.reflect.Method;

import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Reporting{
	
	
	
	private ExtentTest test;
	private ExtentReports extent;

	@BeforeSuite
	public void before() {
		 extent = new ExtentReports("target\\surefire-reports\\ExtentReport.html", true);
	}

	@BeforeMethod
	public void setUp(Method method) throws Exception {
		 test = extent.startTest(method.getClass().getSimpleName(),method.getClass().getEnclosingMethod().getName());
		 test.assignAuthor("Deepak");
		//Rest code will be generic for browser initiliazion.

	}

	@AfterSuite
	public void tearDownSuite() {
		// reporter.endReport();
		extent.flush();
		extent.close();
	}

	//Method for adding logs passed from test cases
	public void reportLog(String message) {    
		test.log(LogStatus.INFO, message);//For extentTest HTML report
	//	logger.info("Message: " + message);
		Reporter.log(message);

	}
}