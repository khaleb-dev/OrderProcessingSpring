package mdev.OrderProcessingSpring.functions.processing;

import mdev.OrderProcessingSpring.utils.DataRow;
import mdev.OrderProcessingSpring.utils.FinalVars;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ErrorMessageCreator {

    @Autowired
    public FinalVars finalVars;

    public String create(
            boolean email, boolean fill, boolean date,
            boolean shippingPrice, boolean salePrice, boolean status,
            boolean orderItemId, boolean orderId, DataRow dr){
        String message = "\nError(s) while trying to upload the row with the \"" +
                dr.getLineNumber() + "\" line number.";
        message += numberFormats(dr);
        message += fillErrors(fill, dr);
        if (!email){
            message += "\n" + finalVars.ERROR_EMAIL;
        }
        if (!date){
            message += "\n" + finalVars.ERROR_DATE;
        }
        if (!shippingPrice){
            message += "\n" + finalVars.ERROR_SHIPPING_PRICE;
        }
        if (!salePrice){
            message += "\n" + finalVars.ERROR_SALE_PRICE;
        }
        if (!status){
            message += "\n" + finalVars.ERROR_STATUS;
        }
        if (!orderItemId){
            message += "\n" + finalVars.ERROR_ORDER_ITEM_ID;
        }
        if (!orderId){
            message += "\n" + finalVars.ERROR_ORDER__ID;
        }
        return message + "\n";
    }

    private String fillErrors(boolean fill, DataRow dr){
        String message = "";
        if (!fill){
            message += "\n" + finalVars.ERROR_FILL + " ";
            if (dr.getBuyerEmail().isEmpty()){
                message += "| BuyerEmail ";
            }
            if (dr.getBuyerName().isEmpty()){
                message += "| BuyerName ";
            }
            if (dr.getSKU().isEmpty()){
                message += "| SKU ";
            }
            if (dr.getStatus().isEmpty()){
                message += "| Status ";
            }
            if (dr.getAddress().isEmpty()){
                message += "| Address ";
            }
            message += "|";
        }
        return message;
    }

    private String numberFormats(DataRow dr){
        String message = "";
        if (dr.getPostcode() == -1){
            message += "\n" + finalVars.ERROR_POSTCODE;
        }
        if (dr.getLineNumber() == -1){
            message += "\n" + finalVars.ERROR_NUMBER_FORMAT;
        }
        return message;
    }

}
