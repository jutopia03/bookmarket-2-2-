package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.market.member.MemberDAO;

public class UserJoinDialog extends JDialog {

    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addrField;

    private MemberDAO memberDAO = new MemberDAO();

    public UserJoinDialog(JDialog owner) {
        super(owner, "회원가입", true);

        Font titleFont = new Font("함초롬돋움", Font.BOLD, 20);
        Font subFont   = new Font("함초롬돋움", Font.PLAIN, 12);
        Font labelFont = new Font("함초롬돋움", Font.PLAIN, 14);
        Font btnFont   = new Font("함초롬돋움", Font.BOLD, 14);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 420);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(20, 20, 20));
        leftPanel.setPreferredSize(new Dimension(230, 0));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 25, 25));

        JLabel iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImageIcon icon = new ImageIcon("./images/shop.png");
        if (icon.getIconWidth() > 0) {
            iconLabel.setIcon(icon);
        }

        JLabel brandLabel = new JLabel("Book Market");
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(new Font("함초롬돋움", Font.BOLD, 22));
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel msgLabel = new JLabel("<html>계정을 만들어<br/>Book Market을 시작해 보세요.</html>");
        msgLabel.setForeground(new Color(220, 220, 220));
        msgLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 13));
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(iconLabel);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(brandLabel);
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(msgLabel);
        leftPanel.add(Box.createVerticalGlue());

        add(leftPanel, BorderLayout.WEST);

        // 오른쪽: 제목 + 폼 카드
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setBackground(new Color(245, 245, 245));
        rightWrapper.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        add(rightWrapper, BorderLayout.CENTER);

        // 상단 제목
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(rightWrapper.getBackground());

        JLabel titleLabel = new JLabel("회원가입");
        titleLabel.setFont(titleFont);

        JLabel subLabel = new JLabel("아래 정보를 입력하고 Book Market 계정을 생성하세요.");
        subLabel.setFont(subFont);
        subLabel.setForeground(new Color(120, 120, 120));

        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subLabel);
        titlePanel.add(Box.createVerticalStrut(15));

        rightWrapper.add(titlePanel, BorderLayout.NORTH);

        // 폼 카드
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(Color.WHITE);
        formCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 225, 225)),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        rightWrapper.add(formCard, BorderLayout.CENTER);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 0, 5, 0);
        gc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;

        // 아이디
        idField = new JTextField(20);
        idField.setFont(labelFont);
        addFormRow(formCard, gc, row++, "아이디", labelFont, idField);

        // 비밀번호
        pwField = new JPasswordField(20);
        pwField.setFont(labelFont);
        addFormRow(formCard, gc, row++, "비밀번호", labelFont, pwField);

        // 이름
        nameField = new JTextField(20);
        nameField.setFont(labelFont);
        addFormRow(formCard, gc, row++, "이름", labelFont, nameField);

        // 전화번호
        phoneField = new JTextField(20);
        phoneField.setFont(labelFont);
        addFormRow(formCard, gc, row++, "전화번호", labelFont, phoneField);

        // 주소
        addrField = new JTextField(20);
        addrField.setFont(labelFont);
        addFormRow(formCard, gc, row++, "주소", labelFont, addrField);

        // 하단 버튼
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(rightWrapper.getBackground());
        btnPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        rightWrapper.add(btnPanel, BorderLayout.SOUTH);

        JButton joinBtn = new JButton("가입");
        joinBtn.setFont(btnFont);
        joinBtn.setPreferredSize(new Dimension(100, 36));
        joinBtn.setBackground(new Color(255, 107, 0));
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setFocusPainted(false);
        joinBtn.setBorderPainted(false);
        joinBtn.setContentAreaFilled(true);
        joinBtn.setOpaque(true);

        JButton cancelBtn = new JButton("취소");
        cancelBtn.setFont(btnFont);
        cancelBtn.setPreferredSize(new Dimension(100, 36));
        cancelBtn.setBackground(new Color(245, 245, 245));
        cancelBtn.setForeground(new Color(60, 60, 60));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));
        cancelBtn.setContentAreaFilled(true);
        cancelBtn.setOpaque(true);

        btnPanel.add(cancelBtn);
        btnPanel.add(joinBtn);

        joinBtn.addActionListener(e -> join());
        cancelBtn.addActionListener(e -> dispose());

        addrField.addActionListener((ActionEvent e) -> join());

        setLocationRelativeTo(owner);
        setVisible(true);
    }

    private void addFormRow(JPanel parent, GridBagConstraints gc, int row,
                            String labelText, Font labelFont, JComponent field) {

        gc.gridx = 0;
        gc.gridy = row;
        gc.weightx = 0;
        JLabel label = new JLabel(labelText + " : ");
        label.setFont(labelFont);
        label.setPreferredSize(new Dimension(70, 24));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        parent.add(label, gc);

        gc.gridx = 1;
        gc.weightx = 1.0;
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        parent.add(field, gc);
    }

    private void join() {
        String username = idField.getText().trim();
        String password = new String(pwField.getPassword());
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addrField.getText().trim();

        if (username.isEmpty() || password.isEmpty() ||
                name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디, 비밀번호, 이름, 전화번호는 필수입니다.");
            return;
        }

        int result = memberDAO.insertMember(username, password, name, phone, address);

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "회원가입 성공! 로그인해 주세요.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "회원가입 실패. 아이디 중복 여부를 확인하세요.");
        }
    }
}
