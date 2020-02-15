package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.OPSpringApp;
import mdev.OrderProcessingSpring.functions.ftp.FtpNet;
import mdev.OrderProcessingSpring.functions.ftp.ResultWriter;
import mdev.OrderProcessingSpring.functions.processing.Validator;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;
import mdev.OrderProcessingSpring.utils.DataRow;
import mdev.OrderProcessingSpring.utils.FinalVars;
import mdev.OrderProcessingSpring.utils.RowDAO;
import mdev.OrderProcessingSpring.utils.UploadError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class Uploader {

    @Autowired
    public FtpNet ftpNet;

    @Autowired
    public Validator validator;

    @Autowired
    public ApplicationContext context;

    @Autowired
    public FinalVars finalVars;



    @Autowired
    public ShellUsrEX shellUsrEX;

    @Autowired
    public ResultWriter resultWriter;

    private StringBuilder sb;
    private ArrayList<UploadError> uploadFail;
    private ArrayList<String> uploadSuccess;

    /**
     * Called to upload data to the database from CommandFunctions
     * @see mdev.OrderProcessingSpring.functions.CommandFunctions#upload(DataRow[], boolean, boolean)
     * @see mdev.OrderProcessingSpring.shell.Commands#uploadFile(String, boolean, boolean)
     *
     * @param dataRows The not fully validated rows
     *                 (number format exceptions are validated at reading the file but more validation is needed)
     * @param uploadResponseToFtp True when the response is needed to be uploaded to the FTP server
     * @param forceUpload True when the valid data is needed to be uploaded to the database from an invalid file
     * @return The results of the process
     */
    public String upload(DataRow[] dataRows, boolean uploadResponseToFtp, boolean forceUpload){
        init();

        validator.validate(dataRows, true);
        sb.append(validity(forceUpload, dataRows.length));
        sb.append(ftpUpload(uploadResponseToFtp));

        try {
            if (validator.isValid()){
                sb.append(db(validator.getValidData()));
            }else if (!validator.isValid() && forceUpload){
                sb.append(db(validator.getValidData()));
            }
        } catch (ParseException e) {
            OPSpringApp.log.error(shellUsrEX.getErrorMessage(e.toString()));
        }

        if (uploadResponseToFtp){
            sb.append(ftp());
        }

        sb.append("\nProcess Done.");
        return sb.toString();
    }

    /**
     * Initializes objects for the upload process
     * @see #upload(DataRow[], boolean, boolean)
     */
    private void init(){
        sb = new StringBuilder();
        uploadFail = new ArrayList<>();
        uploadSuccess = new ArrayList<>();
    }

    /**
     * Generates some validity info about the file
     * @param forceUpload True when the valid data is needed to be uploaded to the database from an invalid file
     * @param drSize The size of the not validated data list
     * @return Validity information
     */
    private String validity(boolean forceUpload, int drSize){
        StringBuilder sb = new StringBuilder();
        if (validator.isValid()){
            sb.append("\nThe file is 100% valid! Uploading...");
        }else{
            sb
                    .append("\nThe file is ")
                    .append(validator.percentageCalculator.calc(drSize, validator.getValidationErrors()))
                    .append("% valid!");
            if (forceUpload){
                sb.append("\nForce uploading...");
            }else{
                sb.append("\n\nIf you want to upload the correct data from the invalid file,\nplease enable \"force uploading\" with the \"-F\" parameter.");
            }
        }
        return sb.toString();
    }

    /**
     * Generates info about the FTP upload process
     * @param uploadResponseToFtp True when the response is needed to be uploaded to the FTP server
     * @return FTP upload information
     */
    private String ftpUpload(boolean uploadResponseToFtp){
        if (uploadResponseToFtp){
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
                OPSpringApp.log.error(shellUsrEX.getErrorMessage(e.toString()));
            }
        }else{
            try {
                result.append(ftpU() ?
                        "\nResponse uploaded to the FTP server successfully!" :
                        "\nFailed to upload the response to the FTP server!");
            } catch (IOException e) {
                OPSpringApp.log.error(shellUsrEX.getErrorMessage(e.toString()));
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
     * @param dataRows Validated correct data
     * @return The results of the process (for printing purposes in the CLI)
     * @throws ParseException Can throw exception for the DateFormats (very rare, almost impossible)
     */
    private String db(DataRow[] dataRows) throws ParseException {
        StringBuilder result = new StringBuilder();

        RowDAO rowDAO = context.getBean(RowDAO.class);
        for (DataRow dr : dataRows){
            boolean u1 = rowDAO.createRow(dataRows, dr, finalVars.ORDER_TABLE);
            boolean u2 = rowDAO.createRow(dataRows, dr, finalVars.ORDER_ITEM_TABLE);

            if(!u1){
                result.append("\nUpload failed when uploading row with OrderId(")
                        .append(dr.getOrderId()).append(") and LineNumber(")
                        .append(dr.getLineNumber())
                        .append(")");
                uploadFail.add(new UploadError(
                        dr.getLineNumber(),
                        "Error when uploading data with OrderId(" + dr.getOrderId() + ")"));
            }else{
                if (!u2){
                    result.append("\nUpload failed when uploading row with OrderId(")
                            .append(dr.getOrderId()).append(") and LineNumber(")
                            .append(dr.getLineNumber())
                            .append(") and OrderItemId(")
                            .append(dr.getLineNumber())
                            .append(")");
                    uploadFail.add(new UploadError(
                            dr.getLineNumber(),
                            "Error when uploading data with OrderItemId(" + dr.getOrderItemId() + ")"));
                }else{
                    uploadSuccess.add(dr.getLineNumber() + "");
                }
            }
        }

        return result.toString();
    }

}
