package org.UnDosTres.test;

import org.UnDosTres.pages.HomePage;
import org.UnDosTres.pages.PaymentDetailPage;
import org.UnDosTres.testBase.TestBase;
import org.UnDosTres.testData.DataFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

public class PaymentDetailPageTest extends TestBase
{
    private static final Logger log= LogManager.getLogger("UnDosTres");

    private String browser,operator,mobileNumber,rechargeType,amount,cardName,cardNumber,expMonth,expYear,
            ccv,email,popUpEmail,popUpPassword;
    private PaymentDetailPage paymentDetailPage;


    @Factory(dataProvider = "getPaymentPageData",dataProviderClass = DataFactory.class)
    public PaymentDetailPageTest(String browser, String operator, String mobileNumber, String rechargeType,
                                 String amount, String cardName, String cardNumber, String expMonth,
                                 String expYear, String ccv, String email,String popUpEmail, String popUpPassword, String stats)
    {
        this.browser=browser;
        this.operator=operator;
        this.mobileNumber=mobileNumber;;
        this.rechargeType=rechargeType;
        this.amount=amount;
        this.cardName=cardName;
        this.cardNumber=cardNumber;;
        this.expMonth=expMonth;
        this.expYear=expYear;
        this.ccv=ccv;
        this.email=email;
        this.popUpEmail=popUpEmail;
        this.popUpPassword=popUpPassword;
    }

    @BeforeMethod
    public void setUp(ITestContext context)
    {
        log.info("Currently executing PaymentDetail Test cases, Initializing browser "+browser);
        initialize_Browser_OpenUrl(browser,propertyFile.getProperty("url"));

        paymentDetailPage=new HomePage().enterRechargeDetailsAndClickSubmit(operator,mobileNumber,rechargeType,amount);

        paymentDetailPage.fillCardDetails(cardName,cardNumber,expMonth,expYear,ccv,email);

        context.setAttribute("browser",browser);
    }

    @Test
    public void validatePaymentPageUrlIsGettingOpen()
    {
        Assert.assertEquals(driver.getCurrentUrl(),"https://prueba.undostres.com.mx/payment.php",
                "Url Of Payment Page Is not Matched");
    }

    @Test(dependsOnMethods ="validatePaymentPageUrlIsGettingOpen")
    public void validateSummaryOnPageIsCorrectlyDisplay()
    {
        String actual= paymentDetailPage.getSummaryMessage().trim();
        String expected="Recarga   Paquete Amigo  de Telcel  al número "+mobileNumber+"  de   $"+amount+" pesos";
       Assert.assertTrue(actual.equalsIgnoreCase(expected),"Page Is opened but text didn't matched");
    }

    @Test(dependsOnMethods ="validatePaymentPageUrlIsGettingOpen")
    public void validatePaymentHasBeenCompletedThroughCard()
    {
        Assert.assertTrue(paymentDetailPage.fillPopUpAndClickSubmit(popUpEmail,popUpPassword),
                "Unable to submit recharge as Pop Up Is not getting disappear");
    }

    @AfterMethod
    public void tearDown()
    {
        driver.quit();
    }
}
