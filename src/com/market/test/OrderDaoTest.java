package com.market.test;

import com.market.order.OrderDAO;
import com.market.bookitem.Book;
import com.market.bookitem.BookDAO;
import com.market.cart.Cart;
import com.market.cart.CartItem;

public class OrderDaoTest {

    public static void main(String[] args) {

        try {
            // 1) Cart 객체 생성
            Cart cart = new Cart();

            // 2) BookDAO로 DB에서 실제 책 2권 가져오기 (테스트용)
            BookDAO bookDAO = new BookDAO();

            Book b1 = bookDAO.getBookById("ISBN-001");
            Book b2 = bookDAO.getBookById("ISBN-002");

            // 3) CartItem으로 묶어서 장바구니에 담기
            CartItem item1 = new CartItem(b1);
            CartItem item2 = new CartItem(b2);

            item1.setQuantity(2);   
            item2.setQuantity(1);  

            cart.mCartItem.add(item1);
            cart.mCartItem.add(item2);

            System.out.println("장바구니 총 금액: " + cart.getTotalPrice());

            // 4) OrderDAO로 주문 생성
            OrderDAO orderDAO = new OrderDAO();

            int memberId = 1;   
            int orderId = orderDAO.createOrder(memberId, cart);

            // 5) 결과 출력
            System.out.println("\n=== 주문 생성 완료! ===");
            System.out.println("생성된 주문번호(order_id): " + orderId);
            System.out.println("=========================");

        } catch (Exception e) {
            System.out.println("OrderDaoTest 실행 중 오류 발생");
            e.printStackTrace();
        }
    }
}
