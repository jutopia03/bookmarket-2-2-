package com.market.main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import com.market.page.*;
import com.market.cart.Cart;
import com.market.bookitem.BookInIt;
import com.market.member.User;
import com.market.member.UserInIt;
import com.market.page.UserInfoPage;


public class MainWindow extends JFrame {

    // 장바구니 객체 (공용)
    static Cart mCart;

    // 메뉴 패널(위), 내용 패널(아래)
    static JPanel mMenuPanel, mPagePanel;

    public MainWindow(String title, int x, int y, int width, int height) {

        // 도서 목록 초기화 (DB → 메모리)
        BookInIt.init();

        mCart = new Cart();

        initContainer(title, x, y, width, height);
        initMenu();

        // 최초 화면: 도서 목록
        showBookListPage();

        setVisible(true);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon("./images/shop.png").getImage());
    }

    // ============================
    // 메인 프레임 기본 설정
    // ============================
    private void initContainer(String title, int x, int y, int width, int height) {

        setTitle(title);
        setBounds(x, y, width, height);
        setLayout(null);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - 1000) / 2, (screenSize.height - 750) / 2);

        // 상단 메뉴 영역
        mMenuPanel = new JPanel();
        mMenuPanel.setBounds(0, 20, width, 80);       // 높이 80 정도만 사용
        add(mMenuPanel);

        // 가운데 내용 영역
        mPagePanel = new JPanel();
        mPagePanel.setBounds(0, 100, width, height - 120);  // 나머지 영역
        add(mPagePanel);

        // 창 닫힘 시 프로그램 종료
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    // ============================
    // 상단 버튼 메뉴
    // ============================
    private void initMenu() {

        Font ft = new Font("함초롬돋움", Font.BOLD, 15);

        // 버튼 5개: 도서 목록 / 내 정보 / 장바구니 / 주문 내역 / 종료
        JButton btBookList     = new JButton("도서 목록");
        JButton btMyInfo       = new JButton("내 정보");
        JButton btCart         = new JButton("장바구니");
        JButton btOrderHistory = new JButton("주문 내역");
        JButton btExit         = new JButton("종료");

        btBookList.setFont(ft);
        btMyInfo.setFont(ft);
        btCart.setFont(ft);
        btOrderHistory.setFont(ft);
        btExit.setFont(ft);

        // 5개 버튼을 가운데 정렬
        // Center align 5 buttons horizontally
        mMenuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 15));
        mMenuPanel.add(btBookList);
        mMenuPanel.add(btMyInfo);
        mMenuPanel.add(btCart);
        mMenuPanel.add(btOrderHistory);
        mMenuPanel.add(btExit);

        // ===== 버튼 동작 =====

        // 1. 도서 목록
        btBookList.addActionListener(e -> showBookListPage());

        // 2. 내 정보
     btMyInfo.addActionListener(e -> {
         // 메인 페이지 패널 정리
         mPagePanel.removeAll();
         mPagePanel.setLayout(new BorderLayout());

         // 1) 로그인한 사용자 정보 가져오기
         User loginUser = UserInIt.getmUser();
         if (loginUser == null) {
             JOptionPane.showMessageDialog(MainWindow.this, "로그인 정보가 없습니다.");
             return;
         }

         // 2) DB에서 사용할 로그인 아이디(user_name) 가져오기
         //    ↓↓↓ 이 한 줄은 User.java의 실제 getter 이름에 맞게 바꿔야 합니다.
         //    예: getUser_name(), getUserName(), getId(), getUsername() 등
         String loginUserName = loginUser.getUsername();  

         
         // 3) UserInfoPage 생성해서 메인 패널에 올리기
         UserInfoPage userInfoPage = new UserInfoPage(mPagePanel, loginUserName);
         mPagePanel.add(userInfoPage, BorderLayout.CENTER);

         mPagePanel.revalidate();
         mPagePanel.repaint();
     });



        // 3. 장바구니
        btCart.addActionListener(e -> {
            if (mCart.mCartCount == 0) {
                JOptionPane.showMessageDialog(btCart, "장바구니에 항목이 없습니다.");
            } else {
                mPagePanel.removeAll();
                mPagePanel.setLayout(new BorderLayout());
                // 장바구니 화면 (여기서 장바구니 비우기 / 수량± / 항목삭제 / 주문하기 버튼 구현)
                mPagePanel.add(new CartItemListPage(mPagePanel, mCart), BorderLayout.CENTER);
                refreshPage();
            }
        });

        // 4. 주문 내역
        btOrderHistory.addActionListener(e -> {
            mPagePanel.removeAll();
            mPagePanel.setLayout(new BorderLayout());
            mPagePanel.add(new MyOrderListPage(mPagePanel), BorderLayout.CENTER);
            refreshPage();
        });

        // 5. 종료
        btExit.addActionListener(e -> {
            int select = JOptionPane.showConfirmDialog(btExit, "쇼핑몰을 종료하시겠습니까?");
            if (select == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    // 도서 목록 화면으로 전환
    private void showBookListPage() {
        mPagePanel.removeAll();
        mPagePanel.setLayout(new BorderLayout());
        mPagePanel.add(new CartAddItemPage(mPagePanel, mCart), BorderLayout.CENTER);
        refreshPage();
    }

    // 패널 새로고침
    private void refreshPage() {
        mPagePanel.revalidate();
        mPagePanel.repaint();
    }
}
