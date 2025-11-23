package main.java.app.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager {
    // Membuat kolam thread (CachedThreadPool efisien untuk banyak task pendek)
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    // Jalankan tugas di background
    public static void execute(Runnable task) {
        executor.submit(task);
    }

    // Matikan thread saat aplikasi tutup
    public static void shutdown() {
        executor.shutdown();
    }
}