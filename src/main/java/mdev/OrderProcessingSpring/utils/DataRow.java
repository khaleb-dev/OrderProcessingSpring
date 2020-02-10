package mdev.OrderProcessingSpring.utils;

import mdev.OrderProcessingSpring.OPSpringApp;
import mdev.OrderProcessingSpring.shell.ShellUsrEX;

@SuppressWarnings("unused")
public class DataRow {

    private String LineNumber, OrderItemId, OrderId, Postcode;
    private String SalePrice, ShippingPrice;

    private String BuyerName, BuyerEmail, Address, SKU, Status;
    private String OrderDate;

    private ShellUsrEX shellUsrEX;

    public int getLineNumber(){
        try{
            return Integer.parseInt(LineNumber);
        }catch (Exception ex){
            OPSpringApp.log.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + LineNumber));
        }
        return -1;
    }

    public int getOrderItemId(){
        try{
            return Integer.parseInt(OrderItemId);
        }catch (Exception ex){
            OPSpringApp.log.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
        }
        return -1;
    }

    public int getOrderId(){
        try{
            return Integer.parseInt(OrderId);
        }catch (Exception ex){
            OPSpringApp.log.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
        }
        return -1;
    }

    public int getPostcode(){
        try{
            return Integer.parseInt(Postcode);
        }catch (Exception ex){
            OPSpringApp.log.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
        }
        return -1;
    }

    public float getSalePrice(){
        try{
            return Float.parseFloat(SalePrice);
        }catch (Exception ex){
            OPSpringApp.log.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
        }
        return -1;
    }

    public float getShippingPrice(){
        try{
            return Float.parseFloat(ShippingPrice);
        }catch (Exception ex){
            OPSpringApp.log.warn(shellUsrEX.getWarningMessage("\n" + ex.toString() + "\nLine number : " + getLineNumber()));
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

}
