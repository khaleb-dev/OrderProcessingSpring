package mdev.OrderProcessingSpring.utils;

public class ValidationError {

    private final String message, status;
    private final int lineNumber;

    public ValidationError(int lineNumber, String message, String status){
        this.message = message;
        this.lineNumber = lineNumber;
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }

    public int getLineNumber(){
        return lineNumber;
    }

}
