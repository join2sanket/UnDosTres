package org.UnDosTres.util;


import org.UnDosTres.testBase.TestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.List;


public class PerformAction extends TestBase
{
    public static WebDriverWait wait;
    public static FluentWait<WebDriver> fluentWait;
    public static Actions actions;
    private static final Logger log= LogManager.getLogger("UnDosTres");

    public static void initializeVariables()
    {
        wait=new WebDriverWait(driver,15);
        fluentWait=new FluentWait(driver);
        actions=new Actions(driver);
    }

    public static void moveToElementAndClick(WebElement element)
    {
        initializeVariables();

        try{

            actions.moveToElement(element).click().build().perform();
            log.info("Successfully Move to the element and clicked-->"+element.toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean setText(WebElement element,String data)
    {
        initializeVariables();

        isPageReady();


        boolean status=false;

        try
        {
            log.info("Waiting until field is clickable->"+element.toString());
            wait.until(ExpectedConditions.elementToBeClickable(element));
        }catch (Exception e)
        {
            log.error("Element->>"+element.toString()+" is not clickable",e.fillInStackTrace());
        }

        actions.moveToElement(element).build().perform();
        log.info("Moving to the element-->"+element.toString());
        element.clear();
        log.info("Clear data from the field if present-->"+element.toString());
        int count=0;

        do
            {
               element.sendKeys(data);
                log.info("Field has been populated with data-->>"+data);

               if(element.getText().trim().equalsIgnoreCase(data))
               {
                   log.info("Validated that Field has been populated with data-->>"+data);
                   //System.out.println("Value of Element "+element+" is "+data+" entered successfully");
                   status=true;
                   break;
               }
               else
                   {
                       if(element.getAttribute("value").trim().equalsIgnoreCase(data))
                       {
                           log.info("Validate with attribute value that field populated with data-->>"+data);
                           status=true;
                           break;
                       }
                       else
                       {
                           count++;
                       }
                   }
            }while (count!=4);

        if(status!=true)
        {
            //System.out.println("Unable to check if data is successfully entered for "+element);
            log.warn("Unable to validate that data has been entered successfully-->>"+data);
        }
        return status;
    }

    public static boolean click(WebElement element)
    {
        initializeVariables();

        isPageReady();

        boolean status=false;

        try
        {
            log.info("Waiting for the element to be clickable-->>"+element.toString());
            wait.until(ExpectedConditions.elementToBeClickable(element));

            actions.moveToElement(element).click().build().perform();
            log.info("Successfully moved to the element and clicked-->>"+element.toString());

            status=true;
        }catch (Exception e)
        {
            //System.out.println("Unable to click over element "+element);
            log.error("Unable to click over click over element-->>"+element.toString()+"\n",e.fillInStackTrace());
        }
        return status;
    }

    public static boolean hover(WebElement element)
    {
        initializeVariables();

        isPageReady();

        boolean status=false;
        try
        {
            log.info("Waiting for the visibility of the element in DOM-->>"+element.toString());
            wait.until(ExpectedConditions.visibilityOf(element));

            actions.moveToElement(element).build().perform();
            log.info("Successfully move to the element ");
            status=true;
        }catch (Exception e)
        {
            //System.out.println("Unable to click over element "+element);
            log.error("Unable to hover over the element",e.fillInStackTrace());
        }
        return status;
    }

    public static String getText(WebElement element)
    {
        initializeVariables();
        isPageReady();
        String text="Dummy";
        try
        {
            log.info("Getting text with the help of getText method");
            if(element.getText()!=null || !element.getText().isEmpty())
          {
              text=element.getText().trim();
              log.info("Text has been found with getText method which is-->>"+text);
          }

          else
              {
                  log.info("Getting text with the help of attribute value");
                  if(element.getAttribute("value")!=null || !element.getAttribute("value").isEmpty())
                  {
                      text=element.getAttribute("value").trim();
                      log.info("Text has been found with Attribute value method which is-->>"+text);
                  }
                  else
                      {
                          //System.out.println("Unable to extract text from element "+element);
                          log.error("Unable to extract text with getText and getAttribute value-->>"+element.toString());
                      }
              }

        }catch (Exception e)
        {
           // System.out.println("Element "+element+ "is not visible properly");
            log.error("Unable to extract text from the element-->>"+element.toString()+"\n",e.fillInStackTrace());
        }

        return text;
    }

    public static boolean setAutoSuggestiveDropDown(By by, String optionToSelect)
    {
        initializeVariables();
        isPageReady();
        boolean status=false;

        List<WebElement> list=null;

        try
        {
            list=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        }catch (Exception e)
        {
            System.out.println("Unable to fetch List");
            e.printStackTrace();
        }
        if(list!=null || !list.isEmpty())
        {
            for (WebElement e:list)
            {
                String text=e.getText().trim().replace("\n"," ");

                System.out.println(text);

                if(text.contains(optionToSelect))
                {
                    e.click();
                    status=true;
                    break;
                }
            }
        }
        return status;
    }

    public static void isPageReady()
    {
        initializeVariables();
        fluentWait.withTimeout(Duration.ofSeconds(30));
        fluentWait.pollingEvery(Duration.ofSeconds(2));
        fluentWait.ignoring(Exception.class);
        try{
            log.info("Getting the readiness of the page");
            fluentWait.until(driver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete"));
            log.info("Page is ready");
        }catch (Exception e)
        {
            //System.out.println("Page is not getting stable within 20 sec");
            log.error("Page is not getting stable within 20 sec",e.fillInStackTrace());
        }
    }


    public static void recursiveDelete(File file)
    {
        //to end the recursive loop
        if (!file.exists())
            return;

        //if directory, go inside and call recursively
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                //call recursively
                recursiveDelete(f);
            }
        }
        //call delete to delete files and empty directory
        file.delete();
        System.out.println("Deleted file/folder: "+file.getAbsolutePath());
    }
}
