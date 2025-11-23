package main.java.app.service;

import main.java.app.util.AssetLoader;
import javax.sound.sampled.*;
import java.net.URL;

public class AudioPlayer {
    private Clip clip;
    private boolean isPaused = false;
    private long pausePosition = 0;

    public void loadAndPlay(String filename) {
        ThreadManager.execute(() -> {
            try {
                stop();
                URL url = AssetLoader.getAudioURL(filename);
                if (url == null) return;

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                isPaused = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            isPaused = false;
            pausePosition = 0;
        }
    }

    // --- FITUR BARU UNTUK UI ---
    
    public boolean isRunning() {
        return clip != null && clip.isRunning();
    }

    // Mendapatkan posisi lagu saat ini (0 - 100%)
    public int getProgressPercentage() {
        if (clip != null && clip.getMicrosecondLength() > 0) {
            long current = clip.getMicrosecondPosition();
            long total = clip.getMicrosecondLength();
            return (int) ((current * 100) / total);
        }
        return 0;
    }

    // Mendapatkan durasi string (contoh "01:30")
    public String getCurrentTimeStr() {
        if (clip == null) return "00:00";
        long seconds = clip.getMicrosecondPosition() / 1_000_000;
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}