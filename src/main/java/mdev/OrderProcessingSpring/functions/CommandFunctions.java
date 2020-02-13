package mdev.OrderProcessingSpring.functions;

import mdev.OrderProcessingSpring.functions.db.Uploader;
import mdev.OrderProcessingSpring.functions.ftp.FtpIO;
import mdev.OrderProcessingSpring.functions.processing.Validator;
import mdev.OrderProcessingSpring.shell.Commands;
import mdev.OrderProcessingSpring.utils.CSVReader;
import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.File;
import java.security.InvalidKeyException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public class CommandFunctions {

    @Autowired
    public CSVReader csvReader;

    @Autowired
    public FtpIO ftpIO;

    @Autowired
    public Validator validator;

    @Autowired
    public Uploader uploader;

    public File readFile(String filePath){
        return new File(filePath);
    }

    public DataRow[] getDataRows(File file){
        return csvReader.readFile(file);
    }

    public String upload(DataRow[] dataRows, boolean uploadResponseToFtp, boolean forceUpload){
        return uploader.upload(dataRows, uploadResponseToFtp, forceUpload);
    }

    public String validate(DataRow[] dataRows){
        return validator.validate(dataRows, false);
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
