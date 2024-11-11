package models;
import exceptions.ParcelDeliveryException;
import interfaces.DriverInterface;

import java.util.*;

public class Driver implements DriverInterface {
    private final String name;
    private final String phoneNumber;
    private boolean available = true;
    private final List<String> orderAssignedToDriver = new ArrayList<>();
    private final Map<String, Integer> rating = new HashMap<>();


    public Driver(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;

    }

    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getName() {
        return name;
    }

    @Override
    public boolean isAvailable() {
        return this.available;
    }

    @Override
    public Integer getRating() {
        return 0;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<String> getOrderAssignedToDriver() {
        return orderAssignedToDriver;
    }
    public void setOrderAssignedToDriver(String OrderID) {
        this.orderAssignedToDriver.add(OrderID);
    }
    public void setRating(String OrderID, Integer rating) {
        this.rating.put(OrderID, rating);
    }
    public Integer getRating(String OrderID) {
        return rating.get(OrderID);
    }

    public boolean hasRating() {
        return rating.isEmpty();
    }
    public int getAverageRating(Driver driver) {


        int sum = 0;

        for (Integer value : rating.values()) {
            sum += value;
        }
        return sum / rating.size();
    }
}
