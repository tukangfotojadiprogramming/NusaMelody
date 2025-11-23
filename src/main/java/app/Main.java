package main.java.app;

import main.java.app.controller.*;
import main.java.app.view.*;
import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } 
            catch (Exception e) {}

            JFrame frame = new JFrame("NusaMelody - Edukasi Lagu Nusantara");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            
            // --- LAYOUT UTAMA (BorderLayout) ---
            // Kiri: Sidebar Menu, Tengah: Konten Berubah-ubah
            JPanel rootPanel = new JPanel(new BorderLayout());
            
            // 1. SETUP CARD LAYOUT (KONTEN TENGAH)
            CardLayout cardLayout = new CardLayout();
            JPanel contentPanel = new JPanel(cardLayout);
            
            LandingPage landingPage = new LandingPage();
            ProvinceSelectionPage provincePage = new ProvinceSelectionPage(); // Ganti flow: Landing -> Province -> Catalog
            CatalogPage catalogPage = new CatalogPage();
            SongDetailPage songDetailPage = new SongDetailPage();
            QuizPage quizPage = new QuizPage();

            contentPanel.add(landingPage, "LANDING");
            contentPanel.add(provincePage, "PROVINCE");
            contentPanel.add(catalogPage, "CATALOG");
            contentPanel.add(songDetailPage, "DETAIL");
            contentPanel.add(quizPage, "QUIZ");

            // 2. SETUP CONTROLLERS
            SongController songCtrl = new SongController(songDetailPage, contentPanel, cardLayout);
            CatalogController catCtrl = new CatalogController(catalogPage, songCtrl);
            new LandingController(landingPage, contentPanel, cardLayout);
            
            // Controller Province (Logic Sederhana: Klik provinsi -> Buka Katalog)
            provincePage.setSelectionListener(e -> {
                 JButton btn = (JButton) e.getSource();
                 System.out.println("Filter Provinsi: " + btn.getClientProperty("provinceName"));
                 // Di real app, ini akan memfilter DB. Sekarang kita tampilkan semua dulu.
                 cardLayout.show(contentPanel, "CATALOG");
            });

            // 3. SETUP SIDEBAR (NAVIGASI SAMPING)
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(UIStyle.COLOR_TEXT);
            sidebar.setPreferredSize(new Dimension(200, 0));
            sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            // Judul Sidebar
            JLabel brand = new JLabel("NusaMelody");
            brand.setForeground(Color.WHITE);
            brand.setFont(new Font("Serif", Font.BOLD, 24));
            brand.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(brand);
            sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

            // Tombol Navigasi
            addSidebarBtn(sidebar, "🏠 Beranda", () -> cardLayout.show(contentPanel, "LANDING"));
            addSidebarBtn(sidebar, "🗺️ Jelajah", () -> cardLayout.show(contentPanel, "PROVINCE"));
            addSidebarBtn(sidebar, "🎶 Katalog", () -> cardLayout.show(contentPanel, "CATALOG"));
            addSidebarBtn(sidebar, "❓ Kuis", () -> cardLayout.show(contentPanel, "QUIZ"));

            rootPanel.add(sidebar, BorderLayout.WEST);
            rootPanel.add(contentPanel, BorderLayout.CENTER);

            frame.add(rootPanel);
            frame.setVisible(true);
        });
    }

    private static void addSidebarBtn(JPanel panel, String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setBackground(UIStyle.COLOR_TEXT);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> action.run());
        
        // Hover Effect Sidebar
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIStyle.COLOR_PRIMARY);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(UIStyle.COLOR_TEXT);
            }
        });

        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}