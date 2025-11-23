package main.java.app.controller;

import main.java.app.model.RegionalSong;
import main.java.app.service.AudioPlayer;
import main.java.app.view.SongDetailPage;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SongController {
    private SongDetailPage view;
    private JPanel mainContainer;
    private CardLayout cardLayout;
    private AudioPlayer audioPlayer;
    private RegionalSong currentSong;
    private Timer uiUpdateTimer; // Timer untuk update slider

    public SongController(SongDetailPage view, JPanel mainContainer, CardLayout cardLayout) {
        this.view = view;
        this.mainContainer = mainContainer;
        this.cardLayout = cardLayout;
        this.audioPlayer = new AudioPlayer();

        // Timer berjalan setiap 500ms (0.5 detik) untuk cek status lagu
        uiUpdateTimer = new Timer(500, e -> updateUI());

        initListeners();
    }

    private void updateUI() {
        if (audioPlayer.isRunning()) {
            view.updateProgress(audioPlayer.getProgressPercentage(), audioPlayer.getCurrentTimeStr());
        } else {
            // Jika lagu selesai otomatis
            // uiUpdateTimer.stop(); 
        }
    }

    private void initListeners() {
        view.getBtnPlay().addActionListener(e -> {
            if (currentSong != null) {
                audioPlayer.loadAndPlay(currentSong.getAudioPath());
                uiUpdateTimer.start(); // Mulai update slider
                view.getBtnPlay().setText("Playing...");
            }
        });

        view.getBtnStop().addActionListener(e -> {
            audioPlayer.stop();
            uiUpdateTimer.stop();
            view.updateProgress(0, "00:00");
            view.getBtnPlay().setText("▶ Putar");
        });

        view.getBtnBack().addActionListener(e -> {
            audioPlayer.stop();
            uiUpdateTimer.stop();
            cardLayout.show(mainContainer, "CATALOG");
        });
    }

    public void showSongDetail(RegionalSong song) {
        this.currentSong = song;
        view.displayData(song);
        cardLayout.show(mainContainer, "DETAIL");
    }
}