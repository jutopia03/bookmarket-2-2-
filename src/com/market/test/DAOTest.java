package com.market.test;

import com.market.bookitem.Book;
import com.market.bookitem.BookDAO;

import java.util.List;

public class DAOTest {

    // 전체 책 목록을 찍는 공통 메서드
    private static void printAllBooks(BookDAO dao, String title) {
        System.out.println("=== " + title + " ===");
        List<Book> list = dao.getAllBooks();
        for (Book b : list) {
            System.out.println(
                    b.getBookId() + " | " +
                    b.getName() + " | " +
                    b.getUnitPrice()
            );
        }
        System.out.println();
    }

    public static void main(String[] args) {

        BookDAO dao = new BookDAO();

        // 1. 초기 상태 출력
        printAllBooks(dao, "초기 전체 조회");

        // 2. INSERT 테스트
        Book temp = new Book(
                "T999",
                "테스트용 책",
                12000,
                "테스트 저자",
                "테스트 설명",
                "IT",
                "2025-01-01",
                50      // 재고 (Book 생성자에 맞게 조정)
        );
        
        int inserted = dao.insertBook(temp);
        System.out.println("INSERT 결과 행 수: " + inserted);
        System.out.println();
        printAllBooks(dao, "INSERT 후 전체 조회");

        // 3. UPDATE 테스트
        temp.setName("수정된 테스트용 책");
        temp.setUnitPrice(15000);
        int updated = dao.updateBook(temp);
        System.out.println("UPDATE 결과 행 수: " + updated);
        System.out.println();
        printAllBooks(dao, "UPDATE 후 전체 조회");

        // 4. DELETE 테스트
        int deleted = dao.deleteBook("T999");
        System.out.println("DELETE 결과 행 수: " + deleted);
        System.out.println();
        printAllBooks(dao, "DELETE 후 전체 조회");

        System.out.println("DAO 테스트 완료");
    }
}
