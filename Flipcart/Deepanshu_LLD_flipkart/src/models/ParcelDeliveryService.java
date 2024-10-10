package models;

import java.util.*;
import java.util.concurrent.*;


import exceptions.*;
import services.*;


public class ParcelDeliveryService {
    
    private final Map<String, Customer> customers = new ConcurrentHashMap<>();
    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    private final Map<String, Order> orders = new ConcurrentHashMap<>();
    private final List<String> deliverableItems = Arrays.asList("Pizza", "Burger", "Cold Drink");
    private final Notification notification = new Notification();
    
    public synchronized void onboardCustomer(String name, String email, String phoneNumber, String address){
        
            if (customers.containsKey(email)) {
                throw new ParcelDeliveryException("Customer already Present");
            }
            Customer customer = new Customer(name, email, phoneNumber, address);
            customers.put(email, customer);
            notification.sendNotification(email, " Welcome Customer " + name + ": ");
        
    }
    
    public synchronized void onboardDriver(String name){
        if(drivers.containsKey(name)){
            throw new ParcelDeliveryException("Driver already Present");
        }
        Driver driver = new Driver(name);
        drivers.put(name, driver);
        notification.sendNotification( name," Welcome Driver" );
    }
    
    public synchronized void placeOrder(String customerEmail, String item, String deliveryAddress, String orderID1){
        if(!deliverableItems.contains(item)){
            throw new ParcelDeliveryException("Items not available");
        }
        
        if(!customers.containsKey(customerEmail)){
            throw new ParcelDeliveryException("Customer not found");
        }
        String orderId= orderID1;
        Customer customer = customers.get(customerEmail);
        Order order = new Order(orderId, customer, item, deliveryAddress);
        orders.put(orderId, order);
        
        assignOrderToDriver(order);
        notification.sendNotification(customerEmail, "Order Placed. Order-id is:" + orderId +" Driver Name is: "+ order.getDriver().getName());
    }
    
    private synchronized void assignOrderToDriver(Order order){
        Optional<Driver> availableDriver = drivers.values().stream().filter(Driver::isAvailable).findFirst();
        if(availableDriver.isPresent()){
            Driver driver = availableDriver.get();
            order.setDriver(driver);
            driver.setAvailable(false);
            notification.sendNotification(driver.getName(), "Order "+ order.getId()+ " is assigned to you");
            startOrderPickupTimer(order);
        } else{
            notification.sendNotification("All drivers busy", " Wait for sometime");
            startOrderPickupTimer(order);
            throw new ParcelDeliveryException("Waiting for Driver");
            
        }
    }
    
    private void startOrderPickupTimer(Order order){
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule( () ->{
            if(order.isCancelled()){
                return;
            }
            notification.sendNotification(order.getCustomer().getName(), "Order "+ order.getId()+ "is cancelled due to no driver available");
        }, 10, TimeUnit.SECONDS);
        
    }
    
    public synchronized  void pickUpOrder(String drivername, String orderId){
        
        Order order = orders.get(orderId);
        
        if( order== null || order.isCancelled() ){
            throw new ParcelDeliveryException("Order cannot be picked up");
        }
        Driver driver = drivers.get(drivername);
        if(driver == null){
            throw new ParcelDeliveryException("Driver not found");
        }
        
        order.setDriver(driver);
        driver.setAvailable(false);
        notification.sendNotification(order.getCustomer().getName(), "Order "+ orderId+" has been picked up by "+ drivername);
        
    }
    
    public synchronized void deliverOrder(String driverName, String orderId){
        Order order = orders.get(orderId);
        
        if(order == null || order.isCancelled()){
            throw new ParcelDeliveryException("order cannot be delivered");
        }
        
        order.markAsDelivered();
        Driver driver = drivers.get(driverName);
        driver.setAvailable(true);
        notification.sendNotification(order.getCustomer().getName(), "Order: "+orderId+" has been delivered by "+ driverName+": to this address: "+ order.getDeliveryAddress());
        
    }
    
    public synchronized  void cancelOrder(String orderId){
        Order order = orders.get(orderId);
        if(order != null && !order.isCancelled() && order.getDriver() != null){
            order.cancel();
            notification.sendNotification(order.getCustomer().getEmail(), "Order "+ orderId+" has been cancelled");
            
        }
        else if(order.getDriver() == null){
            throw new ParcelDeliveryException("Order already delivered");
        }
        else {
            throw  new ParcelDeliveryException("Order not found");
        }
    }
    
    public synchronized void getOrderStatus(String orderId){
        Order order = orders.get(orderId);
        
        if(order != null && order.getDriver() != null){
            String status = order.isCancelled() ? "cancelled" : "in transit";
            System.out.println("order details -> order ID: "+ orderId+" Status is: "+ status );
        } else if (order.getDriver() == null) {
            String status = "Delivered";
            System.out.println("order details -> order ID: "+ orderId+" Status is: "+ status );
        } else{
            throw new ParcelDeliveryException("Order not found");
        }
    }
    
    public synchronized void getDriverStatus(String driverName){
        Driver driver = drivers.get(driverName);
        if(driver != null){
            System.out.println("Driver "+ driverName + " is" +(driver.isAvailable() ? " idle " : " busy "));
        }
        else{
            throw new ParcelDeliveryException("Driver not found");
        }
        
    }
    
}
