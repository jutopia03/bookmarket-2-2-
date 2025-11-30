package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        setLayout(new BorderLayout(10, 10));

        // ===== 제목 =====
        JLabel title = new JLabel("도서 관리", SwingConstants.CENTER);
        title.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        // ===== 검색/정렬 패널 (위쪽 한 줄 전체) =====
        cbField = new JComboBox<>(new String[]{"전체", "제목", "저자", "분야"});
        tfKeyword = new JTextField(15);
        JButton btnSearch = new JButton("검색");
        JButton btnReset = new JButton("초기화");
        cbSort = new JComboBox<>(new String[]{
                "정렬 없음", "가격 오름차순", "가격 내림차순",
                "이름 오름차순", "이름 내림차순"
        });

        JPanel searchPanel = new JPanel(new BorderLayout());

        // 왼쪽: 정렬 영역
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        sortPanel.add(new JLabel("정렬:"));
        sortPanel.add(cbSort);
        searchPanel.add(sortPanel, BorderLayout.WEST);

        // 가운데: 검색 영역
        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        midPanel.add(new JLabel("검색:"));
        midPanel.add(cbField);
        midPanel.add(tfKeyword);
        midPanel.add(btnSearch);
        searchPanel.add(midPanel, BorderLayout.CENTER);

        // 오른쪽: 초기화 버튼
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        resetPanel.add(btnReset);
        searchPanel.add(resetPanel, BorderLayout.EAST);

        // 제목 + 검색/정렬 패널 묶기
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // ===== 중앙: 도서 목록 테이블 =====
        String[] columns = {
                "도서ID", "도서명", "가격", "저자",
                "설명", "분야", "출판일", "재고"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;  // 셀 직접 수정 금지
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== 하단: 버튼들 (상세, 등록, 수정, 새로고침) =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnDetail = new JButton("도서 상세");
        JButton btnAdd = new JButton("도서 등록");
        JButton btnEdit = new JButton("도서 수정");
        JButton btnDelete = new JButton("도서 삭제");
        JButton btnReload = new JButton("새로고침");

        btnPanel.add(btnDetail);
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReload);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== 이벤트 연결 =====
        // 검색
        btnSearch.addActionListener(e -> applyFilterAndSort());
        tfKeyword.addActionListener(e -> applyFilterAndSort());

        // 초기화
        btnReset.addActionListener(e -> {
            cbField.setSelectedIndex(0);
            tfKeyword.setText("");
            cbSort.setSelectedIndex(0);
            applyFilterAndSort();
        });

        // 정렬 변경 시
        cbSort.addActionListener(e -> applyFilterAndSort());

        // 새로고침 (DB 다시 읽기)
        btnReload.addActionListener(e -> loadBooks());

        // 상세 보기
        btnDetail.addActionListener(e -> showDetail());

        // 등록
        btnAdd.addActionListener(e -> openBookForm(null));

        // 수정
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "수정할 도서를 선택하세요.");
                return;
            }
            Book selected = getBookFromRow(row);
            openBookForm(selected);
        });
        
        // 삭제
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "삭제할 도서를 선택하세요.");
                return;
            }

            String bookId = (String) table.getValueAt(row, 0);  // 0번 컬럼 = 도서ID

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "도서ID [" + bookId + "] 를 삭제하시겠습니까?",
                    "도서 삭제",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int result = bookDAO.deleteBook(bookId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
                loadBooks();   // 🔹 다시 목록 갱신
            } else {
                JOptionPane.showMessageDialog(this, "도서 삭제에 실패했습니다.");
            }
        });


        // ===== 처음 실행 시 목록 불러오기 =====
        loadBooks();
    }

    // ================== DB 연동 ==================
    private void loadBooks() {
        allBooks.clear();
        List<Book> list = bookDAO.getAllBooks();
        if (list != null) {
            allBooks.addAll(list);   // 원본 리스트 저장
        }
        applyFilterAndSort();        // 현재 검색/정렬 조건으로 테이블 갱신
    }

    // 검색 + 정렬 적용 후 테이블에 뿌리기
    private void applyFilterAndSort() {
        String field = (String) cbField.getSelectedItem();
        String keyword = tfKeyword.getText().trim().toLowerCase();
        String sort = (String) cbSort.getSelectedItem();

        List<Book> filtered = new ArrayList<>();

        // 1) 검색 필터
        for (Book b : allBooks) {
            if (keyword.isEmpty()) {
                filtered.add(b);
                continue;
            }

            String target;
            switch (field) {
                case "제목":
                    target = b.getName();
                    break;
                case "저자":
                    target = b.getAuthor();
                    break;
                case "분야":
                    target = b.getCategory();
                    break;
                default: // 전체
                    target = (b.getName() + " " + b.getAuthor() + " " + b.getCategory());
            }

            if (target != null && target.toLowerCase().contains(keyword)) {
                filtered.add(b);
            }
        }

        // 2) 정렬 적용
        Comparator<Book> comp = null;

        if ("가격 오름차순".equals(sort)) {
            comp = Comparator.comparingInt(Book::getUnitPrice);
        } else if ("가격 내림차순".equals(sort)) {
            comp = Comparator.comparingInt(Book::getUnitPrice).reversed();
        } else if ("이름 오름차순".equals(sort)) {
            comp = Comparator.comparing(b -> b.getName().toLowerCase());
        } else if ("이름 내림차순".equals(sort)) {
            comp = Comparator.comparing((Book b) -> b.getName().toLowerCase()).reversed();
        }

        if (comp != null) {
            filtered.sort(comp);
        }

        refreshTable(filtered);
    }

    private void refreshTable(List<Book> list) {
        model.setRowCount(0);  // 테이블 비우기
        for (Book b : list) {
            model.addRow(new Object[]{
                    b.getBookId(),
                    b.getName(),
                    b.getUnitPrice(),
                    b.getAuthor(),
                    b.getDescription(),
                    b.getCategory(),
                    b.getReleaseDate(),
                    b.getStock()
            });
        }
    }

    // ================== JTable ←→ Book 변환 ==================
    private Book getBookFromRow(int row) {
        String bookId = (String) table.getValueAt(row, 0);
        String name = (String) table.getValueAt(row, 1);
        int unitPrice = (Integer) table.getValueAt(row, 2);
        String author = (String) table.getValueAt(row, 3);
        String desc = (String) table.getValueAt(row, 4);
        String category = (String) table.getValueAt(row, 5);
        String release = (String) table.getValueAt(row, 6);
        int stock = (Integer) table.getValueAt(row, 7);

        return new Book(
                bookId,
                name,
                unitPrice,
                author,
                desc,
                category,
                release,
                stock
        );
    }

    // ================== 도서 상세 보기 ==================
    private void showDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "상세를 볼 도서를 선택하세요.");
            return;
        }

        Book b = getBookFromRow(row);

        String msg =
                "도서ID : " + b.getBookId() + "\n" +
                "도서명 : " + b.getName() + "\n" +
                "가격   : " + b.getUnitPrice() + "\n" +
                "저자   : " + b.getAuthor() + "\n" +
                "설명   : " + b.getDescription() + "\n" +
                "분야   : " + b.getCategory() + "\n" +
                "출판일 : " + b.getReleaseDate() + "\n" +
                "재고   : " + b.getStock();

        JOptionPane.showMessageDialog(
                this,
                msg,
                "도서 상세 정보",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================== 도서 등록/수정 폼 ==================
    private void openBookForm(Book origin) {
        JDialog dialog = new JDialog((Frame) null,
                (origin == null ? "도서 등록" : "도서 수정"), true);

        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(8, 2, 10, 10));

        JTextField tfId = new JTextField();
        JTextField tfName = new JTextField();
        JTextField tfPrice = new JTextField();
        JTextField tfAuthor = new JTextField();
        JTextField tfDesc = new JTextField();
        JTextField tfCate = new JTextField();
        JTextField tfDate = new JTextField();
        JTextField tfStock = new JTextField();

        form.add(new JLabel("도서ID:"));
        form.add(tfId);
        form.add(new JLabel("도서명:"));
        form.add(tfName);
        form.add(new JLabel("가격:"));
        form.add(tfPrice);
        form.add(new JLabel("저자:"));
        form.add(tfAuthor);
        form.add(new JLabel("설명:"));
        form.add(tfDesc);
        form.add(new JLabel("분야:"));
        form.add(tfCate);
        form.add(new JLabel("출판일:"));
        form.add(tfDate);
        form.add(new JLabel("재고:"));
        form.add(tfStock);

        dialog.add(form, BorderLayout.CENTER);

        // 수정 모드 → 기존 값 채우기
        if (origin != null) {
            tfId.setText(origin.getBookId());
            tfId.setEditable(false);
            tfName.setText(origin.getName());
            tfPrice.setText(String.valueOf(origin.getUnitPrice()));
            tfAuthor.setText(origin.getAuthor());
            tfDesc.setText(origin.getDescription());
            tfCate.setText(origin.getCategory());
            tfDate.setText(origin.getReleaseDate());
            tfStock.setText(String.valueOf(origin.getStock()));
        }

        // 버튼 영역
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("저장");
        JButton btnCancel = new JButton("취소");
        bottom.add(btnSave);
        bottom.add(btnCancel);
        dialog.add(bottom, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            try {
                String id = tfId.getText().trim();
                String name = tfName.getText().trim();
                int price = Integer.parseInt(tfPrice.getText().trim());
                String author = tfAuthor.getText().trim();
                String desc = tfDesc.getText().trim();
                String cate = tfCate.getText().trim();
                String date = tfDate.getText().trim();
                int stock = Integer.parseInt(tfStock.getText().trim());

                if (id.isEmpty() || name.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "도서ID와 도서명은 필수입니다.");
                    return;
                }

                Book b = new Book(id, name, price, author, desc, cate, date, stock);

                int result;
                if (origin == null) {
                    result = bookDAO.insertBook(b);
                    if (result > 0)
                        JOptionPane.showMessageDialog(this, "도서가 등록되었습니다.");
                    else
                        JOptionPane.showMessageDialog(this, "도서 등록 실패.");
                } else {
                    result = bookDAO.updateBook(b);
                    if (result > 0)
                        JOptionPane.showMessageDialog(this, "도서 정보가 수정되었습니다.");
                    else
                        JOptionPane.showMessageDialog(this, "도서 수정 실패.");
                }

                if (result > 0) {
                    dialog.dispose();
                    loadBooks();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력하세요.");
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
