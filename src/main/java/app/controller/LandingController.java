package main.java.app.controller;

import java.awt.CardLayout;
import javax.swing.JPanel;
import main.java.app.view.LandingPage;

public class LandingController {
    
    public LandingController(LandingPage view, JPanel mainContainer, CardLayout cardLayout) {
        // Method 'setStartAction' sudah tidak ada karena Landing Page sekarang punya banyak tombol.
        // Kita gunakan 'setNavAction' untuk mendaftarkan fungsi setiap tombol menu.

        // 1. Tombol "Jelajah Peta" -> Buka Halaman Province
        view.setNavAction("PROVINCE", () -> cardLayout.show(mainContainer, "PROVINCE"));

        // 2. Tombol "Katalog Lengkap" -> Buka Halaman Catalog
        view.setNavAction("CATALOG", () -> cardLayout.show(mainContainer, "CATALOG"));

        // 3. Tombol "Kuis Interaktif" -> Buka Halaman Quiz
        view.setNavAction("QUIZ", () -> cardLayout.show(mainContainer, "QUIZ"));
    }
}