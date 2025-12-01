package main.java.app.util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UIStyle {
    // --- PALET WARNA ---
    public static final Color COLOR_PRIMARY = new Color(139, 26, 26);   // Merah Marun
    public static final Color COLOR_ACCENT = new Color(184, 134, 11);   // Emas Tua
    
    // PASTIKAN BARIS INI ADA:
    public static final Color COLOR_BG = new Color(12, 28, 56);         // Biru Gelap
    
    public static final Color COLOR_SIDEBAR = new Color(45, 40, 37);    // Cokelat Gelap
    public static final Color COLOR_CARD_BG = new Color(250, 240, 230); // Krem/Linen
    public static final Color COLOR_TEXT_LIGHT = new Color(255, 248, 220); 
    public static final Color COLOR_TEXT_DARK = new Color(45, 40, 37);     

    // Font
    public static final Font FONT_TITLE = new Font("Serif", Font.BOLD, 36);
    public static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_GENERAL = new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONT_KING = new Font("Serif", Font.BOLD, 36);
    public static final Font FONT_CARD_TITLE = new Font("Serif", Font.BOLD, 22);
    public static final Color COLOR_GOLD = new Color(218, 165, 32);

    // --- 1. TOMBOL KAYU (UNTUK HEADER/MENU) ---
    public static JButton createWoodenButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Load tekstur kayu
                ImageIcon wood = AssetLoader.loadImage("wood-texture.png");
                if (wood != null) {
                    g2.drawImage(wood.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    // Fallback warna cokelat
                    GradientPaint gp = new GradientPaint(0, 0, new Color(139, 69, 19), 0, getHeight(), new Color(101, 67, 33));
                    g2.setPaint(gp);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                
                // Border Emas
                g2.setColor(COLOR_GOLD);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(3, 3, getWidth()-6, getHeight()-6);
                
                // Shadow Text
                g2.setColor(Color.BLACK);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x+1, y+1);
                
                super.paintComponent(g);
            }
        };
        
        btn.setFont(new Font("Serif", Font.BOLD, 18));
        btn.setForeground(new Color(255, 248, 220)); 
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 50));
        return btn;
    }

    // --- 2. PANEL BACKGROUND ---
    public static JPanel createBackgroundPanel() {
        return new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = AssetLoader.loadImage("bg-main.jpg");
                if (bg != null) {
                    g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(COLOR_BG); // Gunakan COLOR_BG disini
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
    }
    
    // --- 3. HEADER HELPER ---
    public static JPanel createHeader(String titleStr, JButton btnBack) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JPanel redBar = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(139, 0, 0, 200));
                g.fillRect(0,0,getWidth(),getHeight());
            }
        };
        redBar.setOpaque(false);
        
        JLabel title = new JLabel(titleStr, SwingConstants.CENTER);
        title.setFont(FONT_KING);
        title.setForeground(COLOR_GOLD);
        
        JPanel leftBox = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftBox.setOpaque(false);
        if(btnBack != null) leftBox.add(btnBack);
        
        redBar.add(leftBox, BorderLayout.WEST);
        redBar.add(title, BorderLayout.CENTER);
        redBar.add(Box.createHorizontalStrut(100), BorderLayout.EAST); 
        
        header.add(redBar, BorderLayout.CENTER);
        return header;
    }

    // --- 4. METHOD LAMA (UNTUK KOMPATIBILITAS) ---
    public static void applyClassicButton(JButton btn) {
        btn.setFont(new Font("Serif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COLOR_ACCENT);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 70, 0), 2),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void applyModernButton(JButton btn) {
        applyClassicButton(btn); // Redirect ke style klasik agar seragam
    }

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
        lbl.setForeground(COLOR_ACCENT);
        lbl.setBorder(new EmptyBorder(20, 0, 20, 0));
        return lbl;
    }
}