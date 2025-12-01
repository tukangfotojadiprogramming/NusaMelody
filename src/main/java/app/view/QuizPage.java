package main.java.app.view;

import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuizPage extends JPanel {
    private CardLayout layout;
    private JPanel mainPanel; 
    
    // Components
    private JLabel lblTimer, lblQuestionNo, lblStatus;
    private JButton[] btnOptions;
    private JButton btnReplay;
    private JButton btnStart, btnLeaderboard, btnBack; // Menu buttons
    private JLabel countdownLabel;
    private JPanel leaderboardPanelContainer; 

    public QuizPage() {
        setLayout(new BorderLayout());
        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        
        // --- 1. MENU VIEW (Wayang Style) ---
        JPanel menuView = UIStyle.createBackgroundPanel();
        menuView.setLayout(new BorderLayout());
        
        // Header
        btnBack = UIStyle.createWoodenButton("Kembali");
        btnBack.setPreferredSize(new Dimension(120, 40));
        menuView.add(UIStyle.createHeader("", btnBack), BorderLayout.NORTH);

        // Center (Judul & Tombol)
        JPanel centerMenu = new JPanel(new GridBagLayout());
        centerMenu.setOpaque(false);
        
        JLabel title = new JLabel("<html><center>Kuis Musik<br>Nusantara</center></html>", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 48));
        title.setForeground(UIStyle.COLOR_GOLD);
        
        btnStart = UIStyle.createWoodenButton("MULAI MAIN");
        btnStart.setPreferredSize(new Dimension(300, 60));
        
        btnLeaderboard = UIStyle.createWoodenButton("LEADERBOARD");
        btnLeaderboard.setPreferredSize(new Dimension(300, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0; gbc.gridy=0; gbc.insets=new Insets(0,0,30,0);
        centerMenu.add(title, gbc);
        gbc.gridy=1; gbc.insets=new Insets(0,0,15,0);
        centerMenu.add(btnStart, gbc);
        gbc.gridy=2;
        centerMenu.add(btnLeaderboard, gbc);
        
        menuView.add(centerMenu, BorderLayout.CENTER);

        // Ornamen Wayang (Kiri & Kanan)
        JLabel wayangLeft = new JLabel(AssetLoader.loadImage("wayang-left.png"));
        JLabel wayangRight = new JLabel(AssetLoader.loadImage("wayang-right.png"));
        menuView.add(wayangLeft, BorderLayout.WEST);
        menuView.add(wayangRight, BorderLayout.EAST);


        // --- 2. GAME VIEW (Gameplay) ---
        JPanel gameView = UIStyle.createBackgroundPanel();
        gameView.setLayout(new BorderLayout());
        
        // Top: Timer & Info
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(20, 30, 10, 30));
        
        lblTimer = new JLabel("Sisa Waktu: 60s");
        lblTimer.setFont(new Font("Serif", Font.BOLD, 28));
        lblTimer.setForeground(UIStyle.COLOR_GOLD);
        
        lblQuestionNo = new JLabel("Soal: 1/10");
        UIStyle.createWoodenButton("Soal 1"); // Dummy style
        lblQuestionNo.setForeground(Color.WHITE);
        lblQuestionNo.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        topBar.add(lblTimer, BorderLayout.WEST);
        topBar.add(lblQuestionNo, BorderLayout.EAST);
        gameView.add(topBar, BorderLayout.NORTH);

        // Center: Status & Replay
        JPanel centerGame = new JPanel(new GridBagLayout());
        centerGame.setOpaque(false);
        
        lblStatus = new JLabel("Mendengarkan...", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Serif", Font.ITALIC, 24));
        lblStatus.setForeground(new Color(255, 248, 220)); // Krem
        
        btnReplay = UIStyle.createWoodenButton("Putar Ulang (5s)");
        btnReplay.setPreferredSize(new Dimension(200, 45));
        btnReplay.setEnabled(false);
        
        gbc.gridy=0; centerGame.add(lblStatus, gbc);
        gbc.gridy=1; centerGame.add(btnReplay, gbc);
        gameView.add(centerGame, BorderLayout.CENTER);

        // Bottom: Pilihan Jawaban (Tombol Kayu Panjang)
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 20, 15));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(new EmptyBorder(20, 80, 50, 80));
        
        btnOptions = new JButton[4];
        for(int i=0; i<4; i++) {
            btnOptions[i] = UIStyle.createWoodenButton("Pilihan " + (i+1));
            optionsPanel.add(btnOptions[i]);
        }
        gameView.add(optionsPanel, BorderLayout.SOUTH);

        // --- 3. OTHER LAYERS ---
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Serif", Font.BOLD, 150));
        countdownLabel.setForeground(UIStyle.COLOR_GOLD);
        
        leaderboardPanelContainer = new JPanel(new BorderLayout());

        mainPanel.add(menuView, "MENU");
        mainPanel.add(gameView, "GAME");
        mainPanel.add(countdownLabel, "COUNTDOWN");
        mainPanel.add(leaderboardPanelContainer, "LEADERBOARD");

        add(mainPanel, BorderLayout.CENTER);
    }

    // Getters & Helpers (Sama seperti sebelumnya, sesuaikan tombol)
    public void setLeaderboardView(JPanel view) { leaderboardPanelContainer.add(view, BorderLayout.CENTER); }
    public void showMenu() { layout.show(mainPanel, "MENU"); }
    public void showGame() { layout.show(mainPanel, "GAME"); }
    public void showCountdown(String text) { countdownLabel.setText(text); layout.show(mainPanel, "COUNTDOWN"); }
    public void showLeaderboardPanel() { layout.show(mainPanel, "LEADERBOARD"); }
    
    public void updateGameInfo(int qIndex, int timeLeft) {
        lblQuestionNo.setText("Soal: " + (qIndex+1) + "/10");
        lblTimer.setText("Sisa Waktu: " + timeLeft + "s");
    }
    public void setStatus(String text) { lblStatus.setText(text); }
    public void setOptions(String[] titles) {
        for(int i=0; i<4; i++) btnOptions[i].setText(titles[i]);
    }
    
    public JButton getStartButton() { return btnStart; }
    public JButton getBtnLeaderboard() { return btnLeaderboard; }
    public JButton getReplayButton() { return btnReplay; }
    public JButton getOptionButton(int index) { return btnOptions[index]; }
    public JButton getBtnBack() { return btnBack; } // Button kembali di menu utama kuis
}