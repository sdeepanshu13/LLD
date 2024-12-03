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
    private final Map<String,Order> pendingOrders = new LinkedHashMap<>();
    
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
    
    public synchronized void placeOrder(String customerEmail, String item, String deliveryAddress, String orderID1) throws InterruptedException {
        if(!deliverableItems.contains(item)){
            throw new ParcelDeliveryException("Items not available");
        }
        
        if(!customers.containsKey(customerEmail)){
            throw new ParcelDeliveryException("Customer not found");
        }
        String orderId= orderID1;
        Customer customer = customers.get(customerEmail);
        Order order = new Order(orderId, customer, item, deliveryAddress, "Order-Placed");
        
        orders.put(orderId, order);
        
        assignOrderToDriver(order);
        if(order.getDriver() != null) {
            notification.sendNotification(customerEmail, "Order Placed. Order-id is:" + orderId + " Driver Name is: " + order.getDriver().getName());
        }
        //else throw new ParcelDeliveryException("wait");
    }
    
    private synchronized void assignOrderToDriver(Order order) throws InterruptedException {
        Optional<Driver> availableDriver = drivers.values().stream().filter(Driver::isAvailable).findFirst();
        if(availableDriver.isPresent()){
            Driver driver = availableDriver.get();
            order.setDriver(driver);
            driver.setAvailable(false);
            driver.setOrderAssignedToDriver(order.getId());
            order.setOrderStatus("Driver-Assigned");
            notification.sendNotification(driver.getName(), "Order "+ order.getId()+ " is assigned to you");
            order.setOrderStatus("In-Transit");
        } else{
            notification.sendNotification("All drivers busy", " Wait for sometime");
            startOrderPickupWaitTimer(order);
            
        }
    }
    
    
    private void startOrderPickupWaitTimer(Order order) {
        // Start a new thread for the long polling
        new Thread(() -> {
            long endTime = System.currentTimeMillis() + 60000; // 60 seconds from now
            boolean driverAssigned = false;

            while (System.currentTimeMillis() < endTime) {
                // Check for available drivers
                Optional<Driver> availableDriver = drivers.values().stream()
                        .filter(Driver::isAvailable)
                        .findFirst();

                if (availableDriver.isPresent()) {
                    // Assign the order to the available driver
                    Driver driver = availableDriver.get();
                    order.setDriver(driver);
                    driver.setAvailable(false);
                    driver.setOrderAssignedToDriver(order.getId());
                    order.setOrderStatus("Driver-Assigned");
                    notification.sendNotification(driver.getName(), "Order " + order.getId() + " is assigned to you.");
                    order.setOrderStatus("In-Transit");
                    driverAssigned = true;
                    break; // Exit the loop if a driver is assigned
                }

                try {
                    Thread.sleep(1000); // Wait for 1 second before checking again
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore interrupted status
                    return; // Exit the method if interrupted
                }
            }

            // If no driver was assigned after 60 seconds, cancel the order
            if (!driverAssigned) {
                order.cancel();
                notification.sendNotification(order.getCustomer().getEmail(), "Order " + order.getId() + " is canceled due to no driver availability.");
            }
        }).start(); // Start the polling in a new thread
    }




    public void deliverOrder( String orderId) {
        
                Order order = orders.get(orderId);
                Driver driver = order.getDriver();





                // Check if the order is valid for delivery
                if (order == null || order.isCancelled()) {
                    System.out.println("Order cannot be delivered.");
                    return; // Exit if the order is not valid
                }
                order.setOrderStatus("Delievred");


                if (driver != null) {
                    driver.setAvailable(true);
                    notification.sendNotification(order.getCustomer().getName(), "Order: " + orderId + " has been delivered by " + driver.getName() + ": to this address: " + order.getDeliveryAddress());
                }// 10 seconds
            
    }


    public synchronized  void cancelOrder(String orderId){
        Order order = orders.get(orderId);
        if(order != null && !order.isCancelled() && ( order.getOrderStatus() == "Driver-Assigned") ){
            order.cancel();
            notification.sendNotification(order.getCustomer().getEmail(), "Order "+ orderId+" has been cancelled");
            
        }
        else if(order.getOrderStatus() == "Delivered"){
            throw new ParcelDeliveryException("Order already delivered");
        }
        else if(order.getOrderStatus() == "In-Transit"){
            throw new ParcelDeliveryException("Order in Transit. Cannot be cancelled");
        }
        else {
            throw  new ParcelDeliveryException("Order not found");
        }
    }
    
    public synchronized void getOrderStatus(String orderId){
        Order order = orders.get(orderId);
        
        if(order != null ){
            String status = order.getOrderStatus();
            System.out.println("order details -> order ID: "+ orderId+" Status is: "+ status );
        } 
        else{
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
    
    public synchronized  void getDriverOrderList(String driverName){
        
        Driver driver= drivers.get(driverName);
        if(driver != null){
            System.out.println("Driver "+ driverName + " is" + driver.getOrderAssignedToDriver());
        }
        else{
            throw new ParcelDeliveryException("Driver not found");
        }
    }
    
}
