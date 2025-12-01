package com.market.member;

public class Member {

    private String username;
    private String name;
    private String phone;
    private String address;
    private String role;

    public Member(String username, String name, String phone, String address, String role) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
