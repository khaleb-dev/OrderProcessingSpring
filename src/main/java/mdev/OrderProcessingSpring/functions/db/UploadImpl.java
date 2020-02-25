package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.utils.Order;
import mdev.OrderProcessingSpring.utils.FinalVars;
import mdev.OrderProcessingSpring.utils.OrderDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class UploadImpl implements OrderDAO {

    @Autowired
    ValueCounter valueCounter;

    @Autowired
    FinalVars finalVars;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @SuppressWarnings("FieldCanBeLocal")
    private final String INSERT_ROW = "INSERT INTO ";

    /**
     * Overrides (interface) RowDAOs createRow method
     * @see OrderDAO#createRow(Order[], Order, String)
     *
     * @param orders All of the validated rows
     * @param order The row that is being uploaded
     * @param table The name of the table the row is being uploaded into
     * @return True when the upload is successful
     * @throws ParseException Can throw exception for the DateFormats (very rare, almost impossible)
     */
    @Override
    public boolean createRow(Order[] orders, Order order, String table) throws ParseException {
        String query = INSERT_ROW;
        if (table.equals(finalVars.ORDER_TABLE)){
            query += finalVars.ORDER_TABLE + "(`" + finalVars.HEADER_ORDER_ID + "`, " +
                    "`" + finalVars.HEADER_BUYER_NAME + "`, " +
                    "`" + finalVars.HEADER_BUYER_EMAIL + "`, " +
                    "`" + finalVars.HEADER_ORDER_DATE + "`, " +
                    "`" + finalVars.ORDER_TOTAL_VALUE + "`, " +
                    "`" + finalVars.HEADER_ADDRESS + "`, " +
                    "`" + finalVars.HEADER_POSTCODE + "`) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(query, order.getOrderId(), order.getBuyerName(), order.getBuyerEmail(),
                    (order.getOrderDate().isEmpty() ?
                            new Date(Calendar.getInstance().getTime().getTime()) :
                            new Date(new SimpleDateFormat(finalVars.DATE_FORMAT).parse(order.getOrderDate()).getTime())),
                    valueCounter.getOrderTotalValue(order.getOrderId(), orders), order.getAddress(), order.getPostcode()) > 0;

        }else if(table.equals(finalVars.ORDER_ITEM_TABLE)){
            query += finalVars.ORDER_ITEM_TABLE + "(`" + finalVars.HEADER_ORDER_ITEM_ID + "`, " +
                            "`" + finalVars.HEADER_ORDER_ID + "`, " +
                            "`" + finalVars.HEADER_SALE_PRICE + "`, " +
                            "`" + finalVars.HEADER_SHIPPING_PRICE + "`, " +
                            "`" + finalVars.ORDER_ITEM_TOTAL_VALUE + "`, " +
                            "`" + finalVars.HEADER_SKU + "`, " +
                            "`" + finalVars.HEADER_STATUS + "`) "+
                            "VALUES(?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(query, order.getOrderItemId(), order.getOrderId(),
                    order.getSalePrice(), order.getShippingPrice(), order.getSalePrice() + order.getShippingPrice(),
                    order.getSKU(), order.getStatus()) > 0;

        }
        return false;
    }
}
