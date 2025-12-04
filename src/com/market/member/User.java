package com.market.member;

public class User extends Person {

    private int memberId;
    private String username;  // ← 로그인 아이디 저장용 필드 추가**

    public User(String name, String phone) {
        super(name, phone);
    }

    public User(String username, String phone, String address) {
        super(username, phone, address);
        this.username = username;  // 생성자에서 username 필드도 채움**
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    // === username getter / setter 추가 ===
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
