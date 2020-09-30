/******************
Author @ Sanket Jha
Singleton class, Use getInstance method for using other private methods.
 *******************/
package org.UnDosTres.util;

import org.UnDosTres.testBase.TestBase;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class ExcelReader extends TestBase
{
    private static Sheet excelSheet;
    public static Workbook workbook;
    private static FileOutputStream fileOutputStream;
    public static Map<String,String> credentials;
    public static ExcelReader reader=null;

    private ExcelReader()
    {
        try
        {
            String path=System.getProperty("user.dir")+"/"+propertyFile.getProperty("dataExcel");
            FileInputStream file=new FileInputStream(path);

            String fileExtension[]=propertyFile.getProperty("dataExcel").split("\\.");

            String book=fileExtension[1].trim().toLowerCase();

            switch (book)
            {
                case "xlsx":
                {
                    workbook=new XSSFWorkbook(file);
                    break;
                }
                case "xls":
                {
                    workbook=new HSSFWorkbook(file);
                    break;
                }
                default:
                {
                    System.out.println("Unable to open workbook please excel file extension");
                }
            }

        }catch (Exception e)
        {
            System.out.println("Some Error Occured while reading data excel file ");
            e.printStackTrace();
        }

        credentials=getMapOfSheet("Config");
    }

    public static ExcelReader getInstance()
    {
        if(reader!=null)
        {
          return reader;
        }
        else
            {
                reader=new ExcelReader();
            }
        return reader;
    }



    //--- Method 2-----//
    public String getCellData(String SheetName, String ColoumnName, int RowNumber)
    {
        String cellData = "No Value Found";

        Object value = null;

        int coloumnNumber = -1;

        if(is_Sheet_Present(SheetName))
        {
            excelSheet = workbook.getSheet(SheetName);

            Row row = excelSheet.getRow(0);

            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().equalsIgnoreCase(ColoumnName)) {
                    coloumnNumber = i;
                    break;
                }
            }

            if (coloumnNumber > -1)
            {
                try {
                    Cell cell = excelSheet.getRow(RowNumber - 1).getCell(coloumnNumber);
                    DataFormatter formatter = new DataFormatter();
                    cellData = formatter.formatCellValue(cell);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                {
                System.out.println("Unable to get Coloumn with Name->\""+ColoumnName+"\" in sheet-> "+"\""+SheetName+"\"");
            }
        }else
        {
            System.out.println("Unable to get Cell Data, Sheet with name->\""+ SheetName+"\" not exists");
        }
        return cellData;
    }

    //---------Method 3-----------//
    public Object[][] getAllDataFromExcelSheet(String sheetName)
    {
        try
        {
            excelSheet = workbook.getSheet(sheetName);
        }catch (Exception e)
        {
            System.out.println("Please check sheet name in data excel and which has been passed in parameter");
            e.printStackTrace();
        }
        DataFormatter formatter = new DataFormatter();

        Object data[][]=new Object[excelSheet.getLastRowNum()][excelSheet.getRow(0).getLastCellNum()];

        for (int i=0;i<excelSheet.getLastRowNum();i++)
        {
            for (int j=0;j<excelSheet.getRow(0).getLastCellNum();j++)
            {
                //data[i][j]=sheet.getRow(i+1).getCell(j).toString();
                try
                {
                    Cell cell = excelSheet.getRow(i+1).getCell(j);
                    String cellData = formatter.formatCellValue(cell);
                    data[i][j]= cellData;
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    //---------Method 4-------------//
    public void writeDataInCell(String sheetName, String coloumnName, int rowNumber, Object data)
    {
        Row row;
        Cell cell;

        if(!is_Sheet_Present(sheetName))
        {
            System.out.println("Unable to find sheet with name-> "+sheetName);
            System.out.println("A new Sheet with name-> "+sheetName+" will get created");
        }
            if (workbook.getSheet(sheetName) == null)
            {
                workbook.createSheet(sheetName);
                excelSheet = workbook.getSheet(sheetName);
            } else {
                excelSheet = workbook.getSheet(sheetName);
            }

            for (int i = 0; i < excelSheet.getRow(0).getLastCellNum(); i++) {
                if (excelSheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase(coloumnName)) {
                    if (excelSheet.getRow(rowNumber - 1) == null) {
                        row = excelSheet.createRow(rowNumber - 1);
                    } else {
                        row = excelSheet.getRow(rowNumber - 1);
                    }


                    cell = row.getCell(i);

                    if (cell == null) {
                        cell = row.createCell(i);
                    }

                    if (data instanceof String) {
                        cell.setCellValue((String) data);
                    } else if (data instanceof Integer) {
                        cell.setCellValue((Integer) data);
                    }
                }
            }

            try {
                fileOutputStream = new FileOutputStream(System.getProperty("user.dir") + "/" + propertyFile.getProperty("dataExcel"));
                workbook.write(fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                System.out.println("Unable to write into the excel due to error-->" + e.getMessage());
                e.printStackTrace();
            }

    }


    //-----Method-5-------//
    public Map<String,String> getMapOfSheet(String sheetName)
    {


        credentials=new HashMap<>();
        if(is_Sheet_Present(sheetName))
        {
        Object obj[][]=getAllDataFromExcelSheet(sheetName);

        for (int i=0;i<obj.length;i++)
        {
            credentials.put(obj[i][0].toString(),obj[i][1].toString());
        }
        }
        else
            {
                System.out.println("Unable to Find excet sheet with given name-> "+sheetName);
                System.out.println("Returning empty Map");
            }
        return credentials;
    }

    private boolean is_Sheet_Present(String sheetName)
    {
        boolean status=false;

        int numberOfSheets=workbook.getNumberOfSheets();

        for(int i=0;i<numberOfSheets;i++)
        {
            if(workbook.getSheetName(i).equalsIgnoreCase(sheetName))
            {
                status=true;
                break;
            }
        }

        return status;
    }
    public int numberOfRowsIn(String sheetName)
    {
        if(is_Sheet_Present(sheetName))
        {
            return workbook.getSheet(sheetName).getLastRowNum();
        }

        else
            {
                System.out.println("Please check sheet, either it is misspelled or not present");
                return -1;
            }
    }

    public int numberOfColoumnsIn(String sheetName)
    {
        if(is_Sheet_Present(sheetName))
        {
            return workbook.getSheet(sheetName).getRow(0).getLastCellNum();
        }

        else
        {
            System.out.println("Please check sheet, either it is misspelled or not present");
            return -1;
        }
    }

    public Sheet getExcelSheet(String sheetName)
    {
        Sheet s=null;
        if(is_Sheet_Present(sheetName))
        {
            s=workbook.getSheet(sheetName);
        }
        else
            {
                System.out.println("Sheet is not present in excel please check");
            }
        return s;
    }
}
