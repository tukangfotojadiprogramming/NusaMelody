package main.java.app.view;

import main.java.app.model.RegionalSong;
import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SongDetailPage extends JPanel {
    private JLabel lblTitle, lblProvince, lblTime;
    private JTextArea txtContent;
    private JButton btnPlay, btnStop, btnBack;
    private JProgressBar progressBar;

    public SongDetailPage() {
        setLayout(new BorderLayout());
        
        // 1. Background Utama (Batik)
        JPanel mainPanel = UIStyle.createBackgroundPanel();
        
        // 2. HEADER INFO
        JPanel topContainer = new JPanel(new GridLayout(2, 1));
        topContainer.setOpaque(false);
        topContainer.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        lblTitle = new JLabel("Judul Lagu", SwingConstants.CENTER);
        lblTitle.setFont(UIStyle.FONT_KING); // Font Besar Serif
        lblTitle.setForeground(UIStyle.COLOR_GOLD); // Warna Emas
        
        lblProvince = new JLabel("Asal Provinsi", SwingConstants.CENTER);
        lblProvince.setFont(new Font("SansSerif", Font.ITALIC, 18));
        lblProvince.setForeground(Color.WHITE); // Warna Putih
        
        topContainer.add(lblTitle);
        topContainer.add(lblProvince);
        
        mainPanel.add(topContainer, BorderLayout.NORTH);

        // 3. KONTEN LIRIK (Di atas Kertas)
        JPanel paperPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon paper = AssetLoader.loadImage("paper-texture.png");
                if (paper != null) {
                    g.drawImage(paper.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(UIStyle.COLOR_CARD_BG);
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                // Border Emas Tipis
                g.setColor(UIStyle.COLOR_GOLD);
                ((Graphics2D)g).setStroke(new BasicStroke(3));
                g.drawRect(2, 2, getWidth()-4, getHeight()-4);
            }
        };
        paperPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        txtContent = new JTextArea();
        txtContent.setEditable(false);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setFont(new Font("Georgia", Font.PLAIN, 18)); // Font yang enak dibaca
        txtContent.setForeground(new Color(50, 30, 10)); // Cokelat tua
        txtContent.setOpaque(false); // Transparan agar tekstur kertas terlihat
        
        JScrollPane scroll = new JScrollPane(txtContent);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        
        paperPanel.add(scroll, BorderLayout.CENTER);

        // Wrapper untuk margin lirik
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(0, 100, 20, 100)); // Margin kiri kanan
        contentWrapper.add(paperPanel, BorderLayout.CENTER);
        
        mainPanel.add(contentWrapper, BorderLayout.CENTER);

        // 4. PLAYER PANEL (Bawah)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(10, 50, 30, 50));

        // Progress Bar
        JPanel progressWrapper = new JPanel(new BorderLayout(10, 0));
        progressWrapper.setOpaque(false);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(UIStyle.COLOR_GOLD);
        progressBar.setBackground(new Color(50, 30, 30)); // Latar gelap bar
        
        lblTime = new JLabel("00:00");
        lblTime.setForeground(UIStyle.COLOR_GOLD);
        lblTime.setFont(new Font("Monospaced", Font.BOLD, 14));
        
        progressWrapper.add(progressBar, BorderLayout.CENTER);
        progressWrapper.add(lblTime, BorderLayout.EAST);

        // Controls (Tombol Kayu)
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setOpaque(false);
        
        btnBack = UIStyle.createWoodenButton("<< Kembali");
        btnBack.setPreferredSize(new Dimension(140, 45));
        
        btnPlay = UIStyle.createWoodenButton("▶ Putar");
        btnPlay.setPreferredSize(new Dimension(140, 45));
        
        btnStop = UIStyle.createWoodenButton("⏹ Stop");
        btnStop.setPreferredSize(new Dimension(140, 45));
        
        controls.add(btnBack);
        controls.add(btnPlay);
        controls.add(btnStop);

        bottomPanel.add(progressWrapper, BorderLayout.NORTH);
        bottomPanel.add(controls, BorderLayout.CENTER);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void displayData(RegionalSong song) {
        lblTitle.setText(song.getTitle());
        lblProvince.setText(song.getProvince());
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== LIRIK LAGU ===\n\n");
        sb.append(song.getLyrics()).append("\n\n");
        sb.append("=== TERJEMAHAN ===\n\n");
        sb.append(song.getTranslation()).append("\n\n");
        sb.append("=== NILAI BUDAYA ===\n\n");
        sb.append(song.getCulturalValues());
        
        txtContent.setText(sb.toString());
        txtContent.setCaretPosition(0);
        progressBar.setValue(0);
        lblTime.setText("00:00");
    }

    public void updateProgress(int percent, String timeStr) {
        progressBar.setValue(percent);
        lblTime.setText(timeStr);
    }

    public JButton getBtnPlay() { return btnPlay; }
    public JButton getBtnStop() { return btnStop; }
    public JButton getBtnBack() { return btnBack; }
}