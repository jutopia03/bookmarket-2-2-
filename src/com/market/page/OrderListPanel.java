package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.market.order.Order;
import com.market.order.OrderDAO;
import com.market.order.OrderItemView;

public class OrderListPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private OrderDAO orderDAO = new OrderDAO();
    private List<Order> allOrders = new ArrayList<>();

    // 검색/필터/정렬용
    private JComboBox<String> cbField;     
    private JTextField tfKeyword;         
    private JComboBox<String> cbStatus;   
    private JComboBox<String> cbSort;      

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public OrderListPanel() {
        // 전체 배경 흰색
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setOpaque(true);

        // ===== 제목 =====
        JLabel title = new JLabel("주문 관리", SwingConstants.CENTER);
        title.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        // ===== 검색/정렬 영역 =====
        cbSort = new JComboBox<>(new String[]{
                "정렬 없음",
                "최신 순",    
                "오래된 순", 
                "금액 높은 순",
                "금액 낮은 순"
        });

        cbField = new JComboBox<>(new String[]{
                "전체", "주문번호", "회원ID", "아이디"
        });

        cbStatus = new JComboBox<>(new String[]{
                "전체", "ORDERED", "CANCELED"
        });

        tfKeyword = new JTextField(15);
        JButton btnSearch = new JButton("검색");
        JButton btnReset  = new JButton("초기화");

        // 왼쪽: 정렬
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        sortPanel.setBackground(Color.WHITE);
        sortPanel.add(new JLabel("정렬:"));
        sortPanel.add(cbSort);

        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        midPanel.setBackground(Color.WHITE);
        midPanel.add(new JLabel("검색:"));
        midPanel.add(cbField);
        midPanel.add(tfKeyword);
        midPanel.add(new JLabel("상태:"));
        midPanel.add(cbStatus);

        midPanel.add(tfKeyword);
        midPanel.add(btnSearch);

        // 오른쪽: 초기화
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        resetPanel.setBackground(Color.WHITE);
        resetPanel.add(btnReset);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.add(sortPanel, BorderLayout.WEST);
        searchPanel.add(midPanel, BorderLayout.CENTER);
        searchPanel.add(resetPanel, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // ===== 중앙: 테이블 =====
        String[] columns = {
                "주문번호", "회원ID", "아이디", "총 금액", "주문일시", "상태"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);
        add(sp, BorderLayout.CENTER);

        // ===== 하단 버튼 =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnPanel.setBackground(Color.WHITE);
        JButton btnDetail = new JButton("주문 상세");
        JButton btnCancel = new JButton("주문 취소");
        JButton btnReload = new JButton("새로고침");

        btnPanel.add(btnDetail);
        btnPanel.add(btnCancel);
        btnPanel.add(btnReload);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== 이벤트 등록 =====

        // 검색 버튼 & 엔터
        btnSearch.addActionListener(e -> applyFilterAndSort());
        tfKeyword.addActionListener(e -> applyFilterAndSort());
        cbStatus.addActionListener(e -> applyFilterAndSort());
        cbSort.addActionListener(e -> applyFilterAndSort());

        // 초기화
        btnReset.addActionListener(e -> {
            cbSort.setSelectedIndex(0);
            cbField.setSelectedIndex(0);
            cbStatus.setSelectedIndex(0);
            tfKeyword.setText("");
            applyFilterAndSort();
        });

        // 새로고침 (DB 다시 조회)
        btnReload.addActionListener(e -> loadOrders());

        // 주문 상세
        btnDetail.addActionListener(e -> showDetail());

        // 주문 취소
        btnCancel.addActionListener(e -> cancelSelectedOrder());

        // 처음 진입 시 목록 불러오기
        loadOrders();
    }

    // ================= 주문 목록 로딩 =================
    private void loadOrders() {
        allOrders.clear();
        List<Order> list = orderDAO.getAllOrders();
        if (list != null) {
            allOrders.addAll(list);
        }
        applyFilterAndSort();
    }

    // ================= 검색 + 정렬 적용 =================
    private void applyFilterAndSort() {
        String field   = (String) cbField.getSelectedItem();
        String keyword = tfKeyword.getText().trim().toLowerCase();
        String statusFilter = (String) cbStatus.getSelectedItem();
        String sort    = (String) cbSort.getSelectedItem();

        List<Order> filtered = new ArrayList<>();

        for (Order o : allOrders) {

            // 1) 상태 필터
            if (!"전체".equals(statusFilter)) {
                if (o.getStatus() == null || !o.getStatus().equals(statusFilter)) {
                    continue;
                }
            }

            // 2) 검색어 필터
            if (!keyword.isEmpty()) {
                String target;

                switch (field) {
                    case "주문번호":
                        target = String.valueOf(o.getOrderId());
                        break;
                    case "회원ID":
                        target = String.valueOf(o.getMemberId());
                        break;
                    case "아이디":
                        target = o.getUsername();
                        break;
                    default:
                        target = o.getOrderId() + " " +
                                 o.getMemberId() + " " +
                                 o.getUsername();
                }

                if (target == null ||
                        !target.toLowerCase().contains(keyword)) {
                    continue;
                }
            }

            filtered.add(o);
        }

        // 3) 정렬
        Comparator<Order> comp = null;

        if ("최신 순".equals(sort)) {
            comp = Comparator.comparing(Order::getOrderDate).reversed();
        } else if ("오래된 순".equals(sort)) {
            comp = Comparator.comparing(Order::getOrderDate);
        } else if ("금액 높은 순".equals(sort)) {
            comp = Comparator.comparingInt(Order::getTotalPrice).reversed();
        } else if ("금액 낮은 순".equals(sort)) {
            comp = Comparator.comparingInt(Order::getTotalPrice);
        }

        if (comp != null) {
            filtered.sort(comp);
        }

        // 4) 테이블 갱신
        refreshTable(filtered);
    }

    private void refreshTable(List<Order> list) {
        model.setRowCount(0);

        for (Order o : list) {
            String dateStr = (o.getOrderDate() != null)
                    ? sdf.format(o.getOrderDate())
                    : "";

            model.addRow(new Object[]{
                    o.getOrderId(),
                    o.getMemberId(),
                    o.getUsername(),
                    o.getTotalPrice(),
                    dateStr,
                    o.getStatus()
            });
        }
    }

    // ================= 주문 상세 =================
    private void showDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "상세를 볼 주문을 선택하세요.");
            return;
        }

        int orderId = (Integer) table.getValueAt(row, 0);

        List<OrderItemView> items = orderDAO.getOrderItems(orderId);
        if (items == null || items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "해당 주문의 상세 내역이 없습니다.");
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

    // ================= 주문 취소(상태 변경) =================
    private void cancelSelectedOrder() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "취소할 주문을 선택하세요.");
            return;
        }

        int orderId = (Integer) table.getValueAt(row, 0);
        String status = (String) table.getValueAt(row, 5);

        if ("CANCELED".equals(status)) {
            JOptionPane.showMessageDialog(this, "이미 취소된 주문입니다.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "주문번호 [" + orderId + "] 를 취소하시겠습니까?",
                "주문 취소",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int result = orderDAO.updateOrderStatus(orderId, "CANCELED");
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "주문이 취소되었습니다.");
            loadOrders();
        } else {
            JOptionPane.showMessageDialog(this, "주문 취소에 실패했습니다.");
        }
    }
}
