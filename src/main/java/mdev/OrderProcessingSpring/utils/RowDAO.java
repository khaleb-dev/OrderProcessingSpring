package mdev.OrderProcessingSpring.utils;

import java.text.ParseException;

public interface RowDAO {

    boolean createRow(DataRow[] dataRows, DataRow dr, String table) throws ParseException;

}
