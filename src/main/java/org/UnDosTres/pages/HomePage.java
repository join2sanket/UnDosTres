package org.UnDosTres.pages;

import org.UnDosTres.testBase.TestBase;
import org.UnDosTres.util.PerformAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage
{
    public WebDriver driver;

    private By Operator= By.xpath("//div[@to-do=\"mobile\"]/div/div[@class=\"form\"] //input[@name=\"operator\"]");

    private By OperatorList=By.xpath("//input[@id=\"suggested\"]/../div/ul/li");

    private By Mobile=By.xpath("//div[@to-do=\"mobile\"]/div/div[@class=\"form\"] //input[@name=\"mobile\"]");

    private By Amount=By.xpath("//div[@to-do=\"mobile\"]/div/div[@class=\"form\"] //input[@name=\"amount\"]");

    private By PerformPaymentButton=By.xpath("//div[@to-do=\"mobile\"]/div //button");

    private By TextRecargaCelular=By.cssSelector("div[to-do=\"mobile\"] h1");

    private static final Logger log= LogManager.getLogger("UnDosTres");


    public HomePage(WebDriver driver)
    {
        this.driver=driver;
    }



    public boolean setOperator(String operator)
    {

        boolean status=false;
        try {
            PerformAction.click(driver.findElement(Operator),driver);
            log.info("Click on the operator field to open the operator list");

            List<WebElement> list = new WebDriverWait(driver,10).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(OperatorList));
            log.info("Oprator list has been found, number of operator in list are->"+list.size());

            for (WebElement e : list)
            {
                if (PerformAction.getText(e,driver).equalsIgnoreCase(operator))
                {
                    PerformAction.click(e,driver);
                    log.info("Operator selected as "+operator+" successfully");
                    status = true;
                    break;
                }
            }
        }catch (Exception e)
        {
            log.error("Unable to select operator-> "+operator,e.fillInStackTrace());
        }
        return status;
    }

    public boolean setMobileNumber(String mobileNumber)
    {
        boolean status=false;

        try
        {
            PerformAction.setText(driver.findElement(Mobile),mobileNumber,driver);
            log.info("Mobile Number->"+mobileNumber+" has been entered successfully");
            status=true;
        }catch (Exception e)
        {
            System.out.println("Unable to Set Mobile Number");
            log.error("Unable to enter mobile number",e.fillInStackTrace());
        }
        return status;
    }

    public boolean setRechargeTypeAndAmount(String RechargeType, String amount)
    {
        boolean status=false;
        int i=0,k=0;

            PerformAction.click(driver.findElement(Amount),driver);
            try {
                log.info("Getting recharge type web element " + RechargeType);
                WebElement rechargeType = driver.findElement(By.xpath("//div[@class=\"category-tab \"]/div[text()=\"" + RechargeType + "\"]"));
                log.info("Recharge type webelement has been found successfully");

                PerformAction.click(rechargeType,driver);
                log.info("Click over Recharge type->"+RechargeType);

                List<WebElement> list = driver.findElements(By.xpath("//div[@class=\"category-tab \"]/div"));

                while (!PerformAction.getText(list.get(i),driver).equalsIgnoreCase(RechargeType) && i < 3)
                {
                    i++;
                }
                List<WebElement> list1 = list.get(i).findElements(By.xpath("../following-sibling::ul[" + (++i) + "]/li"));
                for (WebElement e : list1)
                {
                    if (e.findElement(By.xpath("a/div/b")).getText().contains(amount)) {
                        e.click();
                        log.info("Clicked on the amount "+amount);
                        status = true;
                        break;
                    }
                }
            }catch (Exception e)
            {
                log.error("Unable to set recharge type and amount ",e.fillInStackTrace());
            }
        return status;
    }

    public void clickProceedToPayment()
    {
            log.info("Going to click on submit button to move towards payment page");
            PerformAction.click(driver.findElement(PerformPaymentButton),driver);
            log.info("Clicked on perform payment button");
    }

    public String getTextRecargaCelular()
    {
        log.info("Getting text Recarga Celular");
        return PerformAction.getText(driver.findElement(TextRecargaCelular),driver);
    }

    public PaymentDetailPage enterRechargeDetailsAndClickSubmit(String operator, String mobileNo, String rechType, String amount)
    {
        setOperator(operator);
        setMobileNumber(mobileNo);
        setRechargeTypeAndAmount(rechType,amount);
        clickProceedToPayment();
        return new PaymentDetailPage(driver);
    }
}
