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
        StringBuilder sb = new StringBuilder(finalVars.HEADER_LINE_NUMBER + ";" +
                finalVars.HEADER_MESSAGE + ";" +
                finalVars.HEADER_STATUS + "\n");

        if (uploadSuccess != null){
            sb.append(formatUS(uploadSuccess));
        }

        if (validationErrors != null){
            sb.append(formatVE(validationErrors));
        }

        if (uploadErrors != null){
            sb.append(formatUF(uploadErrors));
        }

        return toFile(sb.toString());
    }

    private String formatUF(ArrayList<UploadError> uploadErrors){
        StringBuilder d = new StringBuilder();
        for (UploadError ue : uploadErrors){
            d.append(ue.getLineNumber())
                    .append(";")
                    .append(ue.getMes().replaceAll("\n", " "))
                    .append(";")
                    .append(finalVars.STATUS_ERROR)
                    .append("\n");
        }
        return d.toString();
    }

    private String formatUS(ArrayList<String> uploadSuccess){
        StringBuilder d = new StringBuilder();
        for (String s : uploadSuccess){
            d.append(s)
                    .append(";")
                    .append(";")
                    .append(finalVars.STATUS_OK)
                    .append("\n");
        }
        return d.toString();
    }

    private String formatVE(ArrayList<ValidationError> validationErrors){
        StringBuilder d = new StringBuilder();
        for (ValidationError ve : validationErrors){
            d.append(ve.getLineNumber())
                    .append(";")
                    .append(ve.getMessage().replaceAll("\n", " "))
                    .append(";")
                    .append(ve.getStatus())
                    .append("\n");
        }
        return d.toString();
    }

    /**
     * Converts string into file
     * @param data The input
     * @return The file output
     */
    private File toFile(String data){
        File f = new File(getFileName());
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append(data);
            bw.close();
        } catch (IOException e) {
            OPSpringApp.log.error(shellUsrEX.getErrorMessage(e.toString()));
            return null;
        }
        return f;
    }

    /**
     * @return Generated file name
     */
    private String getFileName(){
        SimpleDateFormat f = new SimpleDateFormat(finalVars.DATE_FORMAT);
        Date date = new Date(System.currentTimeMillis());
        return f.format(date) + "_upload_response.csv";
    }

}
