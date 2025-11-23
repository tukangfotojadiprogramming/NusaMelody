package main.java.app.view;

import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProvinceSelectionPage extends JPanel {
    private JPanel gridPanel;

    public ProvinceSelectionPage() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BG);

        add(UIStyle.createHeader("Pilih Daerah Nusantara"), BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        gridPanel.setBackground(UIStyle.COLOR_BG);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // Dummy Provinces (Harusnya bisa dari DB, tapi hardcode untuk UI flow)
        addProvinceCard("Sumatera", "Tari Piring");
        addProvinceCard("Jawa", "Wayang Kulit");
        addProvinceCard("Kalimantan", "Hutan Tropis");
        addProvinceCard("Sulawesi", "Phinisi");
        addProvinceCard("Papua", "Cendrawasih");
        addProvinceCard("Bali & Nusa", "Pura");

        add(new JScrollPane(gridPanel), BorderLayout.CENTER);
    }

    private void addProvinceCard(String name, String desc) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(UIStyle.COLOR_PRIMARY, 2));

        JLabel title = new JLabel(name, SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel subtitle = new JLabel(desc, SwingConstants.CENTER);
        subtitle.setForeground(Color.GRAY);

        // Disini bisa ditambahkan action listener nanti
        // Untuk sekarang visual saja
        JButton btnSelect = new JButton("Lihat Lagu");
        UIStyle.applyModernButton(btnSelect);
        
        // Simpan nama provinsi di client property agar Controller bisa baca
        btnSelect.putClientProperty("provinceName", name);

        card.add(title, BorderLayout.NORTH);
        card.add(subtitle, BorderLayout.CENTER);
        card.add(btnSelect, BorderLayout.SOUTH);

        gridPanel.add(card);
    }

    // Method untuk Controller menangkap klik tombol
    public void setSelectionListener(ActionListener listener) {
        for (Component c : gridPanel.getComponents()) {
            if (c instanceof JPanel) {
                JPanel card = (JPanel) c;
                JButton btn = (JButton) card.getComponent(2); // Index 2 adalah tombol
                btn.addActionListener(listener);
            }
        }
    }
}