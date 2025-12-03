package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.market.member.*;
import com.market.main.MainWindow;

public class UserLoginDialog extends JDialog {

    private JTextField idField;
    private JPasswordField pwField;
    private MemberDAO memberDAO = new MemberDAO();
    public boolean isLogin = false;

    public UserLoginDialog(JFrame owner) {
        super(owner, "사용자 로그인", true);

        setLayout(null);
        setSize(400, 250);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 400) / 2, (screenSize.height - 250) / 2);

        Font ft = new Font("맑은 고딕", Font.PLAIN, 16);

        JPanel idPanel = new JPanel();
        idPanel.setBounds(0, 30, 400, 40);
        JLabel idLabel = new JLabel("아이디 : ");
        idLabel.setFont(ft);
        idField = new JTextField(15);
        idField.setFont(ft);
        idPanel.add(idLabel);
        idPanel.add(idField);
        add(idPanel);

        JPanel pwPanel = new JPanel();
        pwPanel.setBounds(0, 80, 400, 40);
        JLabel pwLabel = new JLabel("비밀번호 : ");
        pwLabel.setFont(ft);
        pwField = new JPasswordField(15);
        pwField.setFont(ft);
        pwPanel.add(pwLabel);
        pwPanel.add(pwField);
        add(pwPanel);

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(0, 140, 400, 40);

        JButton loginBtn = new JButton("로그인");
        JButton joinBtn = new JButton("회원가입");
        JButton cancelBtn = new JButton("취소");

        btnPanel.add(loginBtn);
        btnPanel.add(joinBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel);

        loginBtn.addActionListener(e -> login());
        joinBtn.addActionListener(e -> new UserJoinDialog(this));
        cancelBtn.addActionListener(e -> { isLogin = false; dispose(); });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void login() {
        String username = idField.getText().trim();
        String password = new String(pwField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력하세요.");
            return;
        }

        User user = memberDAO.loginUser(username, password);

        if (user != null) {
            UserInIt.setmUser(user);
            isLogin = true;
            JOptionPane.showMessageDialog(this, user.getName() + "님 환영합니다!");
            new MainWindow("Book Market", 0, 0, 1000, 750);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "로그인 실패! 아이디/비밀번호를 확인하세요.");
        }
    }
}
