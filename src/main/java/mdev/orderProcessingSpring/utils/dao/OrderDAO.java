package mdev.orderProcessingSpring.utils.dao;

import mdev.orderProcessingSpring.utils.models.Item;
import mdev.orderProcessingSpring.utils.models.Order;

import java.text.ParseException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public interface OrderDAO {

    boolean createOrder(Item[] items, Order dr) throws ParseException;

}
