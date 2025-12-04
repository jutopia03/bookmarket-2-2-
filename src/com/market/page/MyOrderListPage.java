package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

import com.market.order.Order;
import com.market.order.OrderDAO;
import com.market.member.UserInIt;
import com.market.order.OrderItemView;

public class MyOrderListPage extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private OrderDAO orderDAO = new OrderDAO();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public MyOrderListPage(JPanel parent) {

        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(30, 40, 40, 40));

        Font titleFont = new Font("함초롬돋움", Font.BOLD, 24);
        Font subFont   = new Font("함초롬돋움", Font.PLAIN, 12);
        Font btnFont   = new Font("함초롬돋움", Font.BOLD, 14);
        Font tableFont = new Font("함초롬돋움", Font.PLAIN, 13);

        // 상단 타이틀
        JLabel titleLabel = new JLabel("내 주문 내역");
        titleLabel.setFont(titleFont);

        JLabel subLabel = new JLabel("내가 주문한 내역을 확인할 수 있습니다.");
        subLabel.setFont(subFont);
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

        // 중앙: 흰 카드 + 테이블
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);
        centerWrapper.setBorder(new EmptyBorder(30, 0, 0, 0));
        add(centerWrapper, BorderLayout.CENTER);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(20, 30, 20, 30)
        ));

        GridBagConstraints gbcCard = new GridBagConstraints();
        gbcCard.gridx = 0;
        gbcCard.gridy = 0;
        gbcCard.weightx = 1.0;
        gbcCard.weighty = 1.0;
        gbcCard.fill = GridBagConstraints.BOTH;
        gbcCard.anchor = GridBagConstraints.NORTH;
        centerWrapper.add(card, gbcCard);

        String[] columns = { "주문번호", "총 금액", "주문일시", "상태" };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFont(tableFont);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(235, 235, 235));
        table.setIntercellSpacing(new Dimension(0, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("함초롬돋움", Font.BOLD, 13));
        header.setBackground(new Color(248, 248, 248));
        header.setForeground(new Color(70, 70, 70));
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(0, 0, 0, 0));
        sp.getViewport().setBackground(Color.WHITE);
        sp.setPreferredSize(new Dimension(700, 260));

        card.add(sp, BorderLayout.CENTER);

        // 하단 버튼
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnDetail = new JButton("주문 상세 보기");
        btnDetail.setFont(btnFont);
        btnDetail.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDetail.setPreferredSize(new Dimension(160, 40));
        btnDetail.setMaximumSize(new Dimension(160, 40));
        btnDetail.setBackground(new Color(255, 107, 0));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFocusPainted(false);
        btnDetail.setBorderPainted(false);
        btnDetail.setContentAreaFilled(true);
        btnDetail.setOpaque(true);

        bottomPanel.add(btnDetail);
        add(bottomPanel, BorderLayout.SOUTH);

        btnDetail.addActionListener(e -> showDetail());

        loadMyOrders();
    }

    private void loadMyOrders() {
        model.setRowCount(0);

        int memberId = UserInIt.getmUser().getMemberId();
        List<Order> myOrders = orderDAO.getOrdersByMember(memberId);

        for (Order o : myOrders) {
            String dateStr = (o.getOrderDate() != null) ? sdf.format(o.getOrderDate()) : "";
            model.addRow(new Object[]{
                    o.getOrderId(),
                    o.getTotalPrice(),
                    dateStr,
                    o.getStatus()
            });
        }
    }

    private void showDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "주문을 선택하세요.");
            return;
        }

        int orderId = (Integer) table.getValueAt(row, 0);
        List<OrderItemView> items = orderDAO.getOrderItems(orderId);

        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "주문 상세 정보가 없습니다.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("주문번호: ").append(orderId).append("\n\n");

        int idx = 1;
        for (OrderItemView item : items) {
            sb.append(idx++).append(". [")
              .append(item.getBookId()).append("] ")
              .append(item.getBookName()).append(" - ")
              .append(item.getQuantity()).append("권, ")
              .append(item.getUnitPrice()).append("원\n");
        }

        JOptionPane.showMessageDialog(
                this,
                sb.toString(),
                "주문 상세",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
