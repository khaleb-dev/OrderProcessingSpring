package mdev.OrderProcessingSpring.utils;

import mdev.OrderProcessingSpring.OPSpringApp;

public class DataRow {

    private int LineNumber, OrderItemId, OrderId, Postcode;
    private float SalePrice, ShippingPrice;

    private String BuyerName, BuyerEmail, Address, SKU, Status;
    private String OrderDate;

    public int getLineNumber(){
        return LineNumber;
    }

    public int getOrderItemId(){
        return OrderItemId;
    }

    public int getOrderId(){
        return OrderId;
    }

    public int getPostcode(){
        return Postcode;
    }

    public float getSalePrice(){
        return SalePrice;
    }

    public float getShippingPrice(){
        return ShippingPrice;
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

    public void setLineNumber(String LineNumber){
        try{
            this.LineNumber = Integer.parseInt(LineNumber);
        }catch (NumberFormatException ex){
            OPSpringApp.log.warn(ex.toString() + "\nLine number - unknown");
            this.LineNumber = -1;
        }
    }

    public void setOrderItemId(String OrderItemId){
        try{
            this.OrderItemId = Integer.parseInt(OrderItemId);
        }catch (NumberFormatException ex){
            OPSpringApp.log.warn(ex.toString() + "\nLine number : " + getLineNumber());
            this.OrderItemId = -1;
        }
    }

    public void setOrderId(String OrderId){
        try{
            this.OrderId = Integer.parseInt(OrderId);
        }catch (NumberFormatException ex){
            OPSpringApp.log.warn(ex.toString() + "\nLine number : " + getLineNumber());
            this.OrderId = -1;
        }
    }

    public void setPostcode(String Postcode){
        try{
            this.Postcode = Integer.parseInt(Postcode);
        }catch (NumberFormatException ex){
            OPSpringApp.log.warn(ex.toString() + "\nLine number : " + getLineNumber());
            this.Postcode = -1;
        }
    }

    public void setShippingPrice(String ShippingPrice){
        try{
            this.ShippingPrice = Float.parseFloat(ShippingPrice);
        }catch (NumberFormatException ex){
            OPSpringApp.log.warn(ex.toString() + "\nLine number : " + getLineNumber());
            this.ShippingPrice = -1;
        }
    }

    public void setSalePrice(String SalePrice){
        try{
            this.SalePrice = Float.parseFloat(SalePrice);
        }catch (NumberFormatException ex){
            OPSpringApp.log.warn(ex.toString() + "\nLine number : " + getLineNumber());
            this.SalePrice = -1;
        }
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

}
