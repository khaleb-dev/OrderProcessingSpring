package mdev.OrderProcessingSpring.utils;

import java.text.ParseException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public interface OrderDAO {

    boolean createRow(Order[] orders, Order dr, String table) throws ParseException;

}
