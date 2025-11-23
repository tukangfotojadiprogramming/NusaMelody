package main.java.app.view;

import main.java.app.model.RegionalSong;
import main.java.app.util.AssetLoader;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class CatalogPage extends JPanel {
    private JPanel gridPanel;
    private Consumer<RegionalSong> onSongClick; // Functional Interface untuk callback

    public CatalogPage() {
        setLayout(new BorderLayout());
        
        // Header
        JLabel header = new JLabel("Katalog Lagu Daerah", SwingConstants.CENTER);
        header.setFont(new Font("Serif", Font.BOLD, 32));
        header.setBorder(BorderFactory.createEmptyBorder(20,0,20,0));
        add(header, BorderLayout.NORTH);

        // Grid Container
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20)); // Baris auto, 3 Kolom
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ScrollPane agar bisa discroll jika lagu banyak
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16); // Scroll speed halus
        add(scroll, BorderLayout.CENTER);
    }

    // Dipanggil oleh Controller untuk isi data
    public void setSongList(List<RegionalSong> songs) {
        gridPanel.removeAll(); // Bersihkan data lama
        
        for (RegionalSong song : songs) {
            gridPanel.add(createCard(song));
        }
        
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    // Membuat Kartu Lagu Kecil
    private JPanel createCard(RegionalSong song) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);
        
        // Thumbnail
        JLabel thumb = new JLabel();
        ImageIcon icon = AssetLoader.loadImage(song.getThumbnailPath());
        if(icon != null) {
            // Resize gambar agar rapi
            Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            thumb.setIcon(new ImageIcon(img));
        } else {
            thumb.setText("[No Image]");
            thumb.setHorizontalAlignment(SwingConstants.CENTER);
            thumb.setPreferredSize(new Dimension(200, 150));
        }
        
        // Tombol Judul
        JButton btn = new JButton(song.getTitle());
        btn.addActionListener(e -> {
            if(onSongClick != null) onSongClick.accept(song);
        });

        card.add(thumb, BorderLayout.CENTER);
        card.add(btn, BorderLayout.SOUTH);
        
        return card;
    }

    public void setOnSongSelected(Consumer<RegionalSong> listener) {
        this.onSongClick = listener;
    }
}