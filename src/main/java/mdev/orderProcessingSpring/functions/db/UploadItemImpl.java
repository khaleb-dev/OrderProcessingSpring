package mdev.orderProcessingSpring.functions.db;

import mdev.orderProcessingSpring.utils.IdDAO;
import mdev.orderProcessingSpring.utils.Item;
import mdev.orderProcessingSpring.utils.ItemDAO;
import mdev.orderProcessingSpring.utils.vars.DataBaseVars;
import mdev.orderProcessingSpring.utils.vars.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class UploadItemImpl implements ItemDAO {

    @Autowired
    public ApplicationContext context;

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
     * Overrides (interface) ItemDAOs createItem method
     * @see ItemDAO#createItem(Item)
     *
     * @param item The item that is being uploaded
     * @return True when the upload is successful
     */
    @Override
    public boolean createItem(Item item) {
        IdDAO idDAO = context.getBean(IdDAO.class);

        String query = INSERT_ROW + dataBaseVars.ORDER_ITEM_TABLE + "(`" + headers.HEADER_ORDER_ITEM_ID + "`, " +
                "`" + headers.HEADER_ORDER_ID + "`, " +
                "`" + headers.HEADER_SALE_PRICE + "`, " +
                "`" + headers.HEADER_SHIPPING_PRICE + "`, " +
                "`" + headers.ORDER_ITEM_TOTAL_VALUE + "`, " +
                "`" + headers.HEADER_SKU + "`, " +
                "`" + headers.HEADER_STATUS + "`) "+
                "VALUES(?, ?, ?, ?, ?, ?, ?)";

        if (idDAO.validOrderIdInUse(item.getOrderId()) && item.getOrderId() != -1){
            return jdbcTemplate.update(query, item.getOrderItemId(), item.getOrderId(),
                    item.getSalePrice(), item.getShippingPrice(), item.getSalePrice() + item.getShippingPrice(),
                    item.getSKU(), item.getStatus()) > 0;
        }

        return false; // Foreign key does not exist
    }
}
