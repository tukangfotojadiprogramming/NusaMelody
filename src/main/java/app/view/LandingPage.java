package main.java.app.view;

import main.java.app.util.AssetLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class LandingPage extends JPanel {
    private JButton btnStart;
    private Image heroImage;

    public LandingPage() {
        setLayout(new BorderLayout()); // Layout utama
        
        // Load Background Image
        ImageIcon icon = AssetLoader.loadImage("landing-hero.jpg");
        if (icon != null) heroImage = icon.getImage();

        // Panel Transparan untuk Konten Tengah
        JPanel overlay = new JPanel();
        overlay.setLayout(new BoxLayout(overlay, BoxLayout.Y_AXIS));
        overlay.setOpaque(false); // Agar background terlihat
        overlay.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0)); // Padding atas

        // Judul
        JLabel title = new JLabel("NusaMelody");
        title.setFont(new Font("Serif", Font.BOLD, 64));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subjudul
        JLabel subtitle = new JLabel("Jelajahi Harmoni Nusantara");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 24));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tombol Mulai
        btnStart = new JButton("Mulai Jelajah");
        btnStart.setFont(new Font("SansSerif", Font.BOLD, 18));
        btnStart.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Menambahkan komponen ke panel overlay
        overlay.add(title);
        overlay.add(Box.createRigidArea(new Dimension(0, 20))); // Spasi
        overlay.add(subtitle);
        overlay.add(Box.createRigidArea(new Dimension(0, 50))); // Spasi
        overlay.add(btnStart);

        add(overlay, BorderLayout.CENTER);
    }

    // Menggambar Background Image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (heroImage != null) {
            g.drawImage(heroImage, 0, 0, getWidth(), getHeight(), this);
            // Efek Gelap (Overlay Hitam Transparan) agar teks terbaca
            g.setColor(new Color(0, 0, 0, 100));
            g.fillRect(0, 0, getWidth(), getHeight());
        } else {
            // Backup jika gambar tidak ada
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Method untuk Controller mendaftarkan aksi tombol
    public void setStartAction(ActionListener action) {
        btnStart.addActionListener(action);
    }
}
