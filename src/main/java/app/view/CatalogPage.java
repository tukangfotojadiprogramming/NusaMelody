package main.java.app.view;

import main.java.app.model.RegionalSong;
import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class CatalogPage extends JPanel {
    private JPanel gridPanel;
    private Consumer<RegionalSong> onSongClick;
    private JButton btnBack; // Tombol Kembali

    public CatalogPage() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BG);
        
        // HEADER dengan Tombol Kembali
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.COLOR_BG);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnBack = new JButton("← Kembali");
        UIStyle.applyModernButton(btnBack);
        btnBack.setBackground(Color.GRAY);
        
        JLabel titleLabel = new JLabel("Katalog Lagu Daerah", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(UIStyle.COLOR_PRIMARY);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(btnBack);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(titleLabel, BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(100), BorderLayout.EAST); // Spacer

        add(header, BorderLayout.NORTH);

        // Grid Container
        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(UIStyle.COLOR_BG);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    public void setSongList(List<RegionalSong> songs) {
        gridPanel.removeAll();
        for (RegionalSong song : songs) {
            gridPanel.add(createCard(song));
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCard(RegionalSong song) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setBackground(Color.WHITE);
        
        JLabel thumb = new JLabel();
        ImageIcon icon = AssetLoader.loadImage(song.getThumbnailPath());
        if(icon != null) {
            Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
            thumb.setIcon(new ImageIcon(img));
        } else {
            thumb.setText("[No Image]");
            thumb.setHorizontalAlignment(SwingConstants.CENTER);
            thumb.setPreferredSize(new Dimension(200, 150));
        }
        
        JButton btn = new JButton(song.getTitle());
        UIStyle.applyModernButton(btn); // Pakai style
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
    
    public JButton getBtnBack() { return btnBack; }
}