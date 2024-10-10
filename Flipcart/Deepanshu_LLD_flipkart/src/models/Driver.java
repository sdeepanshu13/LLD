package models;

import interfaces.DriverInterface;

public class Driver implements DriverInterface {
    
    private final String name;
    private boolean available= true;
    
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
}
