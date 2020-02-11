package mdev.OrderProcessingSpring.functions;

import mdev.OrderProcessingSpring.functions.ftp.FtpIO;
import mdev.OrderProcessingSpring.shell.Commands;
import mdev.OrderProcessingSpring.utils.CSVReader;
import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.security.InvalidKeyException;

public class CommandFunctions {

    @Autowired
    public CSVReader csvReader;

    @Autowired
    public FtpIO ftpIO;

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
        return "";
    }

    /**
     * @// TODO: 2/7/20 Percentage calc, Return results
     */
    public String validate(DataRow[] dataRows){
        return "";
    }

    public String saveFtp(String url, int port, String name, String pass) throws BadPaddingException, InvalidKeyException, IllegalBlockSizeException {
        return ftpIO.saveFtp(url, port, name, pass);
    }

    public void removeFtp(){
        ftpIO.removeFtp();
    }

    public void setCommands(Commands commands) {
        csvReader.setCommands(commands);
        ftpIO.setCommands(commands);
    }
}
