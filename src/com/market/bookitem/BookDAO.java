package com.market.bookitem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.market.common.DBUtil;

public class BookDAO {

    // 모든 도서 조회
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date FROM book";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String bookId = rs.getString("book_id");
                String name = rs.getString("name");
                int unitPrice = rs.getInt("unit_price");

                String author = rs.getString("author");
                String description = rs.getString("description");
                String category = rs.getString("category");
                String releaseDate = rs.getString("release_date");

                // Book 클래스의 두 번째 생성자 사용
                Book book = new Book(
                        bookId,
                        name,
                        unitPrice,
                        author,
                        description,
                        category,
                        releaseDate
                );

                bookList.add(book);
            }

        } catch (SQLException e) {
            System.out.println("DB에서 book 목록을 불러오는 중 오류 발생");
            e.printStackTrace();
        }

        return bookList;
    }
    
    public Book getBookById(String bookIdParam) {
        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date " +
                     "FROM book WHERE book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookIdParam);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String bookId = rs.getString("book_id");
                    String name = rs.getString("name");
                    int unitPrice = rs.getInt("unit_price");

                    String author = rs.getString("author");
                    String description = rs.getString("description");
                    String category = rs.getString("category");
                    String releaseDate = rs.getString("release_date");

                    return new Book(
                            bookId,
                            name,
                            unitPrice,
                            author,
                            description,
                            category,
                            releaseDate
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Book 단건 조회 오류");
            e.printStackTrace();
        }
        return null;
    }

}
