package saboloo.proeqti.MusicApp.ui;

import saboloo.proeqti.MusicApp.Main;
import saboloo.proeqti.MusicApp.dao.UserDao;
import saboloo.proeqti.MusicApp.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class Login {

    private final VBox root = new VBox(12);
    private final UserDao userDao = new UserDao();

    public Login() {
        Label title = new Label("Music App - ავტორიზაცია");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("სახელი");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("პაროლი");

        Label message = new Label();
        message.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("ავტორიზაცია");
        Button registerBtn = new Button("რეგისტრაცია");

        loginBtn.setOnAction(e -> {
            try {
                Optional<User> user = userDao.login(
                        usernameField.getText().trim(), passwordField.getText());
                if (user.isPresent()) {
                    Main.showScene(new Scene(
                            new Player(user.get()).getRoot(), 640, 520));
                } else {
                    message.setText("არასწორი სახელი ან პაროლი.");
                }
            } catch (Exception ex) {
                message.setText("მონაცემთა ბაზის შეცდომა: " + ex.getMessage());
            }
        });

        registerBtn.setOnAction(e -> Main.showScene(
                new Scene(new Register().getRoot(), 420, 360)));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        root.getChildren().addAll(
                title, usernameField, passwordField, loginBtn, registerBtn, message);
    }

    public VBox getRoot() {
        return root;
    }
}

