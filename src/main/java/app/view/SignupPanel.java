package main.java.app.view;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import main.java.app.db.DB;

/**
 * SignupPanel yang diperbarui dengan input Nama Lengkap.
 * Layout dan tema persis seperti LoginPanel, disesuaikan untuk 4 input.
 * Catatan: setSignupListener(Object) yang salah telah dihapus untuk memperbaiki kompatibilitas Lambda.
 */
public class SignupPanel extends JPanel {

    public interface SignupListener {
        void onSignupSuccess(String email);
        // Menghapus onGoToLogin, karena Main menggunakan getBackToLoginButton() secara langsung.
    }

    private JTextField txtFullName; // Tambahan: Input Nama Lengkap
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnSignup;
    private JButton btnLoginLink; // Tombol proxy untuk link "Sudah punya akun? Masuk"
    private JButton btnShowPass;
    private JButton btnShowConfirmPass;
    private SignupListener signupListener;

    // Warna dan Konstanta (sama dengan LoginPanel)
    private static final Color BG_OUTER = new Color(244, 238, 234);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(156, 41, 21);
    private static final Color PRIMARY_HOVER = new Color(135, 36, 19);
    private static final Color INPUT_BORDER = new Color(200, 200, 200);
    private static final Dimension INPUT_SIZE = new Dimension(360, 40);

    // Placeholder strings
    private static final String PH_NAME = "Nama lengkap Anda";
    private static final String PH_EMAIL = "anda@contoh.com";
    private static final String PH_PASS = "Masukkan kata sandi";
    private static final String PH_CONFIRM_PASS = "Ulangi kata sandi";

    public SignupPanel() {
        setLayout(new GridBagLayout());
        setBackground(BG_OUTER);
        setOpaque(true);

        // Card: Disesuaikan untuk 4 input field
        RoundedPanel card = new RoundedPanel(14);
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(440, 600));
        card.setMaximumSize(new Dimension(440, 600));
        card.setBorder(BorderFactory.createEmptyBorder(16, 26, 16, 26));
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;

        // Header (sama)
        // Row 0: icon, Row 1: title, Row 2: subtitle
        JLabel icon = new JLabel("\u266B");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 30));
        icon.setForeground(PRIMARY);
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0; c.insets = new Insets(6, 0, 6, 0); card.add(icon, c);

        JLabel lblTitle = new JLabel("Nusa Melody (Melodi Nusantara)");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(45, 40, 37));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 1; c.insets = new Insets(2, 0, 4, 0); card.add(lblTitle, c);

        JLabel lblSub = new JLabel("Buat akun baru Anda");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(125, 112, 103));
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 2; c.insets = new Insets(0, 8, 12, 8); card.add(lblSub, c);

        // --- FORM FIELDS ---
        int currentGridY = 3;

        // Nama
        JLabel lblName = new JLabel("Nama Lengkap");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblName.setHorizontalAlignment(SwingConstants.LEFT);
        lblName.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 4, 0); card.add(lblName, c);

        txtFullName = new JTextField();
        txtFullName.setPreferredSize(INPUT_SIZE);
        txtFullName.setMaximumSize(INPUT_SIZE);
        txtFullName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtFullName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        setPlaceholder(txtFullName, PH_NAME);
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 8, 0); card.add(txtFullName, c);

        // Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 4, 0); card.add(lblEmail, c);

        txtEmail = new JTextField();
        txtEmail.setPreferredSize(INPUT_SIZE);
        txtEmail.setMaximumSize(INPUT_SIZE);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        setPlaceholder(txtEmail, PH_EMAIL);
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 8, 0); card.add(txtEmail, c);
        
        // Password
        JLabel lblPass = new JLabel("Kata Sandi");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setHorizontalAlignment(SwingConstants.LEFT);
        lblPass.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 4, 0); card.add(lblPass, c);

        JPanel passWrapper = createPasswordFieldWrapper(txtPassword = new JPasswordField(), btnShowPass = new JButton("👁"), PH_PASS);
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 8, 0); card.add(passWrapper, c);

        // Confirm Password
        JLabel lblConfirmPass = new JLabel("Konfirmasi Kata Sandi");
        lblConfirmPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblConfirmPass.setHorizontalAlignment(SwingConstants.LEFT);
        lblConfirmPass.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 4, 0); card.add(lblConfirmPass, c);

        JPanel confirmPassWrapper = createPasswordFieldWrapper(txtConfirmPassword = new JPasswordField(), btnShowConfirmPass = new JButton("👁"), PH_CONFIRM_PASS);
        c.gridy = currentGridY++; c.insets = new Insets(2, 0, 10, 0); card.add(confirmPassWrapper, c);


        // Signup Button
        btnSignup = new JButton("Daftar");
        btnSignup.setPreferredSize(new Dimension(360, 44));
        btnSignup.setMaximumSize(new Dimension(360, 44));
        btnSignup.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnSignup.setForeground(Color.WHITE);
        btnSignup.setBackground(PRIMARY);
        btnSignup.setOpaque(true);
        btnSignup.setBorderPainted(false);
        btnSignup.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSignup.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnSignup.setBackground(PRIMARY_HOVER); }
            @Override public void mouseExited(MouseEvent e) { btnSignup.setBackground(PRIMARY); }
        });
        c.gridy = currentGridY++; c.insets = new Insets(6, 0, 6, 0); card.add(btnSignup, c);

        // Go to Login Link
        JLabel alreadyAcct = new JLabel("<html><span style='font-size:12px'>Sudah punya akun? <a href='#'>Masuk</a></span></html>");
        alreadyAcct.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.gridy = currentGridY++; c.insets = new Insets(4, 0, 4, 0); card.add(alreadyAcct, c);

        // Disclaimer (diletakkan di luar card)
        JLabel disclaimer = new JLabel("<html><div style='text-align:center;font-size:11px;color:#7d7067'>Dengan mendaftar, Anda menyetujui Syarat dan Kebijakan Privasi kami</div></html>");
        disclaimer.setHorizontalAlignment(SwingConstants.CENTER);

        // Tambahkan card dan disclaimer ke panel utama.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; gbc.weighty = 0.8;
        add(card, gbc);

        gbc.gridy = 1; gbc.weighty = 0.0; gbc.insets = new Insets(10, 0, 8, 0);
        add(disclaimer, gbc);

        // invisible login proxy button
        btnLoginLink = new JButton();
        btnLoginLink.setVisible(false);
        alreadyAcct.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (btnLoginLink != null) btnLoginLink.doClick(); // Memicu tombol proxy yang didengarkan oleh Main
            }
        });
        
        // --- attach DB signup behavior here
        btnSignup.addActionListener(e -> attemptSignup());
    }

    private JPanel createPasswordFieldWrapper(JPasswordField field, JButton button, String placeholder) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setPreferredSize(INPUT_SIZE);
        wrapper.setMaximumSize(INPUT_SIZE);
        wrapper.setBorder(BorderFactory.createLineBorder(INPUT_BORDER, 1));
        wrapper.setBackground(CARD_BG);

        field.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        setPlaceholder(field, button, placeholder);
        wrapper.add(field, BorderLayout.CENTER);

        button.setPreferredSize(new Dimension(40, 34));
        button.setFocusable(false);
        button.setBorder(null);
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(ev -> togglePasswordVisibility(field, button, placeholder));
        wrapper.add(button, BorderLayout.EAST);
        return wrapper;
    }

    private void togglePasswordVisibility(JPasswordField field, JButton button, String placeholder) {
        String current = String.valueOf(field.getPassword());
        if (current.equals(placeholder)) return;
        if (field.getEchoChar() == (char)0) {
            field.setEchoChar('\u2022');
            button.setText("👁");
        } else {
            field.setEchoChar((char)0);
            button.setText("🙈");
        }
    }

    // Logic Pendaftaran via DB. Termasuk Nama.
    private void attemptSignup() {
        final String name = getFullName(); 
        final String email = getEmail();
        final String pass = getPassword();
        final String confirmPass = getConfirmPassword();

        // Validasi
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua bidang wajib diisi.", "Pendaftaran", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
             JOptionPane.showMessageDialog(this, "Format email tidak valid.", "Pendaftaran", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Kata sandi dan konfirmasi kata sandi tidak cocok.", "Pendaftaran", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (pass.length() < 6) {
             JOptionPane.showMessageDialog(this, "Kata sandi harus minimal 6 karakter.", "Pendaftaran", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnSignup.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            private Exception ex = null;
            private boolean isEmailUsed = false;

            @Override
            protected Boolean doInBackground() {
                try (Connection conn = DB.getConnection()) {
                    // 1. Cek duplikasi email
                    String checkSql = "SELECT email FROM users WHERE email = ?";
                    try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                        psCheck.setString(1, email.toLowerCase().trim());
                        try (ResultSet rs = psCheck.executeQuery()) {
                            if (rs.next()) {
                                isEmailUsed = true;
                                return false;
                            }
                        }
                    }
                    
                    // 2. Insert user baru dengan nama
                    String insertSql = "INSERT INTO users (name, email, password) VALUES (?, ?, ?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                        psInsert.setString(1, name);
                        psInsert.setString(2, email.toLowerCase().trim());
                        psInsert.setString(3, pass); // Plain-text
                        return psInsert.executeUpdate() > 0;
                    }

                } catch (Exception e) {
                    ex = e;
                    return false;
                }
            }

            @Override
            protected void done() {
                btnLoginLink.setEnabled(true);
                btnSignup.setEnabled(true);
                try {
                    boolean ok = get();
                    if (ok) {
                        if (signupListener != null) signupListener.onSignupSuccess(email);
                    } else if (isEmailUsed) {
                        JOptionPane.showMessageDialog(SignupPanel.this, "Email ini sudah terdaftar.", "Pendaftaran Gagal", JOptionPane.ERROR_MESSAGE);
                    } else {
                         if (ex != null) {
                            JOptionPane.showMessageDialog(SignupPanel.this, "Kesalahan koneksi/DB: " + ex.getMessage(), "Pendaftaran Gagal", JOptionPane.ERROR_MESSAGE);
                         } else {
                            JOptionPane.showMessageDialog(SignupPanel.this, "Pendaftaran gagal, coba lagi.", "Pendaftaran Gagal", JOptionPane.ERROR_MESSAGE);
                         }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(SignupPanel.this, "Terjadi kesalahan: " + e.getMessage(), "Pendaftaran Gagal", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // Placeholder helpers (sama)
    private void setPlaceholder(final JTextField field, final String placeholder) {
        field.setText(placeholder);
        field.setForeground(new Color(140, 133, 128));
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (field.getText().trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(140, 133, 128));
                }
            }
        });
    }

    private void setPlaceholder(final JPasswordField field, final JButton button, final String placeholder) {
        field.setText(placeholder);
        field.setForeground(new Color(140, 133, 128));
        field.setEchoChar((char)0);
        button.setText("👁"); 
        field.addFocusListener(new FocusAdapter() {
            private boolean showing = true;
            @Override public void focusGained(FocusEvent e) {
                if (showing && String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('\u2022');
                    showing = false;
                    button.setText("👁");
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(140, 133, 128));
                    field.setEchoChar((char)0);
                    showing = true;
                    button.setText("👁");
                }
            }
        });
    }

    // External accessors
    public String getFullName() {
        String t = txtFullName.getText();
        if (t == null || t.trim().equals(PH_NAME)) return "";
        return t.trim();
    }
    public String getEmail() {
        String t = txtEmail.getText();
        if (t == null || t.trim().equals(PH_EMAIL)) return "";
        return t.trim();
    }
    public String getPassword() {
        String p = String.valueOf(txtPassword.getPassword());
        if (p == null || p.equals(PH_PASS)) return "";
        return p;
    }
    public String getConfirmPassword() {
        String p = String.valueOf(txtConfirmPassword.getPassword());
        if (p == null || p.equals(PH_CONFIRM_PASS)) return "";
        return p;
    }

    /**
     * Metode yang didengarkan oleh Main untuk event pendaftaran sukses.
     */
    public void setSignupListener(SignupListener l) { this.signupListener = l; }
    
    /**
     * Mengembalikan tombol proxy untuk tautan "Sudah punya akun? Masuk".
     * Digunakan oleh Main untuk menambahkan ActionListener kembali ke Login.
     */
    public JButton getBackToLoginButton() { return btnLoginLink; }

    // RoundedPanel (sama)
    private static class RoundedPanel extends JPanel {
        private int radius;
        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }
}