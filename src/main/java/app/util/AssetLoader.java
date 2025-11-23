package main.java.app.util;

import javax.swing.ImageIcon;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AssetLoader {

    // --- LOGIC PENCARIAN FILE YANG LEBIH KUAT ---
    private static URL getResource(String type, String filename) {
        String pathInsideJar = "/assets/" + type + "/" + filename;
        
        // CARA 1: Coba Load via Classpath (Standard Java)
        URL url = AssetLoader.class.getResource(pathInsideJar);
        
        // Jika ketemu, langsung kembalikan
        if (url != null) {
            return url;
        }

        // CARA 2: Jika GAGAL, Coba cari manual di folder Project (Fallback)
        // Kita cek beberapa kemungkinan lokasi folder "assets"
        String[] possiblePaths = {
            "src/main/resources/assets/" + type + "/" + filename,  // Struktur Maven
            "src/assets/" + type + "/" + filename,                 // Struktur Standard
            "assets/" + type + "/" + filename                      // Struktur Root
        };

        for (String path : possiblePaths) {
            File f = new File(path);
            if (f.exists()) {
                try {
                    // System.out.println("File ditemukan via Path Manual: " + path); // Debug
                    return f.toURI().toURL();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Jika masih tidak ketemu juga
        System.err.println("❌ ERROR FATAL: File tidak ditemukan di manapun -> " + filename);
        System.err.println("   Cek apakah file ada di folder: src/main/resources/assets/" + type + "/");
        return null;
    }

    // --- PUBLIC METHODS ---

    public static ImageIcon loadImage(String filename) {
        URL url = getResource("images", filename);
        if (url == null) return null;
        return new ImageIcon(url);
    }

    public static URL getAudioURL(String filename) {
        return getResource("audio", filename);
    }
}