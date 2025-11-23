package main.java.app.model;

public class RecommendationEngine {
    private IRecommendable strategy;

    // Bisa mengganti strategi saat runtime (Polymorphism)
    public void setStrategy(IRecommendable strategy) {
        this.strategy = strategy;
    }

    public void executeRecommendation(User u) {
        if(strategy != null) {
            strategy.recommend(u);
        } else {
            System.out.println("No strategy defined.");
        }
    }
}