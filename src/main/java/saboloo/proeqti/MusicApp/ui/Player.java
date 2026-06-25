package saboloo.proeqti.MusicApp.ui;

import saboloo.proeqti.MusicApp.Main;
import saboloo.proeqti.MusicApp.dao.SkipDao;
import saboloo.proeqti.MusicApp.dao.SongDao;
import saboloo.proeqti.MusicApp.dao.UserDao;
import saboloo.proeqti.MusicApp.model.Song;
import saboloo.proeqti.MusicApp.model.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;

public class Player {

    private final VBox root = new VBox(12);
    private final User user;
    private final SongDao songDao = new SongDao();
    private final SkipDao skipDao = new SkipDao();
    private final UserDao userDao = new UserDao();

    private final ListView<Song> songList = new ListView<>();
    private final Label nowPlaying = new Label("არაფერი უკრავს");
    private final Label statusLabel = new Label();
    private MediaPlayer mediaPlayer;

    public Player(User user) {
        this.user = user;
        buildUi();
        loadSongs();
    }

    private void buildUi() {
        Label header = new Label("გამარჯობა, " + user.getUsername()
                + (user.isPremium() ? "  [PREMIUM]" : "  [FREE]"));
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button playBtn = new Button("Play");
        Button pauseBtn = new Button("Pause");
        Button stopBtn = new Button("Stop");
        Button skipBtn = new Button("Skip");
        Button likeBtn = new Button("Like");
        Button likedBtn = new Button("მოწონებული სიმღერები");
        Button premiumBtn = new Button("Premium პაკეტის ყიდვა");
        Button logoutBtn = new Button("გასვლა");

        playBtn.setOnAction(e -> playSelected());
        pauseBtn.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
            }
        });
        stopBtn.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            nowPlaying.setText("არაფერი უკრავს");
        });
        skipBtn.setOnAction(e -> skip());
        likeBtn.setOnAction(e -> likeSelected());
        likedBtn.setOnAction(e -> showLiked());
        premiumBtn.setOnAction(e -> buyPremium(header));
        logoutBtn.setOnAction(e -> logout());

        HBox controls = new HBox(8, playBtn, pauseBtn, stopBtn, skipBtn, likeBtn);
        controls.setAlignment(Pos.CENTER);

        HBox actions = new HBox(8, likedBtn, premiumBtn, logoutBtn);
        actions.setAlignment(Pos.CENTER);

        statusLabel.setStyle("-fx-text-fill: #1976d2;");

        root.setPadding(new Insets(16));
        root.getChildren().addAll(
                header, songList, nowPlaying, controls, actions, statusLabel);
    }

    private void loadSongs() {
        try {
            List<Song> songs = songDao.findAll();
            songList.setItems(FXCollections.observableArrayList(songs));
            if (!songs.isEmpty()) {
                songList.getSelectionModel().select(0);
            }
        } catch (Exception e) {
            statusLabel.setText("სიმღერების ჩატვირთვისას დაფიქსირდა შეცდომა: " + e.getMessage());
        }
    }

    private void playSelected() {
        Song song = songList.getSelectionModel().getSelectedItem();
        if (song == null) {
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        try {
            Media media = new Media(song.getStreamUrl());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnError(() ->
                    statusLabel.setText("დაკვრის შეცდომა: " + mediaPlayer.getError()));
            mediaPlayer.play();
            nowPlaying.setText("ახლა უკრავს: " + song);
            statusLabel.setText("");
        } catch (Exception e) {
            statusLabel.setText("შეუძლებელია სიმღერის დაკვრა: " + e.getMessage());
        }
    }

    private void skip() {
        try {
            if (!user.isPremium()) {
                int used = skipDao.skipsInLastHour(user.getId());
                if (used >= SkipDao.FREE_SKIP_LIMIT_PER_HOUR) {
                    statusLabel.setText("გამოტოვებების ლიმიტი ამოწურულია (შემდეგი "
                            + SkipDao.FREE_SKIP_LIMIT_PER_HOUR
                            + " საათში).\n შეიძინე Premium ულიმიტო გამოტოვებებისთვის.");
                    return;
                }
                skipDao.logSkip(user.getId());
            }
            int next = songList.getSelectionModel().getSelectedIndex() + 1;
            if (next >= songList.getItems().size()) {
                next = 0;
            }
            songList.getSelectionModel().select(next);
            playSelected();
        } catch (Exception e) {
            statusLabel.setText("სიმღერის გამოტოვების დროს დაფიქსირდა შეცდომა: " + e.getMessage());
        }
    }

    private void likeSelected() {
        Song song = songList.getSelectionModel().getSelectedItem();
        if (song == null) {
            return;
        }
        try {
            if (songDao.isLiked(user.getId(), song.getId())) {
                songDao.unlike(user.getId(), song.getId());
                statusLabel.setText("წაშლილია მოწონებული სიმღერებიდან: " + song.getTitle());
            } else {
                songDao.like(user.getId(), song.getId());
                statusLabel.setText("მოწონებულია: " + song.getTitle());
            }
        } catch (Exception e) {
            statusLabel.setText("სიმღერის მოწონების დროს დაფიქსირდა შეცდომა: " + e.getMessage());
        }
    }

    private void showLiked() {
        try {
            List<Song> liked = songDao.findLiked(user.getId());
            songList.setItems(FXCollections.observableArrayList(liked));
            statusLabel.setText("მოწონებული სიმღერები (" + liked.size() + "). "
                    + "ყველა სიმღერის გამოსაჩენად ახლიდან გახსენით აპლიკაცია.");
        } catch (Exception e) {
            statusLabel.setText("მოწონებული სიმღერების ჩატვირთვის დროს დაფიქსირდა შეცდომა: " + e.getMessage());
        }
    }

    private void buyPremium(Label header) {
        if (user.isPremium()) {
            statusLabel.setText("თქვენ უკვე გაქვთ შეძენილი Premium.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "გსურთ Premium პაკეტის ყიდვა?");
        confirm.setHeaderText("Premium პაკეტი");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn.getButtonData().isDefaultButton()) {
                try {
                    userDao.upgradeToPremium(user.getId());
                    user.setPremium(true);
                    header.setText("გამარჯობა, " + user.getUsername() + "  [PREMIUM]");
                    statusLabel.setText("Premium პაკეტი გააქტიურებულია! თქვენ გაქვთ სიმღერის ულიმიტო გამოტოვების შესაძლებლობა.");
                } catch (Exception e) {
                    statusLabel.setText("Premium პაკეტის ყიდვის დროს დაფიქსირდა შეცდომა: " + e.getMessage());
                }
            }
        });
    }

    private void logout() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        Main.showScene(new Scene(new Login().getRoot(), 420, 360));
    }

    public VBox getRoot() {
        return root;
    }
}
