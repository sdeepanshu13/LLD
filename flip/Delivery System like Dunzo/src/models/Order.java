package models;

public class Order {

    private final String orderID;
    private final Customer customer;
    private final String item;
    private final String deliveryAddress;
    private Driver driver;
    private boolean isCancelled = false;
    private String orderStatus;

    public Order(String orderID, Customer customer, String item, String deliveryAddress, String orderStatus) {

        this.orderID = orderID;
        this.orderStatus = orderStatus;
        this.customer = customer;
        this.item = item;
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderID(){
        return orderID;
    }
     public Customer getCustomer() {
         return customer;
     }
     public String getOrderStatus(){
         return this.orderStatus;
     }
     public void setOrderStatus(String orderStatus){
         this.orderStatus=orderStatus;
     }
     public String getItem(){
        return item;
    }
    public String getDeliveryAddress(){
        return deliveryAddress;
    }

    public Driver getDriver(){
        return driver;
    }
    public void setDriver(Driver driver){
        this.driver=driver;
    }
    public boolean isCancelled(){
        return isCancelled;
    }
    public void setCancelled(){
        this.isCancelled= true;
        this.orderStatus = "Cancelled";
    }

}
