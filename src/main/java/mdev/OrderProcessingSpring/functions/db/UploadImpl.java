package mdev.OrderProcessingSpring.functions.db;

import mdev.OrderProcessingSpring.utils.Order;
import mdev.OrderProcessingSpring.utils.vars.DataBaseVars;
import mdev.OrderProcessingSpring.utils.vars.Headers;
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
    public ValueCounter valueCounter;

    @Autowired
    public Headers headers;

    @Autowired
    public DataBaseVars dataBaseVars;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @SuppressWarnings("FieldCanBeLocal")
    private final String INSERT_ROW = "INSERT INTO ";

    /**
     * Overrides (interface) RowDAOs createRow method
     * @see OrderDAO#createOrder(Order[], Order, String)
     *
     * @param orders All of the validated rows
     * @param order The row that is being uploaded
     * @param table The name of the table the row is being uploaded into
     * @return True when the upload is successful
     * @throws ParseException Can throw exception for the DateFormats (very rare, almost impossible)
     */
    @Override
    public boolean createOrder(Order[] orders, Order order, String table) throws ParseException {
        String query = INSERT_ROW;
        if (table.equals(dataBaseVars.ORDER_TABLE)){
            query += dataBaseVars.ORDER_TABLE + "(`" + headers.HEADER_ORDER_ID + "`, " +
                    "`" + headers.HEADER_BUYER_NAME + "`, " +
                    "`" + headers.HEADER_BUYER_EMAIL + "`, " +
                    "`" + headers.HEADER_ORDER_DATE + "`, " +
                    "`" + headers.ORDER_TOTAL_VALUE + "`, " +
                    "`" + headers.HEADER_ADDRESS + "`, " +
                    "`" + headers.HEADER_POSTCODE + "`) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(query, order.getOrderId(), order.getBuyerName(), order.getBuyerEmail(),
                    (order.getOrderDate().isEmpty() ?
                            new Date(Calendar.getInstance().getTime().getTime()) :
                            new Date(new SimpleDateFormat(dataBaseVars.DATE_FORMAT).parse(order.getOrderDate()).getTime())),
                    valueCounter.getOrderTotalValue(order.getOrderId(), orders), order.getAddress(), order.getPostcode()) > 0;

        }else if(table.equals(dataBaseVars.ORDER_ITEM_TABLE)){
            query += dataBaseVars.ORDER_ITEM_TABLE + "(`" + headers.HEADER_ORDER_ITEM_ID + "`, " +
                            "`" + headers.HEADER_ORDER_ID + "`, " +
                            "`" + headers.HEADER_SALE_PRICE + "`, " +
                            "`" + headers.HEADER_SHIPPING_PRICE + "`, " +
                            "`" + headers.ORDER_ITEM_TOTAL_VALUE + "`, " +
                            "`" + headers.HEADER_SKU + "`, " +
                            "`" + headers.HEADER_STATUS + "`) "+
                            "VALUES(?, ?, ?, ?, ?, ?, ?)";

            return jdbcTemplate.update(query, order.getOrderItemId(), order.getOrderId(),
                    order.getSalePrice(), order.getShippingPrice(), order.getSalePrice() + order.getShippingPrice(),
                    order.getSKU(), order.getStatus()) > 0;

        }
        return false;
    }
}
