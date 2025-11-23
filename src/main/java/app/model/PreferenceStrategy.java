package main.java.app.model;

public class PreferenceStrategy implements IRecommendable {
    @Override
    public void recommend(User user) {
        System.out.println("LOGIC: Merekomendasikan lagu yang mirip dengan history: " + user.getName());
    }
}