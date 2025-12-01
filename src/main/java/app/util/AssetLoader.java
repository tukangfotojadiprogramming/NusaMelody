package main.java.app.util;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private static final Map<String, ImageIcon> imageCache = new HashMap<>();

    public static ImageIcon loadImage(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;
        
        // Bersihkan path: Jika database menyimpan "images/file.jpg", kita ambil "file.jpg" saja
        // karena loader kita sudah pintar mencari di folder images.
        String cleanName = new File(dbPath).getName(); 
        
        if (imageCache.containsKey(cleanName)) {
            return imageCache.get(cleanName);
        }

        URL url = getResource("images", cleanName);
        
        // Fallback ke gambar default jika tidak ditemukan
        if (url == null) {
            // System.err.println("⚠️ Asset tidak ditemukan: " + cleanName + " (Menggunakan Fallback)");
            if (imageCache.containsKey("landing-hero.jpg")) {
                return imageCache.get("landing-hero.jpg");
            }
            url = getResource("images", "landing-hero.jpg"); 
        }

        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            imageCache.put(cleanName, icon);
            return icon;
        }
        
        return null; 
    }

    public static URL getAudioURL(String dbPath) {
        if (dbPath == null || dbPath.isEmpty()) return null;
        String cleanName = new File(dbPath).getName(); // Ambil nama file saja
        return getResource("audio", cleanName);
    }

    private static URL getResource(String type, String filename) {
        // Coba berbagai kemungkinan path (Relatif & Absolute)
        String[] possiblePaths = {
            "/assets/" + type + "/" + filename,
            "src/main/resources/assets/" + type + "/" + filename,
            "bin/main/resources/assets/" + type + "/" + filename,
            "src/assets/" + type + "/" + filename
        };

        for (String pathStr : possiblePaths) {
            // Cek Classpath dulu (Paling Benar)
            if (pathStr.startsWith("/")) {
                URL url = AssetLoader.class.getResource(pathStr);
                if (url != null) return url;
            } 
            // Cek File System (Untuk Development/VS Code)
            else {
                File f = new File(pathStr);
                if (f.exists()) {
                    try { return f.toURI().toURL(); } catch (Exception e) {}
                }
            }
        }
        return null;
    }
}