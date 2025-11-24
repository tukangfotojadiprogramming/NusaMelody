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
    private Timer uiUpdateTimer;

    public SongController(SongDetailPage view, JPanel mainContainer, CardLayout cardLayout) {
        this.view = view;
        this.mainContainer = mainContainer;
        this.cardLayout = cardLayout;
        this.audioPlayer = new AudioPlayer();

        // Timer update slider setiap 500ms
        uiUpdateTimer = new Timer(500, e -> updateUI());

        initListeners();
    }

    private void updateUI() {
        if (audioPlayer.isRunning()) {
            view.updateProgress(audioPlayer.getProgressPercentage(), audioPlayer.getCurrentTimeStr());
        }
    }

    private void initListeners() {
        view.getBtnPlay().addActionListener(e -> {
            if (currentSong != null) {
                audioPlayer.loadAndPlay(currentSong.getAudioPath());
                uiUpdateTimer.start();
                view.getBtnPlay().setText("Playing...");
            }
        });

        view.getBtnStop().addActionListener(e -> {
            stopAudio(); // Panggil method helper
        });

        view.getBtnBack().addActionListener(e -> {
            stopAudio(); // Matikan musik saat kembali
            cardLayout.show(mainContainer, "CATALOG");
        });
    }

    public void showSongDetail(RegionalSong song) {
        this.currentSong = song;
        view.displayData(song);
        cardLayout.show(mainContainer, "DETAIL");
    }

    // === TAMBAHKAN METHOD INI AGAR ERROR DI MAIN HILANG ===
    public void stopAudio() {
        if (audioPlayer != null) {
            audioPlayer.stop();
        }
        if (uiUpdateTimer != null) {
            uiUpdateTimer.stop();
        }
        view.updateProgress(0, "00:00");
        view.getBtnPlay().setText("▶ Putar");
    }
}