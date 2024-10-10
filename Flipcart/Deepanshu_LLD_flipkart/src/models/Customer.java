package models;

import interfaces.CustomerInterface;

public class Customer implements CustomerInterface {
    
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final String address;
    
    public Customer( String name, String email,String phoneNumber,String address ){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
