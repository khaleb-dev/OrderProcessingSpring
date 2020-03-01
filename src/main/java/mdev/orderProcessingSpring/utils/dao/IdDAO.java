package mdev.orderProcessingSpring.utils.dao;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
public interface IdDAO {

    boolean validOrderItemId(int id);

    boolean validOrderIdInUse(int id);

    boolean idExists(String query);

}
