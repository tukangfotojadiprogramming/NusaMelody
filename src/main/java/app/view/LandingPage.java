package main.java.app.view;

import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LandingPage extends JPanel {
    private Map<String, Runnable> navActions = new HashMap<>();

    public LandingPage() {
        setLayout(new BorderLayout());
        
        // Panel Konten Utama (Vertical Stack)
        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);

        // 1. HERO SECTION
        JPanel heroPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = AssetLoader.loadImage("landing-hero.jpg");
                if (icon != null) {
                    g.drawImage(icon.getImage(), 0, 0, getWidth(), getHeight(), this);
                    g.setColor(new Color(0, 0, 0, 120)); // Overlay Gelap
                    g.fillRect(0, 0, getWidth(), getHeight());
                } else {
                    g.setColor(UIStyle.COLOR_PRIMARY);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        heroPanel.setPreferredSize(new Dimension(1200, 400));
        heroPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        heroPanel.setLayout(new BoxLayout(heroPanel, BoxLayout.Y_AXIS));
        heroPanel.setBorder(new EmptyBorder(100, 0, 0, 0));

        JLabel title = new JLabel("NusaMelody");
        title.setFont(new Font("Serif", Font.BOLD, 72));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Jelajahi Kekayaan Musik Nusantara dalam Satu Genggaman");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 24));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        heroPanel.add(title);
        heroPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        heroPanel.add(subtitle);

        // 2. FEATURES SECTION (GRID)
        JPanel featuresPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 40));
        featuresPanel.setBackground(Color.WHITE);
        
        featuresPanel.add(createFeatureCard("Jelajah Peta", "Temukan lagu berdasarkan provinsi asal.", "PROVINCE"));
        featuresPanel.add(createFeatureCard("Katalog Lengkap", "Daftar seluruh lagu daerah A-Z.", "CATALOG"));
        featuresPanel.add(createFeatureCard("Kuis Interaktif", "Uji pengetahuan musikmu.", "QUIZ"));

        // 3. FOOTER
        JPanel footer = new JPanel();
        footer.setBackground(UIStyle.COLOR_SIDEBAR);
        footer.setPreferredSize(new Dimension(1200, 80));
        JLabel copy = new JLabel("© 2024 NusaMelody Team - PBO Project");
        copy.setForeground(Color.LIGHT_GRAY);
        footer.add(copy);

        mainContent.add(heroPanel);
        mainContent.add(featuresPanel);
        mainContent.add(Box.createVerticalGlue());
        mainContent.add(footer);

        // Bungkus dengan ScrollPane
        JScrollPane scroll = new JScrollPane(mainContent);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private JPanel createFeatureCard(String title, String desc, String actionKey) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(250, 200));
        card.setBackground(UIStyle.COLOR_BG);
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(UIStyle.COLOR_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + desc + "</center></html>");
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btn = new JButton("Buka");
        UIStyle.applyModernButton(btn);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            if (navActions.containsKey(actionKey)) navActions.get(actionKey).run();
        });

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblDesc);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(btn);

        return card;
    }

    public void setNavAction(String key, Runnable action) {
        navActions.put(key, action);
    }
}