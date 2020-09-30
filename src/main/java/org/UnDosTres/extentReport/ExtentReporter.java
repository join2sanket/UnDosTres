package org.UnDosTres.extentReport;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.UnDosTres.testBase.TestBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExtentReporter extends TestBase
{

    static ExtentReports extent;
    private static final Logger log= LogManager.getLogger("UnDosTres");

    public static ExtentReports getReportObject()
    {

        String path =System.getProperty("user.dir")+"./AutomationReport/report.html";
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("UnDosTres Results");
        reporter.config().setDocumentTitle("Test Results");
        reporter.config().setTheme(Theme.DARK);


        extent =new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester", "Sanket Jha");
        extent.setSystemInfo("Application Url","http://prueba.undostres.com.mx");
        return extent;

    }

}
