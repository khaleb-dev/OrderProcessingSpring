package mdev.orderProcessingSpring.utils;

import java.text.ParseException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public interface OrderDAO {

    boolean createOrder(Item[] items, Order dr) throws ParseException;

}
