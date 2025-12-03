package com.market.page;

import javax.swing.*;
import com.market.cart.Cart;
import com.market.cart.CartItem;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CartItemListPage extends JPanel {

    private JTable cartTable;
    private final Object[] tableHeader = { "도서ID", "도서명", "단가", "수량", "총가격" };

    private Cart mCart;
    private JPanel parentPanel;

    public static int mSelectRow = -1;

    public CartItemListPage(JPanel panel, Cart cart) {

        this.parentPanel = panel;
        this.mCart = cart;

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        setLayout(new BorderLayout());

        // ============================
        // 테이블 데이터 준비
        // ============================
        ArrayList<CartItem> cartItem = mCart.getmCartItem();
        Object[][] content = new Object[cartItem.size()][tableHeader.length];
        int totalPrice = 0;

        for (int i = 0; i < cartItem.size(); i++) {
            CartItem item = cartItem.get(i);
            content[i][0] = item.getBookID();
            content[i][1] = item.getItemBook().getName();
            content[i][2] = item.getItemBook().getUnitPrice();
            content[i][3] = item.getQuantity();
            content[i][4] = item.getTotalPrice();
            totalPrice += item.getTotalPrice();
        }

        // ============================
        // 상단: 제목 + 총금액 표시
        // ============================
        JLabel titleLabel = new JLabel("장바구니", SwingConstants.CENTER);
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        JPanel totalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel totalPricelabel = new JLabel("총금액: " + totalPrice + " 원");
        totalPricelabel.setForeground(Color.red);
        totalPricelabel.setFont(ft);
        totalPricePanel.add(totalPricelabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(totalPricePanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // ============================
        // 가운데: 장바구니 테이블 (크기 축소 + 테두리 제거)
        // ============================
        cartTable = new JTable(content, tableHeader);
        JScrollPane jScrollPane = new JScrollPane(cartTable);

        // ★★★ 모든 테두리 제거 ★★★
        jScrollPane.setBorder(BorderFactory.createEmptyBorder());  // JScrollPane 외곽선 제거
        jScrollPane.getViewport().setBorder(null);                 // Viewport 내부선 제거
        cartTable.setBorder(null);                                 // JTable 외곽선 제거
 
        
        // ★ 테이블 전체 크기 줄이기
        jScrollPane.setPreferredSize(new Dimension(700, 160));

        // ★ 가운데 정렬용 wrapper
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        centerWrapper.setBackground(getBackground());
        centerWrapper.add(jScrollPane);

        add(centerWrapper, BorderLayout.CENTER);

        // 테이블 클릭 시 행 선택 저장
        cartTable.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                mSelectRow = cartTable.getSelectedRow();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });

        // ============================
        // 하단: 버튼 5개
        // ============================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 25, 0));
        add(buttonPanel, BorderLayout.SOUTH);

        // 1) 장바구니 비우기
        JButton clearButton = new JButton("장바구니 비우기");
        clearButton.setFont(ft);
        buttonPanel.add(clearButton);

        clearButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(clearButton, "장바구니에 항목이 없습니다");
                } else {
                    int select = JOptionPane.showConfirmDialog(clearButton, "장바구니의 모든 항목을 삭제하겠습니까?");
                    if (select == JOptionPane.YES_OPTION) {

                        TableModel tableModel = new DefaultTableModel(new Object[0][0], tableHeader);
                        cartTable.setModel(tableModel);
                        totalPricelabel.setText("총금액: 0 원");

                        JOptionPane.showMessageDialog(clearButton, "장바구니의 모든 항목을 삭제했습니다");

                        mCart.deleteBook();
                        mSelectRow = -1;
                    }
                }
            }
        });

        // 2) 수량 증가
        JButton plusButton = new JButton("수량 증가");
        plusButton.setFont(ft);
        buttonPanel.add(plusButton);

        plusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(plusButton, "장바구니가 비었습니다.");
                    return;
                }
                if (mSelectRow == -1) {
                    JOptionPane.showMessageDialog(plusButton, "수량을 증가시킬 항목을 선택하세요.");
                    return;
                }

                CartItem item = mCart.getmCartItem().get(mSelectRow);
                item.setQuantity(item.getQuantity() + 1);

                refreshTable(cartTable, totalPricelabel, mCart);
            }
        });

        // 3) 수량 감소
        JButton minusButton = new JButton("수량 감소");
        minusButton.setFont(ft);
        buttonPanel.add(minusButton);

        minusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(minusButton, "장바구니가 비었습니다.");
                    return;
                }
                if (mSelectRow == -1) {
                    JOptionPane.showMessageDialog(minusButton, "수량을 감소시킬 항목을 선택하세요.");
                    return;
                }

                CartItem item = mCart.getmCartItem().get(mSelectRow);

                if (item.getQuantity() == 1) {
                    int result = JOptionPane.showConfirmDialog(minusButton, "수량이 1입니다. 이 항목을 삭제하시겠습니까?");
                    if (result == JOptionPane.YES_OPTION) {
                        mCart.removeCart(mSelectRow);
                        mSelectRow = -1;
                    }
                } else {
                    item.setQuantity(item.getQuantity() - 1);
                }

                refreshTable(cartTable, totalPricelabel, mCart);
            }
        });

        // 4) 항목 삭제
        JButton removeButton = new JButton("항목 삭제");
        removeButton.setFont(ft);
        buttonPanel.add(removeButton);

        removeButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(removeButton, "장바구니에 항목이 없습니다");
                } else if (mSelectRow == -1) {
                    JOptionPane.showMessageDialog(removeButton, "삭제할 항목을 선택하세요");
                } else {

                    mCart.removeCart(mSelectRow);

                    refreshTable(cartTable, totalPricelabel, mCart);
                    mSelectRow = -1;
                }
            }
        });

        // 5) 주문하기
        JButton orderButton = new JButton("주문하기");
        orderButton.setFont(ft);
        buttonPanel.add(orderButton);

        orderButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(orderButton, "장바구니가 비었습니다.");
                    return;
                }

                parentPanel.removeAll();
                parentPanel.setLayout(new BorderLayout());
                parentPanel.add(new CartShippingPage(parentPanel, mCart), BorderLayout.CENTER);
                parentPanel.revalidate();
                parentPanel.repaint();
            }
        });
    }

    // ------------------------------
    // 테이블 및 총액 갱신 함수
    // ------------------------------
    private void refreshTable(JTable cartTable, JLabel totalPricelabel, Cart cart) {

        ArrayList<CartItem> cartItemList = cart.getmCartItem();
        Object[][] content = new Object[cartItemList.size()][tableHeader.length];

        int totalPrice = 0;

        for (int i = 0; i < cartItemList.size(); i++) {

            CartItem item = cartItemList.get(i);
            content[i][0] = item.getBookID();
            content[i][1] = item.getItemBook().getName();
            content[i][2] = item.getItemBook().getUnitPrice();
            content[i][3] = item.getQuantity();
            content[i][4] = item.getTotalPrice();

            totalPrice += item.getTotalPrice();
        }

        TableModel tableModel = new DefaultTableModel(content, tableHeader);
        cartTable.setModel(tableModel);

        totalPricelabel.setText("총금액: " + totalPrice + " 원");
    }
}
