package main.java.app.util;

public class Config {
    // Sesuaikan password dengan setting XAMPP Anda
    // Tambahkan parameter ?allowPublicKeyRetrieval=true&useSSL=false untuk mencegah error SSL di MySQL terbaru
    public static final String DB_URL = "jdbc:mysql://localhost:3306/nusamelody?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    public static final String DB_USER = "root";
    public static final String DB_PASS = ""; 
}