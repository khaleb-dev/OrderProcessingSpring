package mdev.OrderProcessingSpring.functions.processing;

import mdev.OrderProcessingSpring.utils.Order;
import mdev.OrderProcessingSpring.utils.vars.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ErrorMessageCreator {

    @Autowired
    public ErrorCodes errorCodes;

    public String create(
            boolean email, boolean fill, boolean date,
            boolean shippingPrice, boolean salePrice, boolean status,
            boolean orderItemId, boolean orderId, Order dr){
        String message = "\nError(s) while trying to upload the row with the \"" +
                dr.getLineNumber() + "\" line number.";
        message += numberFormats(dr);
        message += fillErrors(fill, dr);
        if (!email){
            message += "\n" + errorCodes.ERROR_EMAIL;
        }
        if (!date){
            message += "\n" + errorCodes.ERROR_DATE;
        }
        if (!shippingPrice){
            message += "\n" + errorCodes.ERROR_SHIPPING_PRICE;
        }
        if (!salePrice){
            message += "\n" + errorCodes.ERROR_SALE_PRICE;
        }
        if (!status){
            message += "\n" + errorCodes.ERROR_STATUS;
        }
        if (!orderItemId){
            message += "\n" + errorCodes.ERROR_ORDER_ITEM_ID;
        }
        if (!orderId){
            message += "\n" + errorCodes.ERROR_ORDER__ID;
        }
        return message + "\n";
    }

    private String fillErrors(boolean fill, Order dr){
        String message = "";
        if (!fill){
            message += "\n" + errorCodes.ERROR_FILL + " ";
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

    private String numberFormats(Order dr){
        String message = "";
        if (dr.getPostcode() == -1){
            message += "\n" + errorCodes.ERROR_POSTCODE;
        }
        if (dr.getLineNumber() == -1){
            message += "\n" + errorCodes.ERROR_NUMBER_FORMAT;
        }
        return message;
    }

}
