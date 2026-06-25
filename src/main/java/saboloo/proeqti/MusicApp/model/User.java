package saboloo.proeqti.MusicApp.model;

public class User {
    private final int id;
    private final String username;
    private boolean premium;

    public User(int id, String username, boolean premium) {
        this.id = id;
        this.username = username;
        this.premium = premium;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}

