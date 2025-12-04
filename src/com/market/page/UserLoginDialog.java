package com.market.page;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

import com.market.member.*;
import com.market.main.MainWindow;
import com.market.main.MainFrame;

public class UserLoginDialog extends JDialog {

    private JTextField idField;
    private JPasswordField pwField;
    private MemberDAO memberDAO = new MemberDAO();
    public boolean isLogin = false;

    // 관리자 로그인 선택
    private JRadioButton adminRadio;

    public UserLoginDialog(JFrame owner) {
        super(owner, "Book Market 로그인", true);

        // 다이얼로그 기본 설정
        setSize(780, 450);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // ========= 왼쪽: 브랜드 / 이미지 영역 =========
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(18, 18, 18));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // 로고 아이콘
        JLabel logoLabel;
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon("./images/shop.png"); 
        } catch (Exception e) {

        }
        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
        } else {
            logoLabel = new JLabel("BOOK MARKET");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 26));
        }

        JLabel titleLabel = new JLabel("Book Market");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 26));

        JLabel subTitleLabel = new JLabel("<html>오늘은 어떤 책을<br/>담아보실래요?</html>");
        subTitleLabel.setForeground(new Color(230, 230, 230));
        subTitleLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 16));

        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(titleLabel);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(subTitleLabel);
        leftPanel.add(Box.createVerticalGlue());

        JLabel footerLabel = new JLabel("© Book Market");
        footerLabel.setForeground(new Color(160, 160, 160));
        footerLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        leftPanel.add(footerLabel);

        add(leftPanel, BorderLayout.WEST);
        leftPanel.setPreferredSize(new Dimension(260, 0));

        // ========= 오른쪽: 로그인 폼 영역 =========
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(245, 245, 245));
        rightPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel loginTitle = new JLabel("로그인");
        loginTitle.setFont(new Font("맑은 고딕", Font.BOLD, 22));
        loginTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel loginDesc = new JLabel("Book Market 계정으로 로그인하세요.");
        loginDesc.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        loginDesc.setForeground(new Color(120, 120, 120));
        loginDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(loginTitle);
        rightPanel.add(Box.createVerticalStrut(5));
        rightPanel.add(loginDesc);
        rightPanel.add(Box.createVerticalStrut(25));

        // 아이디
        JPanel idPanel = new JPanel(new BorderLayout(5, 5));
        idPanel.setBackground(rightPanel.getBackground());
        idPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel idLabel = new JLabel("아이디");
        idLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        idField = new JTextField(18);
        idField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        idField.setMargin(new Insets(5, 8, 5, 8));

        idPanel.add(idLabel, BorderLayout.NORTH);
        idPanel.add(idField, BorderLayout.CENTER);

        // 비밀번호
        JPanel pwPanel = new JPanel(new BorderLayout(5, 5));
        pwPanel.setBackground(rightPanel.getBackground());
        pwPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pwLabel = new JLabel("비밀번호");
        pwLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 13));

        pwField = new JPasswordField(18);
        pwField.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        pwField.setMargin(new Insets(5, 8, 5, 8));

        pwPanel.add(pwLabel, BorderLayout.NORTH);
        pwPanel.add(pwField, BorderLayout.CENTER);

        rightPanel.add(idPanel);
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(pwPanel);
        rightPanel.add(Box.createVerticalStrut(15));

        // 관리자 로그인 체크
        adminRadio = new JRadioButton("관리자로 로그인");
        adminRadio.setBackground(rightPanel.getBackground());
        adminRadio.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        adminRadio.setAlignmentX(Component.LEFT_ALIGNMENT);

        rightPanel.add(adminRadio);
        rightPanel.add(Box.createVerticalStrut(20));

        // 버튼 영역
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setBackground(rightPanel.getBackground());
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton loginBtn  = createPrimaryButton("로그인");
        JButton joinBtn   = createGhostButton("회원가입");
        JButton exitBtn   = createTextButton("종료");

        btnPanel.add(joinBtn);
        btnPanel.add(exitBtn);
        btnPanel.add(loginBtn);

        rightPanel.add(btnPanel);
        rightPanel.add(Box.createVerticalStrut(10));

        add(rightPanel, BorderLayout.CENTER);

        // ========= 이벤트 =========

        // 로그인 버튼
        loginBtn.addActionListener(e -> login());

        // 회원가입
        joinBtn.addActionListener(e -> new UserJoinDialog(this));

        // 종료
        exitBtn.addActionListener(e -> System.exit(0));

        // 엔터키로 로그인
        pwField.addActionListener(e -> login());

        setVisible(true);
    }

    // 메인 컬러 버튼
    private JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(255, 107, 0));      
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(90, 34));
        return btn;
    }

    // 테두리형 버튼
    private JButton createGhostButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        btn.setForeground(new Color(80, 80, 80));
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        btn.setPreferredSize(new Dimension(90, 34));
        return btn;
    }

    // 텍스트형 버튼 (종료)
    private JButton createTextButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        btn.setForeground(new Color(130, 130, 130));
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        btn.setPreferredSize(new Dimension(60, 30));
        return btn;
    }

    // ======================
    // 로그인 처리 로직
    // ======================
    private void login() {
        String username = idField.getText().trim();
        String password = new String(pwField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 입력하세요.");
            return;
        }

        // 1) 관리자 로그인 선택
        if (adminRadio.isSelected()) {
            Admin admin = memberDAO.loginAdmin(username, password);

            if (admin != null) {
                isLogin = true;
                JOptionPane.showMessageDialog(this,
                        admin.getName() + "님 환영합니다!");

                // 관리자 메인 페이지
                new MainFrame();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "관리자 정보가 일치하지 않습니다.");
            }

        // 2) 일반 사용자 로그인
        } else {
            User user = memberDAO.loginUser(username, password);

            if (user != null) {
                UserInIt.setmUser(user);
                isLogin = true;
                JOptionPane.showMessageDialog(this,
                        user.getName() + "님 환영합니다!");

                new MainWindow("Book Market", 0, 0, 1000, 750);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "로그인 실패! 아이디/비밀번호를 확인하세요.");
            }
        }
    }
}
