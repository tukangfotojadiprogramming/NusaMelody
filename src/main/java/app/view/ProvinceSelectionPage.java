package main.java.app.view;

import main.java.app.util.UIStyle;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProvinceSelectionPage extends JPanel {
    private JPanel gridPanel;
    private JButton btnBack; // Tombol Kembali

    public ProvinceSelectionPage() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BG);

        // Header Panel dengan Tombol Kembali
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.COLOR_BG);
        
        btnBack = new JButton("← Kembali");
        UIStyle.applyModernButton(btnBack);
        btnBack.setBackground(Color.GRAY);
        
        JPanel btnContainer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnContainer.setOpaque(false);
        btnContainer.add(btnBack);

        header.add(btnContainer, BorderLayout.WEST);
        header.add(UIStyle.createHeader("Pilih Daerah Nusantara"), BorderLayout.CENTER);
        header.add(Box.createHorizontalStrut(100), BorderLayout.EAST); // Spacer

        add(header, BorderLayout.NORTH);

        gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(UIStyle.COLOR_BG);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        // DATA SESUAI FILE songs (1).sql
        // Pastikan string nama provinsi SAMA PERSIS dengan di database
        addProvinceCard("Bengkulu", "Bunga Rafflesia");
        addProvinceCard("Jawa Tengah", "Borobudur & Batik");
        addProvinceCard("Jawa Barat", "Angklung & Tari Jaipong");
        addProvinceCard("DKI Jakarta", "Ondel-Ondel & Monas");
        addProvinceCard("Kalimantan Selatan", "Pasar Terapung");
        addProvinceCard("Maluku", "Kepulauan Rempah");
        addProvinceCard("Papua", "Cendrawasih & Raja Ampat");
        addProvinceCard("Sumatra Barat", "Jam Gadang & Rendang");
        addProvinceCard("Nusa Tenggara Barat", "Pulau Komodo & Mandalika");

        JScrollPane scroll = new JScrollPane(gridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);
    }

    private void addProvinceCard(String name, String desc) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.COLOR_PRIMARY, 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setPreferredSize(new Dimension(200, 150));

        JLabel title = new JLabel("<html><center>" + name + "</center></html>", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 20));
        title.setForeground(UIStyle.COLOR_SIDEBAR);

        JLabel subtitle = new JLabel("<html><center>" + desc + "</center></html>", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);

        JButton btnSelect = new JButton("Lihat Lagu");
        UIStyle.applyModernButton(btnSelect);
        // Simpan nama provinsi asli untuk query DB
        btnSelect.putClientProperty("provinceName", name);

        card.add(title, BorderLayout.NORTH);
        card.add(subtitle, BorderLayout.CENTER);
        card.add(btnSelect, BorderLayout.SOUTH);

        gridPanel.add(card);
    }

    public void setSelectionListener(ActionListener listener) {
        for (Component c : gridPanel.getComponents()) {
            if (c instanceof JPanel) {
                JPanel card = (JPanel) c;
                if(card.getComponentCount() >= 3) {
                    JButton btn = (JButton) card.getComponent(2);
                    btn.addActionListener(listener);
                }
            }
        }
    }
    
    public JButton getBtnBack() { return btnBack; }
}