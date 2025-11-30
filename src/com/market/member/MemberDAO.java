package com.market.member;

import com.market.common.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

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
    // 1) 전체 회원 조회
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();

        String sql = "SELECT username, name, phone, address, role FROM member";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String name     = rs.getString("name");
                String phone    = rs.getString("phone");    // 문자열 그대로 사용
                String address  = rs.getString("address");
                String role     = rs.getString("role");

                Member m = new Member(username, name, phone, address, role);
                members.add(m);
            }

        } catch (SQLException e) {
            System.out.println("회원 목록 조회 중 오류");
            e.printStackTrace();
        }

        return members;
    }

    // 2) 회원 삭제
    public int deleteMember(String username) {
        String sql = "DELETE FROM member WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            return pstmt.executeUpdate();    // 1이면 성공

        } catch (SQLException e) {
            System.out.println("회원 삭제 중 오류");
            e.printStackTrace();
        }

        return 0;
    }
 // 3) 회원 등록
    public int insertMember(String username,
                            String password,
                            String name,
                            String phone,
                            String address,
                            String role) {

        String sql = "INSERT INTO member " +
                     "(username, password, name, phone, address, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, role);   // 'USER' 또는 'ADMIN'

            return pstmt.executeUpdate();  // 1 이면 성공

        } catch (SQLException e) {
            System.out.println("회원 등록 중 오류");
            e.printStackTrace();
        }

        return 0;
    }

    // 4) 회원 수정  (username 기준으로 수정)
    public int updateMember(String username,
                            String password,
                            String name,
                            String phone,
                            String address,
                            String role) {

        String sql = "UPDATE member SET " +
                     "password = ?, " +
                     "name = ?, " +
                     "phone = ?, " +
                     "address = ?, " +
                     "role = ? " +
                     "WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, password);
            pstmt.setString(2, name);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setString(5, role);
            pstmt.setString(6, username);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("회원 수정 중 오류");
            e.printStackTrace();
        }

        return 0;
    }
}
