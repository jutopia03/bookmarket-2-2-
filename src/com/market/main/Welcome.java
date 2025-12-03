package com.market.main;

import javax.swing.SwingUtilities;

public class Welcome {

    // 프로그램 시작점
    // Program entry point (프로그램 시작 지점)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
