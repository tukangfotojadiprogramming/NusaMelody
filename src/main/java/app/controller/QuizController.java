package main.java.app.controller;

import main.java.app.model.LeaderboardEntry;
import main.java.app.model.RegionalSong;
import main.java.app.service.AudioPlayer;
import main.java.app.service.LeaderboardRepository;
import main.java.app.service.SongRepository;
import main.java.app.service.ThreadManager;
import main.java.app.view.LeaderboardPage;
import main.java.app.view.QuizPage;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

public class QuizController {
    private QuizPage view;
    private LeaderboardPage leaderboardView;
    private SongRepository songRepo;
    private LeaderboardRepository leaderboardRepo;
    private AudioPlayer player;
    
    private List<RegionalSong> sessionSongs;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int globalTime = 60;
    private Timer globalTimer;
    private Timer audioCutoffTimer;
    private RegionalSong currentAnswer;
    
    private String currentUserEmail;
    private String currentUserName;
    private boolean isGameActive = false;

    public QuizController(QuizPage view, LeaderboardPage lbView, SongRepository repo, LeaderboardRepository lbRepo) {
        this.view = view;
        this.leaderboardView = lbView;
        this.songRepo = repo;
        this.leaderboardRepo = lbRepo;
        this.player = new AudioPlayer();

        // --- LISTENER TOMBOL ---
        
        // 1. Tombol di Menu Utama Kuis
        view.getStartButton().addActionListener(e -> startPreGameCountdown());
        view.getBtnLeaderboard().addActionListener(e -> showLeaderboard());
        
        // 2. Tombol di Dalam Game
        view.getReplayButton().addActionListener(e -> playSnippet());
        
        // 3. Tombol Kembali di Leaderboard -> BALIK KE MENU KUIS (Bukan Beranda)
        leaderboardView.getBtnBack().addActionListener(e -> {
            view.showMenu();
        });
    }

    public void setUser(String name, String email) {
        this.currentUserName = name;
        this.currentUserEmail = email;
    }

    // Method untuk dipanggil Main saat navigasi sidebar dipencet
    public void abortGame() {
        if (isGameActive) {
            isGameActive = false;
            stopTimers();
            player.stop();
            view.showMenu(); // Reset tampilan ke menu
        }
    }

    private void showLeaderboard() {
        // Tampilkan loading atau langsung switch
        view.showLeaderboardPanel();
        
        // Ambil data di background
        ThreadManager.execute(() -> {
            List<LeaderboardEntry> data = leaderboardRepo.getTopScores();
            
            // Update UI di thread Swing
            SwingUtilities.invokeLater(() -> {
                leaderboardView.setLeaderboardData(data);
            });
        });
    }

    // ... (Sisa logika game sama, tapi saya tulis ulang biar lengkap) ...

    private void startPreGameCountdown() {
        isGameActive = true;
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override protected Void doInBackground() throws Exception {
                publish("3"); Thread.sleep(1000);
                publish("2"); Thread.sleep(1000);
                publish("1"); Thread.sleep(1000);
                publish("MULAI!"); Thread.sleep(500);
                return null;
            }
            @Override protected void process(List<String> chunks) {
                view.showCountdown(chunks.get(chunks.size()-1));
            }
            @Override protected void done() {
                startGameSession();
            }
        };
        worker.execute();
    }

    private void startGameSession() {
        if (!isGameActive) return;

        List<RegionalSong> all = songRepo.getAllSongs();
        if(all.size() < 4) {
            JOptionPane.showMessageDialog(view, "Data lagu belum cukup untuk kuis (Min 4)!");
            view.showMenu();
            isGameActive = false;
            return;
        }
        Collections.shuffle(all);
        sessionSongs = all.subList(0, Math.min(10, all.size()));
        
        currentQuestionIndex = 0;
        score = 0;
        globalTime = 60;
        
        view.showGame();
        startGlobalTimer();
        loadQuestion();
    }

    private void startGlobalTimer() {
        if (globalTimer != null) globalTimer.stop();
        globalTimer = new Timer(1000, e -> {
            if (!isGameActive) { ((Timer)e.getSource()).stop(); return; }
            
            globalTime--;
            view.updateGameInfo(currentQuestionIndex, globalTime);
            if (globalTime <= 0) {
                endGame("Waktu Habis!");
            }
        });
        globalTimer.start();
    }

    private void loadQuestion() {
        if (!isGameActive) return;
        if (currentQuestionIndex >= sessionSongs.size()) {
            endGame("Permainan Selesai!");
            return;
        }

        currentAnswer = sessionSongs.get(currentQuestionIndex);
        
        List<RegionalSong> options = songRepo.getAllSongs();
        Collections.shuffle(options);
        options.removeIf(s -> s.getId() == currentAnswer.getId());
        List<RegionalSong> choices = options.subList(0, 3);
        choices.add(currentAnswer);
        Collections.shuffle(choices);

        String[] titles = new String[4];
        for(int i=0; i<4; i++) {
            titles[i] = choices.get(i).getTitle();
            JButton btn = view.getOptionButton(i);
            for(ActionListener al : btn.getActionListeners()) btn.removeActionListener(al);
            RegionalSong selected = choices.get(i);
            btn.addActionListener(e -> checkAnswer(selected));
        }
        view.setOptions(titles);
        view.setStatus("Mendengarkan...");
        view.getReplayButton().setEnabled(false);

        playSnippet();
    }

    private void playSnippet() {
        if (!isGameActive) return;
        player.stop();
        player.loadAndPlay(currentAnswer.getAudioPath());
        
        if(audioCutoffTimer != null) audioCutoffTimer.stop();
        audioCutoffTimer = new Timer(5000, e -> {
            player.stop();
            view.setStatus("Waktu dengar habis!");
            if(isGameActive) view.getReplayButton().setEnabled(true);
            audioCutoffTimer.stop();
        });
        audioCutoffTimer.setRepeats(false);
        audioCutoffTimer.start();
    }

    private void checkAnswer(RegionalSong selected) {
        if (!isGameActive) return;
        player.stop();
        if(audioCutoffTimer != null) audioCutoffTimer.stop();

        if (selected.getId() == currentAnswer.getId()) {
            score += 10; 
        }
        currentQuestionIndex++;
        loadQuestion();
    }

    private void endGame(String message) {
        stopTimers();
        player.stop();
        isGameActive = false;
        
        if(globalTime > 0) score += globalTime; 
        
        leaderboardRepo.saveScore(currentUserName, currentUserEmail, score);
        
        JOptionPane.showMessageDialog(view, message + "\nSkor Akhir Kamu: " + score);
        view.showMenu();
    }
    
    private void stopTimers() {
        if (globalTimer != null) globalTimer.stop();
        if (audioCutoffTimer != null) audioCutoffTimer.stop();
    }
}