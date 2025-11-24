package main.java.app.util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UIStyle {
    // Palet Warna Nusantara (Earth Tones)
    public static final Color COLOR_PRIMARY = new Color(156, 41, 21);   // Merah Bata Gelap
    public static final Color COLOR_ACCENT = new Color(210, 105, 30);   // Cokelat Emas
    public static final Color COLOR_BG = new Color(244, 238, 234);      // Krem Lembut
    public static final Color COLOR_SIDEBAR = new Color(45, 40, 37);    // Cokelat Hampir Hitam
    public static final Color COLOR_TEXT_LIGHT = Color.WHITE;
    public static final Color COLOR_TEXT_DARK = new Color(45, 40, 37);

    public static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 28);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 16);
    public static final Font FONT_GENERAL = new Font("SansSerif", Font.PLAIN, 14);

    // Method untuk membuat Tombol Cantik
    public static void applyModernButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_PRIMARY);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Hover Effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_ACCENT);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
            }
        });
    }

    // Method untuk Tombol Sidebar
    public static void applySidebarButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(COLOR_SIDEBAR);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(COLOR_PRIMARY);
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(COLOR_SIDEBAR);
                btn.setForeground(new Color(200, 200, 200));
            }
        });
    }

    public static JLabel createHeader(String text) {
        JLabel lbl = new JLabel(text, SwingConstants.CENTER);
        lbl.setFont(FONT_TITLE);
        lbl.setForeground(COLOR_PRIMARY);
        lbl.setBorder(new EmptyBorder(20, 0, 20, 0));
        return lbl;
    }
}