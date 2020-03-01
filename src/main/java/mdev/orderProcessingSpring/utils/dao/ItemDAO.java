package mdev.orderProcessingSpring.utils.dao;

import mdev.orderProcessingSpring.utils.models.Item;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public interface ItemDAO {

    boolean createItem(Item item);

}
