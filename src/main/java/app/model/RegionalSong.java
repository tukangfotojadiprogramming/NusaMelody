package main.java.app.model;

// INHERITANCE Level 2: RegionalSong mewarisi Song
public class RegionalSong extends Song implements IMetadata {
    private String province;
    private String lyrics;
    private String translation;
    private String culturalValues;

    public RegionalSong(int id, String title, String province, String audioPath, 
                        String lyrics, String translation, String culturalValues, String thumbnail) {
        // Memanggil constructor Song
        super(id, title, thumbnail, audioPath);
        this.province = province;
        this.lyrics = lyrics;
        this.translation = translation;
        this.culturalValues = culturalValues;
    }

    // Encapsulation: Getter methods
    public String getProvince() { return province; }
    public String getCulturalValues() { return culturalValues; }

    // Implementasi Interface IMetadata
    @Override
    public String getLyrics() {
        return lyrics;
    }

    @Override
    public String getTranslation() {
        return translation;
    }
    
    @Override
    public String toString() {
        return title + " (" + province + ")";
    }
}