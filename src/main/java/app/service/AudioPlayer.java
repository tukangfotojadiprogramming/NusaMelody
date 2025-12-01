package main.java.app.service;

import main.java.app.util.AssetLoader;
import javax.sound.sampled.*;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

public class AudioPlayer {
    private Clip clip;
    private boolean isPaused = false;
    private long pausePosition = 0;

    // Method lama untuk play normal (dari awal)
    public void loadAndPlay(String filename) {
        loadAndPlayInternal(filename, false, 0);
    }

    // METHOD BARU: Play dari posisi acak
    // durationSeconds: Berapa detik lagu akan diputar (untuk menghitung batas aman start)
    public void loadAndPlayRandom(String filename, int durationSeconds) {
        loadAndPlayInternal(filename, true, durationSeconds);
    }

    private void loadAndPlayInternal(String filename, boolean randomStart, int snippetDurationSec) {
        ThreadManager.execute(() -> {
            try {
                stop(); // Stop lagu sebelumnya
                
                URL url = AssetLoader.getAudioURL(filename);
                if (url == null) {
                    System.err.println("❌ AUDIO ERROR: File tidak ditemukan -> " + filename);
                    return;
                }

                AudioInputStream audioStream = AudioSystem.getAudioInputStream(url);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                
                // --- LOGIKA RANDOM START ---
                if (randomStart) {
                    long totalMicros = clip.getMicrosecondLength();
                    long snippetMicros = snippetDurationSec * 1_000_000L; // Konversi detik ke mikrod
                    
                    // Pastikan lagu lebih panjang dari durasi snippet (5 detik)
                    if (totalMicros > snippetMicros) {
                        // Batas maksimal start agar lagu tidak putus di tengah jalan
                        long maxStart = totalMicros - snippetMicros;
                        
                        // Pilih posisi acak dari 0 sampai maxStart
                        long randomPos = ThreadLocalRandom.current().nextLong(maxStart);
                        
                        clip.setMicrosecondPosition(randomPos);
                        System.out.println("🎲 Random Start: " + (randomPos / 1_000_000) + "s dari total " + (totalMicros / 1_000_000) + "s");
                    }
                }
                // --------------------------------------------------

                clip.start();
                isPaused = false;
                
            } catch (Exception e) {
                System.err.println("❌ AUDIO ERROR: Gagal memutar file.");
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

    public boolean isRunning() {
        return clip != null && clip.isRunning();
    }

    public int getProgressPercentage() {
        if (clip != null && clip.getMicrosecondLength() > 0) {
            long current = clip.getMicrosecondPosition();
            long total = clip.getMicrosecondLength();
            return (int) ((current * 100) / total);
        }
        return 0;
    }

    public String getCurrentTimeStr() {
        if (clip == null) return "00:00";
        long seconds = clip.getMicrosecondPosition() / 1_000_000;
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}