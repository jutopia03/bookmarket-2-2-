package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

    public MyOrderListPage(JPanel panel) {

        setLayout(new BorderLayout(10, 10));

        // --------------------------
        // 제목
        // --------------------------
        JLabel title = new JLabel("내 주문 내역", SwingConstants.CENTER);
        title.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIManager.getColor("Panel.background"));
        titlePanel.add(title, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // --------------------------
        // 테이블
        // --------------------------
        String[] columns = {
                "주문번호", "총 금액", "주문일시", "상태"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);

        // ★ 테이블이 차지할 '보이는 영역' 크기 설정
        // (실제 데이터 행 수와는 상관없이, 기본 보이는 크기)
        table.setPreferredScrollableViewportSize(new Dimension(700, 250));

        JScrollPane sp = new JScrollPane(table);

        // ★ 테두리(선) 완전히 제거
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBorder(null);

        // ★ BorderLayout.CENTER에 바로 넣지 말고
        //    가운데 패널(FlowLayout)에 넣어서 크기를 그대로 사용하게 함
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        centerPanel.setBackground(UIManager.getColor("Panel.background"));
        centerPanel.add(sp);

        add(centerPanel, BorderLayout.CENTER);

        // --------------------------
        // 하단 버튼
        // --------------------------
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(UIManager.getColor("Panel.background"));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton btnDetail = new JButton("주문 상세 보기");
        btnDetail.setPreferredSize(new Dimension(160, 45));
        btnDetail.setFont(new Font("함초롬돋움", Font.BOLD, 14));

        btnPanel.add(btnDetail);

        add(btnPanel, BorderLayout.SOUTH);

        // --------------------------
        // 상세보기 동작
        // --------------------------
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
