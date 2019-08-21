package TestNgUtilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

	
	public class TestNGCustomReportListener implements IReporter{

		private PrintWriter writer;
		private int m_row;
		private static Integer m_summethodIndex =0,m_detmethodIndex=0;
		
		private String reportTitle= "Radar 4.1 GUI Automation Report";
	//	private String reportFileName = "Automation_report_";
		private static int m_SuitetestIndex =0 , m_dettestIndex=0;
		private static String timeStamp = new SimpleDateFormat("yyyy"+File.separator+"MMM"+File.separator+"dd hh_mm_aa").format(new Date());
		
		protected PrintWriter createWriter(String outdir) throws IOException {
			new File(outdir).mkdirs();
		return	new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir,"Custom-Report.html" ))));	
	//	reportFileName+timeStamp.split(" ")[1]+".html"
		}

		/**
		 * Creates a table showing the highlights of each test method with links to
		 * the method details
		 */
		protected void generateMethodSummaryReport(List<ISuite> suites) {

			startResultSummaryTable("methodOverview");
			for (ISuite suite : suites) {
				if(!suite.getName().contains("Automation Suite")){
					if (suites.size() >= 1) {
						titleRow(suite.getName(), 5);
					}

					Map<String, ISuiteResult> r = suite.getResults();
					for (ISuiteResult r2 : r.values()) {
						ITestContext testContext = r2.getTestContext();
						String testName = testContext.getName();
						//m_testIndex = testIndex;
						resultSummary(suite, testContext.getFailedConfigurations(), testName, "failed", " (configuration methods)");
						resultSummary(suite, testContext.getFailedTests(), testName, "failed", "");
						resultSummary(suite, testContext.getSkippedConfigurations(), testName, "skipped", " (configuration methods)");
						resultSummary(suite, testContext.getSkippedTests(), testName, "skipped", "");
						resultSummary(suite, testContext.getPassedTests(), testName, "passed", "");
					}
				}
			}
			writer.println("</thead>");
			writer.println("</table>");
		}
	   
		/** Creates a section showing known results for each method */
		protected void generateMethodDetailReport(List<ISuite> suites) {

			for (ISuite suite : suites) {
				if(!suite.getName().contains("Automation Suite")){
					Map<String, ISuiteResult> r = suite.getResults();
					for (ISuiteResult r2 : r.values()) {
						ITestContext testContext = r2.getTestContext();
						if (r.values().size() > 0) {
							++m_dettestIndex;
							writer.println("<h1 id=\"test"+m_dettestIndex+"\">"+testContext.getName()+" </h1>");
							//						writer.println("<h2>" + testContext.getName() + "</h2>");
						}
						resultDetail(testContext.getFailedConfigurations());
						resultDetail(testContext.getFailedTests());
						resultDetail(testContext.getSkippedConfigurations());
						resultDetail(testContext.getSkippedTests());
						resultDetail(testContext.getPassedTests());
					}
				}
			}
		}

		/**
		 * @param tests
		 */
		@SuppressWarnings({ "resource", "deprecation" })
		private void resultSummary(ISuite suite, IResultMap tests, String testname, String style, String details) {
			
			if (tests.getAllResults().size() > 0) {
				StringBuffer buff = new StringBuffer();
				String lastClassName = "";
				int mq = 0;
				int cq = 0;
				for (ITestNGMethod method : getMethodSet(tests, suite)) {
					++m_summethodIndex;
					ITestClass testClass = method.getTestClass();
					String className = testClass.getName();
					if (mq == 0) {
						String id = (m_summethodIndex == null ? null : "MethodSummaryTest"+ Integer.toString(m_summethodIndex));
						titleRow(testname + " &#8212; " + style + details, 5, id);						

					}
					if (!className.equalsIgnoreCase(lastClassName)) {
						if (mq > 0) {
							cq += 1;
							writer.print("<tr style=\"background-color:#85C1E9; color:black;\" class=\"\"" + style + (cq % 2 == 0 ? "even" : "odd") + ">" + "<td");
							if (mq > 1) {
								writer.print(" rowspan=\"" + mq + "\"");
							}
							writer.println(">" + lastClassName + "</td>" + buff);
						}
						mq = 0;
						buff.setLength(0);
						lastClassName = className;
					}
					Set<ITestResult> resultSet = tests.getResults(method);
					long end = Long.MIN_VALUE;
					long start = Long.MAX_VALUE;
					long startMS=0;
					String firstLine="";

					for (ITestResult testResult : tests.getResults(method)) {
						if (testResult.getEndMillis() > end) {
							end = testResult.getEndMillis()/1000;
						}
						if (testResult.getStartMillis() < start) {
							startMS = testResult.getStartMillis();
							start =startMS/1000;
						}

						Throwable exception=testResult.getThrowable();
						boolean hasThrowable = exception != null;
						if(hasThrowable){
							String str = Utils.stackTrace(exception, true)[0];
							Scanner scanner = new Scanner(str);
							firstLine = scanner.nextLine();
						}
					}
					DateFormat formatter = new SimpleDateFormat("hh:mm:ss");
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(startMS);

					mq += 1;
					if (mq > 1) {
						buff.append("<tr class=\"\" style=\"background-color:#85C1E9; color:black;\"" + style + (cq % 2 == 0 ? "odd" : "even") + ">");
					}
					String description = method.getDescription();
					String testInstanceName = resultSet.toArray(new ITestResult[] {})[0].getTestName();
					buff.append("<td><a href=\"#MethodsSummaryIndex"+ m_summethodIndex+"\""+ ">"+ qualifiedName(method)+ " "
							+ (description != null && description.length() > 0 ? "(\""+ description + "\")": "") 
							+ "</a>"+ (null == testInstanceName ? "" : "<br>("+ testInstanceName + ")") + "</td>"
							+ "<td class=\"numi\" style=\"text-align:center;padding-right:2em\">" + firstLine+"<br/></td>"
							+ "<td style=\"text-align:center\">" + formatter.format(calendar.getTime()) + "</td>"  
							+ "<td style=\"text-align:center\" class=\"numi\">" + timeConversion(end - start) + "</td>" + "</tr>");
				}
				if (mq > 0) {
					cq += 1;
					writer.print("<tr class=\"\" style=\"background-color:#85C1E9; color:black;\""+ style + (cq % 2 == 0 ? "even" : "odd") + ">" + "<td");
					if (mq > 1) {
						writer.print(" rowspan=\"" + mq + "\"");
					}
					writer.println(">" + lastClassName + "</td>" + buff);
				}
			}
		}
	    
		
		private String timeConversion(long seconds) {

		    final int MINUTES_IN_AN_HOUR = 60;
		    final int SECONDS_IN_A_MINUTE = 60;

		    int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
		    seconds -= minutes * SECONDS_IN_A_MINUTE;

		    int hours = minutes / MINUTES_IN_AN_HOUR;
		    minutes -= hours * MINUTES_IN_AN_HOUR;

		    return prefixZeroToDigit(hours) + ":" + prefixZeroToDigit(minutes) + ":" + prefixZeroToDigit((int)seconds);
		}
		
		private String prefixZeroToDigit(int num){
			int number=num;
			if(number<=9){
				String sNumber="0"+number;
				return sNumber;
			}
			else
				return ""+number;
			
		}
		
		/** Starts and defines columns result summary table */
		private void startResultSummaryTable(String style) {
			tableStart("Methods Summary Report");			
			writer.print("</tr></thead></table>");
			writer.print("<table style=\"width:100%\"><thead>");
			writer.println("<tr style=\"background-color:#85C1E9; color:black\">"
					+ "<th><b>Class</b></th>"
					+ "<th><b>Method</th>"
					+ "<th>Exception Info</b></th>"
					+ "<th><b>Start Time </b></th>"
					+ "<th><b>Execution Time</b><br/>(hh:mm:ss)</th></tr>");
			writer.print("</thead>");
			m_row = 0;
			
		}

		private String qualifiedName(ITestNGMethod method) {
			StringBuilder addon = new StringBuilder();
			String[] groups = method.getGroups();
			int length = groups.length;
			if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
				addon.append("(");
				for (int i = 0; i < length; i++) {
					if (i > 0) {
						addon.append(", ");
					}
					addon.append(groups[i]);
				}
				addon.append(")");
			}

			return "<b>" + method.getMethodName() + "</b> " + addon;
		}

		private synchronized void resultDetail(IResultMap tests) {
			
			Set<ITestResult> testResults=tests.getAllResults();
			List<ITestResult> testResultsList = new ArrayList<ITestResult>(testResults);
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
			Collections.sort(testResultsList, new TestResultsSorter());
			for (ITestResult result : testResultsList) {
				ITestNGMethod method = result.getMethod();
				++m_detmethodIndex;
				String cname = method.getTestClass().getName();
				writer.println("<h5 id=\"MethodsSummaryIndex" + m_detmethodIndex +"\">" + cname + ":"+ method.getMethodName() + "</h5>");
				Set<ITestResult> resultSet = tests.getResults(method);
				generateResult(result, method, resultSet.size());
				writer.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
			}
		}

		private void generateResult(ITestResult ans, ITestNGMethod method,int resultSetSize) {
			@SuppressWarnings("unused")
			Object[] parameters = ans.getParameters();
			
			tableStart("Methods Detailed Report");
			/*	if (hasParameters) {

				/*writer.print("<tr class=\"param\">");
				for (int x = 1; x <= parameters.length; x++) {
					writer.print("<th>Param." + x + "</th>");
				}
				writer.println("</tr>");
				writer.print("<tr class=\"param stripe\">");
				for (Object p : parameters) {
					writer.println("<td>" + Utils.escapeHtml(Utils.toString(p))
							+ "</td>");
				}
				writer.println("</tr>");
			}*/
			List<String> msgs = Reporter.getOutput(ans);
			boolean hasReporterOutput = msgs.size() > 0;
			Throwable exception = ans.getThrowable();
			boolean hasThrowable = exception != null;
			
			
			
			if (hasReporterOutput || hasThrowable) {
//				if (hasParameters) {
			/*	writer.print("</thead>");
				writer.print("</table>"); */
					writer.print("<table style=\"width:100%;\">");
					writer.print("<col style=\"width:10%\"> "
							+ "<col style=\"width:20%\">"
							+ "<col style=\"width:20%\">"
							+ "<col style=\"width:10%\">"
							+ "<col style=\"width:20%\">"
							+ "<col style=\"width:20%\">");
					writer.print("<thead>");
					writer.print("<tr style=\"background-color:#C8C8FA;color:black\">");
					String [] col = {"Test Case ID","Expected", "Actual","Status","Screenshot","Error" };
					for (String p : col) {
						writer.println("<th>" + Utils.escapeHtml(Utils.toString(p))
						+ "</th>");
					}
					writer.print("</tr>");
					writer.print("</thead >");
				} else {
					writer.println("<div>");
				}
					if (hasReporterOutput) {
						writer.print("<tbody style=\"width:100%;\">");
						boolean softAssert =false;

						for(int i = 0 ; i<msgs.size();++i){
							writer.println("<tr>");
							if(i == 0){							
								writer.println("<td>"+method.getMethodName() + "</td>");

							}else{
								writer.println("<td>"+"" + "</td>");

							}
							//writes always the expected one
							if(!msgs.get(i).contains("<a href=")){
								writer.println("<td>"+msgs.get(i) + "</td>");									
								softAssert =true;
								++i;
							}else{
								writer.println("<td>"+"Please log as per Framework Standards" + "</td>");
								softAssert =false;
							}							
							if(softAssert){				
								if(i<msgs.size()){
									if(i != msgs.size()-1 ){
										if(msgs.get(i+1).contains("<a href=") && (i+1) == msgs.size()-1){
											writer.println("<td>"+msgs.get(i) + "</td>");
											writer.println("<td bgcolor=\"#1CD84F\"> Pass </td>");
											writer.println("<td>"+"" + "</td>");
											writer.println("<td>"+"" + "</td>");
											++i;
											
										}else if(msgs.get(i+1).contains("<a href=")){
											writer.println("<td>"+msgs.get(i) + "</td>");
											writer.println("<td bgcolor=\"#FF0000\"> Failed </td>");
											++i;
											//adding screenshot
											writer.println("<td>"+msgs.get(i) + "</td>");											
											writer.println("<td></td>");																																									
											++i;
										}
										
										else if(msgs.get(i).contains("<a href=")){
											boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
											writer.println("<td>");									
											generateExceptionReport(exception, method);									
											writer.println("</td>");
											writer.println("<td bgcolor=\"#E40505\">"+(wantsMinimalOutput ? "Expected Exception": "Failure")+"</td>");
											writer.println("<td>"+msgs.get(i) + "</td>");
											writer.println("<td>");										
											generateStackTraceReport(exception, method);									
											writer.println("</td>");
											++i;
										}else{
											writer.println("<td>"+msgs.get(i) + "</td>");								
											writer.println("<td bgcolor=\"#1CD84F\"> Pass </td>");
											writer.println("<td>"+"" + "</td>");
											writer.println("<td>"+"" + "</td>");
										}
										
										
									}else{
										 if(msgs.get(i).contains("<a href=")){
											boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
											writer.println("<td>");										
											generateExceptionReport(exception, method);									
											writer.println("</td>");
											writer.println("<td bgcolor=\"#E40505\">"+(wantsMinimalOutput ? "Expected Exception": "Failure")+"</td>");
											writer.println("<td>"+msgs.get(i) + "</td>");
											writer.println("<td>");										
											generateStackTraceReport(exception, method);									
											writer.println("</td>");
										}else{
											writer.println("<td>"+msgs.get(i) + "</td>");								
											writer.println("<td bgcolor=\"#1CD84F\"> Pass </td>");
											writer.println("<td>"+"" + "</td>");
											writer.println("<td>"+"" + "</td>");
										}
									}
									softAssert =false;
								}

							}
							writer.println("</tr>");
						}
						writer.print("</tbody>");
					}

					/*if (hasThrowable && errors.size()>1) {
						writer.println("<tr>");
						boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;

						writer.println("<td>"+" " + "</td>");
						writer.println("<td>"+errors.get(0)+"</td>");
						writer.println("<td>");										
						generateExceptionReport(exception, method);									
						writer.println("</td>");

						writer.println("<td bgcolor=\"#FF0000\">"+(wantsMinimalOutput ? "Expected Exception": "Failure")+"</td>");
						writer.println("<td>"+errors.get(1)+"</td>");
						
						writer.println("</tr>");

					}	*/			
				/*if (hasParameters) {
					writer.println("</td></tr>");
				} else {
					writer.println("</div>");
				}*/
			
			/*if (hasParameters) {
				writer.println("</table>");
			}*/
			writer.println("</table>");
		}

		protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {	
			if(exception == null){
				writer.print("Exception occured in method"+ method);
			}else if(exception.getMessage() != null){
				writer.print(exception.getMessage());	
			}else{
				writer.print("Exception occured in method"+ method);
			}
		}
		@SuppressWarnings("deprecation")
		protected void generateStackTraceReport(Throwable exception, ITestNGMethod method) {
			
			writer.print(Utils.stackTrace(exception, true)[0]);	
			
		}
		/**
		 * Since the methods will be sorted chronologically, we want to return the
		 * ITestNGMethod from the invoked methods.
		 */
		private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {
			
			List<IInvokedMethod> r = Lists.newArrayList();
			List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
			for (IInvokedMethod im : invokedMethods) {
				if (tests.getAllMethods().contains(im	.getTestMethod())) {
					r.add(im);
				}
			}
			
			System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
			System.setProperty("java.util.Collections.useLegacyMergeSort", "true");
			Collections.sort(r,new TestSorter());
			List<ITestNGMethod> result = Lists.newArrayList();
			
			// Add all the invoked methods
			for (IInvokedMethod m : r) {
				for (ITestNGMethod temp : result) {
					if (!temp.equals(m.getTestMethod()))
						result.add(m.getTestMethod());
				}
			}
			
			// Add all the methods that weren't invoked (e.g. skipped) that we
			// haven't added yet
			Collection<ITestNGMethod> allMethodsCollection=tests.getAllMethods();
			List<ITestNGMethod> allMethods=new ArrayList<ITestNGMethod>(allMethodsCollection);
			Collections.sort(allMethods, new TestMethodSorter());
			
			for (ITestNGMethod m : allMethods) {
				if (!result.contains(m)) {
					result.add(m);
				}
			}
			return result;
		}

		
		public void generateSuiteSummaryReport(List<ISuite> suites) {
			writer.print("<h2 id=\"summary\"></h2>");
			tableStart("Suite Summary Report");
			writer.print("</tr></thead></table>");
			writer.print("<table style=\"width:100%\"><thead>");
			writer.print("<tr style=\"background-color:#85C1E9; color:black\"><b>");			
			tableColumnStart("Test");
			tableColumnStart("Test Cases<br/>Passed");
			tableColumnStart("# skipped");
			tableColumnStart("# failed");
			tableColumnStart("Start<br/>Time");
			tableColumnStart("End<br/>Time");
			tableColumnStart("Total Time<br/>(hh:mm:ss)");


			writer.println("</b></tr>");
			writer.print("</thead>");
			new DecimalFormat("#,##0.0");
			int qty_tests = 0;
			int qty_pass_m = 0;
			int qty_skip = 0;
			long time_start = Long.MAX_VALUE;
			int qty_fail = 0;
			long time_end = Long.MIN_VALUE;
			m_SuitetestIndex=1;
			for (ISuite suite : suites) {
				if(!suite.getName().contains("Automation Suite")){
					if (suites.size() >= 1) {
						titleRow(suite.getName(), 10);
					}
					Map<String, ISuiteResult> tests = suite.getResults();
					for (ISuiteResult r : tests.values()) {
						qty_tests += 1;
						ITestContext overview = r.getTestContext();

						startSummaryRow(overview.getName());
						int q = getMethodSet(overview.getPassedTests(), suite).size();
						qty_pass_m += q;
						summaryCell(q, Integer.MAX_VALUE);
						q = getMethodSet(overview.getSkippedTests(), suite).size();
						qty_skip += q;
						summaryCell(q, 0);
						q = getMethodSet(overview.getFailedTests(), suite).size();
						qty_fail += q;
						summaryCell(q, 0);

						SimpleDateFormat summaryFormat = new SimpleDateFormat("hh:mm:ss");
						summaryCell(summaryFormat.format(overview.getStartDate()),true);				
						writer.println("</td>");

						summaryCell(summaryFormat.format(overview.getEndDate()),true);
						writer.println("</td>");

						time_start = Math.min(overview.getStartDate().getTime(), time_start);
						time_end = Math.max(overview.getEndDate().getTime(), time_end);
						summaryCell(timeConversion((overview.getEndDate().getTime() - overview.getStartDate().getTime()) / 1000), true);

						writer.println("</tr>");
						m_SuitetestIndex++;
					}
				}
			}
					if (qty_tests > 1) {
						writer.println("<tr class=\"total\"><td>Total</td>");
						summaryCell(qty_pass_m, Integer.MAX_VALUE,"1CD84F");
						summaryCell(qty_skip, 0,"F58E0C");
						summaryCell(qty_fail, 0,"E40505");
						summaryCell(" ", true);
						summaryCell(" ", true);
						summaryCell(timeConversion(((time_end - time_start) / 1000)), true);
						writer.println("</tr>");
					}
				
			
			writer.println("</table>");
		}
		
		@SuppressWarnings("unused")
		private void summaryCell(String[] val) {
			StringBuffer b = new StringBuffer();
			for (String v : val) {
				b.append(v + " ");
			}
			summaryCell(b.toString(), true);
		}

		private void summaryCell(String v, boolean isgood) {
			writer.print("<td class=\"numi\"" + (isgood ? "" : "_attn") + "><center>" + v
					+ "</center></td>");
		}

		private void summaryCell(String v, boolean isgood, String colour) {
			writer.print("<td bgcolor=\"#"+colour+"\" class=\"numi\"" + (isgood ? "" : "_attn") + "><center>" + v
					+ "</center></td>");
		}
		
		
		private void startSummaryRow(String label) {
			m_row += 1;
			writer.print("<tr"
					+ (m_row % 2 == 0 ? " class=\"stripe\"" : "")
					+ "><td style=\"text-align:left;padding-right:2em\"><a href=\"#test" + m_SuitetestIndex+"\"><b>" + label + "</b></a>" + "</td>");
			
		}

		private void summaryCell(int v, int maxexpected) {
			summaryCell(String.valueOf(v), v <= maxexpected);
		}
		private void summaryCell(int v, int maxexpected, String colour) {
			summaryCell(String.valueOf(v), v <= maxexpected, colour);
		}
		private void tableStart(String tableHeader) {
			writer.println("<table style= \"width:100%;\">");
			writer.print("<thead>");
			writer.println("<tr style=\"background-color:#808080;color:white\"><th>"+tableHeader+"</th>");
			m_row = 0;
		}

		private void tableColumnStart(String label) {
			writer.print("<th>" + label + "</th>");
		}

		private void titleRow(String label, int cq) {
			titleRow(label, cq, null);
		}

		private void titleRow(String label, int cq, String id) {
			writer.print("<tr bgcolor=\"#C8C8FA\" ");
			if (id != null) {
				writer.print(" id=\"" + id + "\"");
			}
			writer.println("><th colspan="+"\"" + cq + "\">" + label + "</th></tr>");
			m_row = 0;
		}

		protected void writeReportTitle(String title) {
			writer.print("<center><h1 style= \"color:#1515E9\">" + title + " - " + getDateAsString() + "</h1></center>");
		}
		

		/*
		 * Method to get Date as String
		 */
		private String getDateAsString() {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			return dateFormat.format(date);
		}
		
		/** Starts HTML stream */
		protected void startHtml(PrintWriter out) {
			out.println("<!DOCTYPE html>");
			out.println("<html lang=\"en-US\">");
			out.println("<head>");
			out.println("<title>TestNG Custom Report</title>");
			out.println("<style type=\"text/css\">");
			out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
			out.println("td,th {border:1px solid #009;padding:.25em .5em}");
			out.println(".result th {vertical-align:bottom}");
			out.println(".param th {padding-left:1em;padding-right:1em}");
			out.println(".param td {padding-left:.5em;padding-right:2em}");
			out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
			out.println(".numi,.numi_attn {text-align:right}");
			out.println(".total td {font-weight:bold}");
			out.println(".passedodd td {background-color: #0A0}");
			out.println(".passedeven td {background-color: #3F3}");
			out.println(".skippedodd td {background-color: #CCC}");
			out.println(".skippedodd td {background-color: #DDD}");
			out.println(".failedodd td,.numi_attn {background-color: #F33}");
			out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
			out.println(".stacktrace {white-space:pre;font-family:monospace}");
			out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
			out.println("</style>");
			out.println("</head>");
			out.println("<body style = \"background-color:rgb(240, 240, 240); width:100%; height:100%;\">");
			
		}

		/** Finishes HTML stream */
		protected void endHtml(PrintWriter out) {
			out.println("<center> TestNG Report </center>");
			out.println("</body></html>");
		}

		// ~ Inner Classes --------------------------------------------------------
		/** Arranges methods by classname and method name */
		private class TestSorter implements Comparator<IInvokedMethod> {
			// ~ Methods
			// -------------------------------------------------------------

			/** Arranges methods by classname and method name */
			public int compare(IInvokedMethod obj1, IInvokedMethod obj2) {
				int r = obj1.getTestMethod().getTestClass().getName().compareTo(obj2.getTestMethod().getTestClass().getName());
				return r;
			}
		}
		
		private class TestMethodSorter implements Comparator<ITestNGMethod> {
			public int compare(ITestNGMethod obj1, ITestNGMethod obj2) {
				int r = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
				if (r == 0) {
					r = obj1.getMethodName().compareTo(obj2.getMethodName());
				}
				return r;
			}
		}

		private class TestResultsSorter implements Comparator<ITestResult> {
			public int compare(ITestResult obj1, ITestResult obj2) {
				int result = obj1.getTestClass().getName().compareTo(obj2.getTestClass().getName());
				if (result == 0) {
					result = obj1.getMethod().getMethodName().compareTo(obj2.getMethod().getMethodName());
				}
				return result;
			}
		}

		@Override
		public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outdir) {
			
			try {
				outdir= outdir.replace("test-output", "ResultOutput"+File.separator+timeStamp.split(" ")[0]+File.separator+timeStamp.split(" ")[1]);
				File file = new File(outdir);
				if(!file.exists()){
					file.mkdirs();
				}
				writer = createWriter(outdir);
			} catch (IOException e) {
				System.err.println("Unable to create output file");
				e.printStackTrace();
				return;
			}

			startHtml(writer);
			writeReportTitle(reportTitle);
			generateSuiteSummaryReport(suites);
			generateMethodSummaryReport(suites);
			generateMethodDetailReport(suites);
			endHtml(writer);
			writer.flush();
			writer.close();
			//Outlook.sendMail(outdir+File.separator+"Custom-Report.html");
			
		}
	}

