package mdev.orderProcessingSpring.functions.processing;

import mdev.orderProcessingSpring.utils.Item;
import mdev.orderProcessingSpring.utils.Order;
import mdev.orderProcessingSpring.utils.vars.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ErrorMessageCreator {

    @Autowired
    public ErrorCodes errorCodes;

    public String createForOrder(
            boolean email, boolean fill, boolean date,
            boolean orderId, Order order){
        String message = "\nError(s) while trying to upload the order with the \"" +
                order.getLineNumber() + "\" line number.";
        message += numberFormats(order);
        message += fillErrors(fill, order);
        if (!email){
            message += "\n" + errorCodes.ERROR_EMAIL;
        }
        if (!date){
            message += "\n" + errorCodes.ERROR_DATE;
        }
        if (!orderId){
            message += "\n" + errorCodes.ERROR_ORDER__ID;
        }
        return message + "\n";
    }

    public String createForItem(
            boolean shippingPrice, boolean salePrice, boolean status,
            boolean orderItemId, boolean orderId, boolean fill, Item item){
        String message = "\nError(s) while trying to upload the item with the \"" +
                item.getLineNumber() + "\" line number.";
        message += numberFormats(item);
        message += fillErrors(fill, item);
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
            if (dr.getAddress().isEmpty()){
                message += "| Address ";
            }
            message += "|";
        }
        return message;
    }

    private String fillErrors(boolean fill, Item item){
        String message = "";
        if (!fill){
            message += "\n" + errorCodes.ERROR_FILL + " ";
            if (item.getSKU().isEmpty()){
                message += "| SKU ";
            }
            if (item.getStatus().isEmpty()){
                message += "| Status ";
            }
            message += "|";
        }
        return message;
    }

    private String numberFormats(Order order){
        String message = "";
        if (order.getPostcode() == -1){
            message += "\n" + errorCodes.ERROR_POSTCODE;
        }
        if (order.getLineNumber() == -1){
            message += "\n" + errorCodes.ERROR_NUMBER_FORMAT;
        }
        return message;
    }

    private String numberFormats(Item item){
        String message = "";
        if (item.getLineNumber() == -1){
            message += "\n" + errorCodes.ERROR_NUMBER_FORMAT;
        }
        return message;
    }

}
