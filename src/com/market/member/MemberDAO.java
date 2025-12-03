package com.market.member;

import com.market.common.DBUtil;
import com.market.member.User;

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
                    String phone = 	rs.getString("phone");
                    String address  = rs.getString("address");

                   

                // Person(name, phone, address) 에 맞게 생성자 사용
                    User user = new User(name, phone, address);
                    user.setMemberId(memberId); // ★ 여기서 memberId 주입
                    user.setUsername(username);

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
                    String phone     = rs.getString("phone");
                    String address    = rs.getString("address");
                    String role       = "ADMIN";

                    // 전화번호를 int 로 변환하던 코드 삭제
                    return new Admin(dbUsername, name, phone, address, role);
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
    
    // username 으로 상세 정보 조회 (비밀번호, 가입일자까지 포함)
    public User getUserDetail(String username) {
        String sql =
            "SELECT member_id, username, password, name, phone, address, reg_date " +
            "FROM member WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    int memberId   = rs.getInt("member_id");
                    String dbName  = rs.getString("name");
                    String phone   = rs.getString("phone");
                    String address = rs.getString("address");
                    // 필요하면 아래 값도 나중에 사용
                    // String dbUsername = rs.getString("username");
                    // String password   = rs.getString("password");
                    // Timestamp regDate = rs.getTimestamp("reg_date");

                    // ★ 252번 줄 수정
                    User user = new User(dbName, phone, address);

                    user.setMemberId(memberId);
                    // username, password, reg_date 를 User 에 넣고 싶으면
                    // User 클래스에 맞는 setter 가 있을 때만 사용
                    // 예: user.setUsername(dbUsername);
                    //      user.setPassword(password);
                    //      user.setRegDate(regDate);

                    return user;
                }
            }
        } catch (SQLException e) {
            System.out.println("회원 상세 조회 오류");
            e.printStackTrace();
        }
        return null;
    }


    // 전화번호 / 주소만 수정
    public int updateUserContact(String username, String phone, String address) {
        String sql =
            "UPDATE member SET phone = ?, address = ? WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            pstmt.setString(2, address);
            pstmt.setString(3, username);

            return pstmt.executeUpdate(); // 1 이상이면 성공
        } catch (SQLException e) {
            System.out.println("회원 연락처/주소 수정 오류");
            e.printStackTrace();
        }
        return 0;
    }

}
