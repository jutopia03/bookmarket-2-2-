package com.market.bookitem;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BookInIt {
    private static ArrayList<Book> mBookList;
    private static int mTotalBook = 0;

    public static void init() {
        BookDAO bookDAO = new BookDAO();
        mBookList = new ArrayList<>(bookDAO.getAllBooks());
        mTotalBook = mBookList.size();

        System.out.println("DB에서 불러온 책 개수: " + mTotalBook);
    }

    public static int totalFileToBookList() {
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("book.txt"),
                    Charset.forName("MS949")   // ← 인코딩 지정
                )
            )
        ) {
            String str;
            int num = 0;
            while ((str = reader.readLine()) != null) {
                if (str.contains("ISBN"))
                    ++num;
            }
            return num;
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    public static void setFileToBookList(ArrayList<Book> booklist) {
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    new FileInputStream("book.txt"),
                    Charset.forName("MS949")   // ← 여기도 동일하게
                )
            )
        ) {
            String str2;
            String[] readBook = new String[7];

            while ((str2 = reader.readLine()) != null) {
                if (str2.contains("ISBN")) {
                    readBook[0] = str2;
                    readBook[1] = reader.readLine();
                    readBook[2] = reader.readLine();
                    readBook[3] = reader.readLine();
                    readBook[4] = reader.readLine();
                    readBook[5] = reader.readLine();
                    readBook[6] = reader.readLine();

                    Book bookitem = new Book(
                        readBook[0], readBook[1],
                        Integer.parseInt(readBook[2]),
                        readBook[3], readBook[4],
                        readBook[5], readBook[6]
                    );
                    booklist.add(bookitem);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static ArrayList<Book> getmBookList() {
        return mBookList;
    }

    public static void setmBookList(ArrayList<Book> mBookList) {
        BookInIt.mBookList = mBookList;
    }

    public static int getmTotalBook() {
        return mTotalBook;
    }

    public static void setmTotalBook(int mTotalBook) {
        BookInIt.mTotalBook = mTotalBook;
    }
}
