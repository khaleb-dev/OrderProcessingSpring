package mdev.OrderProcessingSpring.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class FinalVars {

    @Value("${ftp.pass.c.key}")
    private String cKey;

    public final String
            ERROR_EMAIL = "Invalid email format!";
    public final String ERROR_POSTCODE = "Invalid PostCode!";
    public final String ERROR_FILL = "Empty field(s) found:";
    public final String ERROR_DATE = "Invalid date format. Use the yyyy-MM-dd format.";
    public final String ERROR_SHIPPING_PRICE = "Shipping price must be bigger or equal with 0.";
    public final String ERROR_SALE_PRICE = "Sale price must be bigger or equal with 1.";
    public final String ERROR_STATUS = "Status must be a valid value of the enum in the database.";
    public final String ERROR_ORDER_ITEM_ID = "This orderItemId is already in use in the database";
    public final String ERROR_ORDER__ID = "This orderId is already in use in the database";
    public final String ERROR_NUMBER_FORMAT = "Bad number format!";

    public final String STATUS_OK = "OK";
    public final String STATUS_ERROR = "ERROR";
    public final String STATUS_IN = "IN_STOCK";
    public final String STATUS_OUT = "OUT_OF_STOCK";
    public final String DATE_FORMAT = "yyyy-MM-dd";

    public final String FTP_SERVER_KEY = "server";
    public final String FTP_PORT_KEY = "port";
    public final String FTP_USER_KEY = "u";
    public final String FTP_PASS_KEY = "p";
    public final String FTP_CONNECTION_DETAILS_PROPERTIES_NAME = "ftp_details";

    public final String ID_CHECK_RES_AS = "exists";

    public final String ORDER_ITEM_TABLE = "order_item";
    public final String ORDER_TABLE = "order_";
    public final String ORDER_ITEM_ID_COLUMN = "OrderItemId";
    public final String ORDER_ID_COLUMN = "OrderId";

    public final int DEFAULT_FTP_PORT = 21;

    private Cipher cipher;

    @PostConstruct
    public void initCipher(){
        try {
            cipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public Cipher getCipher() {
        return cipher;
    }

    public Key getcKey() {
        return new SecretKeySpec(cKey.getBytes(), "AES");
    }
}
