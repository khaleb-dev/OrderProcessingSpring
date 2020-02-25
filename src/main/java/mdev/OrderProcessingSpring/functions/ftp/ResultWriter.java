package mdev.OrderProcessingSpring.functions.ftp;

import mdev.OrderProcessingSpring.OPSpringApp;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;
import mdev.OrderProcessingSpring.utils.FinalVars;
import mdev.OrderProcessingSpring.utils.UploadError;
import mdev.OrderProcessingSpring.utils.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ResultWriter {

    @Autowired
    public FinalVars finalVars;

    @Autowired
    public ShellUsrEX shellUsrEX;

    /**
     * Called to write the response file
     * @param uploadErrors The errors that occurred at the upload process
     * @param uploadSuccess The successfully uploaded data
     * @param validationErrors The errors that occurred at the validation process
     * @return The response file
     */
    public File write(ArrayList<UploadError> uploadErrors,
                      ArrayList<String> uploadSuccess,
                      ArrayList<ValidationError> validationErrors){
        StringBuilder builder = new StringBuilder(finalVars.HEADER_LINE_NUMBER + ";" +
                finalVars.HEADER_MESSAGE + ";" +
                finalVars.HEADER_STATUS + "\n");

        if (uploadSuccess != null){
            builder.append(formatUS(uploadSuccess));
        }

        if (validationErrors != null){
            builder.append(formatVE(validationErrors));
        }

        if (uploadErrors != null){
            builder.append(formatUF(uploadErrors));
        }

        return toFile(builder.toString());
    }

    private String formatUF(ArrayList<UploadError> uploadErrors){
        StringBuilder builder = new StringBuilder();
        for (UploadError ue : uploadErrors){
            builder.append(ue.getLineNumber())
                    .append(";")
                    .append(ue.getMes().replaceAll("\n", " "))
                    .append(";")
                    .append(finalVars.STATUS_ERROR)
                    .append("\n");
        }
        return builder.toString();
    }

    private String formatUS(ArrayList<String> uploadSuccess){
        StringBuilder builder = new StringBuilder();
        for (String s : uploadSuccess){
            builder.append(s)
                    .append(";")
                    .append(";")
                    .append(finalVars.STATUS_OK)
                    .append("\n");
        }
        return builder.toString();
    }

    private String formatVE(ArrayList<ValidationError> validationErrors){
        StringBuilder builder = new StringBuilder();
        for (ValidationError ve : validationErrors){
            builder.append(ve.getLineNumber())
                    .append(";")
                    .append(ve.getMessage().replaceAll("\n", " "))
                    .append(";")
                    .append(ve.getStatus())
                    .append("\n");
        }
        return builder.toString();
    }

    /**
     * Converts string into file
     * @param data The input
     * @return The file output
     */
    private File toFile(String data){
        File file = new File(getFileName());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.append(data);
            bw.close();
        } catch (IOException e) {
            OPSpringApp.log.error(shellUsrEX.getErrorMessage(e.toString()));
            return null;
        }
        return file;
    }

    /**
     * @return Generated file name
     */
    private String getFileName(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(finalVars.DATE_FORMAT);
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date) + "_upload_response.csv";
    }

}
