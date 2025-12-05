package main.java.app.view;

import java.awt.*; // <-- pastikan DB.java ada dan dikonfigurasi
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import main.java.app.db.DB;

/**
 * LoginPanel (layout preserved exactly seperti versi yang Anda kirim).
 * Tambahan: koneksi DB untuk validasi credential (plain-text compare).
 * Tambahkan listener via setLoginListener(...) untuk menerima event login sukses.
 */
public class LoginPanel extends JPanel {

    public interface LoginListener {
        void onLoginSuccess(String email);
    }

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSignup;
    private JButton btnShowPass;
    private LoginListener loginListener;

    // Warna dan Konstanta
    private static final Color BG_OUTER = new Color(244, 238, 234);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(156, 41, 21);
    private static final Color PRIMARY_HOVER = new Color(135, 36, 19);
    private static final Color INPUT_BORDER = new Color(200, 200, 200);
    private static final Dimension INPUT_SIZE = new Dimension(360, 40);

    public LoginPanel() {
        setLayout(new GridBagLayout());
        setBackground(BG_OUTER);
        setOpaque(true);

        // Card: GridBagLayout untuk kontrol yang presisi
        RoundedPanel card = new RoundedPanel(14);
        card.setBackground(CARD_BG);
        // Sedikit lebih luas secara horizontal, tinggi moderat agar footer tidak terlalu jauh
        card.setPreferredSize(new Dimension(440, 460));
        card.setMaximumSize(new Dimension(440, 460));
        card.setBorder(BorderFactory.createEmptyBorder(16, 26, 16, 26));
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;

        // Row 0: icon (centered)
        JLabel icon = new JLabel("\u266B");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 30));
        icon.setForeground(PRIMARY);
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0;
        c.insets = new Insets(6, 0, 6, 0); 
        card.add(icon, c);

        // Row 1: title
        JLabel lblTitle = new JLabel("Nusa Melody(Melodi Nusantara)");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(45, 40, 37));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 1;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblTitle, c);

        // Row 2: subtitle
        JLabel lblSub = new JLabel("Masukkan kredensial Anda untuk mulai bermain");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(125, 112, 103));
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 2;
        c.insets = new Insets(0, 8, 12, 8); // sedikit ruang sebelum form
        card.add(lblSub, c);

        // Row 3: label Email (left aligned visually but centered cell)
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = 3;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblEmail, c);

        // Row 4: Email field
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(INPUT_SIZE);
        txtEmail.setMaximumSize(INPUT_SIZE);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        setPlaceholder(txtEmail, "anda@contoh.com");
        c.gridy = 4;
        c.insets = new Insets(2, 0, 8, 0); // gap sedang ke label password
        card.add(txtEmail, c);

        // Row 5: label Password
        JLabel lblPass = new JLabel("Kata Sandi");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setHorizontalAlignment(SwingConstants.LEFT);
        lblPass.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = 5;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblPass, c);

        // Row 6: password + eye (wrapped)
        JPanel passWrapper = new JPanel(new BorderLayout());
        passWrapper.setPreferredSize(INPUT_SIZE);
        passWrapper.setMaximumSize(INPUT_SIZE);
        passWrapper.setBorder(BorderFactory.createLineBorder(INPUT_BORDER, 1));
        passWrapper.setBackground(CARD_BG);

        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        setPlaceholder(txtPassword, "Masukkan kata sandi");
        passWrapper.add(txtPassword, BorderLayout.CENTER);

        btnShowPass = new JButton("👁");
        btnShowPass.setPreferredSize(new Dimension(40, 34));
        btnShowPass.setFocusable(false);
        btnShowPass.setBorder(null);
        btnShowPass.setContentAreaFilled(false);
        btnShowPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnShowPass.addActionListener(ev -> togglePasswordVisibility());
        passWrapper.add(btnShowPass, BorderLayout.EAST);

        c.gridy = 6;
        c.insets = new Insets(2, 0, 10, 0); // sedikit lebih rapat ke tombol
        card.add(passWrapper, c);

        // Row 7: login button (keluarga input, gap kecil di atas & bawah)
        btnLogin = new JButton("Masuk");
        btnLogin.setPreferredSize(new Dimension(360, 44));
        btnLogin.setMaximumSize(new Dimension(360, 44));
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 15));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(PRIMARY);
        btnLogin.setOpaque(true);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btnLogin.setBackground(PRIMARY_HOVER); }
            @Override public void mouseExited(MouseEvent e) { btnLogin.setBackground(PRIMARY); }
        });
        c.gridy = 7;
        c.insets = new Insets(6, 0, 6, 0); // cukup dekat ke password tapi tidak menempel
        card.add(btnLogin, c);

        // Row 8: signup (kecil)
        JLabel noAcct = new JLabel("<html><span style='font-size:12px'>Belum punya akun? <a href='#'>Daftar sekarang</a></span></html>");
        noAcct.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.gridy = 8;
        c.insets = new Insets(4, 0, 4, 0);
        card.add(noAcct, c);

        // Disclaimer (diletakkan di luar card agar tidak mempengaruhi tinggi card)
        JLabel disclaimer = new JLabel("<html><div style='text-align:center;font-size:11px;color:#7d7067'>Dengan masuk, Anda menyetujui Syarat dan Kebijakan Privasi kami</div></html>");
        disclaimer.setHorizontalAlignment(SwingConstants.CENTER);

        // Tambahkan card dan disclaimer ke panel utama.
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; gbc.weighty = 0.8; // beri sedikit weighty agar card tidak menempel ke atas
        add(card, gbc);

        gbc.gridy = 1; gbc.weighty = 0.0; gbc.insets = new Insets(10, 0, 8, 0);
        add(disclaimer, gbc);

        // invisible signup proxy button
        btnSignup = new JButton();
        btnSignup.setVisible(false);
        noAcct.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (btnSignup != null) btnSignup.doClick();
            }
        });

        // --- attach DB login behavior here (keamanan: SwingWorker agar UI tetap responsif)
        btnLogin.addActionListener(e -> attemptLogin());
    }

    // Login via DB (plain-text compare). Runs in background.
    private void attemptLogin() {
        final String email = getEmail();
        final String pass = getPassword();

        if (email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email dan Password wajib diisi", "Login", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnLogin.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            private Exception ex = null;

            @Override
            protected Boolean doInBackground() {
                try (Connection conn = DB.getConnection()) {
                    String sql = "SELECT password FROM users WHERE email = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, email.toLowerCase().trim());
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                String stored = rs.getString("password");
                                return pass.equals(stored); // plain compare
                            } else {
                                return false;
                            }
                        }
                    }
                } catch (Exception e) {
                    ex = e;
                    return false;
                }
            }

            @Override
            protected void done() {
                btnLogin.setEnabled(true);
                try {
                    boolean ok = get();
                    if (ok) {
                        // inform listener (Main) jika di-set
                        if (loginListener != null) loginListener.onLoginSuccess(email);
                    } else {
                        if (ex != null) {
                            JOptionPane.showMessageDialog(LoginPanel.this, "Kesalahan koneksi: " + ex.getMessage(), "Login", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(LoginPanel.this, "Email atau password salah.", "Login", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(LoginPanel.this, "Terjadi kesalahan: " + e.getMessage(), "Login", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    // Toggle password visibility
    private void togglePasswordVisibility() {
        String current = String.valueOf(txtPassword.getPassword());
        if (current.equals("Masukkan kata sandi")) return;
        if (txtPassword.getEchoChar() == (char)0) {
            txtPassword.setEchoChar('\u2022');
            btnShowPass.setText("👁");
        } else {
            txtPassword.setEchoChar((char)0);
            btnShowPass.setText("🙈");
        }
    }

    // Placeholder helpers
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

    private void setPlaceholder(final JPasswordField field, final String placeholder) {
        field.setText(placeholder);
        field.setForeground(new Color(140, 133, 128));
        field.setEchoChar((char)0);
        field.addFocusListener(new FocusAdapter() {
            private boolean showing = true;
            @Override public void focusGained(FocusEvent e) {
                if (showing && String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('\u2022');
                    showing = false;
                    btnShowPass.setText("👁");
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(140, 133, 128));
                    field.setEchoChar((char)0);
                    showing = true;
                    btnShowPass.setText("👁");
                }
            }
        });
    }

    // External accessors
    public JButton getLoginButton() { return btnLogin; }
    public JButton getSignupButton() { return btnSignup; }
    public String getEmail() {
        String t = txtEmail.getText();
        if (t == null || t.trim().equals("anda@contoh.com")) return "";
        return t.trim();
    }
    public String getPassword() {
        String p = String.valueOf(txtPassword.getPassword());
        if (p == null || p.equals("Masukkan kata sandi")) return "";
        return p;
    }

    public void setLoginListener(LoginListener l) { this.loginListener = l; }

    // RoundedPanel
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
