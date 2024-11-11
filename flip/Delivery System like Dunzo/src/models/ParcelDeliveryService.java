package models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


import exceptions.ParcelDeliveryException;
import services.Notification;

import static java.lang.Thread.sleep;

public class ParcelDeliveryService {
    public final Map<String, Order> orders = new ConcurrentHashMap<>();
    public final Map<String, Customer> customers = new ConcurrentHashMap<>();
    public final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    public final List<String> deliverableItems = Arrays.asList("Food", "Drinks", "Vegetables", "Fruits");
    public final Notification notification = new Notification();



    public void onboardCustomer(String name, String email, String phoneNumber, String address) {

        if (customers.containsKey(email)) {
            throw new ParcelDeliveryException("Customer already Present");
        }
        synchronized (customers) {
            Customer customer = new Customer(name, email, phoneNumber, address);
            customers.put(email, customer);
            notification.sendNotification(name, " Welcome Customer " + name + ": ");
        }

    }

    public void onboardDriver(String name, String phoneNumber) {
        if (drivers.containsKey(phoneNumber)) {
            throw new ParcelDeliveryException("Driver already Present");
        }
        synchronized (drivers) {
            Driver driver = new Driver(name, phoneNumber);
            drivers.put(phoneNumber, driver);
            notification.sendNotification(name, " Welcome Driver");
        }
    }

    public void placeOrder(String customerEmail, String item, String deliveryAddress, String orderID) throws InterruptedException {
        if (!deliverableItems.contains(item)) {
            throw new ParcelDeliveryException("Items not available");
        }

        if (!customers.containsKey(customerEmail)) {
            throw new ParcelDeliveryException("Customer not found");
        }
        synchronized (orders) {
            Customer customer = customers.get(customerEmail);
            Order order = new Order(orderID, customer, item, deliveryAddress, "Order-Placed");

            orders.put(orderID, order);

            }
    }

    public void assignOrderToDriver(Order order) {
        Optional<Driver> availableDriver = drivers.values().stream().filter(Driver::isAvailable).findFirst();

if(!order.isCancelled()) {
    if (availableDriver.isPresent()) {
        Driver driver = availableDriver.get();
        order.setDriver(driver);
        driver.setAvailable(false);
        driver.setOrderAssignedToDriver(order.getOrderID());
        order.setOrderStatus("Driver-Assigned");
        notification.sendNotification(driver.getName(), "Order Assigned. Order-id is: " + order.getOrderID() + " Customer Name is: " + order.getCustomer().getName());
        order.setOrderStatus("In-Transit");
        deliverOrder(order.getOrderID());
    } else {
        notification.sendNotification(order.getCustomer().getName(), "Order Placed. Order-id is:" + order.getOrderID() + " Drivers are busy. Please wait for sometime");
        startOrderPickUpTimer(order);
    }
}
    }

    private void startOrderPickUpTimer(Order order) {
        new Thread(() -> {
            long endTime = System.currentTimeMillis() + 4000;
            boolean assignedDriver = false;
            while (System.currentTimeMillis() < endTime) {
                Optional<Driver> availableDriver = drivers.values().stream().filter(Driver::isAvailable).findFirst();
                if (availableDriver.isPresent()) {
                    Driver driver = availableDriver.get();
                    order.setOrderStatus("Driver-Assigned");
                    order.setDriver(driver);
                    driver.setAvailable(false);
                    driver.setOrderAssignedToDriver(order.getOrderID());
                    notification.sendNotification(driver.getName(), "Order Assigned. Order-id is: " + order.getOrderID() + " Customer Name is: " + order.getCustomer().getName());
                    order.setOrderStatus("In-Transit");
                    assignedDriver = true;
                    deliverOrder(order.getOrderID());
                    break;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                    return;
                }
            }
            if(!assignedDriver){
                notification.sendNotification(order.getCustomer().getName(), "***Order Cancelled***. Order-id is:" + order.getOrderID() + " No drivers assigned after 60 seconds. Cancelling the Order. Please try again later");
                order.setCancelled();
            }
        }).start();
    }

    public void deliverOrder(String orderID) {
        Order order = orders.get(orderID);
        if (order == null) {
            throw new ParcelDeliveryException("Order not found");
        }


        new Thread(() -> {
            try {
                sleep(2000);
                order.setOrderStatus("Delivered");
                order.getDriver().setAvailable(true);
                notification.sendNotification(order.getCustomer().getName(), "Order Delivered. Order-id is: " + orderID);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }).start();

    }

    public void cancelOrder(String orderID) {
        Order order = orders.get(orderID);
        if (order == null) {
            throw new ParcelDeliveryException("Order not found");
        }
        if (order.isCancelled()) {
            throw new ParcelDeliveryException("Order is already cancelled");
        }

        if(Objects.equals(order.getOrderStatus(), "Order-Placed")){
            order.setCancelled();
            notification.sendNotification(order.getCustomer().getName(), "***Order Cancelled***. Order-id is:" + orderID);
        }else {

            notification.sendNotification(order.getCustomer().getName(), "Cannot cancel Order. Order-id is:" + orderID + "  and order status is: "+order.getOrderStatus());
        }
    }

    public void rateDriver(String orderID, Integer rating) {
        Order order = orders.get(orderID);
        if (order == null) {
            throw new ParcelDeliveryException("Order not found");
        }
        if (order.isCancelled()) {
            throw new ParcelDeliveryException("Order is already cancelled");
        }
        if (!Objects.equals(order.getOrderStatus(), "Delivered")) {
            throw new ParcelDeliveryException("Order is not delivered yet");
        }
        order.getDriver().setRating(orderID, rating);
    }

    public void getOrderStatus(String orderID) {
        Order order = orders.get(orderID);
        if (order == null) {
            throw new ParcelDeliveryException("Order not found");
        }
        notification.sendNotification(order.getCustomer().getName(), "Order Status: " + order.getOrderStatus());
    }

    public void getDriverStatus(String phoneNumber) {
        Driver driver = drivers.get(phoneNumber);
        if (driver == null) {
            throw new ParcelDeliveryException("Driver not found");
        }
        notification.sendNotification(driver.getName(), "Driver Status: " + (driver.isAvailable() ? "Available" : "Not Available"));
    }

    public void getDriverOrderList(String phoneNumber) {
        Driver driver = drivers.get(phoneNumber);
        if (driver == null) {
            throw new ParcelDeliveryException("Driver not found");
        }
        notification.sendNotification(driver.getName(), "Order List: " + driver.getOrderAssignedToDriver());
    }

    public void getAverageRating(String phoneNumber) {
        Driver driver = drivers.get(phoneNumber);
        if (driver == null) {
            throw new ParcelDeliveryException("Driver not found");
        }
        if(driver.hasRating()) {
            throw  new ParcelDeliveryException("No rating found");
        }
        notification.sendNotification(driver.getName(), "Average Rating: " + driver.getAverageRating(driver));
    }

    public void getDriverRating(String phoneNumber, String orderID) {
        Driver driver = drivers.get(phoneNumber);
        if (driver == null) {
            throw new ParcelDeliveryException("Driver not found");
        }
        if(driver.hasRating()) {
            throw  new ParcelDeliveryException("No rating found");
        }
        notification.sendNotification(driver.getName(), "Has got Rating: " + driver.getRating(orderID) + " for Order: " + orderID);
    }










}




