package com.market.main;

import javax.swing.*;
import java.awt.*;

import com.market.page.UserLoginDialog;
import com.market.page.AdminLoginDialog;

public class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Book Market 로그인"); // Book Market login 창 제목
        // Book Market 로그인 창 제목
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("로그인 방법을 선택하세요", SwingConstants.CENTER);
        // Select how to log in (로그인 방식을 선택하세요)
        title.setFont(new Font("함초롬돋움", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton userBtn  = new JButton("사용자 로그인");
        JButton adminBtn = new JButton("관리자 로그인");
        JButton exitBtn  = new JButton("종료");

        buttonPanel.add(userBtn);
        buttonPanel.add(adminBtn);
        buttonPanel.add(exitBtn);

        add(buttonPanel, BorderLayout.CENTER);

        // 사용자 로그인 버튼
        userBtn.addActionListener(e -> {
            // UserLoginDialog는 생성자 안에서 setVisible(true)를 이미 호출함
            // UserLoginDialog already calls setVisible(true) inside constructor
            UserLoginDialog dialog = new UserLoginDialog(this);
            if (dialog.isLogin) {
                // 로그인 성공 시 현재 선택창 닫기
                // Close this selector window on successful login
                dispose();
            }
        });

        // 관리자 로그인 버튼
        adminBtn.addActionListener(e -> {
            AdminLoginDialog dialog = new AdminLoginDialog(this, "관리자 로그인");
            dialog.setVisible(true);   // 모달

            if (dialog.isLogin) {      // 로그인 성공
                new MainFrame();       // 관리자 메인 화면 띄움
                this.dispose();        // 로그인 선택창 닫기
            }
        });


        // 종료 버튼
        exitBtn.addActionListener(e -> System.exit(0));
    }
}
