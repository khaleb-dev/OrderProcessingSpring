package mdev.OrderProcessingSpring.functions.ftp;

import mdev.OrderProcessingSpring.utils.UploadError;
import mdev.OrderProcessingSpring.utils.ValidationError;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;

@Component
public class ResultWriter {

    @SuppressWarnings("unused")
    public File write(ArrayList<UploadError> uploadErrors,
                      ArrayList<String> uploadSuccess,
                      ArrayList<ValidationError> validationErrors){
        // TO-DO
        return null;
    }

}
