package main.java.app.view;

import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProvinceSelectionPage extends JPanel {
    private JPanel gridPanel;
    private JButton btnBack;
    
    // DAFTAR TOMBOL (SOLUSI AGAR KLIK SELALU BERFUNGSI)
    private List<JButton> actionButtons = new ArrayList<>();

    public ProvinceSelectionPage() {
        setLayout(new BorderLayout());
        JPanel mainPanel = UIStyle.createBackgroundPanel();
        
        // Header
        btnBack = UIStyle.createWoodenButton("Kembali");
        btnBack.setPreferredSize(new Dimension(120, 40));
        mainPanel.add(UIStyle.createHeader("Pilih Daerah Nusantara", btnBack), BorderLayout.NORTH);

        // Grid
        gridPanel = new JPanel(new GridLayout(0, 3, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));

        // Load Data
        addCard("Bengkulu", "Terkenal dengan Bunga Rafflesia.");
        addCard("Jawa Tengah", "Dikenal dengan Candi Borobudur.");
        addCard("Jawa Barat", "Rumah bagi Angklung.");
        addCard("DKI Jakarta", "Ikon budaya Betawi & Ondel-Ondel.");
        addCard("Kalimantan Selatan", "Identik dengan Pasar Terapung.");
        addCard("Maluku", "Dijuluki Kepulauan Rempah.");
        addCard("Papua", "Dikenal dengan Cenderawasih.");
        addCard("Sumatra Barat", "Terkenal dengan Jam Gadang.");
        addCard("Nusa Tenggara Barat", "Dikenal dengan Pulau Komodo.");

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        
        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void addCard(String name, String desc) {
        // Panel Kartu
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Coba load texture kertas
                ImageIcon paper = AssetLoader.loadImage("paper-texture.png");
                if(paper != null) g.drawImage(paper.getImage(), 0, 0, getWidth(), getHeight(), null);
                else { g.setColor(new Color(245, 245, 220)); g.fillRect(0,0,getWidth(),getHeight()); }
                
                // Border
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(4));
                g2.setColor(new Color(139, 69, 19));
                g2.drawRect(0,0,getWidth(),getHeight());
            }
        };
        card.setPreferredSize(new Dimension(300, 200));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel title = new JLabel(name, SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        title.setForeground(new Color(101, 67, 33));

        JTextArea description = new JTextArea(desc);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setOpaque(false);
        description.setFont(new Font("SansSerif", Font.PLAIN, 14));
        description.setForeground(Color.BLACK);

        JButton btn = UIStyle.createWoodenButton("Lihat Lagu");
        btn.putClientProperty("provinceName", name);
        
        // PENTING: Masukkan tombol ke daftar agar listener bisa dipasang dengan benar
        actionButtons.add(btn);

        card.add(title, BorderLayout.NORTH);
        card.add(description, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);

        gridPanel.add(card);
    }

    // METHOD INI YANG DIPERBAIKI
    public void setSelectionListener(ActionListener listener) {
        // Kita tidak lagi menebak posisi tombol, tapi langsung mengakses daftar tombol
        for (JButton btn : actionButtons) {
            // Hapus listener lama biar tidak dobel
            for (ActionListener al : btn.getActionListeners()) btn.removeActionListener(al);
            // Pasang listener baru
            btn.addActionListener(listener);
        }
    }
    
    public JButton getBtnBack() { return btnBack; }
}