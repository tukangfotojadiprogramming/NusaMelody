package main.java.app.controller;

import main.java.app.model.RegionalSong;
import main.java.app.service.SongRepository;
import main.java.app.service.ThreadManager;
import main.java.app.view.CatalogPage;
import javax.swing.SwingUtilities;
import java.util.List;

public class CatalogController {
    private CatalogPage view;
    private SongRepository repository;
    private SongController songController; // Butuh ini untuk oper data ke Detail

    public CatalogController(CatalogPage view, SongController songController) {
        this.view = view;
        this.songController = songController;
        this.repository = new SongRepository();

        loadData();
        
        // Listener saat lagu di katalog diklik
        this.view.setOnSongSelected(song -> {
            System.out.println("User memilih: " + song.getTitle());
            songController.showSongDetail(song);
        });
    }

    private void loadData() {
        // PENTING: Akses Database di Thread terpisah agar UI tidak freeze
        ThreadManager.execute(() -> {
            List<RegionalSong> songs = repository.getAllSongs();
            
            // PENTING: Update UI wajib balik ke thread GUI (Event Dispatch Thread)
            SwingUtilities.invokeLater(() -> {
                view.setSongList(songs);
            });
        });
    }
}