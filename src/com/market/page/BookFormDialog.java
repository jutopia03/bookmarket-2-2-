package com.market.page;

import javax.swing.*;
import java.awt.*;
import com.market.bookitem.Book;
import com.market.bookitem.BookDAO;

public class BookFormDialog extends JDialog {

    private JTextField tfId, tfName, tfPrice, tfAuthor, tfDesc, tfCategory, tfDate, tfStock;
    private BookDAO dao = new BookDAO();
    private Book origin;

    public BookFormDialog(JFrame owner, Book origin) {
        super(owner, true);
        this.origin = origin;

        setTitle(origin == null ? "도서 등록" : "도서 수정");
        setSize(480, 550); 
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        tfId = new JTextField(20);
        tfName = new JTextField(20);
        tfPrice = new JTextField(20);
        tfAuthor = new JTextField(20);
        tfDesc = new JTextField(20);
        tfCategory = new JTextField(20);
        tfDate = new JTextField(20);
        tfStock = new JTextField(20);

        addField(formPanel, c, 0, "도서ID", tfId);
        addField(formPanel, c, 1, "도서명", tfName);
        addField(formPanel, c, 2, "가격", tfPrice);
        addField(formPanel, c, 3, "저자", tfAuthor);
        addField(formPanel, c, 4, "설명", tfDesc);
        addField(formPanel, c, 5, "분야", tfCategory);
        addField(formPanel, c, 6, "출판일", tfDate);
        addField(formPanel, c, 7, "재고", tfStock);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 20, 20));

        JButton btnSave = new JButton("저장");
        JButton btnCancel = new JButton("취소");

        btnSave.setPreferredSize(new Dimension(0, 40));
        btnCancel.setPreferredSize(new Dimension(0, 40));

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        add(btnPanel, BorderLayout.SOUTH);

        if (origin != null) {
            tfId.setText(origin.getBookId());
            tfName.setText(origin.getName());
            tfPrice.setText(String.valueOf(origin.getUnitPrice()));
            tfAuthor.setText(origin.getAuthor());
            tfDesc.setText(origin.getDescription());
            tfCategory.setText(origin.getCategory());
            tfDate.setText(origin.getReleaseDate());
            tfStock.setText(String.valueOf(origin.getStock()));
        }


        btnCancel.addActionListener(e -> dispose());

        btnSave.addActionListener(e -> {
            try {
                Book b = new Book(
                        tfId.getText(),
                        tfName.getText(),
                        Integer.parseInt(tfPrice.getText()),
                        tfAuthor.getText(),
                        tfDesc.getText(),
                        tfCategory.getText(),
                        tfDate.getText(),
                        Integer.parseInt(tfStock.getText())
                );

                int result = (origin == null)
                        ? dao.insertBook(b)
                        : dao.updateBook(b);

                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "저장되었습니다.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "저장 실패");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "입력값을 확인하세요.");
            }
        });
    }

    private void addField(JPanel p, GridBagConstraints c, int y, String label, JTextField field) {
        c.gridy = y;

        c.gridx = 0;
        c.weightx = 0.3;
        p.add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 0.7;
        p.add(field, c);
    }
}
