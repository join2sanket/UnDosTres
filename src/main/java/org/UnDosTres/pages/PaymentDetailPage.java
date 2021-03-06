package org.UnDosTres.pages;

import org.UnDosTres.util.PerformAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import java.time.Duration;


public class PaymentDetailPage
{
    public WebDriver driver;

    public PerformAction action;

    private static Logger log= LogManager.getLogger(PaymentDetailPage.class.getName());

    private By SummaryMessage=By.cssSelector("span.summary-message");

    private By CreditCard=By.xpath("//a[contains(@class,'select-card')]");

    private By CardHolderName= By.xpath("//div[@class=\"card-info-box\"] //input[@name=\"cardname\"]");

    private By CardNumber=By.id("cardnumberunique");

    private By ExpiryMonth=By.xpath("//div[@class=\"card-info-box\"] //input[@name=\"expmonth\"]");

    private By ExpiryYear=By.xpath("//div[@class=\"card-info-box\"] //input[@name=\"expyear\"]");

    private By CcvNumber=By.xpath("//div[@class=\"card-info-box\"] //input[@name=\"cvvno\"]");

    private By EmailId=By.cssSelector("div#email_block input.email");

    private By PagarConTarjeta=By.name("formsubmit");

    private By PopUpEmailId=By.cssSelector("#usrname");

    private By PopUpPassword=By.cssSelector("#psw");

    private By PopUpIamNotRobotCheckBox=By.cssSelector("#recaptcha-anchor");

    private By PopUpSubmitButton=By.cssSelector("button#loginBtn");

    private By FacebookButtonOnPopUp=By.cssSelector("img.fbloginButton");

    public PaymentDetailPage(WebDriver driver)
    {
        this.driver=driver;
        action=new PerformAction(driver);
    }

    public String getSummaryMessage()
    {
        String text="Dummy";

        FluentWait<WebDriver>fluentWait=new FluentWait<>(driver).withTimeout(Duration.ofSeconds(15)).pollingEvery(Duration.ofSeconds(3))
        .ignoring(ElementNotVisibleException.class).ignoring(NotFoundException.class);
        try {
            WebElement element =fluentWait.until(ExpectedConditions.visibilityOfElementLocated(SummaryMessage));
            text=element.getText();
        }catch (Exception e)
        {
            System.out.println("Page Is taking more time than expected to navigate to payment");
            log.error("Unable to navigate to payment page ",e.fillInStackTrace());
        }
        return text;
    }

    public boolean selectOptionTarjeta() {


        return action.click(driver.findElement(CreditCard));
    }


    public boolean enterCardHolderName(String name)
    {
        return action.setText(driver.findElement(CardHolderName),name);
    }

    public boolean enterCardNumber(String number)
    {
        return action.setText(driver.findElement(CardNumber),number);
    }

    public boolean enterExpiryMonth(String month)
    {
        return action.setText(driver.findElement(ExpiryMonth),month);
    }

    public boolean enterExpiryYear(String year)
    {
        return action.setText(driver.findElement(ExpiryYear),year);
    }

    public boolean enterCCVNumber(String ccv)
    {
        return action.setText(driver.findElement(CcvNumber),ccv);
    }

    public boolean clickSubmit()
    {
        return action.click(driver.findElement(PagarConTarjeta));
    }

    public boolean enterEmailId(String email)
    {
        return action.setText(driver.findElement(EmailId),email);
    }

    public void fillCardDetails(String name,String numb, String mon,String year,String ccv,String email)
    {
        selectOptionTarjeta();
        enterCardHolderName(name);
        enterCardNumber(numb);
        enterExpiryMonth(mon);
        enterExpiryYear(year);
        enterCCVNumber(ccv);
        enterEmailId(email);
        clickSubmit();
    }


    public boolean fillPopUpAndClickSubmit(String emailId,String pswrd)
    {
        boolean status=false;

        action.setText(driver.findElement(PopUpEmailId),emailId);

        action.setText(driver.findElement(PopUpPassword),pswrd);


        //---switching to iframe-----//
        try{
            int size=driver.findElements(By.tagName("iframe")).size();

            log.info("Number of iframes are "+size);

        for(int i=0;i<size;i++)
        {
            driver.switchTo().frame(i);
            if(driver.findElements(PopUpIamNotRobotCheckBox).size()!=0)
            {
                action.click(driver.findElement(PopUpIamNotRobotCheckBox));
                driver.switchTo().defaultContent();
                break;
            }
        }
            action.click(driver.findElement(PopUpSubmitButton));

            log.error("Waiting for invisibility of the pop up and naviagtion to payment done page");

             FluentWait<WebDriver> fluentWait=new FluentWait<>(driver).withTimeout(Duration.ofSeconds(20))
            .pollingEvery(Duration.ofSeconds(2))
            .ignoring(StaleElementReferenceException.class)
            .ignoring(TimeoutException.class);

            fluentWait.until(ExpectedConditions.invisibilityOfElementLocated(FacebookButtonOnPopUp));
            status=true;
        }catch (Exception e)
        {
            System.out.println("Pop Up Is Still Visible");
           log.error("Pop Up Is Still Visbile Unable to check if recharge is successfull ",e.fillInStackTrace());
        }
        return status;
    }

}
