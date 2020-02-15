package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ValueCounter {

    /**
     * Called to count the value of an order (sum the item values with the same order id)
     * @param orderId The order id to search for items
     * @param rows the validated data
     * @return The total order value for the certain id
     */
    public float getOrderTotalValue(int orderId, DataRow[] rows){
        float value = 0;
        for (DataRow dr : rows){
            if (dr.getOrderId() == orderId){
                value += dr.getSalePrice() + dr.getShippingPrice();
            }
        }
        return value;
    }

}
