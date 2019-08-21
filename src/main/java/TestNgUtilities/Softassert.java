package TestNgUtilities;

import java.util.Map;

import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.asserts.IAssert;
import org.testng.asserts.IAssertLifecycle;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;


import TestBase.TestSetup;

public class Softassert extends SoftAssert {



	@Override
	protected void doAssert(IAssert<?> a){
		Map<AssertionError, IAssert<?>> m_errors = Maps.newLinkedHashMap();
		onBeforeAssert(a);
		try {
			a.doAssert();
			onAssertSuccess(a);
		} catch (AssertionError ex) {	    
			onAssertFailure(a, ex);
			ITestResult result = Reporter.getCurrentTestResult();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Asserter.verifyFail(e+".");
			}
			Reporter.log("Assertion Failed due to "+ex, true);
			new ScreenShotListeners().onTestFailure(result);
			result.setAttribute("softAssert", ex.getCause());;

			m_errors.put(ex, a);
			TestSetup.testcontext.get().setM_errors(m_errors);

		} finally {
			onAfterAssert(a);
		}
	}


	public void assertAll() {

		if (!TestSetup.testcontext.get().getM_errors().isEmpty()) {
			StringBuilder sb = null;
			if(TestSetup.testcontext.get().getM_errors().keySet().size()>1){
				sb = new StringBuilder("Test case failed due to Multiple Assertion failures");
			}else{
				sb = new StringBuilder(TestSetup.testcontext.get().getM_errors().keySet().toString());
			}

			boolean first = true;
			for (Map.Entry<AssertionError, IAssert<?>> ae : TestSetup.testcontext.get().getM_errors().entrySet()) {
				if (first) {
					first = false;
				} else {
					sb.append(",");
				}
				sb.append("\n\t");
				sb.append(ae.getKey().getMessage());
			}
			throw new AssertionError(sb.toString());
		}
	}

	public abstract class Assertion implements IAssertLifecycle {
		protected void doAssert(IAssert<?> assertCommand) {
			onBeforeAssert(assertCommand);
			try {
				executeAssert(assertCommand);
				onAssertSuccess(assertCommand);
			} catch(AssertionError ex) {
				onAssertFailure(assertCommand, ex);
				throw ex;
			} finally {
				onAfterAssert(assertCommand);
			}
		}

	}
}
