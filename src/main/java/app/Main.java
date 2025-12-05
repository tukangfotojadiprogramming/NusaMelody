package main.java.app;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import main.java.app.controller.*;
import main.java.app.service.DatabaseService;
import main.java.app.service.LeaderboardRepository;
import main.java.app.service.SongRepository;
import main.java.app.util.UIStyle;
import main.java.app.view.*;

public class Main {
    private static JFrame frame;
    private static JPanel mainWrapper;
    private static JPanel sidebar;
    private static JPanel loginContainer; // Sekarang akan menggunakan CardLayout
    private static CardLayout loginCardLayout; // CardLayout untuk loginContainer
    private static CardLayout contentLayout;
    private static JPanel contentPanel;
    private static boolean isSidebarVisible = false;

    private static QuizController quizCtrl;
    private static SongController songCtrl;

    // Nama kartu untuk CardLayout Login
    private static final String LOGIN_VIEW = "LOGIN";
    private static final String SIGNUP_VIEW = "SIGNUP";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}

            DatabaseService.initIfNeeded();

            frame = new JFrame("NusaMelody - Jelajah Musik Nusantara");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 800);
            frame.setLocationRelativeTo(null);

            LoginPanel loginPage = new LoginPanel();
            SignupPanel signupPanel = new SignupPanel();
            
            // --- MODIFIKASI UTAMA DI SINI: MENGGANTI JDialog DENGAN CardLayout ---
            loginCardLayout = new CardLayout();
            // Container Login sekarang menggunakan CardLayout
            loginContainer = new JPanel(loginCardLayout) {
                // Background hanya diterapkan pada container, bukan pada panel di dalamnya
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(UIStyle.COLOR_BG); 
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            
            // Tambahkan kedua panel ke loginContainer
            loginContainer.add(loginPage, LOGIN_VIEW);
            loginContainer.add(signupPanel, SIGNUP_VIEW);
            
            // Login Success
            loginPage.setLoginListener(email -> {
                initMainApp(email); 
                frame.setContentPane(mainWrapper);
                frame.revalidate();
                frame.repaint();
            });

            // 1. NAVIGASI DARI LOGIN KE SIGNUP
            loginPage.getSignupButton().addActionListener(e -> {
                loginCardLayout.show(loginContainer, SIGNUP_VIEW);
                frame.revalidate();
            });

            // 2. NAVIGASI DARI SIGNUP KEMBALI KE LOGIN
            signupPanel.getBackToLoginButton().addActionListener(e -> {
                loginCardLayout.show(loginContainer, LOGIN_VIEW);
                frame.revalidate();
            });

            // 3. SIGNUP SUKSES
            signupPanel.setSignupListener((String mail) -> {
                JOptionPane.showMessageDialog(frame, "Akun berhasil dibuat! Silakan login.");
                loginCardLayout.show(loginContainer, LOGIN_VIEW); // Kembali ke tampilan login
                frame.revalidate();
            });
            // ---------------------------------------------------------------------

            frame.setContentPane(loginContainer);
            frame.setVisible(true);
        });
    }
    
    // --- Bagian initMainApp tidak berubah ---
    private static void initMainApp(String userEmail) {
        String userName = userEmail.split("@")[0];
        
        mainWrapper = new JPanel(new BorderLayout());

        // HEADER
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIStyle.COLOR_PRIMARY);
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel lblBrand = new JLabel("NusaMelody");
        lblBrand.setFont(new Font("Serif", Font.BOLD, 28));
        lblBrand.setForeground(UIStyle.COLOR_ACCENT);

        JButton btnHamburger = new JButton("☰"); 
        btnHamburger.setFont(new Font("SansSerif", Font.BOLD, 28));
        btnHamburger.setForeground(Color.WHITE);
        btnHamburger.setBackground(UIStyle.COLOR_PRIMARY);
        btnHamburger.setBorderPainted(false);
        btnHamburger.setFocusPainted(false);
        btnHamburger.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHamburger.addActionListener(e -> toggleSidebar());

        header.add(lblBrand, BorderLayout.WEST);
        header.add(btnHamburger, BorderLayout.EAST);

        // SIDEBAR
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyle.COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));
        sidebar.setVisible(false); 

        JLabel lblUser = new JLabel("Halo, " + userName);
        lblUser.setForeground(UIStyle.COLOR_ACCENT);
        lblUser.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblUser.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(lblUser);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        // CONTENT
        contentLayout = new CardLayout();
        contentPanel = new JPanel(contentLayout);
        
        // ... (inisialisasi Controller dan Repositori)
        SongRepository songRepo = new SongRepository();
        LeaderboardRepository lbRepo = new LeaderboardRepository();

        LandingPage landingPage = new LandingPage();
        ProvinceSelectionPage provincePage = new ProvinceSelectionPage();
        CatalogPage catalogPage = new CatalogPage();
        SongDetailPage songDetailPage = new SongDetailPage();
        QuizPage quizPage = new QuizPage();
        LeaderboardPage lbPage = new LeaderboardPage();

        quizPage.setLeaderboardView(lbPage);

        contentPanel.add(landingPage, "LANDING");
        contentPanel.add(provincePage, "PROVINCE");
        contentPanel.add(catalogPage, "CATALOG");
        contentPanel.add(songDetailPage, "DETAIL");
        contentPanel.add(quizPage, "QUIZ");

        songCtrl = new SongController(songDetailPage, contentPanel, contentLayout);
        CatalogController catCtrl = new CatalogController(catalogPage, songCtrl);
        new LandingController(landingPage, contentPanel, contentLayout);
        quizCtrl = new QuizController(quizPage, lbPage, songRepo, lbRepo);
        quizCtrl.setUser(userName, userEmail);

        // --- NAVIGASI ---
        
        provincePage.getBtnBack().addActionListener(e -> switchView("LANDING"));
        quizPage.getBtnBack().addActionListener(e -> switchView("LANDING"));
        catCtrl.setBackAction(() -> switchView("LANDING"));

        landingPage.setNavAction("PROVINCE", () -> switchView("PROVINCE"));
        landingPage.setNavAction("CATALOG", () -> {
            catCtrl.loadAllSorted();
            catCtrl.setBackAction(() -> switchView("LANDING")); 
            switchView("CATALOG");
        });
        landingPage.setNavAction("QUIZ", () -> switchView("QUIZ"));

        provincePage.setSelectionListener(e -> {
            JButton sourceBtn = (JButton) e.getSource();
            String provName = (String) sourceBtn.getClientProperty("provinceName");
            
            if (provName != null) {
                catCtrl.filterByProvince(provName);
                catCtrl.setBackAction(() -> switchView("PROVINCE")); 
                switchView("CATALOG");
            }
        });
        
        addSidebarBtn(sidebar, "🏠 Beranda", () -> switchView("LANDING"));
        addSidebarBtn(sidebar, "🗺️ Jelajah Peta", () -> switchView("PROVINCE"));
        addSidebarBtn(sidebar, "🎶 Semua Lagu", () -> {
            catCtrl.loadAllSorted();
            catCtrl.setBackAction(() -> switchView("LANDING"));
            switchView("CATALOG");
        });
        addSidebarBtn(sidebar, "❓ Kuis Nusantara", () -> switchView("QUIZ"));
        
        sidebar.add(Box.createVerticalGlue());
        JButton btnLogout = new JButton("🚪 Keluar");
        UIStyle.applySidebarButton(btnLogout);
        btnLogout.setForeground(new Color(255, 100, 100));
        btnLogout.addActionListener(e -> {
            if(quizCtrl != null) quizCtrl.abortGame();
            if(songCtrl != null) songCtrl.stopAudio();
            
            frame.setContentPane(loginContainer);
            frame.revalidate();
            
            isSidebarVisible = false;
            sidebar.setVisible(false);
        });
        sidebar.add(btnLogout);

        mainWrapper.add(header, BorderLayout.NORTH);
        mainWrapper.add(sidebar, BorderLayout.EAST);
        mainWrapper.add(contentPanel, BorderLayout.CENTER);
    }

    private static void switchView(String viewName) {
        if (quizCtrl != null) quizCtrl.abortGame();
        contentLayout.show(contentPanel, viewName);
        if (isSidebarVisible) toggleSidebar();
    }

    private static void toggleSidebar() {
        isSidebarVisible = !isSidebarVisible;
        sidebar.setVisible(isSidebarVisible);
        mainWrapper.revalidate();
        mainWrapper.repaint();
    }

    private static void addSidebarBtn(JPanel panel, String text, Runnable action) {
        JButton btn = new JButton(text);
        UIStyle.applySidebarButton(btn);
        btn.addActionListener(e -> action.run());
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
    }
}