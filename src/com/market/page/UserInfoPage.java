package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import com.market.common.DBUtil;

/**
 * 내 정보 페이지 (A안)
 * - 전화번호, 주소는 수정 가능
 */
public class UserInfoPage extends JPanel {

    private final String loginUserName;

    private JTextField tfName;
    private JTextField tfUserName;
    private JPasswordField tfPassword;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JTextField tfRegDate;

    public UserInfoPage(JPanel parent, String loginUserName) {
        this.loginUserName = loginUserName;

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        // ======================================================
        // 최상위 레이아웃: BorderLayout
        // ======================================================
        setLayout(new BorderLayout());

        // ======================================================
        // 제목 패널
        // ======================================================
        JLabel titleLabel = new JLabel("내 정보", SwingConstants.CENTER);
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIManager.getColor("Panel.background"));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // ======================================================
        // 내용 패널 (입력 필드들)
        // ======================================================
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(40, 200, 60, 200));
        add(contentPanel, BorderLayout.CENTER);

        contentPanel.add(makeFieldRow("이   름", ft, false, FieldType.NAME));
        contentPanel.add(makeFieldRow("아이디", ft, false, FieldType.USERNAME));
        contentPanel.add(makeFieldRow("비밀번호", ft, false, FieldType.PASSWORD));
        contentPanel.add(makeFieldRow("전화번호", ft, true, FieldType.PHONE));
        contentPanel.add(makeFieldRow("주   소", ft, true, FieldType.ADDRESS));
        contentPanel.add(makeFieldRow("가입일자", ft, false, FieldType.REGDATE));

        contentPanel.add(Box.createVerticalStrut(20));

        JButton updateBtn = new JButton("전화번호 / 주소 수정");
        updateBtn.setFont(ft);
        updateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(updateBtn);

        // DB에서 회원 정보 로딩
        loadUserInfoFromDB();

        // 수정 버튼 동작
        updateBtn.addActionListener(e -> {
            String newPhone = tfPhone.getText().trim();
            String newAddr  = tfAddress.getText().trim();

            if (newPhone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "전화번호를 입력하세요.");
                return;
            }
            if (newAddr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "주소를 입력하세요.");
                return;
            }

            int result = updateUserContactInDB(newPhone, newAddr);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "정보가 수정되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "정보 수정에 실패했습니다.");
            }
        });
    }

    // -----------------------------
    // 필드 타입 구분용 enum
    // -----------------------------
    private enum FieldType {
        NAME, USERNAME, PASSWORD, PHONE, ADDRESS, REGDATE
    }

    // 라벨 + 입력 컴포넌트 한 줄 만들기
    private JPanel makeFieldRow(String labelText, Font ft, boolean editable, FieldType type) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JLabel label = new JLabel(labelText + " : ");
        label.setFont(ft);
        label.setPreferredSize(new Dimension(100, 30));
        panel.add(label, BorderLayout.WEST);

        JComponent field;

        switch (type) {
            case NAME:
                tfName = new JTextField();
                tfName.setFont(ft);
                tfName.setEditable(false);
                field = tfName;
                break;

            case USERNAME:
                tfUserName = new JTextField();
                tfUserName.setFont(ft);
                tfUserName.setEditable(false);
                field = tfUserName;
                break;

            case PASSWORD:
                tfPassword = new JPasswordField();
                tfPassword.setFont(ft);
                tfPassword.setEditable(false);
                field = tfPassword;
                break;

            case PHONE:
                tfPhone = new JTextField();
                tfPhone.setFont(ft);
                tfPhone.setEditable(editable);
                field = tfPhone;
                break;

            case ADDRESS:
                tfAddress = new JTextField();
                tfAddress.setFont(ft);
                tfAddress.setEditable(editable);
                field = tfAddress;
                break;

            case REGDATE:
            default:
                tfRegDate = new JTextField();
                tfRegDate.setFont(ft);
                tfRegDate.setEditable(false);
                field = tfRegDate;
                break;
        }

        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    // -----------------------------
    // DB에서 회원 정보 조회
    // -----------------------------
    private void loadUserInfoFromDB() {

        String sql =
            "SELECT username, name, password, phone, address, reg_date " +
            "FROM member " +
            "WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loginUserName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    tfUserName.setText(rs.getString("username"));
                    tfName.setText(rs.getString("name"));
                    tfPassword.setText(rs.getString("password"));
                    tfPhone.setText(rs.getString("phone"));
                    tfAddress.setText(rs.getString("address"));

                    Timestamp regTs = rs.getTimestamp("reg_date");
                    if (regTs != null) {
                        // 날짜 부분(yyyy-MM-dd)만 사용
                        String dateOnly = regTs.toLocalDateTime()
                                               .toLocalDate()
                                               .toString();
                        tfRegDate.setText(dateOnly);
                    } else {
                        tfRegDate.setText("");
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "회원 정보를 찾을 수 없습니다.");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "회원 정보 조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // -----------------------------
    // 전화번호 / 주소 업데이트
    // -----------------------------
    private int updateUserContactInDB(String newPhone, String newAddr) {

        String sql =
            "UPDATE member SET phone = ?, address = ? WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPhone);
            pstmt.setString(2, newAddr);
            pstmt.setString(3, loginUserName);

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "회원 정보 수정 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
        return 0;
    }
}
