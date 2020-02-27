package mdev.orderProcessingSpring.utils;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public class UploadError {

    private final String mes;
    private final int lineNumber;

    public UploadError(int lineNumber, String mes){
        this.lineNumber = lineNumber;
        this.mes = mes;
    }

    public String getMes(){
        return mes;
    }

    public int getLineNumber(){
        return lineNumber;
    }

}
