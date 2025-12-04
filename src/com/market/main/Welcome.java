package com.market.main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.market.page.UserLoginDialog;

public class Welcome {

    // 프로그램 시작점
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            JFrame owner = new JFrame();
            owner.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            owner.setUndecorated(true);       
            owner.setLocationRelativeTo(null);
            owner.setVisible(false);          

            new UserLoginDialog(owner); 
        });
    }
}
