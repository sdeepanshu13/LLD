import exceptions.ParcelDeliveryException;
import models.ParcelDeliveryService;

import java.util.concurrent.TimeUnit;

/*
Problem Statement:
The system should be able to onboard new customers and drivers.
The list of items that can be delivered is preconfigured in the system and is fixed.
Customers should be able to place an order for the delivery of a parcel and also be able to cancel it.

One driver can pickup only one order at a time.
Orders should be auto-assigned to drivers based on availability. Even If no driver is available, the system should accept the order and assign it to a driver when the driver becomes free. The number of ongoing orders can exceed the number of drivers.

Once an order is assigned to a driver, the driver should be able to pick up the order and also mark the order as delivered after delivery. 

If no driver picks up the order within 60 seconds of its creation, the order should be canceled. Regardless of whether an order has been assigned to a driver or not, if no driver picks it up within 60 seconds of order creation, the order should be canceled.

The system should be able to show the status of orders and drivers.

Notify the customer and drivers through email and phone for order updates. For this exercise, write a class that represents a vendor providing the email or SMS service and just print the log indicating that the vendor has processed the request

Canceled orders shouldn’t be assigned to the driver. If an assigned order gets canceled the driver shouldn’t be able to pick up the order, the driver should be available for other orders. 
Once a driver picks up an order the order cannot be canceled by the user nor system.
Assume driver is available 24*7. Ignore the travel time. 
Ensure application is thread safe and all concurrency scenarios. 


 */

public class Main {
    public static void main(String[] args) throws InterruptedException {
            
            System.out.println();
     

            ParcelDeliveryService service = new ParcelDeliveryService();
            
        

                    service.onboardCustomer("John", "john@gmail.com", "1234", "123 street");
                    service.onboardCustomer("Harry", "harry@gmail.com", "98765", "9876 street");

                    System.out.println();

                    service.onboardDriver("Bob");
                    service.onboardDriver("Alice");
                    System.out.println();
                    
                    
                    try{
                    service.getDriverStatus("Bob");
                    service.getDriverStatus("Alice");
                    } catch (ParcelDeliveryException e) {
                            System.out.println("Error: " + e.getMessage());
                    }

                    System.out.println();
                    
                    try{
                    service.placeOrder("john@gmail.com", "Pizza", "123 street", "id-1");
                    //service.cancelOrder("id-1");
                    System.out.println();
                    service.placeOrder("harry@gmail.com", "Burger", "9876  street", "id-2");
                    System.out.println();
                    } catch (ParcelDeliveryException e) {
                            System.out.println("Error: " + e.getMessage());
                    }
            try {
                    service.cancelOrder("id-1");
            } catch (ParcelDeliveryException e) {
                    System.out.println("Error: " + e.getMessage());
            }


           

                    System.out.println();

            try{
                    service.onboardDriver("Steve");

            } catch (ParcelDeliveryException e) {
                    System.out.println("Error: " + e.getMessage());
            }


                    try {
                    service.placeOrder("john@gmail.com", "Cold Drink", "123 street", "id-3");
                    } catch (ParcelDeliveryException e) {
                    System.out.println("Error: " + e.getMessage());
                    }
                    
                    

                    service.getOrderStatus("id-1");
                    service.getDriverStatus("Bob");
                    System.out.println();

            try {

                    service.deliverOrder("id-1");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
            }

                   
                    System.out.println();

            try {

                    service.cancelOrder("id-1");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
                    }



            try {

                    service.deliverOrder("id-2");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
            }
            try {
                    service.placeOrder("john@gmail.com", "Cold Drink", "123 street", "id-4");
            } catch (ParcelDeliveryException e) {
                    System.out.println("Error: " + e.getMessage());
            }
            try {

                    service.deliverOrder("id-3");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
            }
            try {
                    service.placeOrder("john@gmail.com", "Cold Drink", "123 street", "id-5");
            } catch (ParcelDeliveryException e) {
                    System.out.println("Error: " + e.getMessage());
            }
            try {

                    service.deliverOrder("id-4");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
            }
            try {

                    service.deliverOrder("id-5");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
            }

            try {
                    service.placeOrder("john@gmail.com", "Cold Drink", "123 street", "id-6");
            } catch (ParcelDeliveryException e) {
                    System.out.println("Error: " + e.getMessage());
            }
                    System.out.println();
            try {

                    service.deliverOrder("id-6");
            } catch (ParcelDeliveryException e1) {
                    System.out.println("Error: " + e1.getMessage());
            }
                    
            service.getDriverOrderList("Bob");
            service.getDriverOrderList("Alice");
            service.getDriverOrderList("Steve");
            

        }
    }