package main.java.app.view;

import main.java.app.model.LeaderboardEntry;
import main.java.app.util.AssetLoader;
import main.java.app.util.UIStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class LeaderboardPage extends JPanel {
    private JTable tabelLeaderboard;
    private DefaultTableModel modelTabel;
    private JButton btnBack;

    public LeaderboardPage() {
        // 1. Setup Layout Utama dengan Background Batik
        setLayout(new BorderLayout());
        JPanel mainPanel = UIStyle.createBackgroundPanel();
        
        // 2. HEADER SECTION
        btnBack = UIStyle.createWoodenButton("Kembali");
        btnBack.setPreferredSize(new Dimension(120, 40));
        
        // Menggunakan Header Helper dari UIStyle
        mainPanel.add(UIStyle.createHeader("Papan Peringkat Juara", btnBack), BorderLayout.NORTH);

        // 3. CONTENT SECTION (Panel Kertas)
        JPanel paperPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Load tekstur kertas
                ImageIcon paper = AssetLoader.loadImage("paper-texture.png");
                if (paper != null) {
                    g.drawImage(paper.getImage(), 0, 0, getWidth(), getHeight(), null);
                } else {
                    g.setColor(UIStyle.COLOR_CARD_BG); // Fallback warna krem
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                
                // Tambah Border/Frame Cokelat Klasik di sekeliling kertas
                g2.setColor(new Color(101, 67, 33));
                g2.setStroke(new BasicStroke(4));
                g2.drawRect(0, 0, getWidth(), getHeight());
            }
        };
        // Padding agar tabel tidak mepet dengan bingkai kertas
        paperPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // 4. SETUP TABEL (Adaptasi dari Referensi Anda)
        String[] columnNames = {"Peringkat", "Nama Pemain", "Skor", "Waktu Bermain"};
        
        modelTabel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Agar sel tidak bisa diedit
            }
        };
        
        tabelLeaderboard = new JTable(modelTabel);
        tabelLeaderboard.setFont(new Font("SansSerif", Font.BOLD, 15)); // Font isi tabel
        tabelLeaderboard.setRowHeight(40); // Tinggi baris agar nyaman dilihat
        tabelLeaderboard.setShowGrid(true);
        tabelLeaderboard.setGridColor(new Color(139, 69, 19, 50)); // Grid cokelat transparan
        
        // Setting Background Tabel (Semi-Transparan agar tekstur kertas terlihat)
        tabelLeaderboard.setOpaque(false);
        ((DefaultTableCellRenderer)tabelLeaderboard.getDefaultRenderer(Object.class)).setOpaque(false);
        tabelLeaderboard.setBackground(new Color(0, 0, 0, 0)); // Transparan total
        tabelLeaderboard.setForeground(new Color(60, 30, 10)); // Warna teks cokelat tua

        // Styling Header Tabel
        JTableHeader headerTabel = tabelLeaderboard.getTableHeader();
        headerTabel.setFont(new Font("Serif", Font.BOLD, 18));
        headerTabel.setBackground(UIStyle.COLOR_ACCENT);
        headerTabel.setForeground(new Color(60, 30, 10));
        headerTabel.setPreferredSize(new Dimension(0, 50));
        headerTabel.setReorderingAllowed(false); // Kolom tidak bisa digeser

        // Rata Tengah Isi Tabel
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setOpaque(false); // Agar transparan mengikuti tabel
        
        for (int i = 0; i < tabelLeaderboard.getColumnCount(); i++) {
            tabelLeaderboard.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Atur Lebar Kolom agar proporsional
        tabelLeaderboard.getColumnModel().getColumn(0).setPreferredWidth(80);  // Peringkat
        tabelLeaderboard.getColumnModel().getColumn(1).setPreferredWidth(300); // Nama
        tabelLeaderboard.getColumnModel().getColumn(2).setPreferredWidth(100); // Skor
        tabelLeaderboard.getColumnModel().getColumn(3).setPreferredWidth(200); // Waktu

        // ScrollPane Transparan
        JScrollPane scrollPane = new JScrollPane(tabelLeaderboard);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2)); // Garis tipis pembatas tabel
        
        paperPanel.add(scrollPane, BorderLayout.CENTER);

        // Wrapper agar kertas ada margin dari background batik utama
        JPanel contentWrapper = new JPanel(new BorderLayout());
        contentWrapper.setOpaque(false);
        contentWrapper.setBorder(new EmptyBorder(30, 80, 50, 80)); // Margin luar (Kiri Kanan Besar)
        contentWrapper.add(paperPanel, BorderLayout.CENTER);
        
        mainPanel.add(contentWrapper, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    // Method untuk menerima data dari Controller
    public void setLeaderboardData(List<LeaderboardEntry> data) {
        modelTabel.setRowCount(0); // Reset data lama
        
        if (data.isEmpty()) {
            modelTabel.addRow(new Object[]{"-", "Belum ada data", "-", "-"});
        } else {
            int rank = 1;
            for (LeaderboardEntry e : data) {
                // Tambahkan Trophy untuk Juara 1-3 (Opsional: Menggunakan Emoji)
                String rankDisplay = String.valueOf(rank);
                if (rank == 1) rankDisplay = "🥇 1";
                else if (rank == 2) rankDisplay = "🥈 2";
                else if (rank == 3) rankDisplay = "🥉 3";

                modelTabel.addRow(new Object[]{
                    rankDisplay, 
                    e.getName(), 
                    e.getScore(), 
                    e.getDate()
                });
                rank++;
            }
        }
    }

    public JButton getBtnBack() { return btnBack; }
}