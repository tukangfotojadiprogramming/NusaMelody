package main.java.app.model;

// INHERITANCE Level 1: Song mewarisi MediaItem
public class Song extends MediaItem implements IPlayable {
    protected String audioPath;
    
    // State sederhana untuk melacak status (logika audio sesungguhnya ada di Service)
    protected boolean isPlaying = false;

    public Song(int id, String title, String thumbnailPath, String audioPath) {
        super(id, title, thumbnailPath); // Memanggil constructor parent
        this.audioPath = audioPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    @Override
    public String getMediaType() {
        return "Digital Audio";
    }

    // Implementasi Interface IPlayable (Basic Behavior)
    @Override
    public void play() {
        System.out.println("Model Status: Playing " + title);
        isPlaying = true;
    }

    @Override
    public void pause() {
        System.out.println("Model Status: Paused " + title);
        isPlaying = false;
    }

    @Override
    public void stop() {
        System.out.println("Model Status: Stopped " + title);
        isPlaying = false;
    }
}