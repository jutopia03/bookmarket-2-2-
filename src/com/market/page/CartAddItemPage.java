package com.market.page;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import com.market.bookitem.Book;
import com.market.bookitem.BookInIt;
import com.market.cart.Cart;

public class CartAddItemPage extends JPanel {

    private JPanel parentPanel;
    private Cart mCart;
    private JTable table;
    private JLabel imageLabel;
    private JLabel selectedInfoLabel;

    private ArrayList<Book> bookList;
    private int selectedRow = -1;

    public CartAddItemPage(JPanel panel, Cart cart) {

        this.parentPanel = panel;
        this.mCart = cart;

        setLayout(new BorderLayout());
        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        // ============================
        // 상단 제목
        // ============================
        JLabel titleLabel = new JLabel("도서 목록", SwingConstants.CENTER);
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(UIManager.getColor("Panel.background"));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        add(titlePanel, BorderLayout.NORTH);

        // 1) 도서 목록 로딩
        bookList = BookInIt.getmBookList();
        if (bookList == null) {
            bookList = new ArrayList<>();
        }

        // ============================
        // 왼쪽 이미지 영역
        // ============================
        JPanel imagePanel = new JPanel();
        imagePanel.setBorder(BorderFactory.createEmptyBorder(-40, 150, 0, 0));
        imagePanel.setPreferredSize(new Dimension(220, 260));
        imageLabel = new JLabel();
        imagePanel.add(imageLabel);
        imagePanel.setPreferredSize(new Dimension(400, 500));
        imageLabel.setPreferredSize(new Dimension(400, 500));
        setBookImage(null);

        // ============================
        // 오른쪽 도서 테이블
        // ============================
        String[] colNames = { "ID", "제목", "가격" };
        Object[][] rowData = new Object[bookList.size()][3];

        for (int i = 0; i < bookList.size(); i++) {
            Book b = bookList.get(i);
            rowData[i][0] = b.getBookId();
            rowData[i][1] = b.getName();
            rowData[i][2] = b.getUnitPrice();
        }

        table = new JTable(rowData, colNames);
        table.setPreferredScrollableViewportSize(new Dimension(500, 400));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tableWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tableWrapper.add(scrollPane);

        // ============================
        // 가운데: 이미지 + 테이블
        // ============================
        JPanel rowPanel = new JPanel();
        rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));

        rowPanel.add(Box.createHorizontalGlue());
        rowPanel.add(imagePanel);
        rowPanel.add(Box.createHorizontalStrut(10));
        rowPanel.add(tableWrapper);
        rowPanel.add(Box.createHorizontalGlue());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(rowPanel);
        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel, BorderLayout.CENTER);

        // ============================
        // 선택된 도서 정보 라벨
        // ============================
        selectedInfoLabel = new JLabel("선택된 도서가 없습니다.");
        selectedInfoLabel.setFont(ft);
        selectedInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        selectedInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 테이블 클릭 시 정보 업데이트
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String bookId = (String) table.getValueAt(selectedRow, 0);
                    String title  = (String) table.getValueAt(selectedRow, 1);
                    int price     = (Integer) table.getValueAt(selectedRow, 2);

                    setBookImage(bookId);

                    selectedInfoLabel.setText(
                            "선택한 도서: " + bookId + " / " + title + " / " + price + "원"
                    );
                }
            }
        });

        // ============================
        // 하단 버튼
        // ============================
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        JButton addBtn = new JButton("장바구니에 담기");
        addBtn.setFont(ft);
        buttonPanel.add(addBtn);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(selectedInfoLabel);
        bottomPanel.add(Box.createVerticalStrut(5));
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // 장바구니 담기 기능
        addBtn.addActionListener(e -> {
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(addBtn, "추가할 도서를 먼저 선택하세요.");
                return;
            }

            Book selectedBook = bookList.get(selectedRow);

            int select = JOptionPane.showConfirmDialog(
                    addBtn,
                    "선택한 도서\n[" + selectedBook.getBookId() + "] "
                            + selectedBook.getName() + " (" + selectedBook.getUnitPrice() + "원)\n\n"
                            + "을(를) 장바구니에 추가하시겠습니까?",
                    "확인",
                    JOptionPane.YES_NO_OPTION
            );

            if (select == JOptionPane.YES_OPTION) {
                if (!mCart.isCartInBook(selectedBook.getBookId())) {
                    mCart.insertBook(selectedBook);
                    JOptionPane.showMessageDialog(addBtn, "장바구니에 추가되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(addBtn, "이미 장바구니에 있는 도서입니다.");
                }
            }
        });
    }

    private void setBookImage(String bookId) {
        String path;

        if (bookId == null) {
            path = "./images/default.jpg";
        } else {
            path = "./images/" + bookId + ".jpg";
        }

        ImageIcon originalIcon = new ImageIcon(path);

        if (originalIcon.getIconWidth() <= 0) {
            originalIcon = new ImageIcon("./images/default.jpg");
        }

        int ow = originalIcon.getIconWidth();
        int oh = originalIcon.getIconHeight();

        int maxW = 240;
        int maxH = 300;

        double scale = Math.min((double) maxW / ow, (double) maxH / oh);

        int nw = (int) (ow * scale);
        int nh = (int) (oh * scale);

        Image scaledImg = originalIcon.getImage().getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        imageLabel.setIcon(scaledIcon);
    }
}
