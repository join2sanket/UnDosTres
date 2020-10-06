package org.UnDosTres.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.UnDosTres.extentReport.ExtentReporter;
import org.UnDosTres.testBase.TestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class Listeners extends TestBase implements ITestListener
{
    ExtentTest test;
    ExtentReports extent= ExtentReporter.getReportObject();
    ThreadLocal<ExtentTest> extentTest =new ThreadLocal<ExtentTest>();
    private static final Logger log= LogManager.getLogger("UnDosTres");
    public void onTestStart(ITestResult result)
    {
        // TODO Auto-generated method stub
        try {
            log.info(result.getTestContext().getAttribute("browser")+"Browser Start Execution of test case ->"+result.getMethod().getMethodName());
            test = extent.createTest(result.getMethod().getMethodName() + "_" + result.getTestContext().getAttribute("browser"));
            extentTest.set(test);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onTestSuccess(ITestResult result) {
        // TODO Auto-generated method stub
        extentTest.get().log(Status.PASS, "Test Passed");
        log.info("Test Case "+result.getMethod().getMethodName()+" has been passed");

    }

    public void onTestFailure(ITestResult result)
    {
        // TODO Auto-generated method stub
        //Screenshot
        extentTest.get().fail(result.getThrowable());
        String concat=".";
        String testMethodName =result.getMethod().getMethodName();
        String sc= null;
        try {
            sc = concat+getScreenShot(testMethodName,(WebDriver)result.getTestClass().getRealClass().getDeclaredField("driver").
                    get(result.getInstance()));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            extentTest.get().addScreenCaptureFromPath(sc, result.getMethod().getMethodName()
            +"_"+result.getTestContext().getAttribute("browser"));
        } catch(Exception e)
        {
            System.out.println("Unable to Take Screenshot please check");
            e.printStackTrace();
        }

    }

    public void onTestSkipped(ITestResult result) {
        // TODO Auto-generated method stub

        extentTest.get().log(Status.SKIP,result.getMethod().getMethodName());

    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // TODO Auto-generated method stub

    }

    public void onStart(ITestContext context) {
        // TODO Auto-generated method stub

    }

    public void onFinish(ITestContext context) {
        // TODO Auto-generated method stub
        extent.flush();
    }
}
