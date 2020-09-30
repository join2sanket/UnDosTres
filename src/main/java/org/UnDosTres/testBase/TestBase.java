package org.UnDosTres.testBase;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestBase
{

    public static Properties propertyFile;
    public static WebDriver driver;
    private static Logger log= LogManager.getLogger("UnDosTres");


    public TestBase()
    {
        String path=System.getProperty("user.dir")+"/src/main/java/org/UnDosTres/config/config.properties";

        try
        {
            FileInputStream file =new FileInputStream(path);

            propertyFile=new Properties();

            propertyFile.load(file);
        }
        catch (Exception e)
        {
          //  System.out.println("Unable to open Property File Please check file path TestBase");
            log.error("Unable to open Property File Please check file path",e.fillInStackTrace());
        }
    }

    public void initialize_Browser_OpenUrl(String browser,String url) throws TimeoutException
    {

        String browserType=browser.toLowerCase().trim();

           if(driver==null || ((RemoteWebDriver)driver).getSessionId()==null)
            {
               switch (browserType)
               {
                   case "chrome":
                       {

                       WebDriverManager.chromedriver().setup();

                       ChromeOptions options = new ChromeOptions();

                       options.addArguments("--incognito");

                       driver = new ChromeDriver(options);
                       break;
                   }
                   case "firefox":
                   {
                       WebDriverManager.firefoxdriver().setup();

                       FirefoxOptions options=new FirefoxOptions();
                       options.addArguments("-private");

                       driver=new FirefoxDriver(options);
                       break;
                   }
                   case "edge":
                   {
                       WebDriverManager.edgedriver().setup();

                       EdgeOptions options=new EdgeOptions();
                       options.setCapability("InPrivate",true);
                       driver=new EdgeDriver(options);

                       break;
                   }

               }
           }

        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        log.info("Browser->"+browserType+" has been opened successfully");
        driver.get(url);
        log.info("Url Has been entered-> "+ url);

    }

    public String getScreenShot(String testName)
    {
        log.info("Going to take screenshot for test case->"+testName);

        TakesScreenshot sc=(TakesScreenshot)driver;
        log.info("Screenshot has been taken");
        File source=sc.getScreenshotAs(OutputType.FILE);
        String destination="./"+"AutomationReport/ScreenShots/"+testName+".png";

        try {
            FileUtils.copyFile(source,new File(destination));
            log.info("Screenshot has been stored in screenshot folder");
        } catch (IOException e)
        {
            log.error("Unable to take screenshot error has been occured",e.fillInStackTrace());
        }
        return destination;
    }
}
