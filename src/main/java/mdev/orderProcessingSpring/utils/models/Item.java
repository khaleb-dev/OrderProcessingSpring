package mdev.orderProcessingSpring.utils.models;

import ch.qos.logback.classic.Logger;
import mdev.orderProcessingSpring.shell.ShellUsrEX;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@SuppressWarnings("unused")
public class Item {

    private Logger logger;

    @PostConstruct
    public void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(Item.class);
    }

    private String LineNumber, OrderItemId, OrderId;
    private String SalePrice, ShippingPrice;

    private String SKU, Status;

    private ShellUsrEX shellUsrEX;

    private boolean warn = true;

    public int getLineNumber(){
        try{
            return Integer.parseInt(LineNumber);
        }catch (Exception ex){
            if (warn){
                logger.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + LineNumber));
            }
        }
        return -1;
    }

    public int getOrderItemId(){
        try{
            return Integer.parseInt(OrderItemId);
        }catch (Exception ex){
            if (warn){
                logger.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
            }
        }
        return -1;
    }

    public int getOrderId(){
        try{
            return Integer.parseInt(OrderId);
        }catch (Exception ex){
            if (warn){
                logger.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
            }
        }
        return -1;
    }

    public float getSalePrice(){
        try{
            return Float.parseFloat(SalePrice);
        }catch (Exception ex){
            if (warn){
                logger.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
            }
        }
        return -1;
    }

    public float getShippingPrice(){
        try{
            return Float.parseFloat(ShippingPrice);
        }catch (Exception ex){
            if (warn){
                logger.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
            }
        }
        return -1;
    }

    public String getSKU(){
        return SKU;
    }

    public String getStatus(){
        return Status;
    }

    public void setShippingPrice(String shippingPrice) {
        ShippingPrice = shippingPrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setShellUsrEX(ShellUsrEX shellUsrEX) {
        this.shellUsrEX = shellUsrEX;
    }

    public void setLineNumber(String lineNumber) {
        LineNumber = lineNumber;
    }

    public void setOrderItemId(String orderItemId) {
        OrderItemId = orderItemId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public void disableWarn(){
        warn = false;
    }

}
