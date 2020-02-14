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

    private void init(){
        sb = new StringBuilder();
        uploadFail = new ArrayList<>();
        uploadSuccess = new ArrayList<>();
    }

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

    private String ftpUpload(boolean uploadResponseToFtp){
        if (uploadResponseToFtp){
            return "\n\nThe results will be uploaded to your ftp server as a responseFile.";
        }
        return "\n\nThe results wont be uploaded to your ftp server as a responseFile." +
                "\nIf you want to upload the results to an ftp server, please enable the \"-R\" parameter.";
    }

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

    private boolean ftpU() throws IOException {
        return ftpNet.upload(resultWriter.write(
                uploadFail,
                uploadSuccess,
                validator.getValidationErrors()),
                ftpNet.getConnectionDetail());
    }

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
