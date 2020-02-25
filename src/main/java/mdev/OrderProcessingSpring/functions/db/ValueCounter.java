package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.utils.Order;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ValueCounter {

    /**
     * Called to count the value of an order (sum the item values with the same order id)
     * @param orderId The order id to search for items
     * @param orders the validated data
     * @return The total order value for the certain id
     */
    public float getOrderTotalValue(int orderId, Order[] orders){
        float value = 0;
        for (Order order : orders){
            if (order.getOrderId() == orderId){
                value += order.getSalePrice() + order.getShippingPrice();
            }
        }
        return value;
    }

}
