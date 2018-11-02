package tests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import helpers.Helpers;
import helpers.Screenshooter;
import helpers.WebDriverManager;
import pages.PageLogin;
import pages.PageLogon;
import pages.PageReservation;

public class Tests {
	private WebDriver driver;
	ArrayList<String> tabs;
	
	@BeforeMethod
	public void setUp() {
		DesiredCapabilities caps = new DesiredCapabilities();
		System.setProperty("webdriver.chrome.driver", "Driver/chromedriver.exe");
		driver = new ChromeDriver();
		//driver.manage().window().maximize();
		//driver.manage().window().fullscreen();
		//driver.manage().window().setSize(new Dimension(800,600)); //Usa objeto del tipo Dimension
		//driver.manage().window().setSize(new Dimension(200,200)); //Usa objeto del tipo Dimension
		//for (int i=0; i<=800; i++)
		//	driver.manage().window().setPosition(new Point(i,i)); // Hacer import de selenium.Point
		//driver.manage().window().setPosition(new Point(400,300)); // Hacer import de selenium.Point
		driver.navigate().to("http://newtours.demoaut.com/");
		JavascriptExecutor javaScriptExecutor = (JavascriptExecutor)driver; //Castear el driver
		String googleWindow = "window.open('http://www.google.com')";	//googleWindow contiene la url
		javaScriptExecutor.executeScript(googleWindow);
		tabs = new ArrayList<String> (driver.getWindowHandles());	//Handles todos los tabs

	}
	@Test
	public void loginIncorrecto() {
		driver.switchTo().window(tabs.get(1)).navigate().to("http://www.youtube.com"); //Tab activa
		driver.switchTo().window(tabs.get(0)); //Activar Tab para que siga la prueba
		PageLogin pageLogin = new PageLogin(driver);
		PageLogon pageLogon = new PageLogon(driver);
		pageLogin.login("user", "user");
		pageLogon.assertLogonPage();
	}
	
	@Test
	/*Prueba de login correcto*/
	public void login() {
		WebDriverManager.setWindowSize(driver, "fullscreen");
		PageLogin pageLogin = new PageLogin(driver);
		PageReservation pageReservation = new PageReservation(driver);
		pageLogin.login("mercury", "mercury");
		pageReservation.assertPage();
	}
	
	@Test
	public void pruebaTres() {
		WebDriverManager.setWindowSize(driver, 400, 400);
		PageLogin pageLogin = new PageLogin(driver);
		PageReservation pageReservation = new PageReservation(driver);
		pageLogin.login("mercury", "mercury");
		pageReservation.selectPassengers(2);
		pageReservation.selectFromPort(3);
		pageReservation.selectToPort("London");
	}
	
	@Test
	public void pruebaCantidadDeCampos() {
		PageLogin pageLogin = new PageLogin(driver);
		pageLogin.verifyFields();
	}
	
	@AfterMethod
	public void tearDown(ITestResult result) {
		if(!result.isSuccess()) {
			Screenshooter.takeScreenshot("ERROR ", driver);
		}
		driver.switchTo().window(tabs.get(1)).close();	//Cerrar x cada tab abierto
		driver.switchTo().window(tabs.get(0)).close();
		//driver.close();	// Si se activa error Invalid session id
	}

}
