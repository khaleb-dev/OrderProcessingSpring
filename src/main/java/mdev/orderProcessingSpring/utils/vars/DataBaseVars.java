package mdev.orderProcessingSpring.utils.vars;

import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class DataBaseVars {

    public final String DATE_FORMAT = "yyyy-MM-dd";
    public final String ID_CHECK_RES_AS = "exists";
    public final String ORDER_ITEM_TABLE = "order_item";
    public final String ORDER_TABLE = "order_";
    public final String ORDER_ITEM_ID_COLUMN = "OrderItemId";
    public final String ORDER_ID_COLUMN = "OrderId";

}
