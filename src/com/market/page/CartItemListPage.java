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

    JTable cartTable;
    Object[] tableHeader = { "도서ID", "도서명", "단가", "수량", "총가격" };

    Cart mCart; // 기존 new Cart() 제거!
    public static int mSelectRow = -1;

    public CartItemListPage(JPanel panel, Cart cart) {

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        this.mCart = cart;
        this.setLayout(null);

        Rectangle rect = panel.getBounds();
        this.setPreferredSize(rect.getSize());

        JPanel bookPanel = new JPanel();
        bookPanel.setBounds(0, 0, 1000, 400);
        add(bookPanel);

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

        cartTable = new JTable(content, tableHeader);
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setPreferredSize(new Dimension(600, 350));
        jScrollPane.setViewportView(cartTable);
        bookPanel.add(jScrollPane);

        JPanel totalPricePanel = new JPanel();
        totalPricePanel.setBounds(0, 400, 1000, 50);
        JLabel totalPricelabel = new JLabel("총금액: " + totalPrice + " 원");
        totalPricelabel.setForeground(Color.red);
        totalPricelabel.setFont(ft);
        totalPricePanel.add(totalPricelabel);
        add(totalPricePanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBounds(0, 450, 1000, 100);
        add(buttonPanel);

        // ------------------------------
        // 장바구니 비우기
        // ------------------------------
        JLabel buttonLabel = new JLabel("장바구니 비우기");
        buttonLabel.setFont(ft);
        JButton clearButton = new JButton();
        clearButton.add(buttonLabel);
        buttonPanel.add(clearButton);

        clearButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0)
                    JOptionPane.showMessageDialog(clearButton, "장바구니에 항목이 없습니다");
                else {
                    int select = JOptionPane.showConfirmDialog(clearButton, "장바구니의 모든 항목을 삭제하겠습니까?");
                    if (select == 0) {

                        TableModel tableModel = new DefaultTableModel(new Object[0][0], tableHeader);
                        cartTable.setModel(tableModel);
                        totalPricelabel.setText("총금액: 0 원");

                        JOptionPane.showMessageDialog(clearButton, "장바구니의 모든 항목을 삭제했습니다");

                        mCart.deleteBook();
                    }
                }
            }
        });

        // ------------------------------
        // 항목 삭제
        // ------------------------------
        JLabel removeLabel = new JLabel("장바구니 항목 삭제하기");
        removeLabel.setFont(ft);
        JButton removeButton = new JButton();
        removeButton.add(removeLabel);
        buttonPanel.add(removeButton);

        removeButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0)
                    JOptionPane.showMessageDialog(removeButton, "장바구니에 항목이 없습니다");
                else if (mSelectRow == -1)
                    JOptionPane.showMessageDialog(removeButton, "삭제할 항목을 선택하세요");
                else {

                    mCart.removeCart(mSelectRow);

                    refreshTable(cartTable, totalPricelabel, mCart);
                    mSelectRow = -1;
                }
            }
        });

        // ------------------------------
        // ★ 수량 증가 버튼 (+)
        // ------------------------------
        JLabel plusLabel = new JLabel("수량 증가 (+)");
        plusLabel.setFont(ft);
        JButton plusButton = new JButton();
        plusButton.add(plusLabel);
        buttonPanel.add(plusButton);

        plusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(plusButton, "장바구니가 비었습니다.");
                    return;
                }
                if (mSelectRow == -1) {
                    JOptionPane.showMessageDialog(plusButton, "수량을 변경할 항목을 선택하세요.");
                    return;
                }

                CartItem item = mCart.getmCartItem().get(mSelectRow);
                item.setQuantity(item.getQuantity() + 1);

                refreshTable(cartTable, totalPricelabel, mCart);
            }
        });

        // ------------------------------
        // ★ 수량 감소 버튼 (-)
        // ------------------------------
        JLabel minusLabel = new JLabel("수량 감소 (-)");
        minusLabel.setFont(ft);
        JButton minusButton = new JButton();
        minusButton.add(minusLabel);
        buttonPanel.add(minusButton);

        minusButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(minusButton, "장바구니가 비었습니다.");
                    return;
                }
                if (mSelectRow == -1) {
                    JOptionPane.showMessageDialog(minusButton, "수량을 변경할 항목을 선택하세요.");
                    return;
                }

                CartItem item = mCart.getmCartItem().get(mSelectRow);

                if (item.getQuantity() == 1) {
                    int result = JOptionPane.showConfirmDialog(minusButton,
                            "수량이 1입니다. 이 항목을 삭제하시겠습니까?");
                    if (result == 0) {
                        mCart.removeCart(mSelectRow);
                        mSelectRow = -1;
                    }
                } else {
                    item.setQuantity(item.getQuantity() - 1);
                }

                refreshTable(cartTable, totalPricelabel, mCart);
            }
        });

        // ------------------------------
        // 새로 고침
        // ------------------------------
        JLabel refreshLabel = new JLabel("장바구니 새로 고침");
        refreshLabel.setFont(ft);
        JButton refreshButton = new JButton();
        refreshButton.add(refreshLabel);
        buttonPanel.add(refreshButton);

        refreshButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                refreshTable(cartTable, totalPricelabel, mCart);
            }
        });

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

    }

    // ------------------------------
    // ★ 테이블 및 총액 갱신 함수
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
