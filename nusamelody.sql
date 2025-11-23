CREATE DATABASE IF NOT EXISTS nusamelody;
USE nusamelody;

-- Tabel 1: Songs
CREATE TABLE IF NOT EXISTS songs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    province VARCHAR(50),
    audio_path VARCHAR(255),
    lyrics TEXT,
    translation TEXT,
    cultural_values TEXT,
    thumbnail VARCHAR(255)
);

-- Tabel 2: Users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    points INT DEFAULT 0,
    badges TEXT
);

-- Tabel 3: User Progress
CREATE TABLE IF NOT EXISTS user_progress (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    song_id INT,
    listened_seconds INT DEFAULT 0,
    quiz_score INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (song_id) REFERENCES songs(id)
);

-- SEED DATA (DUMMY)
INSERT INTO users (name, points, badges) VALUES ('Siswa Teladan', 0, 'Newbie');

INSERT INTO songs (title, province, audio_path, lyrics, translation, cultural_values, thumbnail) 
VALUES 
('Lalan Belek', 'Bengkulu', 'bengkulu-udarang.wav', 
 'Oi lalan belek, lalan belek...', 
 'Wahai gadis cantik, gadis cantik...', 
 'Lagu ini menceritakan tentang nasihat pernikahan dan kehidupan gadis Bengkulu.', 
 'song-thumb-bengkulu.jpg'),

('Ilir Ilir', 'Jawa Tengah', 'jawa-lirih.wav', 
 'Lir ilir, lir ilir, tandure wus sumilir...', 
 'Bangunlah, bangunlah, tanaman sudah bersemi...', 
 'Tembang ciptaan Sunan Kalijaga sebagai sarana dakwah Islam di tanah Jawa.', 
 'song-thumb-jawa.jpg');