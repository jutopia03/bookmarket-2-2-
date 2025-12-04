package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.market.member.*;

public class UserJoinDialog extends JDialog {

    private JTextField idField;
    private JPasswordField pwField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addrField;

    private MemberDAO memberDAO = new MemberDAO();

    public UserJoinDialog(JDialog owner) {
        super(owner, "회원가입", true);

        setLayout(null);
        setSize(450, 350);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 450) / 2, (screenSize.height - 350) / 2);

        Font ft = new Font("맑은 고딕", Font.PLAIN, 14);

        // 라벨 폭 통일용
        Dimension labelSize = new Dimension(80, 25);

        int y = 20;

        // 아이디
        JPanel idPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        idPanel.setBounds(0, y, 450, 30);
        JLabel idLabel = new JLabel("아이디 : ");
        idLabel.setFont(ft);
        idLabel.setPreferredSize(labelSize);
        idField = new JTextField(25);
        idField.setFont(ft);
        idPanel.add(idLabel);
        idPanel.add(idField);
        add(idPanel);

        y += 40;

        // 비밀번호
        JPanel pwPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pwPanel.setBounds(0, y, 450, 30);
        JLabel pwLabel = new JLabel("비밀번호 : ");
        pwLabel.setFont(ft);
        pwLabel.setPreferredSize(labelSize);
        pwField = new JPasswordField(25);
        pwField.setFont(ft);
        pwPanel.add(pwLabel);
        pwPanel.add(pwField);
        add(pwPanel);

        y += 40;

        // 이름
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.setBounds(0, y, 450, 30);
        JLabel nameLabel = new JLabel("이름 : ");
        nameLabel.setFont(ft);
        nameLabel.setPreferredSize(labelSize);
        nameField = new JTextField(25);
        nameField.setFont(ft);
        namePanel.add(nameLabel);
        namePanel.add(nameField);
        add(namePanel);

        y += 40;

        // 전화번호
        JPanel phonePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        phonePanel.setBounds(0, y, 450, 30);
        JLabel phoneLabel = new JLabel("전화번호 : ");
        phoneLabel.setFont(ft);
        phoneLabel.setPreferredSize(labelSize);
        phoneField = new JTextField(25);
        phoneField.setFont(ft);
        phonePanel.add(phoneLabel);
        phonePanel.add(phoneField);
        add(phonePanel);

        y += 40;

        // 주소
        JPanel addrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addrPanel.setBounds(0, y, 450, 30);
        JLabel addrLabel = new JLabel("주소 : ");
        addrLabel.setFont(ft);
        addrLabel.setPreferredSize(labelSize);
        addrField = new JTextField(25);
        addrField.setFont(ft);
        addrPanel.add(addrLabel);
        addrPanel.add(addrField);
        add(addrPanel);

        y += 50;

        // 버튼
        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(0, y, 450, 40);
        JButton joinBtn = new JButton("가입");
        JButton cancelBtn = new JButton("취소");
        btnPanel.add(joinBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel);

        joinBtn.addActionListener(e -> join());
        cancelBtn.addActionListener(e -> dispose());

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
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
