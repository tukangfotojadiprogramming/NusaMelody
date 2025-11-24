-- 1. BUAT DATABASE BARU (Reset Total)
DROP DATABASE IF EXISTS nusamelody;
CREATE DATABASE nusamelody;
USE nusamelody;

-- --------------------------------------------------------

-- 2. STRUKTUR TABEL: USERS
CREATE TABLE users (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL, -- Disimpan plain text untuk demo (budi123, dll)
  points INT DEFAULT 0,
  badges TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seeding Users (Login Demo)
INSERT INTO users (name, email, password, points, badges) VALUES 
('Siswa Teladan', 'demo@test.com', 'demoPassword', 0, 'Newbie'),
('Budi Santoso', 'budi@test.com', 'budi123', 150, 'Expert'),
('Siti Aminah', 'siti@test.com', 'siti123', 120, 'Intermediate');

-- --------------------------------------------------------

-- 3. STRUKTUR TABEL: SONGS
CREATE TABLE songs (
  id INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(100),
  province VARCHAR(50),
  audio_path VARCHAR(255),
  lyrics TEXT,
  translation TEXT,
  cultural_values TEXT,
  thumbnail VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seeding Songs (Data Lengkap + NTB)
INSERT INTO songs (id, title, province, audio_path, lyrics, translation, cultural_values, thumbnail) VALUES
(1, 'Lalan Belek', 'Bengkulu', 'bengkulu-lalanbelek.wav', 'Oi lalan belek...', 'Wahai gadis cantik...', 'Nasihat kehidupan gadis Bengkulu.', 'song-thumb-bengkulu.jpg'),
(2, 'Udarang', 'Bengkulu', 'bengkulu-udarang.wav', 'Udarang...', 'Terjemahan...', 'Lagu budaya adat Bengkulu.', 'song-thumb-bengkulu.jpg'),
(3, 'Ilir Ilir', 'Jawa Tengah', 'jawa-lirih.wav', 'Lir ilir, lir ilir...', 'Bangunlah, bangunlah...', 'Dakwah Sunan Kalijaga.', 'song-thumb-jawa.jpg'),
(4, 'Gambang Suling', 'Jawa Tengah', 'jateng-gambang.wav', 'Gambang suling...', 'Seruling bambu...', 'Kesenian keraton Jawa.', 'song-thumb-jawa.jpg'),
(5, 'Manuk Dadali', 'Jawa Barat', 'jabar-manuk.wav', 'Mesat ngapung luhur...', 'Terbang melesat tinggi...', 'Semangat nasionalisme.', 'song-thumb-jawa.jpg'),
(6, 'Bubuy Bulan', 'Jawa Barat', 'jabar-bubuy.wav', 'Bubuy bulan...', 'Menyangrai bulan...', 'Kesedihan dan kerinduan.', 'song-thumb-jawa.jpg'),
(7, 'Kicir Kicir', 'DKI Jakarta', 'jakarta-kicir.wav', 'Kicir kicir ini lagunya...', 'Ini lagunya...', 'Lagu hiburan pesta rakyat.', 'song-thumb-jakarta.jpg'),
(8, 'Ondel-Ondel', 'DKI Jakarta', 'jakarta-ondel.wav', 'Nyok kita nonton ondel-ondel...', 'Ayo nonton ondel-ondel...', 'Ikon budaya Betawi.', 'song-thumb-jakarta.jpg'),
(9, 'Ampar-Ampar Pisang', 'Kalimantan Selatan', 'kalsel-ampar.wav', 'Ampar ampar pisang...', 'Menyusun pisang...', 'Lagu permainan anak saat membuat kue.', 'song-thumb-kalsel.jpg'),
(10, 'Paris Barantai', 'Kalimantan Selatan', 'kalsel-paris.wav', 'Paris barantai...', 'Kalung berantai...', 'Kerinduan pada kekasih.', 'song-thumb-kalsel.jpg'),
(11, 'Rasa Sayange', 'Maluku', 'maluku-rasa.wav', 'Rasa sayange...', 'Perasaan sayang...', 'Lagu persaudaraan dan pergaulan.', 'song-thumb-maluku.jpg'),
(12, 'Ayo Mama', 'Maluku', 'maluku-ayomama.wav', 'Ayo mama...', 'Ayo mama...', 'Keceriaan rakyat Maluku.', 'song-thumb-maluku.jpg'),
(13, 'Yamko Rambe Yamko', 'Papua', 'papua-yamko.wav', 'Hee yamko rambe yamko...', 'Hai jalan yang berliku...', 'Semangat perjuangan pahlawan.', 'song-thumb-papua.jpg'),
(14, 'Apuse', 'Papua', 'papua-apuse.wav', 'Apuse kokon dao...', 'Kakek nenek aku mau pergi...', 'Lagu perpisahan cucu dengan kakek nenek.', 'song-thumb-papua.jpg'),
(15, 'Kampuang Nan Jauh di Mato', 'Sumatra Barat', 'sumbar-kampuang.wav', 'Kampuang nan jauh di mato...', 'Kampung yang jauh di mata...', 'Kerinduan perantau Minang pada kampung halaman.', 'song-thumb-sumbar.jpg'),
(16, 'Ayam Den Lapeh', 'Sumatra Barat', 'sumbar-ayamden.wav', 'Luruihlah jalan...', 'Luruslah jalan...', 'Kiasan tentang kehilangan sesuatu yang berharga.', 'song-thumb-sumbar.jpg'),
-- DATA BARU NTB (Sesuai Revisi)
(17, 'Moree', 'Nusa Tenggara Barat', 'ntb-moree.wav', 'Moree, moree, ndak katuru...', 'Mari, mari, jangan tidur...', 'Lagu ajakan semangat masyarakat Sasak.', 'song-thumb-ntb.jpg'),
(18, 'Tutu Koda', 'Nusa Tenggara Barat', 'ntb-tutukoda.wav', 'O ina, o ama, tutu koda...', 'Wahai ibu, wahai ayah, peganglah janji...', 'Nasihat memegang amanah dari daerah Bima.', 'song-thumb-ntb.jpg'),
(19, 'Orong-Orong', 'Nusa Tenggara Barat', 'ntb-orong.wav', 'Orong orong...', 'Orong orong...', 'Lagu rakyat pengiring permainan atau kerja.', 'song-thumb-ntb.jpg');

-- --------------------------------------------------------

-- 4. STRUKTUR TABEL: LEADERBOARD
CREATE TABLE leaderboard (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100),
    score INT,
    played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Seeding Leaderboard (Data Dummy Juara)
INSERT INTO leaderboard (name, email, score, played_at) VALUES 
('Budi Santoso', 'budi@test.com', 150, NOW() - INTERVAL 1 DAY),
('Siti Aminah', 'siti@test.com', 120, NOW() - INTERVAL 2 HOUR),
('Joko Anwar', 'joko@test.com', 110, NOW() - INTERVAL 5 HOUR),
('Dewi Sartika', 'dewi@test.com', 95, NOW() - INTERVAL 1 DAY),
('Rahmat Hidayat', 'rahmat@test.com', 80, NOW() - INTERVAL 30 MINUTE);

-- --------------------------------------------------------

-- 5. STRUKTUR TABEL: USER PROGRESS
CREATE TABLE user_progress (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    song_id INT,
    listened_seconds INT DEFAULT 0,
    quiz_score INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;