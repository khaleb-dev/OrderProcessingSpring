package mdev.orderProcessingSpring.functions;

import mdev.orderProcessingSpring.functions.db.Uploader;
import mdev.orderProcessingSpring.functions.ftp.FtpIO;
import mdev.orderProcessingSpring.functions.processing.Validator;
import mdev.orderProcessingSpring.shell.Commands;
import mdev.orderProcessingSpring.utils.CSVReader;
import mdev.orderProcessingSpring.utils.models.Item;
import mdev.orderProcessingSpring.utils.models.Order;
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

    public Order[] getOrders(File file){
        return csvReader.readOrdersFromFile(file);
    }

    public Item[] getItems(File file){
        return csvReader.readItemsFromFile(file);
    }

    public String upload(Order[] orders, Item[] items, boolean uploadResponseToFtp, boolean forceUpload){
        return uploader.upload(orders, items, uploadResponseToFtp, forceUpload);
    }

    public String validate(Order[] orders, Item[] items){
        return validator.validate(orders, items, false);
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
