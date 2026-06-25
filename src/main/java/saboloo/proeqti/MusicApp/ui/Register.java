package saboloo.proeqti.MusicApp.ui;

import saboloo.proeqti.MusicApp.Main;
import saboloo.proeqti.MusicApp.dao.UserDao;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class Register {
    private final VBox root = new VBox(12);
    private final UserDao userDao = new UserDao();

    public Register() {
        Label title = new Label("ანგარიშის შექმნა");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("სახელი");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("პაროლი");

        Label message = new Label();

        Button createBtn = new Button("რეგისტრაცია");
        Button backBtn = new Button("უკან");

        createBtn.setOnAction(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText();
            if (u.isEmpty() || p.isEmpty()) {
                message.setStyle("-fx-text-fill: red;");
                message.setText("სახელი და პაროლი აუცილებელია.");
                return;
            }
            try {
                if (userDao.register(u, p)) {
                    message.setStyle("-fx-text-fill: green;");
                    message.setText("ანგარიში შექმნილია. შეგიძლია გაიარო ავტორიზაცია.");
                } else {
                    message.setStyle("-fx-text-fill: red;");
                    message.setText("სახელი დაკავებულია.");
                }
            } catch (Exception ex) {
                message.setStyle("-fx-text-fill: red;");
                message.setText("მონაცემთა ბაზის შეცდომა: " + ex.getMessage());
            }
        });

        backBtn.setOnAction(e -> Main.showScene(
                new Scene(new Login().getRoot(), 420, 360)));

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(24));
        root.getChildren().addAll(
                title, usernameField, passwordField, createBtn, backBtn, message);
    }

    public VBox getRoot() {
        return root;
    }
}
