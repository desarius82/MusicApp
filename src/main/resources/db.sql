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
VALUES ('For Our Friends', 'Telecasted', 'https://github.com/desarius82/MusicApp/releases/download/v1.0.0/For.Our.Friends.-.Telecasted.mp3'),
       ('Paradise', 'Anno Domini Beats', 'https://github.com/desarius82/MusicApp/releases/download/v1.0.0/Paradise.-.Anno.Domini.Beats.mp3'),
       ('Pawn', 'Golden Palms', 'https://github.com/desarius82/MusicApp/releases/download/v1.0.0/Pawn.-.The.Grey.Room._.Golden.Palms.mp3'),
       ('Tonight Again', 'Rod Kim (feat. Mostly Moss)', 'https://github.com/desarius82/MusicApp/releases/download/v1.0.0/Tonight.Again.-.Rod.Kim.feat.Mostly.Moss.mp3'),
       ('Two Things', 'Anno Domini Beats', 'https://github.com/desarius82/MusicApp/releases/download/v1.0.0/Two.Things.-.Anno.Domini.Beats.mp3');
