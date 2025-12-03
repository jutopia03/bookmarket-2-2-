package com.market.order;

import com.market.common.DBUtil;
import com.market.cart.Cart;
import com.market.cart.CartItem;
import com.market.bookitem.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // ============================
    //  주문 생성 (재고 확인 + 재고 차감 포함)
    // ============================
    public int createOrder(int memberId, Cart cart) throws SQLException {

        String insertOrderSql =
                "INSERT INTO orders (member_id, total_price) VALUES (?, ?)";

        String insertOrderItemSql =
                "INSERT INTO order_item (order_id, book_id, quantity, unit_price) " +
                "VALUES (?, ?, ?, ?)";

        String selectStockSql =
                "SELECT stock, name FROM book WHERE book_id = ? FOR UPDATE";

        String updateStockSql =
                "UPDATE book SET stock = stock - ? WHERE book_id = ?";

        Connection conn = null;
        PreparedStatement orderPstmt = null;
        PreparedStatement itemPstmt = null;
        PreparedStatement stockSelectPstmt = null;
        PreparedStatement stockUpdatePstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 0) 장바구니 아이템들 재고 확인
            List<CartItem> cartItems = cart.getmCartItem();
            stockSelectPstmt = conn.prepareStatement(selectStockSql);

            for (CartItem ci : cartItems) {
                Book book = ci.getItemBook();
                String bookId = book.getBookId();
                int qty = ci.getQuantity();

                stockSelectPstmt.setString(1, bookId);
                try (ResultSet rsStock = stockSelectPstmt.executeQuery()) {
                    if (!rsStock.next()) {
                        throw new SQLException("STOCK_NOT_FOUND: " + bookId + " 도서를 찾을 수 없습니다.");
                    }
                    int stock = rsStock.getInt("stock");
                    String name = rsStock.getString("name");

                    if (stock < qty) {
                        throw new SQLException(
                                "STOCK_NOT_ENOUGH: [" + bookId + "] " + name +
                                " / 남은 재고: " + stock +
                                " / 요청 수량: " + qty
                        );
                    }
                }
            }

            // 1) orders INSERT
            orderPstmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS);

            int totalPrice = cart.getTotalPrice();
            orderPstmt.setInt(1, memberId);
            orderPstmt.setInt(2, totalPrice);

            int affectedRows = orderPstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("주문 저장 실패: orders INSERT 결과 0행");
            }

            // 2) 생성된 order_id 얻기
            rs = orderPstmt.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                throw new SQLException("주문 저장 실패: 생성된 order_id 없음");
            }

            // 3) order_item INSERT + book 재고 차감
            itemPstmt = conn.prepareStatement(insertOrderItemSql);
            stockUpdatePstmt = conn.prepareStatement(updateStockSql);

            for (CartItem ci : cartItems) {
                Book book = ci.getItemBook();
                String bookId = book.getBookId();
                int qty = ci.getQuantity();
                int unitPrice = book.getUnitPrice();

                // order_item insert
                itemPstmt.setInt(1, orderId);
                itemPstmt.setString(2, bookId);
                itemPstmt.setInt(3, qty);
                itemPstmt.setInt(4, unitPrice);
                itemPstmt.addBatch();

                // 재고 차감
                stockUpdatePstmt.setInt(1, qty);
                stockUpdatePstmt.setString(2, bookId);
                stockUpdatePstmt.addBatch();
            }

            itemPstmt.executeBatch();
            stockUpdatePstmt.executeBatch();

            conn.commit();

            return orderId;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (orderPstmt != null) try { orderPstmt.close(); } catch (Exception ignored) {}
            if (itemPstmt != null) try { itemPstmt.close(); } catch (Exception ignored) {}
            if (stockSelectPstmt != null) try { stockSelectPstmt.close(); } catch (Exception ignored) {}
            if (stockUpdatePstmt != null) try { stockUpdatePstmt.close(); } catch (Exception ignored) {}
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    // ============================
    //  전체 주문 조회
    // ============================
    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();

        String sql =
                "SELECT o.order_id, o.member_id, m.username, " +
                "       o.total_price, o.order_date, o.status " +
                "FROM orders o " +
                "LEFT JOIN member m ON o.member_id = m.member_id " +
                "ORDER BY o.order_id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int orderId     = rs.getInt("order_id");
                int memberId    = rs.getInt("member_id");
                String username = rs.getString("username");
                int totalPrice  = rs.getInt("total_price");
                Timestamp orderDate = rs.getTimestamp("order_date");
                String status   = rs.getString("status");

                Order order = new Order(orderId, memberId, username,
                        totalPrice, orderDate, status);
                list.add(order);
            }

        } catch (SQLException e) {
            System.out.println("주문 목록 조회 중 오류");
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    //  회원별 주문 조회
    // ============================
    public List<Order> getOrdersByMember(int memberId) {
        List<Order> list = new ArrayList<>();

        String sql =
                "SELECT o.order_id, o.member_id, m.username, " +
                "       o.total_price, o.order_date, o.status " +
                "FROM orders o " +
                "LEFT JOIN member m ON o.member_id = m.member_id " +
                "WHERE o.member_id = ? " +
                "ORDER BY o.order_id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int orderId     = rs.getInt("order_id");
                    int mid         = rs.getInt("member_id");
                    String username = rs.getString("username");
                    int totalPrice  = rs.getInt("total_price");
                    Timestamp orderDate = rs.getTimestamp("order_date");
                    String status   = rs.getString("status");

                    Order order = new Order(orderId, mid, username,
                            totalPrice, orderDate, status);
                    list.add(order);
                }
            }

        } catch (SQLException e) {
            System.out.println("회원별 주문 조회 중 오류");
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    //  주문 상세 조회
    // ============================
    public List<OrderItemView> getOrderItems(int orderId) {
        List<OrderItemView> list = new ArrayList<>();

        String sql =
                "SELECT oi.book_id, b.name, oi.quantity, oi.unit_price " +
                "FROM order_item oi " +
                "JOIN book b ON oi.book_id = b.book_id " +
                "WHERE oi.order_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String bookId = rs.getString("book_id");
                    String name   = rs.getString("name");
                    int quantity  = rs.getInt("quantity");
                    int unitPrice = rs.getInt("unit_price");

                    OrderItemView item =
                            new OrderItemView(bookId, name, quantity, unitPrice);
                    list.add(item);
                }
            }

        } catch (SQLException e) {
            System.out.println("주문 상세 조회 중 오류");
            e.printStackTrace();
        }

        return list;
    }

    // ============================
    //  주문 상태 변경 (취소 등)
    // ============================
    public int updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);

            return pstmt.executeUpdate();   // 1 이면 성공

        } catch (SQLException e) {
            System.out.println("주문 상태 변경 중 오류");
            e.printStackTrace();
        }

        return 0;
    }
}
