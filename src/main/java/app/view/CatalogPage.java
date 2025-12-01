package main.java.app.view;

import main.java.app.model.RegionalSong;
import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class CatalogPage extends JPanel {
    private JPanel gridPanel;
    private Consumer<RegionalSong> onSongClick;
    private JButton btnBack;
    private JLabel lblTitle;

    public CatalogPage() {
        setLayout(new BorderLayout());
        JPanel mainPanel = UIStyle.createBackgroundPanel();

        btnBack = UIStyle.createWoodenButton("Kembali");
        btnBack.setPreferredSize(new Dimension(120, 40));
        
        // Header
        JPanel header = UIStyle.createHeader("Katalog Lagu Daerah", btnBack);
        lblTitle = (JLabel) ((JPanel)header.getComponent(0)).getComponent(1); // Hacky access to title label, or just recreate header logic if needed
        mainPanel.add(header, BorderLayout.NORTH);

        // Grid Container dengan FlowLayout (agar kartu tidak dipaksa stretch)
        // WrapFlowLayout atau GridLayout dengan gap yang pas
        gridPanel = new JPanel(new GridLayout(0, 3, 40, 40)); // 3 Kolom, Gap 40px
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(30, 50, 30, 50));
        
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        mainPanel.add(scroll, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void setSongList(List<RegionalSong> songs) {
        gridPanel.removeAll();
        
        if (songs.isEmpty()) {
            JLabel empty = new JLabel("Tidak ada lagu ditemukan.", SwingConstants.CENTER);
            empty.setFont(new Font("SansSerif", Font.BOLD, 18));
            empty.setForeground(Color.WHITE);
            gridPanel.setLayout(new BorderLayout()); // Reset layout sementara
            gridPanel.add(empty);
        } else {
            gridPanel.setLayout(new GridLayout(0, 3, 40, 40)); // Kembalikan ke Grid 3 kolom
            for (RegionalSong song : songs) {
                gridPanel.add(createSongCard(song));
            }
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // --- KARTU LAGU VERSI PREMIUM ---
    private JPanel createSongCard(RegionalSong song) {
        JPanel card = new JPanel(new BorderLayout());
        
        // Background Kayu Frame
        card.setOpaque(false); // Kita gambar manual backgroundnya
        
        // Content Wrapper
        JPanel content = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Gambar Frame Kayu
                ImageIcon wood = AssetLoader.loadImage("wood-texture.png");
                if(wood != null) g.drawImage(wood.getImage(), 0, 0, getWidth(), getHeight(), null);
                else { g.setColor(new Color(101, 67, 33)); g.fillRect(0,0,getWidth(),getHeight()); }
                
                // Border Emas Dalam
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(UIStyle.COLOR_GOLD);
                g2.setStroke(new BasicStroke(3));
                g2.drawRect(5, 5, getWidth()-10, getHeight()-10);
            }
        };
        content.setBorder(new EmptyBorder(15, 15, 15, 15));
        content.setPreferredSize(new Dimension(300, 350)); // Tinggi ditambah agar muat vertikal

        // 1. Thumbnail (Atas) - Besar & Persegi
        JLabel thumb = new JLabel();
        thumb.setPreferredSize(new Dimension(270, 200)); 
        thumb.setHorizontalAlignment(SwingConstants.CENTER);
        thumb.setBorder(BorderFactory.createLineBorder(UIStyle.COLOR_GOLD, 2));
        
        ImageIcon icon = AssetLoader.loadImage(song.getThumbnailPath());
        if(icon != null) {
            // Crop/Scale gambar agar pas kotak
            Image img = icon.getImage().getScaledInstance(270, 200, Image.SCALE_SMOOTH);
            thumb.setIcon(new ImageIcon(img));
        } else {
            thumb.setOpaque(true);
            thumb.setBackground(Color.GRAY);
            thumb.setText("No Image");
        }

        // 2. Info & Tombol (Bawah)
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);
        infoPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel lblTitle = new JLabel(song.getTitle());
        lblTitle.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitle.setForeground(UIStyle.COLOR_GOLD); // Emas
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblProv = new JLabel("Lagu Daerah " + song.getProvince());
        lblProv.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblProv.setForeground(Color.LIGHT_GRAY); // Putih Abu
        lblProv.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton btnPlay = UIStyle.createWoodenButton("Lihat Lagu");
        btnPlay.setPreferredSize(new Dimension(150, 40));
        btnPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPlay.setFont(new Font("Serif", Font.BOLD, 14));
        btnPlay.addActionListener(e -> { if(onSongClick != null) onSongClick.accept(song); });

        infoPanel.add(lblTitle);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(lblProv);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        infoPanel.add(btnPlay);

        content.add(thumb, BorderLayout.NORTH);
        content.add(infoPanel, BorderLayout.CENTER);

        return content;
    }

    public void setOnSongSelected(Consumer<RegionalSong> listener) { this.onSongClick = listener; }
    public JButton getBtnBack() { return btnBack; }
    
    // Helper untuk update judul header (opsional, jika ingin judul dinamis per provinsi)
    public void updateTitle(String newTitle) {
        // Logic update title di sini jika diperlukan
    }
}