package com.market.page;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.market.member.Member;
import com.market.member.MemberDAO;

public class MemberListPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    private MemberDAO memberDAO = new MemberDAO();
    private List<Member> allMembers = new ArrayList<>();

    private JComboBox<String> cbField;   
    private JTextField tfKeyword;       
    private JComboBox<String> cbSort;   

    public MemberListPanel() {

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setOpaque(true);

        // ===== 상단 제목 =====
        JLabel title = new JLabel("회원 관리", SwingConstants.CENTER);
        title.setFont(new Font("함초롬돋움", Font.BOLD, 22));

        // ===== 검색/정렬 영역 =====
        cbField   = new JComboBox<>(new String[]{"전체", "아이디", "이름", "역할"});
        tfKeyword = new JTextField(15);
        JButton btnSearch = new JButton("검색");
        JButton btnReset  = new JButton("초기화");
        cbSort = new JComboBox<>(new String[]{
                "정렬 없음",
                "아이디 오름차순", "아이디 내림차순",
                "이름 오름차순",   "이름 내림차순"
        });

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);

        // 왼쪽: 정렬
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        sortPanel.setBackground(Color.WHITE);
        sortPanel.add(new JLabel("정렬:"));
        sortPanel.add(cbSort);
        searchPanel.add(sortPanel, BorderLayout.WEST);

        // 가운데: 검색
        JPanel midPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        midPanel.setBackground(Color.WHITE);
        midPanel.add(new JLabel("검색:"));
        midPanel.add(cbField);
        midPanel.add(tfKeyword);
        midPanel.add(btnSearch);
        searchPanel.add(midPanel, BorderLayout.CENTER);

        // 오른쪽: 초기화
        JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 5));
        resetPanel.setBackground(Color.WHITE);
        resetPanel.add(btnReset);
        searchPanel.add(resetPanel, BorderLayout.EAST);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(Color.WHITE);
        northPanel.add(title, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // ===== 중앙: 회원 목록 테이블 =====
        String[] columns = { "아이디", "이름", "전화번호", "주소", "역할" };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   
            }
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

        // ===== 하단: 버튼 (상세, 등록, 수정, 삭제, 새로고침) =====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton btnDetail = new JButton("회원 상세");
        JButton btnAdd    = new JButton("회원 등록");
        JButton btnEdit   = new JButton("회원 수정");
        JButton btnDelete = new JButton("회원 삭제");
        JButton btnReload = new JButton("새로고침");

        btnPanel.add(btnDetail);
        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReload);

        add(btnPanel, BorderLayout.SOUTH);

        // ===== 버튼 이벤트 =====
        // 검색
        btnSearch.addActionListener(e -> applyFilterAndSort());
        tfKeyword.addActionListener(e -> applyFilterAndSort());

        // 정렬 변경 시 즉시 적용
        cbSort.addActionListener(e -> applyFilterAndSort());

        // 초기화 → 검색조건/정렬 초기화 + DB 다시 로드
        btnReset.addActionListener(e -> {
            cbField.setSelectedIndex(0);
            tfKeyword.setText("");
            cbSort.setSelectedIndex(0);
            loadMembers();
        });

        // 새로고침 (DB 다시 읽기)
        btnReload.addActionListener(e -> loadMembers());

        // 상세 보기
        btnDetail.addActionListener(e -> showDetail());

        // 등록
        btnAdd.addActionListener(e -> openMemberForm(null));

        // 수정
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "수정할 회원을 선택하세요.");
                return;
            }
            Member m = getMemberFromRow(row);
            openMemberForm(m);
        });

        // 삭제
        btnDelete.addActionListener(e -> deleteSelectedMember());

        // 처음 진입 시 목록 불러오기
        loadMembers();
    }

    // ================== DB에서 회원 목록 가져오기 ==================
    private void loadMembers() {
        allMembers.clear();
        List<Member> list = memberDAO.getAllMembers();
        if (list != null) {
            allMembers.addAll(list);
        }
        applyFilterAndSort();
    }

    // 검색/정렬 적용해서 테이블 갱신
    private void applyFilterAndSort() {
        String field   = (String) cbField.getSelectedItem();
        String keyword = tfKeyword.getText().trim().toLowerCase();
        String sort    = (String) cbSort.getSelectedItem();

        List<Member> filtered = new ArrayList<>();

        // 1) 검색 필터
        for (Member m : allMembers) {
            if (keyword.isEmpty()) {
                filtered.add(m);
                continue;
            }

            String target;
            switch (field) {
                case "아이디":
                    target = m.getUsername();
                    break;
                case "이름":
                    target = m.getName();
                    break;
                case "역할":
                    target = m.getRole();
                    break;
                default:
                    target = (m.getUsername() + " " + m.getName() + " " + m.getRole());
            }

            if (target != null && target.toLowerCase().contains(keyword)) {
                filtered.add(m);
            }
        }

        // 2) 정렬
        Comparator<Member> comp = null;

        if ("아이디 오름차순".equals(sort)) {
            comp = Comparator.comparing(m -> m.getUsername().toLowerCase());
        } else if ("아이디 내림차순".equals(sort)) {
            comp = Comparator.comparing(
                    (Member m) -> m.getUsername().toLowerCase()
            ).reversed();
        } else if ("이름 오름차순".equals(sort)) {
            comp = Comparator.comparing(m -> m.getName().toLowerCase());
        } else if ("이름 내림차순".equals(sort)) {
            comp = Comparator.comparing(
                    (Member m) -> m.getName().toLowerCase()
            ).reversed();
        }

        if (comp != null) {
            filtered.sort(comp);
        }

        // 3) 테이블에 반영
        refreshTable(filtered);
    }

    private void refreshTable(List<Member> list) {
        model.setRowCount(0);
        for (Member m : list) {
            model.addRow(new Object[]{
                    m.getUsername(),
                    m.getName(),
                    m.getPhone(),
                    m.getAddress(),
                    m.getRole()
            });
        }
    }

    // ================== JTable → Member 변환 ==================
    private Member getMemberFromRow(int row) {
        String username = (String) table.getValueAt(row, 0);
        String name     = (String) table.getValueAt(row, 1);
        String phone    = (String) table.getValueAt(row, 2);
        String address  = (String) table.getValueAt(row, 3);
        String role     = (String) table.getValueAt(row, 4);

        return new Member(username, name, phone, address, role);
    }

    // ================== 회원 상세 보기 ==================
    private void showDetail() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "상세를 볼 회원을 선택하세요.");
            return;
        }

        Member m = getMemberFromRow(row);

        String msg =
                "아이디 : " + m.getUsername() + "\n" +
                "이름   : " + m.getName()     + "\n" +
                "전화   : " + m.getPhone()    + "\n" +
                "주소   : " + m.getAddress()  + "\n" +
                "역할   : " + m.getRole();

        JOptionPane.showMessageDialog(
                this,
                msg,
                "회원 상세 정보",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================== 회원 등록/수정 폼 ==================
    // origin == null  → 등록,  origin != null → 수정
    private void openMemberForm(Member origin) {
        JDialog dialog = new JDialog(
                (Frame) null,
                (origin == null ? "회원 등록" : "회원 수정"),
                true
        );

        dialog.setSize(500, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getRootPane().setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));

        JTextField tfUsername     = new JTextField();
        JPasswordField pfPassword = new JPasswordField();
        JTextField tfName         = new JTextField();
        JTextField tfPhone        = new JTextField();
        JTextField tfAddress      = new JTextField();
        JComboBox<String> cbRole  = new JComboBox<>(new String[]{"USER", "ADMIN"});

        form.add(new JLabel("아이디:"));
        form.add(tfUsername);
        form.add(new JLabel("비밀번호:"));
        form.add(pfPassword);
        form.add(new JLabel("이름:"));
        form.add(tfName);
        form.add(new JLabel("전화:"));
        form.add(tfPhone);
        form.add(new JLabel("주소:"));
        form.add(tfAddress);
        form.add(new JLabel("역할:"));
        form.add(cbRole);

        dialog.add(form, BorderLayout.CENTER);

        // 수정 모드면 기존 값 채우기
        if (origin != null) {
            tfUsername.setText(origin.getUsername());
            tfUsername.setEditable(false); // 아이디는 수정 X
            tfName.setText(origin.getName());
            tfPhone.setText(origin.getPhone());
            tfAddress.setText(origin.getAddress());
            cbRole.setSelectedItem(origin.getRole());
        }

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave   = new JButton("저장");
        JButton btnCancel = new JButton("취소");
        bottom.add(btnSave);
        bottom.add(btnCancel);
        dialog.add(bottom, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String username = tfUsername.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();
            String name     = tfName.getText().trim();
            String phone    = tfPhone.getText().trim();
            String address  = tfAddress.getText().trim();
            String role     = (String) cbRole.getSelectedItem();

            if (username.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "아이디와 이름은 필수입니다.");
                return;
            }

            int result;

            if (origin == null) {
                // 등록일 때는 비밀번호도 필수
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "등록 시에는 비밀번호를 입력해야 합니다.");
                    return;
                }
                result = memberDAO.insertMember(
                        username, password, name, phone, address, role);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "회원이 등록되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "회원 등록 실패.");
                }
            } else {
                // 수정 모드
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "비밀번호까지 함께 수정하도록 구현했습니다.\n" +
                            "새 비밀번호를 입력해 주세요.");
                    return;
                }
                result = memberDAO.updateMember(
                        username, password, name, phone, address, role);
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "회원 정보가 수정되었습니다.");
                } else {
                    JOptionPane.showMessageDialog(this, "회원 수정 실패.");
                }
            }

            if (result > 0) {
                dialog.dispose();
                loadMembers();
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }

    // ================== 회원 삭제 ==================
    private void deleteSelectedMember() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 회원을 선택하세요.");
            return;
        }

        String username = (String) table.getValueAt(row, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "아이디 [" + username + "] 회원을 삭제하시겠습니까?",
                "회원 삭제",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int result = memberDAO.deleteMember(username);
        if (result > 0) {
            JOptionPane.showMessageDialog(this, "회원이 삭제되었습니다.");
            loadMembers();
        } else {
            JOptionPane.showMessageDialog(this, "회원 삭제에 실패했습니다.");
        }
    }
}
