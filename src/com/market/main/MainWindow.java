package com.market.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.market.page.*;
import com.market.cart.Cart;
import com.market.bookitem.BookInIt;
import com.market.member.User;
import com.market.member.UserInIt;
import com.market.page.UserInfoPage;

public class MainWindow extends JFrame {

    // 장바구니 객체 (공용)
    static Cart mCart;

    // 상단 전체 영역(로고+메뉴+인사), 그 아래 내용 영역
    private JPanel topPanel;
    private JPanel mMenuPanel;
    private JPanel mPagePanel;
    private JLabel greetingLabel;

    public MainWindow(String title, int x, int y, int width, int height) {

        // 도서 목록 초기화 (DB → 메모리)
        BookInIt.init();
        mCart = new Cart();

        initContainer(title, x, y, width, height);
        initMenu();

        // 최초 화면: 도서 목록
        showBookListPage();

        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());
    }

    // ============================
    // 메인 프레임 기본 설정
    // ============================
    private void initContainer(String title, int x, int y, int width, int height) {

        setTitle(title);
        setBounds(x, y, width, height);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // ===== 상단 바 (로고 + 메뉴 + 인사) =====
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(18, 18, 18));
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        );

        // 왼쪽: 로고 + 타이틀
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        logoPanel.setOpaque(false);

        JLabel logoLabel;
        ImageIcon logoIcon = null;
        try {
            logoIcon = new ImageIcon("./images/shop.png");
        } catch (Exception ignored) {}

        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
        } else {
            logoLabel = new JLabel("BM");
            logoLabel.setForeground(Color.WHITE);
            logoLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        }

        JLabel titleLabel = new JLabel("Book Market");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 18));

        logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);

        topPanel.add(logoPanel, BorderLayout.WEST);

        // 가운데: 메뉴 버튼 줄
        mMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        mMenuPanel.setOpaque(false);
        topPanel.add(mMenuPanel, BorderLayout.CENTER);

        // 오른쪽: 인사말
        greetingLabel = new JLabel(makeGreetingText());
        greetingLabel.setForeground(new Color(230, 230, 230));
        greetingLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));

        JPanel greetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        greetPanel.setOpaque(false);
        greetPanel.add(greetingLabel);

        topPanel.add(greetPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // ===== 가운데 내용 영역 =====
        mPagePanel = new JPanel();
        mPagePanel.setLayout(new BorderLayout());
        mPagePanel.setBackground(new Color(245, 245, 245));

        add(mPagePanel, BorderLayout.CENTER);

        // 창 닫힘 시 프로그램 종료 (사용자 창이니까)
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // 인사말 텍스트
    private String makeGreetingText() {
        User u = UserInIt.getmUser();
        String name = (u != null) ? u.getName() : "손님";
        return "안녕하세요, " + name + "님";
    }

    // ============================
    // 상단 버튼 메뉴
    // ============================
    private void initMenu() {

        Font menuFont = new Font("함초롬돋움", Font.PLAIN, 14);

        // 버튼 5개: 도서 목록 / 내 정보 / 장바구니 / 주문 내역 / 종료
        JButton btBookList     = createTopMenuButton("도서 목록", menuFont);
        JButton btMyInfo       = createTopMenuButton("내 정보", menuFont);
        JButton btCart         = createTopMenuButton("장바구니", menuFont);
        JButton btOrderHistory = createTopMenuButton("주문 내역", menuFont);
        JButton btExit         = createTopMenuButton("종료", menuFont);

        JButton[] btns = { btBookList, btMyInfo, btCart, btOrderHistory, btExit };
        for (JButton b : btns) {
            mMenuPanel.add(b);
        }

        // ===== 버튼 동작 =====

        // 1. 도서 목록 (메인 쇼핑 페이지)
        btBookList.addActionListener(e -> showBookListPage());

        // 2. 내 정보
        btMyInfo.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.setLayout(new BorderLayout());

            User loginUser = UserInIt.getmUser();
            if (loginUser == null) {
                JOptionPane.showMessageDialog(MainWindow.this, "로그인 정보가 없습니다.");
                return;
            }

            String loginUserName = loginUser.getUsername();

            UserInfoPage userInfoPage = new UserInfoPage(mPagePanel, loginUserName);
            mPagePanel.add(userInfoPage, BorderLayout.CENTER);

            refreshPage();
        });

        // 3. 장바구니
        btCart.addActionListener(e -> {
            if (mCart.mCartCount == 0) {
                JOptionPane.showMessageDialog(btCart, "장바구니에 항목이 없습니다.");
            } else {
                mPagePanel.removeAll();
                mPagePanel.setLayout(new BorderLayout());
                mPagePanel.add(new CartItemListPage(mPagePanel, mCart), BorderLayout.CENTER);
                refreshPage();
            }
        });

        // 4. 주문 내역
        btOrderHistory.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.setLayout(new BorderLayout());
            mPagePanel.add(new MyOrderListPage(mPagePanel), BorderLayout.CENTER);
            refreshPage();
        });

        // 5. 종료
        btExit.addActionListener(e -> {
            int select = JOptionPane.showConfirmDialog(
                    MainWindow.this,
                    "쇼핑몰을 종료하시겠습니까?",
                    "종료",
                    JOptionPane.YES_NO_OPTION
            );
            if (select == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    // 상단 메뉴용 플랫 버튼 생성 (관리자 화면이랑 톤 맞추기)
    private JButton createTopMenuButton(String text, Font font) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(255, 180, 80));  
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    // ============================
    // 페이지 전환
    // ============================

    // 도서 목록 화면으로 전환 (쇼핑 메인)
    private void showBookListPage() {
        mPagePanel.removeAll();
        mPagePanel.setLayout(new BorderLayout());
        mPagePanel.add(new CartAddItemPage(mPagePanel, mCart), BorderLayout.CENTER);
        refreshPage();
    }

    // 패널 새로고침
    private void refreshPage() {
        mPagePanel.revalidate();
        mPagePanel.repaint();
    }
}
