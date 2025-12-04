package com.market.member;

public class User extends Person {

    private int memberId;
    private String username;  

    public User(String name, String phone) {
        super(name, phone);
    }

    public User(String username, String phone, String address) {
        super(username, phone, address);
        this.username = username; 
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
