package mdev.orderProcessingSpring.utils.models;

import ch.qos.logback.classic.Logger;
import mdev.orderProcessingSpring.shell.ShellUsrEX;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@SuppressWarnings("unused")
public class Order {

    private Logger logger;

    @PostConstruct
    private void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(Order.class);
    }

    private String LineNumber, OrderId, Postcode;
    private String BuyerName, BuyerEmail, Address, OrderDate;

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

    public int getPostcode(){
        try{
            return Integer.parseInt(Postcode);
        }catch (Exception ex){
            if (warn){
                logger.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
            }
        }
        return -1;
    }

    public String getBuyerName(){
        return BuyerName;
    }

    public String getBuyerEmail(){
        return BuyerEmail;
    }

    public String getAddress(){
        return Address;
    }

    public String getOrderDate(){
        return OrderDate;
    }

    public void setDate(String d){
        OrderDate = d;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setBuyerEmail(String buyerEmail) {
        BuyerEmail = buyerEmail;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public void setShellUsrEX(ShellUsrEX shellUsrEX) {
        this.shellUsrEX = shellUsrEX;
    }

    public void setLineNumber(String lineNumber) {
        LineNumber = lineNumber;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public void disableWarn(){
        warn = false;
    }

}
