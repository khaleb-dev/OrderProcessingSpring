package mdev.OrderProcessingSpring.functions.db;

import ch.qos.logback.classic.Logger;
import mdev.OrderProcessingSpring.functions.ftp.FtpNet;
import mdev.OrderProcessingSpring.functions.ftp.ResultWriter;
import mdev.OrderProcessingSpring.functions.processing.Validator;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;
import mdev.OrderProcessingSpring.utils.Order;
import mdev.OrderProcessingSpring.utils.OrderDAO;
import mdev.OrderProcessingSpring.utils.UploadError;
import mdev.OrderProcessingSpring.utils.vars.DataBaseVars;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    public void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(Uploader.class);
    }

    @Autowired
    public FtpNet ftpNet;

    @Autowired
    public Validator validator;

    @Autowired
    public ApplicationContext context;

    @Autowired
    public DataBaseVars dataBaseVars;

    @Autowired
    public ShellUsrEX shellUsrEX;

    @Autowired
    public ResultWriter resultWriter;

    private StringBuilder stringBuilder;
    private ArrayList<UploadError> uploadFail;
    private ArrayList<String> uploadSuccess;

    /**
     * Called to upload data to the database from CommandFunctions
     * @see mdev.OrderProcessingSpring.functions.CommandFunctions#upload(Order[], boolean, boolean)
     * @see mdev.OrderProcessingSpring.shell.Commands#uploadFile(String, boolean, boolean)
     *
     * @param orders The not fully validated rows
     *                 (number format exceptions are validated at reading the file but more validation is needed)
     * @param uploadResponseToFTP True when the response is needed to be uploaded to the FTP server
     * @param forceUpload True when the valid data is needed to be uploaded to the database from an invalid file
     * @return The results of the process
     */
    public String upload(Order[] orders, boolean uploadResponseToFTP, boolean forceUpload){
        init();

        validator.validate(orders, true);
        stringBuilder.append(validity(forceUpload, orders.length));
        stringBuilder.append(ftpUpload(uploadResponseToFTP));

        try {
            if (validator.isValid()){
                stringBuilder.append(db(validator.getValidData()));
            }else if (!validator.isValid() && forceUpload){
                stringBuilder.append(db(validator.getValidData()));
            }
        } catch (ParseException e) {
            logger.error(shellUsrEX.getErrorMessage(e.toString()));
        }

        if (uploadResponseToFTP){
            stringBuilder.append(ftp());
        }

        stringBuilder.append("\nProcess Done.");
        return stringBuilder.toString();
    }

    /**
     * Initializes objects for the upload process
     * @see #upload(Order[], boolean, boolean)
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
    private String ftp(){
        StringBuilder result = new StringBuilder();

        if (!ftpNet.isConnected() && ftpNet.getConnectionDetail() != null){
            try {
                ftpNet.connect(ftpNet.getConnectionDetail());
                result.append(ftpU() ?
                            "\nResponse uploaded to the FTP server successfully!" :
                            "\nFailed to upload the response to the FTP server!");
            } catch (IOException e) {
                logger.error(shellUsrEX.getErrorMessage(e.toString()));
            }
        }else{
            try {
                result.append(ftpU() ?
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
    private boolean ftpU() throws IOException {
        return ftpNet.upload(resultWriter.write(
                uploadFail,
                uploadSuccess,
                validator.getValidationErrors()),
                ftpNet.getConnectionDetail());
    }

    /**
     * Called to upload data into the database
     * @param orders Validated correct data
     * @return The results of the process (for printing purposes in the CLI)
     * @throws ParseException Can throw exception for the DateFormats (very rare, almost impossible)
     */
    private String db(Order[] orders) throws ParseException {
        StringBuilder result = new StringBuilder();

        OrderDAO orderDAO = context.getBean(OrderDAO.class);
        for (Order order : orders){
            boolean u1 = orderDAO.createRow(orders, order, dataBaseVars.ORDER_TABLE);
            boolean u2 = orderDAO.createRow(orders, order, dataBaseVars.ORDER_ITEM_TABLE);

            if(!u1){
                result.append("\nUpload failed when uploading row with OrderId(")
                        .append(order.getOrderId()).append(") and LineNumber(")
                        .append(order.getLineNumber())
                        .append(")");
                uploadFail.add(new UploadError(
                        order.getLineNumber(),
                        "Error when uploading data with OrderId(" + order.getOrderId() + ")"));
            }else{
                if (!u2){
                    result.append("\nUpload failed when uploading row with OrderId(")
                            .append(order.getOrderId()).append(") and LineNumber(")
                            .append(order.getLineNumber())
                            .append(") and OrderItemId(")
                            .append(order.getLineNumber())
                            .append(")");
                    uploadFail.add(new UploadError(
                            order.getLineNumber(),
                            "Error when uploading data with OrderItemId(" + order.getOrderItemId() + ")"));
                }else{
                    uploadSuccess.add(order.getLineNumber() + "");
                }
            }
        }

        return result.toString();
    }

}
