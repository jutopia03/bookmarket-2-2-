package com.market.member;

import com.market.common.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDAO {

    // 관리자 로그인
    public Admin loginAdmin(String username, String password) {

        String sql = "SELECT username, name, phone, address "
                   + "FROM member "
                   + "WHERE username = ? AND password = ? AND role = 'ADMIN'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {

            	if (rs.next()) {

            	    String dbUsername = rs.getString("username"); 
            	    String name       = rs.getString("name");
            	    String phoneStr   = rs.getString("phone");
            	    String address    = rs.getString("address");
            	    String role       = "ADMIN"; 

            	    int phoneInt = 0;
            	    try {
            	        phoneInt = Integer.parseInt(phoneStr.replaceAll("[^0-9]", ""));
            	    } catch (Exception ignored) {}

            
            	    Admin admin = new Admin(
            	            dbUsername,   
            	            name,     
            	            phoneInt,  
            	            address,     
            	            role          
            	    );

            	    return admin;
            	}
            }

        } catch (SQLException e) {
            System.out.println("관리자 로그인 쿼리 오류");
            e.printStackTrace();
        }

        return null;  // 실패
    }
}
