package main.java.app.controller;

import main.java.app.model.RegionalSong;
import main.java.app.service.SongRepository;
import main.java.app.service.ThreadManager;
import main.java.app.view.CatalogPage;
import javax.swing.SwingUtilities;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogController {
    private CatalogPage view;
    private SongRepository repository;
    private SongController songController;
    private List<RegionalSong> allSongsCache;

    public CatalogController(CatalogPage view, SongController songController) {
        this.view = view;
        this.songController = songController;
        this.repository = new SongRepository();
        
        // Default behavior tombol back (bisa ditimpa nanti)
        view.getBtnBack().addActionListener(e -> System.out.println("Back clicked (Default)"));

        view.setOnSongSelected(song -> {
            songController.showSongDetail(song);
        });
    }

    // METHOD BARU: Untuk mengatur tombol kembali secara dinamis
    public void setBackAction(Runnable action) {
        // Hapus listener lama agar tidak menumpuk
        for (ActionListener al : view.getBtnBack().getActionListeners()) {
            view.getBtnBack().removeActionListener(al);
        }
        // Tambah listener baru
        view.getBtnBack().addActionListener(e -> action.run());
    }

    public void loadAllSorted() {
        ThreadManager.execute(() -> {
            if (allSongsCache == null) allSongsCache = repository.getAllSongs();
            
            List<RegionalSong> sorted = allSongsCache.stream()
                .sorted(Comparator.comparing(RegionalSong::getTitle)) 
                .collect(Collectors.toList());

            SwingUtilities.invokeLater(() -> view.setSongList(sorted));
        });
    }

    public void filterByProvince(String provinceName) {
        ThreadManager.execute(() -> {
            if (allSongsCache == null) allSongsCache = repository.getAllSongs();
            
            List<RegionalSong> filtered = allSongsCache.stream()
                .filter(s -> s.getProvince().equalsIgnoreCase(provinceName))
                .collect(Collectors.toList());
                
            SwingUtilities.invokeLater(() -> view.setSongList(filtered));
        });
    }
}