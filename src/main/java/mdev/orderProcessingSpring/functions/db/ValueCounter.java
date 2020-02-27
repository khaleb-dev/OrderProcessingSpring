package mdev.orderProcessingSpring.functions.db;

import mdev.orderProcessingSpring.utils.Item;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ValueCounter {

    /**
     * Called to count the value of an order (sum the item values with the same order id)
     * @param orderId The order id to search for items
     * @param items the validated data
     * @return The total order value for the certain id
     */
    public float getOrderTotalValue(int orderId, Item[] items){
        float value = 0;
        for (Item item : items){
            if (item.getOrderId() == orderId){
                value += item.getSalePrice() + item.getShippingPrice();
            }
        }
        return value;
    }

}
