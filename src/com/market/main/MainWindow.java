package com.market.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.market.page.*;
import com.market.cart.Cart;
import com.market.bookitem.BookInIt;

public class MainWindow extends JFrame {

    static Cart mCart;
    static JPanel mMenuPanel, mPagePanel;

    public MainWindow(String title, int x, int y, int width, int height) {
        initContainer(title, x, y, width, height);
        initMenu();

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
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2);

        mMenuPanel = new JPanel();
        mMenuPanel.setBounds(0, 20, width, 130);
        menuIntroduction();
        add(mMenuPanel);

        mPagePanel = new JPanel();
        mPagePanel.setBounds(0, 150, width, height);
        add(mPagePanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // ============================
    // 상단 버튼 메뉴 (고객기능)
    // ============================
    private void menuIntroduction() {
        mCart = new Cart();
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        JButton bt1 = new JButton("고객 정보 확인하기", new ImageIcon("./images/1.png"));
        bt1.setFont(ft);
        mMenuPanel.add(bt1);
        bt1.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("고객 정보 확인", new GuestInfoPage(mPagePanel));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

        JButton bt2 = new JButton("장바구니 상품목록보기", new ImageIcon("./images/2.png"));
        bt2.setFont(ft);
        mMenuPanel.add(bt2);
        bt2.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt2, "장바구니에 항목이 없습니다");
            else {
                mPagePanel.removeAll();
                mPagePanel.add("장바구니 상품 목록", new CartItemListPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt3 = new JButton("장바구니 비우기", new ImageIcon("./images/3.png"));
        bt3.setFont(ft);
        mMenuPanel.add(bt3);
        bt3.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt3, "장바구니가 비어 있습니다");
            else {
                mPagePanel.removeAll();
                menuCartClear(bt3);
                mPagePanel.add("장바구니 비우기", new CartItemListPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt4 = new JButton("장바구니에 항목추가하기", new ImageIcon("./images/4.png"));
        bt4.setFont(ft);
        mMenuPanel.add(bt4);
        bt4.addActionListener(e -> {
            mPagePanel.removeAll();
            BookInIt.init();
            mPagePanel.add("장바구니 항목 추가", new CartAddItemPage(mPagePanel, mCart));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

        JButton bt6 = new JButton("장바구니 항목삭제하기", new ImageIcon("./images/6.png"));
        bt6.setFont(ft);
        mMenuPanel.add(bt6);
        bt6.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt6, "장바구니가 비어 있습니다");
            else {
                mPagePanel.removeAll();
                CartItemListPage cartList = new CartItemListPage(mPagePanel, mCart);

                if (cartList.mSelectRow == -1)
                    JOptionPane.showMessageDialog(bt6, "삭제할 항목을 선택하세요");
                else {
                    mCart.removeCart(cartList.mSelectRow);
                    cartList.mSelectRow = -1;
                }

                mPagePanel.add("항목 삭제", new CartItemListPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt7 = new JButton("주문하기", new ImageIcon("./images/7.png"));
        bt7.setFont(ft);
        mMenuPanel.add(bt7);
        bt7.addActionListener(e -> {
            if (mCart.mCartCount == 0)
                JOptionPane.showMessageDialog(bt7, "장바구니가 비어 있습니다");
            else {
                mPagePanel.removeAll();
                mPagePanel.add("주문 배송지", new CartShippingPage(mPagePanel, mCart));
                mPagePanel.revalidate();
                mPagePanel.repaint();
            }
        });

        JButton bt8 = new JButton("종료", new ImageIcon("./images/8.png"));
        bt8.setFont(ft);
        mMenuPanel.add(bt8);
        bt8.addActionListener(e -> {
            int select = JOptionPane.showConfirmDialog(bt8, "쇼핑몰을 종료하시겠습니까?");
            if (select == 0) System.exit(0);
        });

        JButton bt9 = new JButton("관리자", new ImageIcon("./images/9.png"));
        bt9.setFont(ft);
        mMenuPanel.add(bt9);
        bt9.addActionListener(e -> {
            AdminLoginDialog adminDialog = new AdminLoginDialog(this, "관리자 로그인");
            adminDialog.setVisible(true);
            if (adminDialog.isLogin) {
                setVisible(false);
                new MainFrame();
            }
        });

        JButton bt10 = new JButton("내 주문 내역", new ImageIcon("./images/10.png"));
        bt10.setFont(ft);
        mMenuPanel.add(bt10);

        bt10.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add(new MyOrderListPage(mPagePanel));
            mPagePanel.revalidate();
            mPagePanel.repaint();
        });

    }

    // ============================
    // 상단 메뉴바 (JMenuBar)
    // ============================
    private void initMenu() {
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);
        JMenuBar menuBar = new JMenuBar();

        JMenu menu01 = new JMenu("고객");
        menu01.setFont(ft);
        JMenuItem item01 = new JMenuItem("고객 정보");
        JMenuItem item11 = new JMenuItem("종료");
        menu01.add(item01);
        menu01.add(item11);
        menuBar.add(menu01);

        JMenu menu02 = new JMenu("상품");
        menu02.setFont(ft);
        JMenuItem item02 = new JMenuItem("상품 목록");
        menu02.add(item02);
        menuBar.add(menu02);

        JMenu menu03 = new JMenu("장바구니");
        menu03.setFont(ft);
        JMenuItem item03 = new JMenuItem("항목 추가");
        JMenuItem item04 = new JMenuItem("항목 수량 줄이기");
        JMenuItem item05 = new JMenuItem("항목 삭제하기");
        JMenuItem item06 = new JMenuItem("장바구니 비우기");
        menu03.add(item03);
        menu03.add(item04);
        menu03.add(item05);
        menu03.add(item06);
        menuBar.add(menu03);

        JMenu menu04 = new JMenu("주문");
        menu04.setFont(ft);
        JMenuItem item07 = new JMenuItem("영수증 표시");
        menu04.add(item07);
        menuBar.add(menu04);

        setJMenuBar(menuBar);

        item01.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.add("고객 정보 확인", new GuestInfoPage(mPagePanel));
            mPagePanel.revalidate();
        });

        item02.addActionListener(e -> {
            mPagePanel.removeAll();
            BookInIt.init();
            mPagePanel.add("상품 목록", new CartAddItemPage(mPagePanel, mCart));
            mPagePanel.revalidate();
        });

        item11.addActionListener(e -> {
            int select = JOptionPane.showConfirmDialog(item11, "쇼핑몰을 종료하시겠습니까?");
            if (select == 0) System.exit(0);
        });
    }

    // ============================
    // 장바구니 전체 삭제
    // ============================
    private void menuCartClear(JButton button) {
        if (mCart.mCartCount == 0)
            JOptionPane.showMessageDialog(button, "장바구니가 비어 있습니다");
        else {
            int select = JOptionPane.showConfirmDialog(button, "모든 항목을 삭제하시겠습니까?");
            if (select == 0) {
                mCart.deleteBook();
                JOptionPane.showMessageDialog(button, "장바구니가 모두 삭제되었습니다");
            }
        }
    }


}
