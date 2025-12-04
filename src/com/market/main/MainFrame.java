package com.market.main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.TitledBorder;

import com.market.page.BookListPanel;
import com.market.page.MemberListPanel;
import com.market.page.OrderListPanel;

import com.market.bookitem.Book;
import com.market.bookitem.BookDAO;
import com.market.member.MemberDAO;
import com.market.order.OrderDAO;
import com.market.order.Order;

public class MainFrame extends JFrame {

    // 위쪽 전체 영역(메뉴 + 인사), 가운데 페이지 영역
    private JPanel topPanel;
    private JPanel mMenuPanel;
    private JPanel mPagePanel;

    private JLabel greetingLabel;

    private BookDAO bookDAO     = new BookDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private OrderDAO orderDAO   = new OrderDAO();

    public MainFrame() {
        initFrame();
        initTopArea();
        initPagePanel();
        setVisible(true);
    }

    // 프레임 기본 세팅
    private void initFrame() {
        setTitle("관리자");
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());

        // 전체 배경 패널 (대시보드 흰 배경)
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);
        setContentPane(content);
    }

    // 상단 메뉴 + 인사 영역 (검은 바)
    private void initTopArea() {
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setPreferredSize(new Dimension(0, 60));
        topPanel.setBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))
        );

        // ===== 왼쪽: 메뉴 버튼 줄 =====
        mMenuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        mMenuPanel.setOpaque(false);

        Font menuFont = new Font("함초롬돋움", Font.PLAIN, 14);

        JButton btHome    = createTopMenuButton("홈", menuFont);
        JButton btBookMng = createTopMenuButton("도서관리", menuFont);
        JButton btMember  = createTopMenuButton("회원관리", menuFont);
        JButton btOrder   = createTopMenuButton("주문관리", menuFont);
        JButton btExit    = createTopMenuButton("종료", menuFont);

        JButton[] btns = { btHome, btBookMng, btMember, btOrder, btExit };
        for (JButton b : btns) {
            mMenuPanel.add(b);
        }

        topPanel.add(mMenuPanel, BorderLayout.WEST);

        // ===== 오른쪽: 인사/날짜 =====
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 18));
        rightPanel.setOpaque(false);

        greetingLabel = new JLabel(makeGreetingText());
        greetingLabel.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
        greetingLabel.setForeground(Color.WHITE);

        rightPanel.add(greetingLabel);
        topPanel.add(rightPanel, BorderLayout.EAST);

        getContentPane().add(topPanel, BorderLayout.NORTH);

        // 버튼 액션
        btHome.addActionListener(e -> showHomePage());
        btBookMng.addActionListener(e -> showBookListPage());
        btMember.addActionListener(e -> showMemberPage());
        btOrder.addActionListener(e -> showOrderPage());
        btExit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    MainFrame.this,
                    "관리자 화면을 종료하시겠습니까?",
                    "종료",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                dispose();
            }
        });
    }

    // 상단 메뉴용 플랫 버튼 생성
    private JButton createTopMenuButton(String text, Font font) {
        JButton btn = new JButton(text);
        btn.setFont(font);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setForeground(new Color(180, 200, 255));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setForeground(Color.WHITE);
            }
        });

        return btn;
    }

    // 가운데 내용 영역
    private void initPagePanel() {
        mPagePanel = new JPanel(new BorderLayout());
        mPagePanel.setOpaque(true);
        mPagePanel.setBackground(Color.WHITE);
        getContentPane().add(mPagePanel, BorderLayout.CENTER);

        showHomePage();
    }

    // 화면 전환 공통
    private void setPage(Component comp) {
        mPagePanel.removeAll();
        mPagePanel.add(comp, BorderLayout.CENTER);
        mPagePanel.revalidate();
        mPagePanel.repaint();
    }

    // 인사 문구 만들기
    private String makeGreetingText() {
        LocalDate today = LocalDate.now();
        String dateStr = today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        return "관리자님, 안녕하세요  |  오늘 날짜 : " + dateStr;
    }

    // ===== 홈(대시보드) 화면 구성 =====
    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 40, 40));

        // 상단: 제목 + 구분선
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        JLabel title = new JLabel("대시보드", SwingConstants.CENTER);
        title.setFont(new Font("함초롬돋움", Font.BOLD, 26));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        titlePanel.add(title, BorderLayout.NORTH);

        JSeparator line = new JSeparator();
        line.setForeground(Color.BLACK);
        titlePanel.add(line, BorderLayout.SOUTH);

        panel.add(titlePanel, BorderLayout.NORTH);

        // ===== 가운데: 카드 + (재고/최근주문) =====
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setOpaque(false);

        // 1) 카드 3개
        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        cardPanel.setOpaque(false);

        String bookCount   = "-";
        String memberCount = "-";
        String orderCount  = "-";

        try {
            bookCount = String.valueOf(bookDAO.getAllBooks().size());
        } catch (Exception ignored) {}

        try {
            memberCount = String.valueOf(memberDAO.getAllMembers().size());
        } catch (Exception ignored) {}

        try {
            List<Order> orders = orderDAO.getAllOrders();
            orderCount = String.valueOf(orders.size());
        } catch (Exception ignored) {}

        // 카드 클릭 시 해당 화면으로 이동
        cardPanel.add(createStatCard("총 도서 수", bookCount, "등록된 도서의 개수",
                () -> showBookListPage()));
        cardPanel.add(createStatCard("총 회원 수", memberCount, "가입한 회원의 수",
                () -> showMemberPage()));
        cardPanel.add(createStatCard("총 주문 수", orderCount, "저장된 주문의 수",
                () -> showOrderPage()));

        centerWrapper.add(cardPanel, BorderLayout.NORTH);

        // 2) 카드 아래: 재고 부족 / 최근 주문 3건
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        infoPanel.add(createLowStockPanel());    
        infoPanel.add(createRecentOrderPanel());

        centerWrapper.add(infoPanel, BorderLayout.CENTER);

        panel.add(centerWrapper, BorderLayout.CENTER);

        JLabel guide = new JLabel("", SwingConstants.CENTER);
        guide.setFont(new Font("함초롬돋움", Font.PLAIN, 13));
        guide.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        panel.add(guide, BorderLayout.SOUTH);

        return panel;
    }

    // 대시보드 카드 하나 (클릭 가능)
    private JPanel createStatCard(String title, String value, String desc, Runnable onClick) {
        Color baseColor  = Color.WHITE;
        Color hoverColor = new Color(235, 242, 255);

        JPanel card = new JPanel(new BorderLayout(0, 5));
        card.setPreferredSize(new Dimension(200, 180));
        card.setBackground(baseColor);
        card.setOpaque(true);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));

        JLabel lbTitle = new JLabel(title, SwingConstants.CENTER);
        lbTitle.setFont(new Font("함초롬돋움", Font.BOLD, 14));

        JLabel lbValue = new JLabel(value, SwingConstants.CENTER);
        lbValue.setFont(new Font("함초롬돋움", Font.BOLD, 24));

        JLabel lbDesc = new JLabel(desc, SwingConstants.CENTER);
        lbDesc.setFont(new Font("함초롬돋움", Font.PLAIN, 11));

        card.add(lbTitle, BorderLayout.NORTH);
        card.add(lbValue, BorderLayout.CENTER);
        card.add(lbDesc, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBackground(hoverColor);
                card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBackground(baseColor);
                card.setCursor(Cursor.getDefaultCursor());
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    onClick.run();
                }
            }
        });

        return card;
    }

    // 재고 2권 이하 도서 목록 패널
    private JPanel createLowStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "재고 부족 도서 (2권 이하)",
                TitledBorder.CENTER,
                TitledBorder.TOP
        ));

        String[] cols = { "도서ID", "도서명", "재고" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        List<Book> lowStock = new ArrayList<>();
        try {
            List<Book> all = bookDAO.getAllBooks();
            for (Book b : all) {
                if (b.getStock() <= 2) {        
                    lowStock.add(b);
                    if (lowStock.size() >= 5) {   
                        break;
                    }
                }
            }
        } catch (Exception ignored) {}

        for (Book b : lowStock) {
            model.addRow(new Object[] {
                    b.getBookId(),
                    b.getName(),
                    b.getStock()
            });
        }

        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);
        table.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);

        panel.add(sp, BorderLayout.CENTER);

        if (lowStock.isEmpty()) {
            JLabel empty = new JLabel("재고 부족 도서가 없습니다.", SwingConstants.CENTER);
            empty.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
            panel.add(empty, BorderLayout.SOUTH);
        }

        return panel;
    }

    // 최근 주문 3건 패널
    private JPanel createRecentOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                "최근 주문 3건",
                TitledBorder.CENTER,
                TitledBorder.TOP
        ));

        String[] cols = { "주문번호", "아이디", "총 금액", "주문일시" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            List<Order> orders = orderDAO.getAllOrders(); 
            int limit = Math.min(3, orders.size());
            for (int i = 0; i < limit; i++) {
                Order o = orders.get(i);
                model.addRow(new Object[] {
                        o.getOrderId(),
                        o.getUsername(),
                        o.getTotalPrice(),
                        o.getOrderDate()
                });
            }
        } catch (Exception ignored) {}

        JTable table = new JTable(model);
        table.setBackground(Color.WHITE);
        table.setOpaque(true);
        table.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Color.WHITE);

        panel.add(sp, BorderLayout.CENTER);

        if (model.getRowCount() == 0) {
            JLabel empty = new JLabel("저장된 주문이 없습니다.", SwingConstants.CENTER);
            empty.setFont(new Font("함초롬돋움", Font.PLAIN, 12));
            panel.add(empty, BorderLayout.SOUTH);
        }

        return panel;
    }

    // ===== 각 화면 전환 메서드 =====
    private void showHomePage() {
        greetingLabel.setText(makeGreetingText()); 
        setPage(createHomePanel());
    }

    private void showBookListPage() {
        setPage(new BookListPanel());
    }

    private void showMemberPage() {
        setPage(new MemberListPanel());
    }

    private void showOrderPage() {
        setPage(new OrderListPanel());
    }
}
