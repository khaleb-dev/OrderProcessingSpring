package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.utils.DataRow;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class ValueCounter {

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
