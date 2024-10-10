package models;

public class Order {
    
    private final String id;
    private final Customer customer;
    private final String item;
    private final String deliveryAddress;
    private Driver driver;
    private boolean isCancelled = false;
    
    public Order(String id, Customer customer, String item, String deliveryAddress) {
        
        this.id = id;
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
    
    public void cancel(){
        
        this.isCancelled= true;
        if(driver != null){
            driver.setAvailable(true);
        }
    }
    
    public void markAsDelivered(){
        this.driver = null;
        
    }
}
