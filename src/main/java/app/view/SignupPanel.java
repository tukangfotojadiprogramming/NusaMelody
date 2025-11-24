package main.java.app.view;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;
import main.java.app.db.DB;

public class SignupPanel extends JPanel {

    public interface SignupListener { void onSignupSuccess(String email); }

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnSignup;
    private JButton btnBackToLogin;
    private JButton btnShowPass;
    private JButton btnShowConfirmPass;
    private SignupListener listener;

    private static final Color BG_OUTER = new Color(244, 238, 234);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color PRIMARY = new Color(156, 41, 21);
    private static final Color PRIMARY_HOVER = new Color(135, 36, 19);
    private static final Color INPUT_BORDER = new Color(200, 200, 200);
    private static final Dimension INPUT_SIZE = new Dimension(360, 44);

    public SignupPanel() {
        setLayout(new GridBagLayout());
        setBackground(BG_OUTER);
        setOpaque(true);

        // Rounded card with GridBagLayout to match LoginPanel structure
        RoundedPanel card = new RoundedPanel(14);
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(440, 620));
        card.setMaximumSize(new Dimension(440, 620));
        card.setBorder(BorderFactory.createEmptyBorder(16, 26, 16, 26));
        card.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;

        // Row 0: icon
        JLabel icon = new JLabel("\u266B");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 30));
        icon.setForeground(PRIMARY);
        icon.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 0;
        c.insets = new Insets(6, 0, 6, 0);
        card.add(icon, c);

        // Row 1: title
        JLabel lblTitle = new JLabel("Daftar Akun Baru");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblTitle.setForeground(new Color(45, 40, 37));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 1;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblTitle, c);

        // Row 2: subtitle
        JLabel lblSub = new JLabel("Buat akun untuk mulai bermain Kuis Lirik Nusantara");
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblSub.setForeground(new Color(125, 112, 103));
        lblSub.setHorizontalAlignment(SwingConstants.CENTER);
        c.gridy = 2;
        c.insets = new Insets(0, 8, 12, 8);
        card.add(lblSub, c);

        // Row 3: label Name
        JLabel lblName = new JLabel("Nama Lengkap");
        lblName.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblName.setHorizontalAlignment(SwingConstants.LEFT);
        lblName.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = 3;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblName, c);

        // Row 4: Name field
        txtName = new JTextField();
        txtName.setPreferredSize(INPUT_SIZE);
        txtName.setMaximumSize(INPUT_SIZE);
        txtName.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtName.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        setPlaceholder(txtName, "Masukkan nama lengkap");
        c.gridy = 4;
        c.insets = new Insets(2, 0, 8, 0);
        card.add(txtName, c);

        // Row 5: label Email
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblEmail.setHorizontalAlignment(SwingConstants.LEFT);
        lblEmail.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = 5;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblEmail, c);

        // Row 6: Email field
        txtEmail = new JTextField();
        txtEmail.setPreferredSize(INPUT_SIZE);
        txtEmail.setMaximumSize(INPUT_SIZE);
        txtEmail.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        setPlaceholder(txtEmail, "anda@contoh.com");
        c.gridy = 6;
        c.insets = new Insets(2, 0, 8, 0);
        card.add(txtEmail, c);

        // Row 7: label Password
        JLabel lblPass = new JLabel("Kata Sandi");
        lblPass.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblPass.setHorizontalAlignment(SwingConstants.LEFT);
        lblPass.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = 7;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblPass, c);

        // Row 8: password wrapper + eye
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
        btnShowPass.addActionListener(ev -> togglePasswordVisibility(txtPassword, btnShowPass));
        passWrapper.add(btnShowPass, BorderLayout.EAST);

        c.gridy = 8;
        c.insets = new Insets(2, 0, 10, 0);
        card.add(passWrapper, c);

        // Row 9: label Confirm Password
        JLabel lblConfirm = new JLabel("Konfirmasi Kata Sandi");
        lblConfirm.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblConfirm.setHorizontalAlignment(SwingConstants.LEFT);
        lblConfirm.setPreferredSize(new Dimension(INPUT_SIZE.width, 18));
        c.gridy = 9;
        c.insets = new Insets(2, 0, 4, 0);
        card.add(lblConfirm, c);

        // Row 10: confirm pass wrapper + eye
        JPanel confirmWrapper = new JPanel(new BorderLayout());
        confirmWrapper.setPreferredSize(INPUT_SIZE);
        confirmWrapper.setMaximumSize(INPUT_SIZE);
        confirmWrapper.setBorder(BorderFactory.createLineBorder(INPUT_BORDER, 1));
        confirmWrapper.setBackground(CARD_BG);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        txtConfirmPassword.setFont(new Font("SansSerif", Font.PLAIN, 14));
        setPlaceholder(txtConfirmPassword, "Konfirmasi kata sandi");
        confirmWrapper.add(txtConfirmPassword, BorderLayout.CENTER);

        btnShowConfirmPass = new JButton("👁");
        btnShowConfirmPass.setPreferredSize(new Dimension(40, 34));
        btnShowConfirmPass.setFocusable(false);
        btnShowConfirmPass.setBorder(null);
        btnShowConfirmPass.setContentAreaFilled(false);
        btnShowConfirmPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnShowConfirmPass.addActionListener(ev -> togglePasswordVisibility(txtConfirmPassword, btnShowConfirmPass));
        confirmWrapper.add(btnShowConfirmPass, BorderLayout.EAST);

        c.gridy = 10;
        c.insets = new Insets(2, 0, 12, 0);
        card.add(confirmWrapper, c);

        // Row 11: Signup button
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
        c.gridy = 11;
        c.insets = new Insets(6, 0, 6, 0);
        card.add(btnSignup, c);

        // Row 12: back to login link
        JLabel backToLogin = new JLabel("<html><span style='font-size:12px'>Sudah punya akun? <a href='#'>Masuk di sini</a></span></html>");
        backToLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        c.gridy = 12;
        c.insets = new Insets(6, 0, 6, 0);
        card.add(backToLogin, c);

        // Disclaimer outside card
        JLabel disclaimer = new JLabel("<html><div style='text-align:center;font-size:11px;color:#7d7067'>Dengan mendaftar, Anda menyetujui Syarat dan Kebijakan Privasi kami</div></html>");
        disclaimer.setHorizontalAlignment(SwingConstants.CENTER);

        // Add card + disclaimer to main panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0; gbc.weighty = 0.9;
        add(card, gbc);

        gbc.gridy = 1; gbc.weighty = 0.0; gbc.insets = new Insets(10, 0, 8, 0);
        add(disclaimer, gbc);

        // invisible back to login proxy
        btnBackToLogin = new JButton();
        btnBackToLogin.setVisible(false);
        backToLogin.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (btnBackToLogin != null) btnBackToLogin.doClick();
            }
        });

        // action: signup
        btnSignup.addActionListener(e -> attemptSignup());
    }

    public void setSignupListener(SignupListener l) { this.listener = l; }

    private void attemptSignup() {
        final String name = getName();
        final String email = getEmail();
        final String pass = getPassword();
        final String confirm = getConfirmPassword();

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi", "Daftar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Konfirmasi password tidak cocok", "Daftar", JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnSignup.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            private String error = null;
            @Override protected Boolean doInBackground() throws Exception {
                try (Connection c = DB.getConnection()) {
                    String sql = "INSERT INTO users (name, email, password, points, badges) VALUES (?, ?, ?, 0, ?)";
                    try (PreparedStatement ps = c.prepareStatement(sql)) {
                        ps.setString(1, name);
                        ps.setString(2, email.toLowerCase().trim());
                        ps.setString(3, pass); // plain password stored per request
                        ps.setString(4, "Newbie");
                        ps.executeUpdate();
                        return true;
                    }
                } catch (Exception ex) {
                    error = ex.getMessage();
                    return false;
                }
            }
            @Override protected void done() {
                btnSignup.setEnabled(true);
                try {
                    boolean ok = get();
                    if (ok) {
                        JOptionPane.showMessageDialog(SignupPanel.this, "Pendaftaran berhasil. Silakan login.", "Daftar", JOptionPane.INFORMATION_MESSAGE);
                        if (listener != null) listener.onSignupSuccess(email);
                    } else {
                        if (error != null && (error.toLowerCase().contains("duplicate") || error.toLowerCase().contains("unique"))) {
                            JOptionPane.showMessageDialog(SignupPanel.this, "Email sudah terdaftar.", "Daftar", JOptionPane.WARNING_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(SignupPanel.this, "Gagal mendaftar: " + error, "Daftar", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(SignupPanel.this, "Terjadi kesalahan: " + ex.getMessage(), "Daftar", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }

    private void togglePasswordVisibility(JPasswordField field, JButton btn) {
        String current = String.valueOf(field.getPassword());
        if (current.equals("Masukkan kata sandi") || current.equals("Konfirmasi kata sandi")) return;
        if (field.getEchoChar() == (char)0) {
            field.setEchoChar('\u2022');
            btn.setText("👁");
        } else {
            field.setEchoChar((char)0);
            btn.setText("🙈");
        }
    }

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
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).trim().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(140, 133, 128));
                    field.setEchoChar((char)0);
                    showing = true;
                }
            }
        });
    }

    public JButton getSignupButton() { return btnSignup; }
    public JButton getBackToLoginButton() { return btnBackToLogin; }
    public String getName() {
        String t = txtName.getText();
        if (t == null || t.trim().equals("Masukkan nama lengkap")) return "";
        return t.trim();
    }
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
    public String getConfirmPassword() {
        String p = String.valueOf(txtConfirmPassword.getPassword());
        if (p == null || p.equals("Konfirmasi kata sandi")) return "";
        return p;
    }

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
