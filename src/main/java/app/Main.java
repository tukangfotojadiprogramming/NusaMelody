package main.java.app;

import java.awt.*;
import javax.swing.*;

import main.java.app.db.DB;
import main.java.app.view.LoginPanel;
import main.java.app.view.SignupPanel;
import main.java.app.view.QuizPage;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) {}

            // Init DB
            DB.initIfNeeded();

            JFrame frame = new JFrame("Kuis Lirik Nusantara");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 780);
            frame.setLocationRelativeTo(null);

            SignupPanel signupPanel = new SignupPanel();
            LoginPanel loginPage = new LoginPanel();

            // ================= CENTER LOGIN CONTAINER ==================
            JPanel centerContainer = new JPanel(new GridBagLayout());
            centerContainer.setBackground(loginPage.getBackground());

            GridBagConstraints gbcCenter = new GridBagConstraints();
            gbcCenter.gridx = 0; 
            gbcCenter.gridy = 0;
            gbcCenter.anchor = GridBagConstraints.CENTER;
            gbcCenter.weightx = 1.0; 
            gbcCenter.weighty = 1.0;

            centerContainer.add(loginPage, gbcCenter);

            frame.setContentPane(centerContainer);
            frame.validate();
            frame.repaint();

            // ================= HEADER ==================
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
            headerPanel.setBackground(new Color(244, 238, 234));

            JLabel title = new JLabel("Kuis Lirik Nusantara");
            title.setFont(new Font("SansSerif", Font.BOLD, 18));
            title.setForeground(new Color(45, 40, 37));
            headerPanel.add(title, BorderLayout.WEST);

            JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            headerRight.setOpaque(false);
            JLabel lblUser = new JLabel("Halo, guest");
            JButton btnLogout = new JButton("Keluar");
            btnLogout.setVisible(false);

            headerRight.add(lblUser);
            headerRight.add(btnLogout);
            headerPanel.add(headerRight, BorderLayout.EAST);

            // ================= SIDEBAR ==================
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(new Color(115, 47, 27));
            sidebar.setPreferredSize(new Dimension(200, 0));
            sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

            JLabel brand = new JLabel("NusaMelody");
            brand.setForeground(Color.WHITE);
            brand.setFont(new Font("Serif", Font.BOLD, 24));
            brand.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(brand);

            // ---- Tombol Mulai Kuis ----
            JButton btnQuiz = new JButton("Mulai Kuis");
            btnQuiz.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnQuiz.setMaximumSize(new Dimension(180, 40));
            btnQuiz.setFocusPainted(false);
            btnQuiz.setBackground(new Color(255, 230, 200));
            btnQuiz.setForeground(Color.BLACK);

            sidebar.add(Box.createVerticalStrut(20));
            sidebar.add(btnQuiz);

            // ================= CONTENT (CardLayout) ==================
            JPanel contentPanel = new JPanel(new CardLayout());

            // Landing page
            JPanel landing = new JPanel();
            landing.add(new JLabel("Landing Page"));
            landing.setBackground(Color.LIGHT_GRAY);
            contentPanel.add(landing, "LANDING");

            // Quiz Page
            QuizPage quizPage = new QuizPage();
            contentPanel.add(quizPage, "QUIZ");

            // ================= MAIN WRAPPER ==================
            JPanel mainWrapper = new JPanel(new BorderLayout());
            mainWrapper.add(headerPanel, BorderLayout.NORTH);
            mainWrapper.add(sidebar, BorderLayout.WEST);
            mainWrapper.add(contentPanel, BorderLayout.CENTER);

            headerPanel.setVisible(false);
            sidebar.setVisible(false);

            // ================= LOGIN CALLBACK ==================
            loginPage.setLoginListener(email -> {
                SwingUtilities.invokeLater(() -> {
                    lblUser.setText("Halo, " + (email == null || email.isEmpty() ? "player" : email));
                    btnLogout.setVisible(true);
                    headerPanel.setVisible(true);
                    sidebar.setVisible(true);

                    frame.setContentPane(mainWrapper);
                    frame.revalidate();
                    frame.repaint();
                });
            });

            // ================= SIGNUP DIALOG ==================
            loginPage.getSignupButton().addActionListener(e -> {
                JDialog signupDialog = new JDialog(frame, "Daftar Akun Baru", Dialog.ModalityType.APPLICATION_MODAL);
                signupDialog.setContentPane(signupPanel);
                signupDialog.setBounds(frame.getBounds());

                signupPanel.setSignupListener(email -> {
                    signupDialog.dispose();
                    frame.setContentPane(centerContainer);
                    frame.revalidate();
                    frame.repaint();

                    JOptionPane.showMessageDialog(frame,
                            "Akun terdaftar: " + email + ". Silakan login.",
                            "Info", JOptionPane.INFORMATION_MESSAGE);
                });

                signupDialog.setLocationRelativeTo(frame);
                signupDialog.setVisible(true);
            });

            signupPanel.getBackToLoginButton().addActionListener(ev -> {
                Window w = SwingUtilities.getWindowAncestor(signupPanel);
                if (w instanceof JDialog) ((JDialog) w).dispose();
            });

            // ================= LOGOUT ==================
            btnLogout.addActionListener(e -> {
                headerPanel.setVisible(false);
                sidebar.setVisible(false);
                btnLogout.setVisible(false);
                lblUser.setText("Halo, guest");

                frame.setContentPane(centerContainer);
                frame.revalidate();
                frame.repaint();
            });

            // ================= EVENT: MULAI KUIS ==================
            btnQuiz.addActionListener(e -> {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, "QUIZ");
                quizPage.startCountdown();    // mulai timer
            });

            frame.setVisible(true);
        });
    }
}
