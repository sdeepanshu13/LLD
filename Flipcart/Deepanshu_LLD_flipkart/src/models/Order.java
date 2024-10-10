package models;

public class Order {
    
    private final String id;
    private final Customer customer;
    private final String item;
    private final String deliveryAddress;
    private Driver driver;
    private boolean isCancelled = false;
    private String orderStatus = "";
    public Order(String id, Customer customer, String item, String deliveryAddress, String orderStatus) {
        
        this.id = id;
        this.orderStatus = orderStatus;
        this.customer = customer;
        this.item = item;
        this.deliveryAddress = deliveryAddress;
    }
    
    public String getId(){
        return id;
    }
    
    public Customer getCustomer(){
        return customer;
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
    
    public String getOrderStatus(){
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus){
        this.orderStatus=orderStatus;
    }
    
    public void cancel(){
        
        this.isCancelled= true;
        this.orderStatus = "Cancelled";
        if(driver != null){
            driver.setAvailable(true);
        }
    }
    
    public void markAsDelivered(){
        this.orderStatus = "Delivered";
        
    }
}
