package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.functions.ftp.FtpNet;
import mdev.OrderProcessingSpring.functions.processing.Validator;
import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class Uploader {

    @Autowired
    public FtpNet ftpNet;

    @Autowired
    public Validator validator;

    public String upload(DataRow[] dataRows, boolean uploadResponseToFtp, boolean forceUpload){
        StringBuilder sb = new StringBuilder();

        validator.validate(dataRows, true);
        sb.append(validity(forceUpload, dataRows.length));
        sb.append(ftpUpload(uploadResponseToFtp));

        if (validator.isValid()){
            sb.append(db());
        }else if (!validator.isValid() && forceUpload){
            sb.append(db());
        }

        if (uploadResponseToFtp){
            sb.append(ftp());
        }

        sb.append("\nProcess Done.");
        return sb.toString();
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
                sb.append("Force uploading...");
            }else{
                sb.append("If you want to upload the correct data from the invalid file,\nplease enable \"force uploading\" with the \"-F\" parameter.");
            }
        }
        return sb.toString();
    }

    private String ftpUpload(boolean uploadResponseToFtp){
        if (uploadResponseToFtp){
            return "The results will be uploaded to your ftp server as a responseFile.";
        }
        return "The results wont be uploaded to your ftp server as a responseFile." +
                "\nIf you want to upload the results to an ftp server, please enable the \"-R\" parameter.";
    }

    // TO-DO
    @SuppressWarnings({"StringBufferReplaceableByString", "MismatchedQueryAndUpdateOfStringBuilder"})
    private String ftp(){
        StringBuilder result = new StringBuilder();

        //noinspection StatementWithEmptyBody
        if (!ftpNet.isConnected() && ftpNet.getConnectionDetail() != null){
            try {
                ftpNet.connect(ftpNet.getConnectionDetail());
                // Upload the response
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // Upload the response
        }

        return result.toString();
    }

    // TO-DO
    @SuppressWarnings({"StringBufferReplaceableByString", "MismatchedQueryAndUpdateOfStringBuilder"})
    private String db(){
        StringBuilder result = new StringBuilder();

        return result.toString();
    }

}
