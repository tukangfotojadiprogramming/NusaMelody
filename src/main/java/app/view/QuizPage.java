package main.java.app.view;

import java.awt.*;
import javax.swing.*;

public class QuizPage extends JPanel {
    private CardLayout layout;
    private JPanel mainPanel; 
    private JPanel gamePanel; 
    
    private JLabel lblTimer, lblQuestionNo, lblStatus;
    private JButton[] btnOptions;
<<<<<<< HEAD

    private final Color CREAM = new Color(255, 248, 235);

    // ===== TIMER =====
    private JLabel lblTimer;
    private Timer countdown;
    private int timeLeft = 60;
=======
    private JButton btnReplay;
    private JButton btnLeaderboard; 
    private JLabel countdownLabel;
    
    // Panel Leaderboard external
    private JPanel leaderboardPanelContainer; 
>>>>>>> perbaikan-jaris

    public QuizPage() {

        setLayout(new BorderLayout());
<<<<<<< HEAD
        setOpaque(true);
        setBackground(CREAM);

        // ============== TIMER LABEL ==============
        lblTimer = new JLabel("Time: 60", SwingConstants.RIGHT);
        lblTimer.setFont(new Font("Georgia", Font.BOLD, 24));
        lblTimer.setForeground(new Color(120, 60, 20));
        lblTimer.setBorder(BorderFactory.createEmptyBorder(15, 20, 5, 25));
        add(lblTimer, BorderLayout.NORTH);

        // ============== MAIN PANEL ==============
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        add(mainPanel, BorderLayout.CENTER);

        // ==========================================
        // PANEL PERTANYAAN
        // ==========================================
        GradientRoundedPanel questionPanel = new GradientRoundedPanel();
        questionPanel.setLayout(new BorderLayout());
        questionPanel.setPreferredSize(new Dimension(800, 220));
        questionPanel.setOpaque(false);

        lblQuestion = new JLabel("Pertanyaan Akan Muncul di Sini", SwingConstants.CENTER);
        lblQuestion.setFont(new Font("Georgia", Font.BOLD, 24));
        lblQuestion.setForeground(Color.DARK_GRAY);

        questionPanel.add(lblQuestion, BorderLayout.CENTER);
        mainPanel.add(questionPanel, BorderLayout.NORTH);

        // ==========================================
        // PANEL PILIHAN JAWABAN
        // ==========================================
        JPanel optionsGrid = new JPanel(new GridLayout(2, 2, 20, 25));
        optionsGrid.setOpaque(false);
        optionsGrid.setBorder(BorderFactory.createEmptyBorder(40, 80, 80, 80));
        mainPanel.add(optionsGrid, BorderLayout.CENTER);

        btnOptions = new JButton[4];
        String[] labels = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            GradientOptionPanel optionPanel = new GradientOptionPanel(labels[i]);
            optionPanel.setLayout(new BorderLayout());
            optionPanel.setPreferredSize(new Dimension(300, 90));
            optionPanel.setOpaque(false);

            JButton btn = new JButton("Pilihan " + labels[i]);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setForeground(new Color(60, 40, 30));   // warna normal
            btn.setFont(new Font("Georgia", Font.BOLD, 18));

            // ============= HOVER EFFECT =============
            final Color normalColor = new Color(60, 40, 30);
            final Color hoverColor  = new Color(255, 140, 60); // orange lembut

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setForeground(hoverColor);
                    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setForeground(normalColor);
                    btn.setCursor(Cursor.getDefaultCursor());
                }
            });

            optionPanel.add(btn, BorderLayout.CENTER);
            btnOptions[i] = btn;
            optionsGrid.add(optionPanel);
        }
    }

    // =====================================================
    // TIMER METHOD
    // =====================================================
    public void startCountdown() {
        timeLeft = 60;
        lblTimer.setText("Time: 60");
        lblTimer.setForeground(new Color(120, 60, 20));

        countdown = new Timer(1000, e -> {
            timeLeft--;
            lblTimer.setText("Time: " + timeLeft);

            if (timeLeft <= 10) lblTimer.setForeground(Color.RED);

            if (timeLeft <= 0) {
                countdown.stop();
                JOptionPane.showMessageDialog(this, "Waktu habis!", "Timeout", JOptionPane.WARNING_MESSAGE);
            }
        });

        countdown.start();
    }

    // =====================================================
    // PANEL PERTANYAAN (ROUNDED + GRADIENT BORDER)
    // =====================================================
    class GradientRoundedPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(CREAM);
            g2.fillRoundRect(15, 15, w - 30, h - 30, 50, 50);

            GradientPaint gp = new GradientPaint(0, 0, new Color(255,160,0), w, h, new Color(255,120,180));
            g2.setStroke(new BasicStroke(8));
            g2.setPaint(gp);
            g2.drawRoundRect(15, 15, w - 30, h - 30, 50, 50);

            g2.dispose();
        }
    }

    // =====================================================
    // PANEL OPSI (GRADIENT BORDER + LABEL LINGKARAN)
    // =====================================================
    class GradientOptionPanel extends JPanel {
        private final String label;

        public GradientOptionPanel(String label) {
            this.label = label;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(CREAM);
            g2.fillRoundRect(10, 10, w - 20, h - 20, 40, 40);

            GradientPaint gp = new GradientPaint(0, 0, new Color(90,220,90), w, h, new Color(180,120,255));
            g2.setStroke(new BasicStroke(6));
            g2.setPaint(gp);
            g2.drawRoundRect(10, 10, w - 20, h - 20, 40, 40);

            g2.setColor(Color.WHITE);
            g2.fillOval(20, h/2 - 22, 45, 45);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Georgia", Font.BOLD, 20));
            g2.drawString(label, 20 + 18, h/2 + 8);

            g2.dispose();
        }
    }

    // Getter
    public void setQuestion(String text) { lblQuestion.setText(text); }
    public JButton getOptionBtn(int index) { return btnOptions[index]; }
=======
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
>>>>>>> perbaikan-jaris
}