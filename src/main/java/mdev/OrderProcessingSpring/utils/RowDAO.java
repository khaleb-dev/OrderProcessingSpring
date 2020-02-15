package mdev.OrderProcessingSpring.utils;

import java.text.ParseException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public interface RowDAO {

    boolean createRow(DataRow[] dataRows, DataRow dr, String table) throws ParseException;

}
