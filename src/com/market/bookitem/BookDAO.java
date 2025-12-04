package com.market.bookitem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.market.common.DBUtil;

public class BookDAO {

    // 모든 도서 조회
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();

        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date,stock FROM book";
        
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
                int stock = rs.getInt("stock");
                
                Book book = new Book(
                        bookId,
                        name,
                        unitPrice,
                        author,
                        description,
                        category,
                        releaseDate,
                        stock
                );

                bookList.add(book);
            }

        } catch (SQLException e) {
            System.out.println("DB에서 book 목록을 불러오는 중 오류 발생");
            e.printStackTrace();
        }

        return bookList;
    }
    
    //도서 조회
    public Book getBookById(String bookIdParam) {
        String sql = "SELECT book_id, name, unit_price, author, description, category, release_date,stock " +
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
                    int stock = rs.getInt("stock");
            

                    return new Book(
                            bookId,
                            name,
                            unitPrice,
                            author,
                            description,
                            category,
                            releaseDate,
                            stock
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Book 단건 조회 오류");
            e.printStackTrace();
        }
        return null;
    }
    
    // 도서 추가
    public int insertBook(Book book) {
        String sql = "INSERT INTO book (" +
                     "book_id, name, unit_price, author, description, category, release_date, stock" +
                     ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getBookId());          
            pstmt.setString(2, book.getName());            
            pstmt.setInt(3, book.getUnitPrice());          
            pstmt.setString(4, book.getAuthor());          
            pstmt.setString(5, book.getDescription());   
            pstmt.setString(6, book.getCategory());        
            pstmt.setString(7, book.getReleaseDate());   
            pstmt.setInt(8, book.getStock());      

            return pstmt.executeUpdate();  

        } catch (SQLException e) {
            System.out.println("insertBook 오류");
            e.printStackTrace();
        }

        return 0; 
    }
    // 도서 수정
    public int updateBook(Book book) {
        String sql = "UPDATE book SET " +
                     "name = ?, " +
                     "unit_price = ?, " +
                     "author = ?, " +
                     "description = ?, " +
                     "category = ?, " +
                     "release_date = ?, " +
                     "stock = ? " +
                     "WHERE book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getName());
            pstmt.setInt(2, book.getUnitPrice());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getDescription());
            pstmt.setString(5, book.getCategory());
            pstmt.setString(6, book.getReleaseDate());
            pstmt.setInt(7, book.getStock());
            pstmt.setString(8, book.getBookId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("updateBook 오류");
            e.printStackTrace();
        }

        return 0;
    }
    // 도서 삭제
    public int deleteBook(String bookId) {
        String sql = "DELETE FROM book WHERE book_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, bookId);

            return pstmt.executeUpdate();  

        } catch (SQLException e) {
            System.out.println("deleteBook 오류");
            e.printStackTrace();
        }

        return 0;
    }

}
