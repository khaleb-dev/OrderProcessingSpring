package mdev.OrderProcessingSpring.functions;

import mdev.OrderProcessingSpring.utils.CSVReader;
import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class CommandFunctions {

    @Autowired
    public CSVReader csvReader;

    public File readFile(String filePath){
        return new File(filePath);
    }

    public DataRow[] getDataRows(File file){
        return csvReader.readFile(file);
    }

    /**
     * @// TODO: 2/7/20 Percentage calc, Question to upload not 100% fine file, Upload, Return process results
     */
    public String upload(DataRow[] dataRows){

        return null;
    }

    /**
     * @// TODO: 2/7/20 Percentage calc, Return results
     */
    public String validate(DataRow[] dataRows){
        return null;
    }

    /**
     * @// TODO: 2/7/20 Save details, return results
     */
    public String saveFtp(String url, String name, String pass){

        return null;
    }

    /**
     * @// TODO: 2/7/20 Check for details, remove details
     */
    public void removeFtp(){

    }

}
