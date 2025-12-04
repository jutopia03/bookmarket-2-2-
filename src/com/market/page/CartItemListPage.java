package com.market.page;

import javax.swing.*;
import com.market.cart.Cart;
import com.market.cart.CartItem;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

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
        Font baseFont = new Font("함초롬돋움", Font.PLAIN, 14);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(30, 40, 40, 40));

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

        // 상단 타이틀
        JLabel titleLabel = new JLabel("장바구니");
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 24));

        JLabel subLabel = new JLabel("담겨 있는 도서를 확인하고 수량을 조정한 뒤 주문할 수 있습니다.");
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

        // 중앙 영역: 총금액 + 카드형 테이블
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(30, 0, 0, 0));
        add(centerWrapper, BorderLayout.CENTER);

        JLabel totalPriceLabel = new JLabel("총금액: " + totalPrice + " 원");
        totalPriceLabel.setFont(ft);
        totalPriceLabel.setForeground(Color.RED);

        JPanel totalPricePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        totalPricePanel.setOpaque(false);
        totalPricePanel.add(totalPriceLabel);
        centerWrapper.add(totalPricePanel, BorderLayout.NORTH);

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        centerWrapper.add(tableCard, BorderLayout.CENTER);

        cartTable = new JTable(content, tableHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        cartTable.setFont(baseFont);
        cartTable.setRowHeight(26);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartTable.setShowVerticalLines(false);
        cartTable.setShowHorizontalLines(true);
        cartTable.setGridColor(new Color(235, 235, 235));
        cartTable.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = cartTable.getTableHeader();
        header.setFont(new Font("함초롬돋움", Font.BOLD, 13));
        header.setBackground(new Color(248, 248, 248));
        header.setForeground(new Color(70, 70, 70));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        CartTableCellRenderer cellRenderer = new CartTableCellRenderer();
        for (int i = 0; i < cartTable.getColumnModel().getColumnCount(); i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane jScrollPane = new JScrollPane(cartTable);
        jScrollPane.getViewport().setBackground(Color.WHITE);
        jScrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));

        tableCard.add(jScrollPane, BorderLayout.CENTER);

        cartTable.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                mSelectRow = cartTable.getSelectedRow();
            }
            public void mousePressed(MouseEvent e) {}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
        });

        // 하단 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonRow.setOpaque(false);

        JButton clearButton = new JButton("장바구니 비우기");
        JButton plusButton = new JButton("수량 증가");
        JButton minusButton = new JButton("수량 감소");
        JButton removeButton = new JButton("항목 삭제");
        JButton orderButton = new JButton("주문하기");

        JButton[] grayButtons = { clearButton, plusButton, minusButton, removeButton };
        for (JButton b : grayButtons) {
            b.setFont(ft);
            b.setPreferredSize(new Dimension(130, 36));
            b.setFocusPainted(false);
            b.setBackground(new Color(245, 245, 245));
            b.setForeground(new Color(60, 60, 60));
            b.setBorder(new LineBorder(new Color(210, 210, 210)));
            b.setContentAreaFilled(true);
            b.setOpaque(true);
        }

        orderButton.setFont(ft);
        orderButton.setPreferredSize(new Dimension(130, 36));
        orderButton.setFocusPainted(false);
        orderButton.setBackground(new Color(255, 107, 0));
        orderButton.setForeground(Color.WHITE);
        orderButton.setBorderPainted(false);
        orderButton.setContentAreaFilled(true);
        orderButton.setOpaque(true);

        buttonRow.add(clearButton);
        buttonRow.add(plusButton);
        buttonRow.add(minusButton);
        buttonRow.add(removeButton);
        buttonRow.add(orderButton);

        bottomPanel.add(buttonRow);
        add(bottomPanel, BorderLayout.SOUTH);

        clearButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(clearButton, "장바구니에 항목이 없습니다");
                } else {
                    int select = JOptionPane.showConfirmDialog(clearButton, "장바구니의 모든 항목을 삭제하겠습니까?");
                    if (select == JOptionPane.YES_OPTION) {

                        TableModel tableModel = new DefaultTableModel(new Object[0][0], tableHeader);
                        cartTable.setModel(tableModel);
                        totalPriceLabel.setText("총금액: 0 원");

                        JOptionPane.showMessageDialog(clearButton, "장바구니의 모든 항목을 삭제했습니다");

                        mCart.deleteBook();
                        mSelectRow = -1;
                    }
                }
            }
        });

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

                refreshTable(cartTable, totalPriceLabel, mCart);
            }
        });

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

                refreshTable(cartTable, totalPriceLabel, mCart);
            }
        });

        removeButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                if (mCart.mCartCount == 0) {
                    JOptionPane.showMessageDialog(removeButton, "장바구니에 항목이 없습니다");
                } else if (mSelectRow == -1) {
                    JOptionPane.showMessageDialog(removeButton, "삭제할 항목을 선택하세요");
                } else {

                    mCart.removeCart(mSelectRow);

                    refreshTable(cartTable, totalPriceLabel, mCart);
                    mSelectRow = -1;
                }
            }
        });

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

    private void refreshTable(JTable cartTable, JLabel totalPriceLabel, Cart cart) {

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

        CartTableCellRenderer cellRenderer = new CartTableCellRenderer();
        for (int i = 0; i < cartTable.getColumnModel().getColumnCount(); i++) {
            cartTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        totalPriceLabel.setText("총금액: " + totalPrice + " 원");
    }

    private static class CartTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            setHorizontalAlignment(SwingConstants.LEFT);
            setBorder(new EmptyBorder(0, 8, 0, 8));

            if (isSelected) {
                c.setBackground(new Color(255, 245, 230));
            } else {
                if (row % 2 == 0) {
                    c.setBackground(Color.WHITE);
                } else {
                    c.setBackground(new Color(250, 250, 250));
                }
            }

            return c;
        }
    }
}
