package mdev.OrderProcessingSpring.utils;

import ch.qos.logback.classic.Logger;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

/**
 * @author markodevelopment (Mihálovics Márkó)
 */
@SuppressWarnings("unused")
public class Order {

    private Logger logger;

    @PostConstruct
    public void initLogger(){
        logger = (Logger) LoggerFactory.getLogger(Order.class);
    }

    private String LineNumber, OrderItemId, OrderId, Postcode;
    private String SalePrice, ShippingPrice;

    private String BuyerName, BuyerEmail, Address, SKU, Status;
    private String OrderDate;

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

    public String getBuyerName(){
        return BuyerName;
    }

    public String getBuyerEmail(){
        return BuyerEmail;
    }

    public String getAddress(){
        return Address;
    }

    public String getSKU(){
        return SKU;
    }

    public String getStatus(){
        return Status;
    }

    public String getOrderDate(){
        return OrderDate;
    }

    public void setDate(String d){
        OrderDate = d;
    }

    public void setShippingPrice(String shippingPrice) {
        ShippingPrice = shippingPrice;
    }

    public void setSalePrice(String salePrice) {
        SalePrice = salePrice;
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

    public void setPostcode(String postcode) {
        Postcode = postcode;
    }

    public void disableWarn(){
        warn = false;
    }

}
