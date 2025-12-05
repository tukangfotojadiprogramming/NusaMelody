package main.java.app.controller;

import java.awt.CardLayout;
import javax.swing.JPanel;
import main.java.app.view.LandingPage;

public class LandingController {
    
    public LandingController(LandingPage view, JPanel mainContainer, CardLayout cardLayout) {
        view.setNavAction("PROVINCE", () -> cardLayout.show(mainContainer, "PROVINCE"));

        view.setNavAction("CATALOG", () -> cardLayout.show(mainContainer, "CATALOG"));

        view.setNavAction("QUIZ", () -> cardLayout.show(mainContainer, "QUIZ"));
    }
}