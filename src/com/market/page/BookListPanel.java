package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import com.market.bookitem.Book;
import com.market.bookitem.BookDAO;

public class BookListPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private BookDAO bookDAO = new BookDAO();

    // 검색/정렬용 필드
    private List<Book> allBooks = new ArrayList<>();
    private JComboBox<String> cbField;
    private JTextField tfKeyword;
    private JComboBox<String> cbSort;

    public BookListPanel() {
    	
        // 패널 전체 흰색
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new BorderLayout(10, 10));

        // ===== 제목 =====
        JLabel title = new JLabel("도서 관리", SwingConstants.CENTER);
        title.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        // ===== 검색/정렬 패널 =====
        cbField = new JComboBox<>(new String[]{"전체", "제목", "저자", "분야"});
        tfKeyword = new JTextField(15);
        JButton btnSearch = new JButton("검색");
        JButton btnReset  = new JButton("초기화");
        cbSort = new JComboBox<>(new String[]{
                "정렬 없음", "가격 오름차순", "가격 내림차순",
                "이름 오름차순", "이름 내림차순"
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        // 왼쪽 정렬 영역
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        sortPanel.setBackground(Color.WHITE);
        sortPanel.add(new JLabel("정렬:"));
        sortPanel.add(cbSort);
        searchPanel.add(sortPanel, BorderLayout.WEST);

        // 가운데 검색 영역
        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        midPanel.setBackground(Color.WHITE);
        midPanel.add(new JLabel("검색:"));
        midPanel.add(cbField);
        midPanel.add(tfKeyword);
        midPanel.add(btnSearch);
        searchPanel.add(midPanel, BorderLayout.CENTER);

        // 오른쪽 초기화 영역
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        resetPanel.setBackground(Color.WHITE);
        resetPanel.add(btnReset);
        searchPanel.add(resetPanel, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);

        // ===== 테이블 =====
        String[] columns = {
                "도서ID", "도서명", "가격", "저자",
                "설명", "분야", "출판일", "재고"
        };

        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
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

        // ===== 하단 버튼들 =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton btnDetail = new JButton("도서 상세");
        JButton btnAdd    = new JButton("도서 등록");
        JButton btnEdit   = new JButton("도서 수정");
        JButton btnDelete = new JButton("도서 삭제");
        JButton btnReload = new JButton("새로고침");

        btnPanel.add(btnDetail);
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReload);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== 이벤트 =====
        btnSearch.addActionListener(e -> applyFilterAndSort());
        tfKeyword.addActionListener(e -> applyFilterAndSort());

        btnReset.addActionListener(e -> {
            cbField.setSelectedIndex(0);
            tfKeyword.setText("");
            cbSort.setSelectedIndex(0);
            applyFilterAndSort();
        });

        cbSort.addActionListener(e -> applyFilterAndSort());
        btnReload.addActionListener(e -> loadBooks());
        btnDetail.addActionListener(e -> showDetail());
        btnAdd.addActionListener(e -> openBookForm(null));

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "수정할 도서를 선택하세요."); return; }
            openBookForm(getBookFromRow(row));
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) { JOptionPane.showMessageDialog(this, "삭제할 도서를 선택하세요."); return; }

            String id = (String) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "도서ID ["+id+"] 를 삭제하시겠습니까?",
                    "도서 삭제",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                int result = bookDAO.deleteBook(id);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "삭제되었습니다.");
                    loadBooks();
                } else JOptionPane.showMessageDialog(this, "삭제 실패.");
            }
        });

        // 첫 로딩
        loadBooks();
    }

    // ================= 기타 메서드는 그대로 =================
    private void loadBooks() {
        allBooks.clear();
        List<Book> list = bookDAO.getAllBooks();
        if (list != null) allBooks.addAll(list);
        applyFilterAndSort();
    }

    private void applyFilterAndSort() {
        String field = (String) cbField.getSelectedItem();
        String keyword = tfKeyword.getText().trim().toLowerCase();
        String sort = (String) cbSort.getSelectedItem();

        List<Book> filtered = new ArrayList<>();

        for (Book b : allBooks) {
            if (keyword.isEmpty()) { filtered.add(b); continue; }
            String target;
            switch (field) {
                case "제목": target = b.getName(); break;
                case "저자": target = b.getAuthor(); break;
                case "분야": target = b.getCategory(); break;
                default: target = b.getName()+" "+b.getAuthor()+" "+b.getCategory();
            }
            if (target != null && target.toLowerCase().contains(keyword))
                filtered.add(b);
        }

        Comparator<Book> comp = null;
        if ("가격 오름차순".equals(sort)) comp = Comparator.comparingInt(Book::getUnitPrice);
        if ("가격 내림차순".equals(sort)) comp = Comparator.comparingInt(Book::getUnitPrice).reversed();
        if ("이름 오름차순".equals(sort)) comp = Comparator.comparing(b -> b.getName().toLowerCase());
        if ("이름 내림차순".equals(sort)) comp = Comparator.comparing((Book b) -> b.getName().toLowerCase()).reversed();

        if (comp != null) filtered.sort(comp);

        refreshTable(filtered);
    }

    private void refreshTable(List<Book> list) {
        model.setRowCount(0);
        for (Book b : list) {
            model.addRow(new Object[]{
                    b.getBookId(), b.getName(), b.getUnitPrice(),
                    b.getAuthor(), b.getDescription(),
                    b.getCategory(), b.getReleaseDate(), b.getStock()
            });
        }
    }

    private Book getBookFromRow(int row) {
        return new Book(
                (String) table.getValueAt(row, 0),
                (String) table.getValueAt(row, 1),
                (Integer) table.getValueAt(row, 2),
                (String) table.getValueAt(row, 3),
                (String) table.getValueAt(row, 4),
                (String) table.getValueAt(row, 5),
                (String) table.getValueAt(row, 6),
                (Integer) table.getValueAt(row, 7)
        );
    }

    private void showDetail() {
        int r = table.getSelectedRow();
        if (r == -1) { JOptionPane.showMessageDialog(this, "도서를 선택하세요."); return; }
        Book b = getBookFromRow(r);

        String msg =
                "도서ID : "+b.getBookId()+"\n"+
                "도서명 : "+b.getName()+"\n"+
                "가격   : "+b.getUnitPrice()+"\n"+
                "저자   : "+b.getAuthor()+"\n"+
                "설명   : "+b.getDescription()+"\n"+
                "분야   : "+b.getCategory()+"\n"+
                "출판일 : "+b.getReleaseDate()+"\n"+
                "재고   : "+b.getStock();

        JOptionPane.showMessageDialog(this, msg, "도서 상세", JOptionPane.INFORMATION_MESSAGE);
    }

    private void openBookForm(Book origin) {

    }
}
