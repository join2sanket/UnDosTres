package org.UnDosTres.dataFactory;


import org.UnDosTres.util.ExcelReader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

public class DataFactory
{
    @DataProvider(name = "getPaymentPageData")
    public Object[][] getPaymentPageData()
    {
        Sheet excelSheet= ExcelReader.getInstance().getExcelSheet("PaymentPage");

        DataFormatter formatter = new DataFormatter();

        int rowCount=0;

        for(int i=0;i<excelSheet.getLastRowNum();i++)
        {
            if(ExcelReader.getInstance().getCellData("PaymentPage","RunStatus",i+2).equalsIgnoreCase("yes"))
            {
                rowCount++;
            }
        }

        Object data[][]=new Object[rowCount][excelSheet.getRow(0).getLastCellNum()];
        int da=-1;
        for (int i=0;i<excelSheet.getLastRowNum();i++)
        {
            if(ExcelReader.getInstance().getCellData("PaymentPage","RunStatus",i+2).equalsIgnoreCase("yes"))
            {   da++;
                for (int j=0;j<excelSheet.getRow(0).getLastCellNum();j++)
                {
                    //data[i][j]=sheet.getRow(i+1).getCell(j).toString();
                    try
                    {
                        Cell cell = excelSheet.getRow(i+1).getCell(j);
                        String cellData = formatter.formatCellValue(cell);
                        data[da][j]= cellData;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return data;
    }

    @DataProvider(name = "getHomePageData")
    public Object[][] getHomePageData()
    {
        Sheet excelSheet= ExcelReader.getInstance().getExcelSheet("HomePage");

        DataFormatter formatter = new DataFormatter();

        int rowCount=0;

        for(int i=0;i<excelSheet.getLastRowNum();i++)
        {
            if(ExcelReader.getInstance().getCellData("HomePage","RunStatus",i+2).equalsIgnoreCase("yes"))
            {
                rowCount++;
            }
        }

        Object data[][]=new Object[rowCount][excelSheet.getRow(0).getLastCellNum()];
        int da=-1;
        for (int i=0;i<excelSheet.getLastRowNum();i++)
        {
            if(ExcelReader.getInstance().getCellData("HomePage","RunStatus",i+2).equalsIgnoreCase("yes"))
            {   da++;
                for (int j=0;j<excelSheet.getRow(0).getLastCellNum();j++)
                {
                    //data[i][j]=sheet.getRow(i+1).getCell(j).toString();
                    try
                    {
                        Cell cell = excelSheet.getRow(i+1).getCell(j);
                        String cellData = formatter.formatCellValue(cell);
                        data[da][j]= cellData;
                    } catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return data;
    }
}
