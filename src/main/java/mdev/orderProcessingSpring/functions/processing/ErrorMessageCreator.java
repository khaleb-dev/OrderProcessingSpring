package mdev.orderProcessingSpring.functions.processing;

import mdev.orderProcessingSpring.utils.models.Item;
import mdev.orderProcessingSpring.utils.models.Order;
import mdev.orderProcessingSpring.utils.vars.ErrorCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ErrorMessageCreator {

    @Autowired
    private ErrorCodes errorCodes;

    public String createForOrder(
            boolean email, boolean fill, boolean date,
            boolean orderId, Order order){
        StringBuilder stringBuilder = new StringBuilder("\nError(s) while trying to upload the order with the \"");
        stringBuilder.append(order.getLineNumber());
        stringBuilder.append("\" line number.");
        stringBuilder.append(numberFormats(order));
        stringBuilder.append(fillErrors(fill, order));
        if (!email){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_EMAIL);
        }
        if (!date){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_DATE);
        }
        if (!orderId){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_ORDER__ID);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public String createForItem(
            boolean shippingPrice, boolean salePrice, boolean status,
            boolean orderItemId, boolean orderId, boolean fill, Item item){
        StringBuilder stringBuilder = new StringBuilder("\nError(s) while trying to upload the item with the \"");
        stringBuilder.append(item.getLineNumber());
        stringBuilder.append("\" line number.");
        stringBuilder.append(numberFormats(item));
        stringBuilder.append(fillErrors(fill, item));
        if (!shippingPrice){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_SHIPPING_PRICE);
        }
        if (!salePrice){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_SALE_PRICE);
        }
        if (!status){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_STATUS);
        }
        if (!orderItemId){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_ORDER_ITEM_ID);
        }
        if (!orderId){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_ORDER__ID);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private String fillErrors(boolean fill, Order dr){
        StringBuilder stringBuilder = new StringBuilder();
        if (!fill){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_FILL);
            stringBuilder.append(" ");
            if (dr.getBuyerEmail().isEmpty()){
                stringBuilder.append("| BuyerEmail ");
            }
            if (dr.getBuyerName().isEmpty()){
                stringBuilder.append("| BuyerName ");
            }
            if (dr.getAddress().isEmpty()){
                stringBuilder.append("| Address ");
            }
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    private String fillErrors(boolean fill, Item item){
        StringBuilder stringBuilder = new StringBuilder();
        if (!fill){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_FILL);
            stringBuilder.append(" ");
            if (item.getSKU().isEmpty()){
                stringBuilder.append("| SKU ");
            }
            if (item.getStatus().isEmpty()){
                stringBuilder.append("| Status ");
            }
            stringBuilder.append("|");
        }
        return stringBuilder.toString();
    }

    private String numberFormats(Order order){
        StringBuilder stringBuilder = new StringBuilder();
        if (order.getPostcode() == -1){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_POSTCODE);
        }
        if (order.getLineNumber() == -1){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_NUMBER_FORMAT);
        }
        return stringBuilder.toString();
    }

    private String numberFormats(Item item){
        StringBuilder stringBuilder = new StringBuilder();
        if (item.getLineNumber() == -1){
            stringBuilder.append("\n");
            stringBuilder.append(errorCodes.ERROR_NUMBER_FORMAT);
        }
        return stringBuilder.toString();
    }

}
