package main.java.app.model;

// ABSTRACTION: Abstract class tidak bisa diinstansiasi langsung
public abstract class MediaItem {
    // ENCAPSULATION: Protected agar bisa diakses oleh child class (Song), tapi private dari luar
    protected int id;
    protected String title;
    protected String thumbnailPath;

    public MediaItem(int id, String title, String thumbnailPath) {
        this.id = id;
        this.title = title;
        this.thumbnailPath = thumbnailPath;
    }

    // ABSTRACT METHOD: Wajib di-override oleh class turunannya
    public abstract String getMediaType();

    // GETTERS (Encapsulation)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getThumbnailPath() { return thumbnailPath; }
}