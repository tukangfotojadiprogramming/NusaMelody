package main.java.app.model;

public class RuleBasedStrategy implements IRecommendable {
    @Override
    public void recommend(User user) {
        System.out.println("LOGIC: Merekomendasikan lagu berdasarkan Provinsi asal User: " + user.getName());
    }
}