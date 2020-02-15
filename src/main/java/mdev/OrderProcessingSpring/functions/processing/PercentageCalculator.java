package mdev.OrderProcessingSpring.functions.processing;

import mdev.OrderProcessingSpring.utils.FinalVars;
import mdev.OrderProcessingSpring.utils.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class PercentageCalculator {

    @Autowired
    public FinalVars finalVars;

    /**
     * Called to calculated the validity percentage of an invalid file (~ a file that contains any kind of invalid data)
     * @param drSize The number of the data rows recognized in the file
     * @param validationErrors The validation errors in the file (NumberFormatExceptions will be ignored - they are totally invalid)
     * @return The percentage
     */
    public float calc(int drSize, ArrayList<ValidationError> validationErrors){
        float vP = 0.0f;

        if (drSize != 1){
            int dontCountNumberFormat = 0;

            dontCountNumberFormat = validationErrors.stream().filter((ve) -> (ve.getMessage().equals("\nError on line " + ve.getLineNumber() + "\n" +
                    finalVars.ERROR_NUMBER_FORMAT + "\n"))).map((_item) -> 1).reduce(dontCountNumberFormat, Integer::sum);

            vP = 100.0f - ((float)(validationErrors.size() - dontCountNumberFormat)  /
                    (float)drSize * 100.0f);
        }

        return vP;
    }

}
