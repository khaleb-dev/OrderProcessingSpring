package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.utils.DataRow;
import mdev.OrderProcessingSpring.utils.FinalVars;
import mdev.OrderProcessingSpring.utils.RowDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class UploadImpl implements RowDAO {

    @Autowired
    ValueCounter valueCounter;

    @Autowired
    FinalVars finalVars;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @SuppressWarnings("FieldCanBeLocal")
    private final String INSERT_ROW = "INSERT INTO ";

    @Override
    public boolean createRow(DataRow[] dataRows, DataRow dr, String table) throws ParseException {
        String q = INSERT_ROW;
        if (table.equals(finalVars.ORDER_TABLE)){
            q += finalVars.ORDER_TABLE + "(`" + finalVars.HEADER_ORDER_ID + "`, " +
                    "`" + finalVars.HEADER_BUYER_NAME + "`, " +
                    "`" + finalVars.HEADER_BUYER_EMAIL + "`, " +
                    "`" + finalVars.HEADER_ORDER_DATE + "`, " +
                    "`" + finalVars.ORDER_TOTAL_VALUE + "`, " +
                    "`" + finalVars.HEADER_ADDRESS + "`, " +
                    "`" + finalVars.HEADER_POSTCODE + "`) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(q, dr.getOrderId(), dr.getBuyerName(), dr.getBuyerEmail(),
                    (dr.getOrderDate().isEmpty() ?
                            new Date(Calendar.getInstance().getTime().getTime()) :
                            new Date(new SimpleDateFormat(finalVars.DATE_FORMAT).parse(dr.getOrderDate()).getTime())),
                    valueCounter.getOrderTotalValue(dr.getOrderId(), dataRows), dr.getAddress(), dr.getPostcode()) > 0;

        }else if(table.equals(finalVars.ORDER_ITEM_TABLE)){
            q += finalVars.ORDER_ITEM_TABLE + "(`" + finalVars.HEADER_ORDER_ITEM_ID + "`, " +
                            "`" + finalVars.HEADER_ORDER_ID + "`, " +
                            "`" + finalVars.HEADER_SALE_PRICE + "`, " +
                            "`" + finalVars.HEADER_SHIPPING_PRICE + "`, " +
                            "`" + finalVars.ORDER_ITEM_TOTAL_VALUE + "`, " +
                            "`" + finalVars.HEADER_SKU + "`, " +
                            "`" + finalVars.HEADER_STATUS + "`) "+
                            "VALUES(?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(q, dr.getOrderItemId(), dr.getOrderId(),
                    dr.getSalePrice(), dr.getShippingPrice(), dr.getSalePrice() + dr.getShippingPrice(),
                    dr.getSKU(), dr.getStatus()) > 0;

        }
        return false;
    }
}
