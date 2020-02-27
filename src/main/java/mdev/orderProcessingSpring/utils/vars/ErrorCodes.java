package mdev.orderProcessingSpring.utils.vars;

import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ErrorCodes {

    public final String
            ERROR_EMAIL = "Invalid email format!";
    public final String ERROR_POSTCODE = "Invalid PostCode!";
    public final String ERROR_FILL = "Empty field(s) found:";
    public final String ERROR_DATE = "Invalid date format. Use the yyyy-MM-dd format.";
    public final String ERROR_SHIPPING_PRICE = "Shipping price must be bigger or equal with 0.";
    public final String ERROR_SALE_PRICE = "Sale price must be bigger or equal with 1.";
    public final String ERROR_STATUS = "Status must be a valid value of the enum in the database.";
    public final String ERROR_ORDER_ITEM_ID = "This orderItemId is already in use in the database";
    public final String ERROR_ORDER__ID = "This orderId is already in use in the database";
    public final String ERROR_NUMBER_FORMAT = "Bad number format!";

}
