package main.java.app.view;

import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;

public class QuizPage extends JPanel {
    private CardLayout layout;
    private JPanel mainPanel; 
    private JPanel gamePanel; 
    
    private JLabel lblTimer, lblQuestionNo, lblStatus;
    private JButton[] btnOptions;
    private JButton btnReplay;
    private JButton btnLeaderboard; 
    private JLabel countdownLabel;
    
    // Panel Leaderboard external
    private JPanel leaderboardPanelContainer; 

    public QuizPage() {
        setLayout(new BorderLayout());
        layout = new CardLayout();
        mainPanel = new JPanel(layout);
        
        // 1. MENU VIEW
        JPanel menuView = new JPanel(new GridBagLayout());
        menuView.setBackground(UIStyle.COLOR_BG);
        JButton btnStart = new JButton("MULAI MAIN");
        btnLeaderboard = new JButton("LEADERBOARD"); 
        
        UIStyle.applyModernButton(btnStart);
        UIStyle.applyModernButton(btnLeaderboard);
        
        JPanel menuBox = new JPanel(new GridLayout(2, 1, 10, 10));
        menuBox.setOpaque(false);
        menuBox.add(btnStart);
        menuBox.add(btnLeaderboard);
        menuView.add(menuBox);

        // 2. GAME VIEW
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(Color.WHITE);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        topPanel.setBackground(UIStyle.COLOR_SIDEBAR);
        
        lblTimer = new JLabel("Waktu: 60s");
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 24));
        lblTimer.setForeground(Color.GREEN);
        
        lblQuestionNo = new JLabel("Soal 1/10");
        lblQuestionNo.setForeground(Color.WHITE);
        
        topPanel.add(lblTimer, BorderLayout.WEST);
        topPanel.add(lblQuestionNo, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        lblStatus = new JLabel("Dengarkan lagunya...", SwingConstants.CENTER);
        lblStatus.setFont(new Font("Serif", Font.ITALIC, 24));
        
        btnReplay = new JButton("Putar Ulang (5s)");
        UIStyle.applyModernButton(btnReplay);
        btnReplay.setBackground(Color.GRAY);
        btnReplay.setEnabled(false);
        JPanel btnPanel = new JPanel(); btnPanel.setOpaque(false); btnPanel.add(btnReplay);

        centerPanel.add(lblStatus);
        centerPanel.add(btnPanel);

        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        btnOptions = new JButton[4];
        for(int i=0; i<4; i++) {
            btnOptions[i] = new JButton();
            UIStyle.applyModernButton(btnOptions[i]);
            optionsPanel.add(btnOptions[i]);
        }

        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.add(optionsPanel, BorderLayout.SOUTH);

        // 3. COUNTDOWN
        countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Serif", Font.BOLD, 150));
        countdownLabel.setForeground(UIStyle.COLOR_PRIMARY);

        // 4. LEADERBOARD CONTAINER
        leaderboardPanelContainer = new JPanel(new BorderLayout());

        mainPanel.add(menuView, "MENU");
        mainPanel.add(gamePanel, "GAME");
        mainPanel.add(countdownLabel, "COUNTDOWN");
        mainPanel.add(leaderboardPanelContainer, "LEADERBOARD");

        add(mainPanel, BorderLayout.CENTER);
    }

    public void setLeaderboardView(JPanel view) {
        leaderboardPanelContainer.add(view, BorderLayout.CENTER);
    }

    public void showMenu() { layout.show(mainPanel, "MENU"); }
    public void showGame() { layout.show(mainPanel, "GAME"); }
    public void showCountdown(String text) { 
        countdownLabel.setText(text);
        layout.show(mainPanel, "COUNTDOWN");
    }
    public void showLeaderboardPanel() { layout.show(mainPanel, "LEADERBOARD"); }
    
    public void updateGameInfo(int qIndex, int timeLeft) {
        lblQuestionNo.setText("Soal: " + (qIndex+1) + "/10");
        lblTimer.setText("Sisa Waktu: " + timeLeft + "s");
    }

    public void setStatus(String text) { lblStatus.setText(text); }
    public void setOptions(String[] titles) {
        for(int i=0; i<4; i++) btnOptions[i].setText(titles[i]);
    }
    
    public JButton getStartButton() { return (JButton) ((JPanel)((JPanel)mainPanel.getComponent(0)).getComponent(0)).getComponent(0); }
    public JButton getBtnLeaderboard() { return btnLeaderboard; }
    public JButton getReplayButton() { return btnReplay; }
    public JButton getOptionButton(int index) { return btnOptions[index]; }
}