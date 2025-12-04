package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

import com.market.common.DBUtil;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        setBorder(new EmptyBorder(30, 40, 40, 40));

        // ===== 상단 타이틀 =====
        JLabel titleLabel = new JLabel("내 정보");
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 24));

        JLabel subLabel = new JLabel("내 계정 정보를 확인하고, 연락처와 주소를 수정할 수 있습니다.");
        subLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
        subLabel.setForeground(new Color(120, 120, 120));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(getBackground());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subLabel);
        titlePanel.add(Box.createVerticalStrut(10));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 220, 220));
        titlePanel.add(sep);

        add(titlePanel, BorderLayout.NORTH);

        // ===== 중앙 카드 =====
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);

        centerWrapper.setBorder(new EmptyBorder(40, 0, 0, 0));
        add(centerWrapper, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(25, 60, 25, 60)
        ));
        contentPanel.setPreferredSize(new Dimension(900, 260));
        contentPanel.setMinimumSize(new Dimension(800, 240));
        contentPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));


        JPanel contentAlign = new JPanel();
        contentAlign.setOpaque(false);
        contentAlign.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        contentAlign.add(contentPanel, gbc);

        centerWrapper.add(contentAlign, BorderLayout.NORTH);

        contentPanel.add(makeFieldRow("이   름", ft, false, FieldType.NAME));
        contentPanel.add(makeFieldRow("아이디", ft, false, FieldType.USERNAME));
        contentPanel.add(makeFieldRow("비밀번호", ft, false, FieldType.PASSWORD));
        contentPanel.add(makeFieldRow("전화번호", ft, true,  FieldType.PHONE));
        contentPanel.add(makeFieldRow("주   소", ft, true,  FieldType.ADDRESS));
        contentPanel.add(makeFieldRow("가입일자", ft, false, FieldType.REGDATE));

        // ===== 하단 버튼 (카드 밖) =====
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        JButton updateBtn = new JButton("전화번호 / 주소 수정");
        updateBtn.setFont(ft);
        updateBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateBtn.setPreferredSize(new Dimension(260, 40));
        updateBtn.setMaximumSize(new Dimension(260, 40));
        updateBtn.setBackground(new Color(255, 107, 0));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorderPainted(false);
        updateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        bottomPanel.add(updateBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        loadUserInfoFromDB();

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

    private enum FieldType {
        NAME, USERNAME, PASSWORD, PHONE, ADDRESS, REGDATE
    }

    private JPanel makeFieldRow(String labelText, Font ft, boolean editable, FieldType type) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        panel.setPreferredSize(new Dimension(1000, 44));

        JLabel label = new JLabel(labelText + " : ", SwingConstants.RIGHT);
        label.setFont(ft);
        label.setPreferredSize(new Dimension(90, 30));
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

        if (field instanceof JTextField || field instanceof JPasswordField) {
            field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(220, 220, 220)),
                    new EmptyBorder(5, 12, 5, 12)
            ));
            field.setBackground(editable ? Color.WHITE : new Color(248, 248, 248));
        }

        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

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
