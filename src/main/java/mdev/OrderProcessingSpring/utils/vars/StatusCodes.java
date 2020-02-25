package mdev.OrderProcessingSpring.utils.vars;

import org.springframework.stereotype.Component;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@Component
public class StatusCodes {

    public final String STATUS_OK = "OK";
    public final String STATUS_ERROR = "ERROR";
    public final String STATUS_IN = "IN_STOCK";
    public final String STATUS_OUT = "OUT_OF_STOCK";

}
