package main.java.app.model;

public class User {
    private int id;
    private String name;
    private int points;

    public User(int id, String name, int points) {
        this.id = id;
        this.name = name;
        this.points = points;
    }

    public String getName() { return name; }
    public int getPoints() { return points; }
    
    public void addPoints(int p) {
        this.points += p;
    }
}