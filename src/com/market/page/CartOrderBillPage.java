package com.market.page;

import javax.swing.*;

import com.market.bookitem.BookInIt;
import com.market.cart.Cart;
import com.market.cart.CartItem;
import com.market.member.UserInIt;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CartOrderBillPage extends JPanel {

    private final Cart mCart;

    public CartOrderBillPage(JPanel parent, Cart cart) {

        this.mCart = cart;

        setOpaque(false);
        setLayout(new BorderLayout());

        Font titleFont = new Font("함초롬돋움", Font.BOLD, 15);
        Font baseFont  = new Font("함초롬돋움", Font.PLAIN, 14);

        String name    = UserInIt.getmUser() != null ? UserInIt.getmUser().getName()   : "";
        String phone   = UserInIt.getmUser() != null ? String.valueOf(UserInIt.getmUser().getPhone()) : "";
        String address = UserInIt.getmUser() != null ? UserInIt.getmUser().getAddress() : "";

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(20, 40, 20, 40));
        add(centerWrapper, BorderLayout.CENTER);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(20, 60, 25, 60)
        ));

        GridBagConstraints gbcCard = new GridBagConstraints();
        gbcCard.gridx = 0;
        gbcCard.gridy = 0;
        gbcCard.weightx = 1.0;
        gbcCard.weighty = 1.0;
        gbcCard.fill = GridBagConstraints.BOTH;
        gbcCard.anchor = GridBagConstraints.NORTH;
        centerWrapper.add(card, gbcCard);

        // 배송 정보
        JLabel sectionTitle = new JLabel("배송 받을 고객 정보", SwingConstants.CENTER);
        sectionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        sectionTitle.setFont(new Font("함초롬돋움", Font.BOLD, 16));

        card.add(sectionTitle);
        card.add(Box.createVerticalStrut(8));

        JPanel infoGrid = new JPanel(new GridLayout(2, 2, 40, 8));
        infoGrid.setOpaque(false);
        infoGrid.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nameLabel  = new JLabel("고객명 : " + name);
        JLabel phoneLabel = new JLabel("연락처 : " + phone);
        JLabel addrLabel  = new JLabel("배송지 : " + address);
        JLabel dateLabel  = new JLabel("발송일 : " + strDate);

        nameLabel.setFont(baseFont);
        phoneLabel.setFont(baseFont);
        addrLabel.setFont(baseFont);
        dateLabel.setFont(baseFont);

        infoGrid.add(nameLabel);
        infoGrid.add(phoneLabel);
        infoGrid.add(addrLabel);
        infoGrid.add(dateLabel);

        card.add(infoGrid);
        card.add(Box.createVerticalStrut(12));

        JSeparator sep1 = new JSeparator();
        sep1.setForeground(new Color(230, 230, 230));
        card.add(sep1);
        card.add(Box.createVerticalStrut(10));

        // 장바구니 목록
        JLabel listTitle = new JLabel("장바구니 상품 목록");
        listTitle.setFont(titleFont);
        listTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(listTitle);
        card.add(Box.createVerticalStrut(6));

        JPanel headerRow = new JPanel(new GridLayout(1, 3));
        headerRow.setOpaque(false);

        JLabel h1 = new JLabel("도서ID", SwingConstants.LEFT);
        JLabel h2 = new JLabel("수량",   SwingConstants.CENTER);
        JLabel h3 = new JLabel("합계",   SwingConstants.RIGHT);

        h1.setFont(baseFont);
        h2.setFont(baseFont);
        h3.setFont(baseFont);

        headerRow.add(h1);
        headerRow.add(h2);
        headerRow.add(h3);

        card.add(headerRow);

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(230, 230, 230));
        card.add(sep2);

        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setOpaque(false);

        int sum = 0;

        for (CartItem item : mCart.mCartItem) {
            JPanel row = new JPanel(new GridLayout(1, 3));
            row.setOpaque(false);
            row.setBorder(new EmptyBorder(4, 0, 4, 0));

            JLabel c1 = new JLabel(item.getBookID(), SwingConstants.LEFT);
            JLabel c2 = new JLabel(String.valueOf(item.getQuantity()), SwingConstants.CENTER);
            JLabel c3 = new JLabel(String.valueOf(item.getTotalPrice()), SwingConstants.RIGHT);

            c1.setFont(baseFont);
            c2.setFont(baseFont);
            c3.setFont(baseFont);

            row.add(c1);
            row.add(c2);
            row.add(c3);

            itemsPanel.add(row);

            sum += item.getTotalPrice();
        }

        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.setBorder(new EmptyBorder(5, 0, 5, 0));
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setPreferredSize(new Dimension(0, 260)); 
        card.add(scroll);
        card.add(Box.createVerticalStrut(10));

        JSeparator sep3 = new JSeparator();
        sep3.setForeground(new Color(230, 230, 230));
        card.add(sep3);
        card.add(Box.createVerticalStrut(10));

        JLabel totalLabel = new JLabel("주문 총금액 : " + sum + "원");
        totalLabel.setFont(new Font("함초롬돋움", Font.BOLD, 15));
        totalLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(totalLabel);
        card.add(Box.createVerticalGlue());
    }

    public static void main(String[] args) {

        Cart mCart = new Cart();
        JFrame frame = new JFrame();
        frame.setBounds(0, 0, 1000, 750);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mPagePanel = new JPanel(new BorderLayout());
        mPagePanel.setBackground(new Color(245, 245, 245));
        frame.add(mPagePanel, BorderLayout.CENTER);

        BookInIt.init();
        mPagePanel.add(new CartOrderBillPage(mPagePanel, mCart), BorderLayout.CENTER);

        frame.setVisible(true);
    }
}
