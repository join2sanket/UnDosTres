package org.UnDosTres.util;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFile
{
    static FileInputStream file=null;
    static Properties config=null ;
    static FileOutputStream out=null;

    public static Properties getPropertyFile(String fileName)
    {   String path = System.getProperty("user.dir")+"/src/main/java/" + fileName;
        config=new Properties();

        try {
            file = new FileInputStream(path);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        try {
            config.load(file);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return config;
    }


    public static void setProperty(String ConfigFileNameAndPackage,String key, String value) throws IOException
    {
        String SystemName=System.getProperty("os.name");
        String[] split=SystemName.split(" ");
        String switchToCase=split[0].toLowerCase();

        switch (switchToCase)
        {
            case ("mac"):
            {
                file = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/"+ConfigFileNameAndPackage);
                Properties config = new Properties();
                config.load(file);
                String oldvalue=config.getProperty(key);
                file.close();
                out = new FileOutputStream(System.getProperty("user.dir")+"/src/main/java/"+ConfigFileNameAndPackage);
                config.replace(key,oldvalue,value);
                config.store(out, null);
                out.close();
                break;
            }

            case ("windows"):
            {
                file = new FileInputStream(System.getProperty("user.dir")+"\\src\\main\\java\\"+ConfigFileNameAndPackage);
                Properties config = new Properties();
                config.load(file);
                String oldvalue=config.getProperty(key);
                file.close();
                out = new FileOutputStream(System.getProperty("user.dir")+"\\src\\main\\java\\"+ConfigFileNameAndPackage);
                config.replace(key,oldvalue,value);
                config.store(out, null);
                out.close();
                break;
            }

            default:
            {
                file = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/"+ConfigFileNameAndPackage);
                Properties config = new Properties();
                config.load(file);
                String oldvalue=config.getProperty(key);
                file.close();
                out = new FileOutputStream(System.getProperty("user.dir")+"/src/main/java/"+ConfigFileNameAndPackage);
                config.replace(key,oldvalue,value);
                config.store(out, null);
                out.close();
            }
        }


    }

}