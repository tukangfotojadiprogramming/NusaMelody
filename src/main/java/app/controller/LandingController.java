package main.java.app.controller;

import java.awt.CardLayout;
import javax.swing.JPanel;
import main.java.app.view.LandingPage;

public class LandingController {
    public LandingController(LandingPage view, JPanel mainContainer, CardLayout cardLayout) {
        // Saat tombol "Mulai" diklik, pindah ke halaman CATALOG
        view.setStartAction(e -> cardLayout.show(mainContainer, "CATALOG"));
    }
}