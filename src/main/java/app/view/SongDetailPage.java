package main.java.app.view;

import main.java.app.model.RegionalSong;
import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;

public class SongDetailPage extends JPanel {
    private JLabel lblTitle, lblProvince, lblTime;
    private JTextArea txtContent;
    private JButton btnPlay, btnStop, btnBack;
    private JProgressBar progressBar; // Slider Lagu

    public SongDetailPage() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BG);
        
        // HEADER
        JPanel headerInfo = new JPanel(new GridLayout(2, 1));
        headerInfo.setBackground(UIStyle.COLOR_BG);
        lblTitle = new JLabel("Judul Lagu", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 32));
        lblTitle.setForeground(UIStyle.COLOR_PRIMARY);
        
        lblProvince = new JLabel("Asal Provinsi", SwingConstants.CENTER);
        lblProvince.setFont(new Font("SansSerif", Font.ITALIC, 18));
        
        headerInfo.add(lblTitle);
        headerInfo.add(lblProvince);
        add(headerInfo, BorderLayout.NORTH);

        // CONTENT (Lirik) dengan Card Look
        txtContent = new JTextArea();
        txtContent.setEditable(false);
        txtContent.setLineWrap(true);
        txtContent.setWrapStyleWord(true);
        txtContent.setFont(new Font("Georgia", Font.PLAIN, 16));
        txtContent.setBackground(new Color(255, 250, 240)); // Warna kertas tua
        txtContent.setMargin(new Insets(20, 40, 20, 40));
        add(new JScrollPane(txtContent), BorderLayout.CENTER);

        // PLAYER PANEL
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(UIStyle.COLOR_BG);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Slider Progress
        JPanel progressPanel = new JPanel(new BorderLayout(10, 0));
        progressPanel.setOpaque(false);
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setForeground(UIStyle.COLOR_ACCENT);
        
        lblTime = new JLabel("00:00");
        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(lblTime, BorderLayout.EAST);

        // Controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controls.setOpaque(false);
        
        btnBack = new JButton("<< Kembali");
        btnPlay = new JButton("▶ Putar");
        btnStop = new JButton("⏹ Stop");
        
        UIStyle.applyModernButton(btnBack);
        UIStyle.applyModernButton(btnPlay);
        UIStyle.applyModernButton(btnStop);
        btnBack.setBackground(Color.GRAY); // Pembeda
        
        controls.add(btnBack);
        controls.add(btnPlay);
        controls.add(btnStop);

        bottomPanel.add(progressPanel, BorderLayout.NORTH);
        bottomPanel.add(controls, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void displayData(RegionalSong song) {
        lblTitle.setText(song.getTitle());
        lblProvince.setText(song.getProvince());
        
        StringBuilder sb = new StringBuilder();
        sb.append("📜 LIRIK LAGU\n").append("----------------\n").append(song.getLyrics()).append("\n\n");
        sb.append("📖 TERJEMAHAN\n").append("----------------\n").append(song.getTranslation()).append("\n\n");
        sb.append("💡 NILAI BUDAYA\n").append("----------------\n").append(song.getCulturalValues());
        
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