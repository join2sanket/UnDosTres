package org.UnDosTres.test;

import org.UnDosTres.pages.HomePage;
import org.UnDosTres.testBase.TestBase;
import org.UnDosTres.testData.DataFactory;
import org.UnDosTres.util.PerformAction;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileReader;

public class HomePageTest extends TestBase
{
    private String browser,operator,mobileNumber,rechargeType,amount;
    private HomePage homePage;


    @Factory(dataProvider = "getHomePageData",dataProviderClass = DataFactory.class)
    public HomePageTest(String browser, String operator, String mobileNumber, String rechargeType,
                        String amount,String stats)
    {
        this.browser=browser;
        this.operator=operator;
        this.mobileNumber=mobileNumber;
        this.rechargeType=rechargeType;
        this.amount=amount;
    }

    @BeforeTest
    public void beforeStartOfClass()
    {
        String report = System.getProperty("user.dir")+"/automationReport";
        //delete folder recursively
        File file=new File(report);

        if(file.exists())
        {
            file.delete();
        }

     //   PerformAction.recursiveDelete(new File(report));

        String logs=System.getProperty("user.dir")+"/logs";
        //PerformAction.recursiveDelete(new File(logs));
        File file1=new File(logs);

        if(file1.exists())
        {
            file1.delete();
        }
    }

    @BeforeMethod
    public void setUp(ITestContext context)
    {
         initialize_Browser_OpenUrl(browser,propertyFile.getProperty("url"));
         homePage=new HomePage();
         context.setAttribute("browser",browser);
    }
    @AfterMethod
    public void tearDown()
    {
        driver.quit();
    }




    @Test
    public void validateHomePageIsGettingOpen()
    {
        Assert.assertEquals(driver.getTitle(),"UnDosTres - Recargas en línea Telcel, Movistar, AT&T, Unefon, Virgin, Televia, Pase, Boletos de Cine, Pagos CFE","Page Title Is not matchhed");
    }

    @Test(dependsOnMethods = "validateHomePageIsGettingOpen")
    public void validateRecargaCelularIsShown()
    {
        Assert.assertEquals(homePage.getTextRecargaCelular(),"Recarga Celular","Url Is opened But Page text is not visible");
    }

    @Test (dependsOnMethods = "validateHomePageIsGettingOpen")
    public void validateUserIsAbleToSubmitDetails()
    {
        Assert.assertEquals(homePage.enterRechargeDetailsAndClickSubmit(operator,mobileNumber,rechargeType,amount).
                getSummaryMessage(),"Recarga   Paquete Amigo  de Telcel  al número "+mobileNumber+"  de   $"+amount+" pesos",
                "User Is Unable to Navigate to Payment Page");
    }

}
