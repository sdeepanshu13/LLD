package models;

import interfaces.DriverInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Driver implements DriverInterface {
    
    private final String name;
    private boolean available= true;
    private List<String> orderAssignedToDriver= new ArrayList<>();
    
    public Driver(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable( boolean available){
        this.available = available;
    }

    public List<String> getOrderAssignedToDriver() {
        return orderAssignedToDriver;
    }

    public void setOrderAssignedToDriver(String OrderID) {
        this.orderAssignedToDriver.add(OrderID);
    }
}
