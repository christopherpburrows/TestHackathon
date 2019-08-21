package TestBase;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.IAssert;

public class TestContext {

	private WebDriver webDriver ;
	private Map<AssertionError, IAssert<?>> m_errors;
	private String filmNames ;
	private String filemDirectors;
	private String window;
	private String parentWindow;
	
	public WebDriver getWebDriver() {
		return webDriver;
	}

	public void setWebDriver(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	public Map<AssertionError, IAssert<?>> getM_errors() {
		return m_errors;
	}

	public void setM_errors(Map<AssertionError, IAssert<?>> m_errors) {
		this.m_errors = m_errors;
	}

	public String getFilmNames() {
		return filmNames;
	}

	public void setFilmNames(String filmNames) {
		this.filmNames = filmNames;
	}

	public String getFilemDirectors() {
		return filemDirectors;
	}

	public void setFilemDirectors(String filemDirectors) {
		this.filemDirectors = filemDirectors;
	}

	public String getWindow() {
		return window;
	}

	public void setWindow(String window) {
		this.window = window;
	}

	public String getParentWindow() {
		return parentWindow;
	}

	public void setParentWindow(String parentWindow) {
		this.parentWindow = parentWindow;
	}
	
}
