package main.java.app.view;

import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;

public class QuizPage extends JPanel {
    private JLabel lblQuestion;
    private JButton[] btnOptions;
    private JLabel lblScore;

    public QuizPage() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BG);

        // Header
        add(UIStyle.createHeader("Kuis Telinga Nusantara"), BorderLayout.NORTH);

        // Center (Pertanyaan)
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.setOpaque(false);
        
        lblQuestion = new JLabel("Dengarkan lagu yang sedang diputar...", SwingConstants.CENTER);
        lblQuestion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        
        lblScore = new JLabel("Skor: 0", SwingConstants.CENTER);
        lblScore.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblScore.setForeground(UIStyle.COLOR_ACCENT);

        centerPanel.add(lblQuestion);
        centerPanel.add(lblScore);
        add(centerPanel, BorderLayout.CENTER);

        // Footer (Pilihan Jawaban)
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 50, 50));
        optionsPanel.setOpaque(false);

        btnOptions = new JButton[4];
        for (int i = 0; i < 4; i++) {
            btnOptions[i] = new JButton("Pilihan " + (i+1));
            UIStyle.applyModernButton(btnOptions[i]);
            optionsPanel.add(btnOptions[i]);
        }
        add(optionsPanel, BorderLayout.SOUTH);
    }
    
    // Method untuk Controller mengatur tampilan
    public void setQuestion(String text) { lblQuestion.setText(text); }
    public void setScore(int score) { lblScore.setText("Skor: " + score); }
    public JButton getOptionBtn(int index) { return btnOptions[index]; }
}