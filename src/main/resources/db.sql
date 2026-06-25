CREATE DATABASE IF NOT EXISTS Music_App
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE Music_App;

CREATE TABLE IF NOT EXISTS users
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(12) NOT NULL,
    is_premium  BOOLEAN     NOT NULL DEFAULT FALSE,
    create_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS songs
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(150)  NOT NULL,
    artist     VARCHAR(150)  NOT NULL,
    stream_url VARCHAR(2000) NOT NULL
);

CREATE TABLE IF NOT EXISTS liked_songs
(
    user_id    INT       NOT NULL,
    song_id    INT       NOT NULL,
    liked_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, song_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (song_id) REFERENCES songs (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS subscriptions
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    user_id    INT         NOT NULL,
    plan       VARCHAR(30) NOT NULL DEFAULT 'PREMIUM',
    start_date TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS skip_log
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    user_id   INT       NOT NULL,
    skip_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

INSERT INTO songs (title, artist, stream_url)
VALUES ('Sunny', 'Audio Library', 'https://download.samplelib.com/mp3/sample-15s.mp3'),
       ('Acoustic Breeze', 'Audio Library', 'https://download.samplelib.com/mp3/sample-12s.mp3'),
       ('Creative Minds', 'Audio Library', 'https://download.samplelib.com/mp3/sample-9s.mp3'),
       ('Ukulele', 'Audio Library', 'https://download.samplelib.com/mp3/sample-6s.mp3'),
       ('Energy', 'Audio Library', 'https://download.samplelib.com/mp3/sample-3s.mp3');
