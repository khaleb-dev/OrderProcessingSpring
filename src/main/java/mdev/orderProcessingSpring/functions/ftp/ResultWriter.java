package mdev.orderProcessingSpring.functions.ftp;

import ch.qos.logback.classic.Logger;
import mdev.orderProcessingSpring.shell.ShellUsrEX;
import mdev.orderProcessingSpring.utils.UploadError;
import mdev.orderProcessingSpring.utils.ValidationError;
import mdev.orderProcessingSpring.utils.vars.DataBaseVars;
import mdev.orderProcessingSpring.utils.vars.Headers;
import mdev.orderProcessingSpring.utils.vars.StatusCodes;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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

    private Logger logger;

    @PostConstruct
    public void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(ResultWriter.class);
    }

    @Autowired
    public StatusCodes statusCodes;

    @Autowired
    public Headers headers;

    @Autowired
    public DataBaseVars dataBaseVars;

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
        StringBuilder builder = new StringBuilder(headers.HEADER_LINE_NUMBER + ";" +
                headers.HEADER_MESSAGE + ";" +
                headers.HEADER_STATUS + "\n");

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
                    .append(statusCodes.STATUS_ERROR)
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
                    .append(statusCodes.STATUS_OK)
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
            logger.error(shellUsrEX.getErrorMessage(e.toString()));
            return null;
        }
        return file;
    }

    /**
     * @return Generated file name
     */
    private String getFileName(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(dataBaseVars.DATE_FORMAT);
        Date date = new Date(System.currentTimeMillis());
        return dateFormat.format(date) + "_upload_response.csv";
    }

}
