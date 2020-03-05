package mdev.orderProcessingSpring.functions.db.impl;

import mdev.orderProcessingSpring.functions.db.ValueCounter;
import mdev.orderProcessingSpring.utils.dao.IdDAO;
import mdev.orderProcessingSpring.utils.dao.ItemDAO;
import mdev.orderProcessingSpring.utils.models.Item;
import mdev.orderProcessingSpring.utils.vars.DataBaseVars;
import mdev.orderProcessingSpring.utils.vars.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Repository
public class UploadItemImpl implements ItemDAO {

    @Autowired
    private ValueCounter valueCounter;

    @Autowired
    private Headers headers;

    @Autowired
    private DataBaseVars dataBaseVars;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IdDAO idDAO;

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
                    item.getSalePrice(), item.getShippingPrice(), item.getSalePrice().add(item.getShippingPrice()),
                    item.getSKU(), item.getStatus()) > 0;
        }

        return false; // Foreign key does not exist
    }
}
