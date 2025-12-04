package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.market.cart.Cart;
import com.market.member.UserInIt;
import com.market.order.OrderDAO;

import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CartShippingPage extends JPanel {

    private final Cart mCart;

    private JPanel radioPanel;
    private JPanel shippingPanel;
    private JPanel bottomPanel;

    private JTextField tfName;
    private JTextField tfPhone;
    private JTextField tfAddress;

    private JButton orderButton;

    public CartShippingPage(JPanel panel, Cart cart) {

        this.mCart = cart;

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(30, 40, 40, 40));

        // 상단 타이틀
        JLabel titleLabel = new JLabel("배송 정보");
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 24));

        JLabel subLabel = new JLabel("배송받을 분의 정보를 확인하고 배송지를 입력해 주세요.");
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

        // 중앙 영역: 라디오 + 배송 정보 카드
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(30, 0, 0, 0));
        add(centerWrapper, BorderLayout.CENTER);

        // 라디오 영역
        radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        radioPanel.setOpaque(false);

        JLabel radioLabel = new JLabel("배송받을 분은 고객정보와 같습니까?");
        radioLabel.setFont(ft);

        JRadioButton radioOk = new JRadioButton("예");
        radioOk.setFont(ft);
        JRadioButton radioNo = new JRadioButton("아니오");
        radioNo.setFont(ft);

        ButtonGroup bg = new ButtonGroup();
        bg.add(radioOk);
        bg.add(radioNo);

        radioOk.setOpaque(false);
        radioNo.setOpaque(false);

        radioPanel.add(radioLabel);
        radioPanel.add(Box.createHorizontalStrut(10));
        radioPanel.add(radioOk);
        radioPanel.add(radioNo);

        centerWrapper.add(radioPanel, BorderLayout.NORTH);

        // 배송 정보 카드
        shippingPanel = new JPanel(new GridBagLayout());
        shippingPanel.setOpaque(false);
        centerWrapper.add(shippingPanel, BorderLayout.CENTER);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(30, 60, 30, 60)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        GridBagConstraints gbcCard = new GridBagConstraints();
        gbcCard.gridx = 0;
        gbcCard.gridy = 0;
        gbcCard.weightx = 1.0;
        gbcCard.weighty = 1.0;
        gbcCard.anchor = GridBagConstraints.NORTH;
        gbcCard.fill = GridBagConstraints.HORIZONTAL;
        shippingPanel.add(card, gbcCard);

        tfName = createFieldRow(card, "고객명", ft, false);
        tfPhone = createFieldRow(card, "연락처", ft, false);
        tfAddress = createFieldRow(card, "배송지", ft, false);

        // 하단 버튼
        bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        orderButton = new JButton("주문완료");
        orderButton.setFont(ft);
        orderButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        orderButton.setPreferredSize(new Dimension(180, 40));
        orderButton.setMaximumSize(new Dimension(180, 40));
        orderButton.setBackground(new Color(255, 107, 0));
        orderButton.setForeground(Color.WHITE);
        orderButton.setFocusPainted(false);
        orderButton.setBorderPainted(false);
        orderButton.setContentAreaFilled(true);
        orderButton.setOpaque(true);

        bottomPanel.add(orderButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 초기값: 예(고객정보와 동일)
        radioOk.setSelected(true);
        applyUserInfoToFields(true);

        radioOk.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                applyUserInfoToFields(true);
            }
        });

        radioNo.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                applyUserInfoToFields(false);
            }
        });

        // 주문 완료
        orderButton.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {

                try {
                    String recvName = tfName.getText().trim();
                    String recvPhone = tfPhone.getText().trim();
                    String recvAddr = tfAddress.getText().trim();

                    if (recvName.isEmpty() || recvPhone.isEmpty() || recvAddr.isEmpty()) {
                        JOptionPane.showMessageDialog(
                                CartShippingPage.this,
                                "고객명, 연락처, 배송지를 모두 입력해 주세요."
                        );
                        return;
                    }

                    int memberId = UserInIt.getmUser().getMemberId();

                    OrderDAO orderDAO = new OrderDAO();
                    int orderId = orderDAO.createOrder(memberId, mCart);

                    JOptionPane.showMessageDialog(
                            CartShippingPage.this,
                            "주문이 완료되었습니다!\n주문번호: " + orderId
                    );

                    // 주문 완료 후 버튼/버튼 영역 숨기기
                    orderButton.setEnabled(false);
                    orderButton.setVisible(false);
                    bottomPanel.setVisible(false);

                    radioPanel.removeAll();
                    radioPanel.revalidate();
                    radioPanel.repaint();

                    shippingPanel.removeAll();
                    shippingPanel.setLayout(new BorderLayout());
                    shippingPanel.add(new CartOrderBillPage(shippingPanel, mCart), BorderLayout.CENTER);
                    mCart.deleteBook();

                    shippingPanel.revalidate();
                    shippingPanel.repaint();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    String msg = ex.getMessage();

                    if (msg != null && msg.startsWith("STOCK_NOT_ENOUGH")) {
                        JOptionPane.showMessageDialog(
                                CartShippingPage.this,
                                "재고 부족으로 주문할 수 없습니다.\n\n"
                                        + msg.replace("STOCK_NOT_ENOUGH: ", ""),
                                "재고 부족",
                                JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        JOptionPane.showMessageDialog(
                                CartShippingPage.this,
                                "주문 처리 중 오류가 발생했습니다.\n" + msg,
                                "주문 오류",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
    }

    private JTextField createFieldRow(JPanel parent, String labelText, Font ft, boolean editable) {

        JPanel row = new JPanel(new BorderLayout());
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(235, 235, 235)));

        JLabel label = new JLabel(labelText + " : ", SwingConstants.RIGHT);
        label.setFont(ft);
        label.setPreferredSize(new Dimension(80, 30));
        row.add(label, BorderLayout.WEST);

        JTextField field = new JTextField();
        field.setFont(ft);
        field.setEditable(editable);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220)),
                new EmptyBorder(5, 10, 5, 10)
        ));
        field.setBackground(new Color(248, 248, 248));

        row.add(field, BorderLayout.CENTER);
        parent.add(row);

        return field;
    }

    private void applyUserInfoToFields(boolean sameAsUser) {

        if (sameAsUser && UserInIt.getmUser() != null) {
            tfName.setText(UserInIt.getmUser().getName());
            tfPhone.setText(String.valueOf(UserInIt.getmUser().getPhone()));
            tfAddress.setText(UserInIt.getmUser().getAddress());

            tfName.setEditable(false);
            tfPhone.setEditable(false);
            tfAddress.setEditable(false);

            tfName.setBackground(new Color(240, 240, 240));
            tfPhone.setBackground(new Color(240, 240, 240));
            tfAddress.setBackground(new Color(240, 240, 240));
        } else {
            tfName.setEditable(true);
            tfPhone.setEditable(true);
            tfAddress.setEditable(true);

            tfName.setBackground(Color.WHITE);
            tfPhone.setBackground(Color.WHITE);
            tfAddress.setBackground(Color.WHITE);

            tfName.setText("");
            tfPhone.setText("");
            tfAddress.setText("");
        }
    }
}
