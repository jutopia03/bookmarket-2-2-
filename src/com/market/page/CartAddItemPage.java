package com.market.page;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
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
        setBackground(new Color(245, 245, 245));
        setBorder(new EmptyBorder(30, 40, 40, 40));

        Font baseFont = new Font("함초롬돋움", Font.PLAIN, 14);

        // 상단 타이틀 영역
        JLabel titleLabel = new JLabel("도서 목록");
        titleLabel.setFont(new Font("함초롬돋움", Font.BOLD, 24));

        JLabel subLabel = new JLabel("원하는 도서를 선택하고 장바구니에 담아보세요.");
        subLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
        subLabel.setForeground(new Color(120, 120, 120));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(getBackground());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalStrut(5));
        titlePanel.add(subLabel);
        titlePanel.add(Box.createVerticalStrut(10));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(220, 220, 220));
        titlePanel.add(sep);

        add(titlePanel, BorderLayout.NORTH);

        // 도서 목록 로딩
        bookList = BookInIt.getmBookList();
        if (bookList == null) {
            bookList = new ArrayList<>();
        }

        // 가운데: 이미지 카드 + 테이블 카드
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(getBackground());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 20);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        // 왼쪽: 이미지 카드
        JPanel imageCard = new JPanel(new BorderLayout());
        imageCard.setBackground(Color.WHITE);
        imageCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(20, 20, 20, 20)
        ));
        imageCard.setPreferredSize(new Dimension(260, 340));

        JLabel imageTitle = new JLabel("도서 미리보기");
        imageTitle.setFont(new Font("함초롬돋움", Font.BOLD, 14));
        imageCard.add(imageTitle, BorderLayout.NORTH);

        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(220, 280));
        imageCard.add(imageLabel, BorderLayout.CENTER);

        setBookImage(null);

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(imageCard, gbc);

        // 오른쪽: 테이블 카드
        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(new CompoundBorder(
                new LineBorder(new Color(225, 225, 225)),
                new EmptyBorder(15, 15, 15, 15)
        ));
        tableCard.setPreferredSize(new Dimension(500, 340));

        JLabel tableTitle = new JLabel("도서 목록");
        tableTitle.setFont(new Font("함초롬돋움", Font.BOLD, 14));
        tableCard.add(tableTitle, BorderLayout.NORTH);

        String[] colNames = { "ID", "제목", "가격" };
        Object[][] rowData = new Object[bookList.size()][3];

        for (int i = 0; i < bookList.size(); i++) {
            Book b = bookList.get(i);
            rowData[i][0] = b.getBookId();
            rowData[i][1] = b.getName();
            rowData[i][2] = b.getUnitPrice();
        }

        table = new JTable(rowData, colNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFont(baseFont);
        table.setRowHeight(26);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235, 235, 235));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(255, 245, 230));
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("함초롬돋움", Font.BOLD, 13));
        header.setBackground(new Color(248, 248, 248));
        header.setForeground(new Color(70, 70, 70));
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        BookTableCellRenderer defaultRenderer = new BookTableCellRenderer();
        table.setDefaultRenderer(Object.class, defaultRenderer);

        BookTableCellRenderer priceRenderer = new BookTableCellRenderer();
        priceRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        table.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(260);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(new EmptyBorder(5, 0, 0, 0));

        tableCard.add(scrollPane, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(tableCard, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // 하단: 선택된 도서 정보 + 버튼
        selectedInfoLabel = new JLabel("선택된 도서가 없습니다.");
        selectedInfoLabel.setFont(baseFont);
        selectedInfoLabel.setForeground(new Color(80, 80, 80));
        selectedInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        infoPanel.setOpaque(false);
        infoPanel.add(selectedInfoLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton addBtn = new JButton("장바구니에 담기");
        addBtn.setFont(new Font("함초롬돋움", Font.BOLD, 14));
        addBtn.setForeground(Color.WHITE);
        addBtn.setBackground(new Color(255, 107, 0));
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setPreferredSize(new Dimension(200, 38));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        buttonPanel.add(addBtn);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        bottomPanel.add(infoPanel);
        bottomPanel.add(Box.createVerticalStrut(10));
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // 이벤트
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

        int maxW = 220;
        int maxH = 280;

        double scale = Math.min((double) maxW / ow, (double) maxH / oh);

        int nw = (int) (ow * scale);
        int nh = (int) (oh * scale);

        Image scaledImg = originalIcon.getImage()
                .getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        imageLabel.setIcon(scaledIcon);
    }

    private static class BookTableCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);

            setBorder(new EmptyBorder(0, 8, 0, 8));

            if (isSelected) {
                c.setBackground(new Color(255, 245, 230));
            } else {
                if (row % 2 == 0)
                    c.setBackground(Color.WHITE);
                else
                    c.setBackground(new Color(250, 250, 250));
            }

            return c;
        }
    }
}
