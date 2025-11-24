package main.java.app.view;

import main.java.app.model.LeaderboardEntry;
import main.java.app.util.UIStyle;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class LeaderboardPage extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnBack;

    public LeaderboardPage() {
        setLayout(new BorderLayout());
        setBackground(UIStyle.COLOR_BG);

        // 1. HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.COLOR_PRIMARY); 
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        // Tombol Kembali
        btnBack = new JButton("← Kembali");
        UIStyle.applyModernButton(btnBack); // Pakai style standar
        btnBack.setBackground(new Color(100, 30, 30)); // Override warna jadi merah gelap
        
        JLabel title = new JLabel("Papan Peringkat Juara", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        
        // Layout Header
        JPanel leftContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15)); // Vertical align center
        leftContainer.setOpaque(false);
        leftContainer.add(btnBack);

        header.add(leftContainer, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        // Dummy panel kanan agar judul tetap di tengah
        JPanel dummy = new JPanel(); 
        dummy.setOpaque(false); 
        dummy.setPreferredSize(new Dimension(100, 10));
        header.add(dummy, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 2. TABLE SETUP
        String[] columnNames = {"Peringkat", "Nama Pemain", "Skor", "Waktu Bermain"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Matikan edit sel
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setRowHeight(40); // Baris lebih tinggi biar lega
        table.setShowGrid(true);
        table.setGridColor(new Color(230, 230, 230));
        table.setFillsViewportHeight(true);
        table.setFocusable(false); // Matikan highlight focus sel
        table.setRowSelectionAllowed(false); // Matikan seleksi baris (opsional, biar murni info)

        // Styling Header Tabel
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 16));
        tableHeader.setBackground(UIStyle.COLOR_SIDEBAR);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 50));
        
        // --- REVISI: MATIKAN FITUR GESER KOLOM ---
        tableHeader.setReorderingAllowed(false); 
        tableHeader.setResizingAllowed(false);   // Opsional: Matikan resize lebar kolom

        // Rata Tengah Isi Tabel
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Atur Lebar Kolom Spesifik (Opsional, biar rapi)
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Peringkat (Kecil)
        table.getColumnModel().getColumn(1).setPreferredWidth(300); // Nama (Lebar)
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Skor (Sedang)
        table.getColumnModel().getColumn(3).setPreferredWidth(200); // Waktu (Lebar)

        // ScrollPane Wrapper
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new EmptyBorder(30, 80, 30, 80)); // Margin kiri-kanan besar
        scroll.getViewport().setBackground(Color.WHITE);
        
        add(scroll, BorderLayout.CENTER);
    }

    public void setLeaderboardData(List<LeaderboardEntry> data) {
        tableModel.setRowCount(0); // Bersihkan data lama
        
        if (data.isEmpty()) {
            // Tampilkan placeholder jika kosong
            tableModel.addRow(new Object[]{"-", "Belum ada data", "-", "-"});
        } else {
            int rank = 1;
            for (LeaderboardEntry e : data) {
                tableModel.addRow(new Object[]{
                    rank, 
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