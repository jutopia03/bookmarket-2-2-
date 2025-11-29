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
            // CartItem(Book) 생성자는 책 1권을 장바구니에 1개로 담습니다.
            CartItem item1 = new CartItem(b1);
            CartItem item2 = new CartItem(b2);

            // 수량 증가가 필요하면 item1.setQuantity(2)처럼 설정 가능
            item1.setQuantity(2);   // ISBN-001 두 권 주문
            item2.setQuantity(1);   // ISBN-002 한 권 주문

            cart.mCartItem.add(item1);
            cart.mCartItem.add(item2);

            // 총합 확인
            System.out.println("장바구니 총 금액: " + cart.getTotalPrice());

            // 4) OrderDAO로 주문 생성
            OrderDAO orderDAO = new OrderDAO();

            int memberId = 1;   // 반드시 member 테이블에 존재하는 ID여야 함
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
