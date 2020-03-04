package mdev.orderProcessingSpring.functions.db;

import ch.qos.logback.classic.Logger;
import mdev.orderProcessingSpring.functions.ftp.FtpNet;
import mdev.orderProcessingSpring.functions.ftp.ResultWriter;
import mdev.orderProcessingSpring.functions.processing.Validator;
import mdev.orderProcessingSpring.shell.ShellUsrEX;
import mdev.orderProcessingSpring.utils.*;
import mdev.orderProcessingSpring.utils.dao.ItemDAO;
import mdev.orderProcessingSpring.utils.dao.OrderDAO;
import mdev.orderProcessingSpring.utils.models.Item;
import mdev.orderProcessingSpring.utils.models.Order;
import mdev.orderProcessingSpring.utils.vars.DataBaseVars;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class Uploader {

    private Logger logger;

    @PostConstruct
    private void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(Uploader.class);
    }

    @Autowired
    private FtpNet ftpNet;

    @Autowired
    private Validator validator;

    @Autowired
    private DataBaseVars dataBaseVars;

    @Autowired
    private ShellUsrEX shellUsrEX;

    @Autowired
    private ResultWriter resultWriter;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ItemDAO itemDAO;

    private StringBuilder stringBuilder;
    private ArrayList<UploadError> uploadFail;
    private ArrayList<String> uploadSuccess;

    /**
     * Called to upload data to the database from CommandFunctions
     * @see mdev.orderProcessingSpring.functions.CommandFunctions#upload(Order[], Item[], boolean, boolean)
     * @see mdev.orderProcessingSpring.shell.Commands#uploadFile(String, boolean, boolean)
     *
     * @param orders The not fully validated rows
     *                 (number format exceptions are validated at reading the file but more validation is needed)
     * @param uploadResponseToFTP True when the response is needed to be uploaded to the FTP server
     * @param forceUpload True when the valid data is needed to be uploaded to the database from an invalid file
     * @return The results of the process
     */
    public String upload(Order[] orders, Item[] items, boolean uploadResponseToFTP, boolean forceUpload){
        init();

        validator.validate(orders, items, true);
        stringBuilder.append(validity(forceUpload, orders.length + items.length));
        stringBuilder.append(ftpUpload(uploadResponseToFTP));

        try {
            if (validator.isValid()){
                stringBuilder.append(uploadToDB(validator.getValidOrders(), validator.getValidItems()));
            }else if (!validator.isValid() && forceUpload){
                stringBuilder.append(uploadToDB(validator.getValidOrders(), validator.getValidItems()));
            }
        } catch (ParseException e) {
            logger.error(shellUsrEX.getErrorMessage(e.toString()));
        }

        if (uploadResponseToFTP){
            stringBuilder.append(FtpUploadProcess());
        }

        stringBuilder.append("\nProcess Done.");
        return stringBuilder.toString();
    }

    /**
     * Initializes objects for the upload process
     * @see #upload(Order[], Item[], boolean, boolean)
     */
    private void init(){
        stringBuilder = new StringBuilder();
        uploadFail = new ArrayList<>();
        uploadSuccess = new ArrayList<>();
    }

    /**
     * Generates some validity info about the file
     * @param forceUpload True when the valid data is needed to be uploaded to the database from an invalid file
     * @param orderSize The size of the not validated data list
     * @return Validity information
     */
    private String validity(boolean forceUpload, int orderSize){
        StringBuilder validityInformationBuilder = new StringBuilder();
        if (validator.isValid()){
            validityInformationBuilder.append("\nThe file is 100% valid! Uploading...");
        }else{
            validityInformationBuilder
                    .append("\nThe file is ")
                    .append(validator.percentageCalculator.calc(orderSize, validator.getValidationErrors()))
                    .append("% valid!");
            if (forceUpload){
                validityInformationBuilder.append("\nForce uploading...");
            }else{
                validityInformationBuilder.append("\n\nIf you want to upload the correct data from the invalid file,\nplease enable \"force uploading\" with the \"-F\" parameter.");
            }
        }
        return validityInformationBuilder.toString();
    }

    /**
     * Generates info about the FTP upload process
     * @param uploadResponseToFTP True when the response is needed to be uploaded to the FTP server
     * @return FTP upload information
     */
    private String ftpUpload(boolean uploadResponseToFTP){
        if (uploadResponseToFTP){
            return "\n\nThe results will be uploaded to your ftp server as a responseFile.";
        }
        return "\n\nThe results wont be uploaded to your ftp server as a responseFile." +
                "\nIf you want to upload the results to an ftp server, please enable the \"-R\" parameter.";
    }

    /**
     * Called to save and upload the results of the db upload to an FTP
     * @return The FTP upload results
     */
    private String FtpUploadProcess(){
        StringBuilder result = new StringBuilder();

        if (!ftpNet.isConnected() && ftpNet.getConnectionDetail() != null){
            try {
                ftpNet.connect(ftpNet.getConnectionDetail());
                result.append(uploadToFTP() ?
                            "\nResponse uploaded to the FTP server successfully!" :
                            "\nFailed to upload the response to the FTP server!");
            } catch (IOException e) {
                logger.error(shellUsrEX.getErrorMessage(e.toString()));
            }
        }else{
            try {
                result.append(uploadToFTP() ?
                        "\nResponse uploaded to the FTP server successfully!" :
                        "\nFailed to upload the response to the FTP server!");
            } catch (IOException e) {
                logger.error(shellUsrEX.getErrorMessage(e.toString()));
            }
        }

        return result.toString();
    }

    /**
     * Uploads a file to the FTP server
     * @return The process results
     * @throws IOException Can throw exception if the file or the connection or the process is corrupted
     */
    private boolean uploadToFTP() throws IOException {
        return ftpNet.upload(resultWriter.write(
                uploadFail,
                uploadSuccess,
                validator.getValidationErrors()),
                ftpNet.getConnectionDetail());
    }

    /**
     * Called to upload data into the database
     * @param orders Validated correct orders
     * @param items Validated correct items
     * @return The results of the process (for printing purposes in the CLI)
     * @throws ParseException Can throw exception for the DateFormats (very rare, almost impossible)
     */
    private String uploadToDB(Order[] orders, Item[] items) throws ParseException {
        return uploadOrders(orders, items) +
                uploadItems(items);
    }

    private String uploadOrders(Order[] orders, Item[] items) throws ParseException {
        StringBuilder result = new StringBuilder();

        for (Order order : orders){
            boolean uploadSuccess = orderDAO.createOrder(items, order);

            if(!uploadSuccess){
                result.append("\nUpload failed when uploading order with OrderId(")
                        .append(order.getOrderId()).append(") and LineNumber(")
                        .append(order.getLineNumber())
                        .append(")");
                uploadFail.add(new UploadError(
                        order.getLineNumber(),
                        "Error when uploading data with OrderId(" + order.getOrderId() + ")"));
            }
        }

        return result.toString();
    }

    private String uploadItems(Item[] items) {
        StringBuilder result = new StringBuilder();

        for (Item item : items){
            boolean uploadSuccess = false;
            try{
                uploadSuccess = itemDAO.createItem(item);
            }catch (Exception ex){
                logger.debug(shellUsrEX.getErrorMessage(ex.toString()));
            }

            if (!uploadSuccess){
                result.append("\nUpload failed when uploading item with OrderId(")
                        .append(item.getOrderId()).append(") and LineNumber(")
                        .append(item.getLineNumber())
                        .append(") and ItemId(")
                        .append(item.getOrderItemId())
                        .append(")");
                uploadFail.add(new UploadError(
                        item.getLineNumber(),
                        "Error when uploading data with ItemId(" + item.getOrderItemId() + ")"));
            }else{
                this.uploadSuccess.add(item.getLineNumber() + "");
            }
        }

        return result.toString();
    }

}
