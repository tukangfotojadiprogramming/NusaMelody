package main.java.app.model;

public class LeaderboardEntry {
    private String name;
    private int score;
    private String date;

    public LeaderboardEntry(String name, int score, String date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName() { return name; }
    public int getScore() { return score; }
    public String getDate() { return date; }
}