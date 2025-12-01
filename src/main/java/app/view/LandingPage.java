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
        
        // Gunakan Background Panel Custom
        JPanel mainPanel = UIStyle.createBackgroundPanel();
        
        // 1. HEADER LOGO
        JLabel lblHero = new JLabel("NusaMelody", SwingConstants.CENTER);
        lblHero.setFont(new Font("Serif", Font.BOLD, 64));
        lblHero.setForeground(Color.WHITE);
        lblHero.setBorder(new EmptyBorder(50, 0, 10, 0));

        JLabel lblSub = new JLabel("Jelajahi Kekayaan Musik Nusantara dalam Satu Genggaman", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 18));
        lblSub.setForeground(new Color(255, 215, 0)); // Emas

        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setOpaque(false);
        lblHero.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        topBox.add(lblHero);
        topBox.add(lblSub);

        // 2. MENU CARDS (Grid 3 Kolom)
        JPanel cardsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        cardsPanel.setOpaque(false);
        
        cardsPanel.add(createMenuCard("Jelajah Peta", "Temukan lagu berdasarkan provinsi.", "icon-map.png", "PROVINCE"));
        cardsPanel.add(createMenuCard("Katalog Lengkap", "Daftar seluruh lagu daerah A-Z.", "icon-book.png", "CATALOG"));
        cardsPanel.add(createMenuCard("Kuis Interaktif", "Uji pengetahuan musikmu.", "icon-quiz.png", "QUIZ"));

        mainPanel.add(topBox, BorderLayout.NORTH);
        mainPanel.add(cardsPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createMenuCard(String title, String desc, String iconName, String actionKey) {
        // Kartu dengan background kertas
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                ImageIcon paper = AssetLoader.loadImage("paper-texture.png");
                if(paper != null) {
                    g.drawImage(paper.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(new Color(255, 248, 220)); // Cream fallback
                    g.fillRect(0,0,getWidth(),getHeight());
                }
                
                // Border kayu manual (Sekarang menggunakan g2, bukan g)
                g2.setColor(new Color(101, 67, 33));
                g2.setStroke(new BasicStroke(5)); // TEBAL GARIS
                g2.drawRect(0,0,getWidth(),getHeight());
            }
        };
        card.setPreferredSize(new Dimension(280, 320));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Icon
        JLabel lblIcon = new JLabel();
        ImageIcon ic = AssetLoader.loadImage(iconName);
        if(ic != null) lblIcon.setIcon(new ImageIcon(ic.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH)));
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Text
        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(139, 69, 19)); // Cokelat text
        
        JTextArea txtDesc = new JTextArea(desc);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setLineWrap(true);
        txtDesc.setOpaque(false);
        txtDesc.setEditable(false);
        txtDesc.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtDesc.setForeground(Color.DARK_GRAY);
        
        // Button Kayu
        JButton btnOpen = UIStyle.createWoodenButton("Buka");
        btnOpen.addActionListener(e -> {
            if(navActions.containsKey(actionKey)) navActions.get(actionKey).run();
        });

        JPanel center = new JPanel(new GridLayout(3, 1, 0, 5));
        center.setOpaque(false);
        center.add(lblIcon);
        center.add(lblTitle);
        center.add(txtDesc);

        card.add(center, BorderLayout.CENTER);
        card.add(btnOpen, BorderLayout.SOUTH);
        return card;
    }

    public void setNavAction(String key, Runnable action) {
        navActions.put(key, action);
    }
}