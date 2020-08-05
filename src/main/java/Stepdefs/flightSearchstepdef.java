package Stepdefs;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class flightSearchstepdef 
{
	WebDriver driver ;
	List<Integer> prices =new ArrayList<Integer>();
	Integer minPrice ;
	WebElement Source;
	WebElement Destination;
	WebElement departureDate;
	WebElement searchbutton;
	List< WebElement > flightPrices;
	List<WebElement>departureTimes;
	List<WebElement>arriavlTimes;
	

	@Given("Open the browser \"([^\"]*)\" and navigate to the Flight Booking \"([^\"]*)\"$")
	public void open_the_browser_and_navigate_to_the_flight_booking(String browserName, String URL) 
	{

		if (browserName.equalsIgnoreCase("chrome")) 
		{
			
	System.out.println("Chrome broswer is launching");
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--disable-notifications");

			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\Drivers\\chromedriver.exe");

			driver = new ChromeDriver(options);
		}
		else if(browserName.equalsIgnoreCase("firefox"))
		{
			System.out.println("firefox broswer is launching");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\Drivers\\geckodriver.exe");
			 
	        driver= new FirefoxDriver();  
		}
		driver .get(URL);
		driver.manage().window().maximize();

	}
	@Then("Enter {string} and {string}")
	public void enter_and_(String source, String destination) throws InterruptedException 
	{

		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//span[text()='From']")));

		 Source= driver.findElement(By.xpath("//input[@placeholder='From']"));

		Source.sendKeys(source);
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",driver.findElement(By.xpath("(//ul[@role='listbox'])[1]/li")));

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//span[text()='To']")));
		 Destination = driver.findElement(By.xpath("//input[@placeholder='To']"));

		Destination.sendKeys(destination);

		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();",driver.findElement(By.xpath("(//ul[@role='listbox'])[1]/li")));


	}

	@Then("Select Date")
	public void select_date() throws InterruptedException {
		 departureDate=driver.findElement(By.xpath("//*[@data-cy='departureDate']"));
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", departureDate);

		Thread.sleep(2000);
		Date date1 = new Date();
		date1.setDate(date1.getDate() + 2);
		SimpleDateFormat df1 = new SimpleDateFormat("EEE MMM dd YYYY");
		String  ddDate= df1.format(date1);

		System.out.println("date is "+ ddDate);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@aria-label='"+ddDate+"']")));


	}
	@Then("Click on the search button")
	public void click_on_the_search_button() {
		 searchbutton=driver.findElement(By.xpath("//*[text()='Search']"));

		((JavascriptExecutor) driver).executeScript("arguments[0].click();", searchbutton);
		explicitWait(driver.findElement(By.xpath("(//*[text()='View Prices'])[1]")));
	}

	@Then("Findout the cheapest and Fastest Flight")
	public void findout_the_cheapest_and_fastest_flight() 
	{
		//finding chepeast flight

		 flightPrices = driver.findElements(By.xpath("//div[@class='dept-options-section clearfix']/div[3]/p/span"));
		for(WebElement flightPriceElement: flightPrices)
		{
			String price =flightPriceElement.getText().replace(",", "");

			prices.add(Integer.parseInt(price.substring(2)));
		}
		
		minPrice = Collections.min(prices);

		String convertedPrice=NumberFormat.getNumberInstance(Locale.US).format(minPrice.intValue());

		int minpriceSize=driver.findElements(By.xpath("(//div[3]/p/span[contains(text(),'"+convertedPrice+"')])")).size();

		departureTimes=driver.findElements(By.xpath("//div[3]/p/span[contains(text(),'"+convertedPrice+"')]/../../../div[@class='pull-left']/descendant::div[@class='dept-time']"));
		arriavlTimes=driver.findElements(By.xpath("//div[3]/p/span[contains(text(),'"+convertedPrice+"')]/../../../div[@class='pull-left']/descendant::div[@class='text-left pull-left wdh_full']/p[1]"));

		List<Double>entireDuration=new ArrayList<Double>();
		Map<String,Double> dd=new HashMap<String,Double>();

		for(int i=0;i<=departureTimes.size()-1;i++)
		{
			double arriavlTime=Double.parseDouble(arriavlTimes.get(i).getText().replace(":", "."));
			double departureTime=Double.parseDouble(departureTimes.get(i).getText().replace(":", "."));
			entireDuration.add(arriavlTime-departureTime);	
			dd.put(departureTimes.get(i).getText()+","+arriavlTimes.get(i).getText(), arriavlTime-departureTime);
		}
	
		double minDuration=Collections.min(entireDuration);
		System.out.println(minDuration);
		int count=0;
		String depTime=null;

		for (Entry<String,Double> entry : dd.entrySet()) 
		{
			if (entry.getValue().equals(minDuration)) 
			{
				count=count+1;
				depTime= entry.getKey();
			}   
		}

		String viewpricesbutton="//div[3]/p/span[contains(text(),'"+convertedPrice+"')]/../../../div[@class='pull-left']/descendant::div[text()='"+depTime.substring(0, depTime.indexOf(","))+"']/../following-sibling::div[2]/div/p[text()='"+depTime.substring(depTime.indexOf(",")+1)+"']/../../../../../../../following-sibling::div[2]/button";
		String FlightName="//div[3]/p/span[contains(text(),'"+convertedPrice+"')]/../../../div[@class='pull-left']/descendant::div[text()='"+depTime.substring(0, depTime.indexOf(","))+"']/../following-sibling::div[2]/div/p[text()='"+depTime.substring(depTime.indexOf(",")+1)+"']/../../../../../../../preceding-sibling::div/div[2]/p[1]/span";
		
		System.out.println("Cheapest and Fastest flight is "+driver.findElement(By.xpath(FlightName)).getText());
		
		driver.findElement(By.xpath(viewpricesbutton)).click();
	}



	public void explicitWait(WebElement element)
	{
		WebDriverWait wait=new WebDriverWait(driver,60);
		wait.until(ExpectedConditions.visibilityOf(element));
	}
}
