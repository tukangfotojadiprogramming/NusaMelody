package main.java.app.util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UIStyle {
    // Palet Warna Nusantara
    public static final Color COLOR_PRIMARY = Color.decode("#8B4513"); // Saddle Brown (Kayu)
    public static final Color COLOR_ACCENT = Color.decode("#D2691E");  // Chocolate
    public static final Color COLOR_BG = Color.decode("#F5F5DC");      // Beige (Krem)
    public static final Color COLOR_TEXT = Color.decode("#3E2723");    // Dark Brown

    public static void applyModernButton(JButton btn) {
        btn.setBackground(COLOR_PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover Effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_ACCENT);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(COLOR_PRIMARY);
            }
        });
    }

    public static JLabel createHeader(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(new Font("Serif", Font.BOLD, 28));
        lbl.setForeground(COLOR_PRIMARY);
        lbl.setBorder(new EmptyBorder(20, 0, 20, 0));
        return lbl;
    }
}