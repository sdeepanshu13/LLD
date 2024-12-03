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


onboardCustomer -- done
onboardDriver -- done
placeOrder --done
DeliverOrder -- done
CancelOrder --done
rateDriver
getOrderStatus
getDriverStatus


 */
import exceptions.ParcelDeliveryException;
import models.ParcelDeliveryService;

import static java.lang.Thread.sleep;

public static void main(String[] args) throws InterruptedException {

    System.out.println();

    ParcelDeliveryService service = new ParcelDeliveryService();


    try {
        service.onboardCustomer("John", "john@service.com", "1234", "123 street");
        service.onboardCustomer("Harry", "harry@service.com", "98765", "9876 street");
    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }


    try {
        service.onboardDriver("Bob", "12");
        service.onboardDriver("Alice", "98");
    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }

    try {
        service.getDriverStatus("12");
        service.getDriverStatus("98");
    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }

    try {
        service.placeOrder("john@service.com", "Vegetables", "123 street", "id-1");
        service.placeOrder("harry@service.com", "Food", "9876 street", "id-2");
        service.placeOrder("harry@service.com", "Fruits", "9876 street", "id-3");
        service.placeOrder("harry@service.com", "Fruits", "9876 street", "id-4");
        service.placeOrder("john@service.com", "Food", "9876 street", "id-5");
        service.placeOrder("harry@service.com", "Vegetables", "9876 street", "id-6");

    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }

    try {
        service.cancelOrder("id-4");

        service.assignOrderToDriver(service.orders.get("id-1"));
        service.assignOrderToDriver(service.orders.get("id-2"));
        service.assignOrderToDriver(service.orders.get("id-3"));
        service.getDriverStatus("12");
        service.getDriverStatus("98");
        service.getOrderStatus("id-1");
        service.getOrderStatus("id-2");
       // service.assignOrderToDriver(service.orders.get("id-4"));
        service.getDriverStatus("12");
        service.getDriverStatus("98");
        service.assignOrderToDriver(service.orders.get("id-5"));
        service.getDriverStatus("12");
        service.getDriverStatus("98");
        service.assignOrderToDriver(service.orders.get("id-6"));
        service.cancelOrder("id-2");

        //service.onboardDriver("Google", "56");


    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }

    try {
        service.getDriverStatus("12");
        service.getOrderStatus("id-1");
    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }
    try {
        sleep(10000);
        service.rateDriver("id-3", 3);
        service.rateDriver("id-1", 5);
        service.rateDriver("id-2", 5);
        service.rateDriver("id-5", 3);
        service.rateDriver("id-6", 4);
        service.getDriverOrderList("12");
        service.getDriverOrderList("98");
       // service.getDriverOrderList("56");
        service.getAverageRating("12");
        service.getAverageRating("98");
        //service.getAverageRating("56");
        service.getDriverRating("12", service.drivers.get("12").getOrderAssignedToDriver().get(0));

    } catch (ParcelDeliveryException e) {
        System.out.println("Error: " + e.getMessage());
    }


}