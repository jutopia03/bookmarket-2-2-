package com.market.member;

import com.market.common.DBUtil;

import java.sql.*;
import java.util.*;

public class MemberDAO {

    // ============================
    // 사용자 로그인
    // ============================
    public User loginUser(String username, String password) {

        String sql = "SELECT member_id, username, name, phone, address " +
                    "FROM member " +
                    "WHERE username = ? AND password = ? AND role = 'USER'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int memberId    = rs.getInt("member_id");
                    String name     = rs.getString("name");      // 진짜 회원 이름
                    String phoneStr = rs.getString("phone");
                    String address  = rs.getString("address");

                    int phoneInt = 0;
                    try {
                        // 010-1111-2222 같은 형식 제거 후 숫자만
                        phoneInt = Integer.parseInt(phoneStr.replaceAll("[^0-9]", ""));
                    } catch (Exception ignored) {}

                // Person(name, phone, address) 에 맞게 생성자 사용
                    User user = new User(name, phoneInt, address);
                    user.setMemberId(memberId); // ★ 여기서 memberId 주입

                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("사용자 로그인 쿼리 오류");
            e.printStackTrace();
        }

    // 로그인 실패
        return null;
    }


    // ============================
    // 관리자 로그인
    // ============================
    public Admin loginAdmin(String username, String password) {
        String sql = "SELECT username, name, phone, address " +
                     "FROM member " +
                     "WHERE username = ? AND password = ? AND role = 'ADMIN'";

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

                    return new Admin(dbUsername, name, phoneInt, address, role);
                }
            }

        } catch (SQLException e) {
            System.out.println("관리자 로그인 오류");
            e.printStackTrace();
        }

        return null;
    }

    // ============================
    // 전체 회원 조회
    // ============================
    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();

        String sql = "SELECT username, name, phone, address, role FROM member";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");
                String name     = rs.getString("name");
                String phone    = rs.getString("phone");
                String address  = rs.getString("address");
                String role     = rs.getString("role");

                members.add(new Member(username, name, phone, address, role));
            }

        } catch (SQLException e) {
            System.out.println("회원 목록 오류");
            e.printStackTrace();
        }

        return members;
    }

    // ============================
    // 회원 삭제
    // ============================
    public int deleteMember(String username) {
        String sql = "DELETE FROM member WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("회원 삭제 오류");
            e.printStackTrace();
        }

        return 0;
    }

    // ============================
    // 회원 등록 (USER / ADMIN 모두 가능)
    // ============================
    public int insertMember(String username,
                            String password,
                            String name,
                            String phone,
                            String address,
                            String role) {

        String sql = "INSERT INTO member (username, password, name, phone, address, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);
            pstmt.setString(6, role);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("회원 등록 오류");
            e.printStackTrace();
        }

        return 0;
    }

    // ============================
    // 회원 수정
    // ============================
    public int updateMember(String username,
                            String password,
                            String name,
                            String phone,
                            String address,
                            String role) {

        String sql = "UPDATE member SET " +
                     "password = ?, name = ?, phone = ?, address = ?, role = ? " +
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
            System.out.println("회원 수정 오류");
            e.printStackTrace();
        }

        return 0;
    }

    // ============================
    // 회원 가입 (USER 전용)
    // ============================
    public int insertMember(String username,
                            String password,
                            String name,
                            String phone,
                            String address) {

        String sql = "INSERT INTO member (username, password, name, phone, address, role) " +
                     "VALUES (?, ?, ?, ?, ?, 'USER')";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, name);
            pstmt.setString(4, phone);
            pstmt.setString(5, address);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("회원 가입 오류");
            e.printStackTrace();
        }

        return 0;
    }
}
