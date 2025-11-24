package main.java.app;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.java.app.controller.*;
import main.java.app.service.DatabaseService; // Pakai DatabaseService, bukan DB
import main.java.app.service.SongRepository;
import main.java.app.service.LeaderboardRepository;
import main.java.app.util.UIStyle;
import main.java.app.view.*;

public class Main {
    private static JFrame frame;
    private static JPanel mainWrapper;
    private static JPanel sidebar;
    private static JPanel loginContainer;
    private static CardLayout contentLayout;
    private static JPanel contentPanel;
    private static boolean isSidebarVisible = false;
    
    // Controller Quiz perlu diakses global untuk fitur abortGame
    private static QuizController quizCtrl; 

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Set Tampilan agar lebih modern sesuai OS
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
            
            // 1. INISIALISASI DATABASE OTOMATIS
            // Ini akan mengecek apakah tabel ada. Jika tidak, tabel & data dummy akan dibuat.
            DatabaseService.initIfNeeded();

            // Setup Frame Utama
            frame = new JFrame("NusaMelody - Jelajah Musik Nusantara");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 800); // Ukuran frame yang lega
            frame.setLocationRelativeTo(null); // Center di layar

            // --- LOGIN SYSTEM ---
            LoginPanel loginPage = new LoginPanel();
            SignupPanel signupPanel = new SignupPanel();
            
            // Container untuk menengahkan form login
            loginContainer = new JPanel(new GridBagLayout());
            loginContainer.setBackground(UIStyle.COLOR_BG);
            loginContainer.add(loginPage);

            // Listener Login Berhasil
            loginPage.setLoginListener(email -> {
                // Setelah login sukses, bangun UI utama aplikasi
                initMainApp(email); 
                frame.setContentPane(mainWrapper); // Ganti tampilan ke Main App
                frame.revalidate();
                frame.repaint();
            });

            // Listener Tombol Daftar Akun
            loginPage.getSignupButton().addActionListener(e -> {
                JDialog d = new JDialog(frame, "Daftar Akun Baru", Dialog.ModalityType.APPLICATION_MODAL);
                d.setContentPane(signupPanel);
                d.setSize(450, 650);
                d.setLocationRelativeTo(frame);
                
                signupPanel.setSignupListener(mail -> {
                    d.dispose();
                    JOptionPane.showMessageDialog(frame, "Akun berhasil dibuat! Silakan login.");
                });
                
                signupPanel.getBackToLoginButton().addActionListener(ev -> d.dispose());
                d.setVisible(true);
            });

            // Tampilkan Halaman Login saat pertama buka
            frame.setContentPane(loginContainer);
            frame.setVisible(true);
        });
    }

    // Method untuk membangun UI Aplikasi Utama (Sidebar + Content)
    private static void initMainApp(String userEmail) {
        String userName = userEmail.split("@")[0]; // Ambil nama dari email
        
        mainWrapper = new JPanel(new BorderLayout());

        // 1. HEADER BAR (Atas)
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(0, 60));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lblBrand = new JLabel("NusaMelody");
        lblBrand.setFont(new Font("Serif", Font.BOLD, 24));
        lblBrand.setForeground(Color.WHITE);

        // Tombol Hamburger untuk Toggle Sidebar
        JButton btnHamburger = new JButton("☰"); 
        btnHamburger.setFont(new Font("SansSerif", Font.BOLD, 24));
        btnHamburger.setForeground(Color.WHITE);
        btnHamburger.setBackground(UIStyle.COLOR_PRIMARY);
        btnHamburger.setBorderPainted(false);
        btnHamburger.setFocusPainted(false);
        btnHamburger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHamburger.addActionListener(e -> toggleSidebar());

        header.add(lblBrand, BorderLayout.WEST);
        header.add(btnHamburger, BorderLayout.EAST);

        // 2. SIDEBAR (Kanan - Default Hidden)
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyle.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.setVisible(false); 

        JLabel lblUser = new JLabel("Hi, " + userName);
        lblUser.setForeground(Color.LIGHT_GRAY);
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblUser);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. CONTENT AREA (Tengah)
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        
        // Inisialisasi Repositories
        SongRepository songRepo = new SongRepository();
        LeaderboardRepository lbRepo = new LeaderboardRepository();

        // Inisialisasi Views
        LandingPage landingPage = new LandingPage();
        ProvinceSelectionPage provincePage = new ProvinceSelectionPage();
        CatalogPage catalogPage = new CatalogPage();
        SongDetailPage songDetailPage = new SongDetailPage();
        QuizPage quizPage = new QuizPage();
        LeaderboardPage lbPage = new LeaderboardPage();

        // Setup Relasi View (Quiz butuh Leaderboard)
        quizPage.setLeaderboardView(lbPage); 

        // Daftarkan View ke CardLayout
        contentPanel.add(landingPage, "LANDING");
        contentPanel.add(provincePage, "PROVINCE");
        contentPanel.add(catalogPage, "CATALOG");
        contentPanel.add(songDetailPage, "DETAIL");
        contentPanel.add(quizPage, "QUIZ");

        // Inisialisasi Controllers (Wiring Logic)
        SongController songCtrl = new SongController(songDetailPage, contentPanel, contentLayout);
        CatalogController catCtrl = new CatalogController(catalogPage, songCtrl);
        new LandingController(landingPage, contentPanel, contentLayout);
        
        // Init Quiz Controller (Global Variable agar bisa di-stop dari main)
        quizCtrl = new QuizController(quizPage, lbPage, songRepo, lbRepo);
        quizCtrl.setUser(userName, userEmail);

        // === LOGIKA NAVIGASI & TOMBOL KEMBALI ===
        
        // 1. Tombol Kembali di Pilihan Provinsi -> Selalu ke Landing
        provincePage.getBtnBack().addActionListener(e -> switchView("LANDING"));

        // 2. Tombol Kembali di Katalog (DINAMIS)
        // Default: Jika user masuk katalog tanpa filter, kembali ke Landing
        catCtrl.setBackAction(() -> switchView("LANDING"));

        // 3. Navigasi dari Landing Page (Menu Utama)
        landingPage.setNavAction("PROVINCE", () -> switchView("PROVINCE"));
        landingPage.setNavAction("CATALOG", () -> {
            catCtrl.loadAllSorted();
            catCtrl.setBackAction(() -> switchView("LANDING")); // Set back ke Landing
            switchView("CATALOG");
        });
        landingPage.setNavAction("QUIZ", () -> switchView("QUIZ"));

        // 4. Logic Pindah dari Provinsi ke Katalog
        provincePage.setSelectionListener(e -> {
            JButton btn = (JButton) e.getSource();
            String prov = (String) btn.getClientProperty("provinceName");
            
            catCtrl.filterByProvince(prov);
            
            // Jika masuk dari Provinsi, tombol back katalog harus balik ke Provinsi
            catCtrl.setBackAction(() -> switchView("PROVINCE")); 
            
            switchView("CATALOG");
        });

        // 5. Navigasi Sidebar (Menu Samping)
        addSidebarBtn(sidebar, "🏠 Beranda", () -> switchView("LANDING"));
        addSidebarBtn(sidebar, "🗺️ Jelajah Peta", () -> switchView("PROVINCE"));
        addSidebarBtn(sidebar, "🎶 Semua Lagu", () -> {
            catCtrl.loadAllSorted();
            catCtrl.setBackAction(() -> switchView("LANDING"));
            switchView("CATALOG");
        });
        addSidebarBtn(sidebar, "❓ Kuis Nusantara", () -> switchView("QUIZ"));
        
        // Spacer agar tombol logout di bawah
        sidebar.add(Box.createVerticalGlue());
        
        // Tombol Logout
        JButton btnLogout = new JButton("🚪 Keluar");
        UIStyle.applySidebarButton(btnLogout);
        btnLogout.setBackground(new Color(100, 30, 30)); // Merah
        btnLogout.addActionListener(e -> {
            if(quizCtrl != null) quizCtrl.abortGame(); // Stop kuis jika sedang main
            songCtrl.stopAudio(); // Matikan lagu jika sedang play
            
            frame.setContentPane(loginContainer); // Balik ke Login
            frame.revalidate();
        });
        sidebar.add(btnLogout);

        // Rakit Layout Utama
        mainWrapper.add(header, BorderLayout.NORTH);
        mainWrapper.add(sidebar, BorderLayout.EAST); // Sidebar di Kanan
        mainWrapper.add(contentPanel, BorderLayout.CENTER);
    }

    // Helper: Pindah halaman & handle logika sampingan
    private static void switchView(String viewName) {
        // Jika user sedang main kuis lalu pindah menu, batalkan game (agar skor tidak curang)
        if (quizCtrl != null) {
            quizCtrl.abortGame();
        }
        contentLayout.show(contentPanel, viewName);
        
        // Tutup sidebar otomatis setelah memilih menu
        if (isSidebarVisible) toggleSidebar();
    }

    // Helper: Buka/Tutup Sidebar
    private static void toggleSidebar() {
        isSidebarVisible = !isSidebarVisible;
        sidebar.setVisible(isSidebarVisible);
        mainWrapper.revalidate();
        mainWrapper.repaint();
    }

    // Helper: Buat tombol sidebar dengan style konsisten
    private static void addSidebarBtn(JPanel panel, String text, Runnable action) {
        JButton btn = new JButton(text);
        UIStyle.applySidebarButton(btn);
        btn.addActionListener(e -> action.run());
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}