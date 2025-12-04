package com.market.member;

public class Admin extends Person {

    private String username;
    private String address;    
    private String role;       

    public Admin(String username, String name, String phone, String address, String role) {
        super(name, phone);
        this.username = username;
        this.address = address;
        this.role = role;
    }


    public String getUsername() {
        return username;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }
}
