package mdev.orderProcessingSpring.functions.db;

import mdev.orderProcessingSpring.utils.models.Item;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
    public BigDecimal getOrderTotalValue(int orderId, Item[] items){
        BigDecimal value = BigDecimal.ZERO;
        for (Item item : items){
            if (item.getOrderId() == orderId){
                value = value.add(item.getSalePrice().add(item.getShippingPrice()));
            }
        }
        return value;
    }

}
