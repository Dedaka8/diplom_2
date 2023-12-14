package data;

public class OrderData {
    private String[] ingredients;

    public OrderData() {
    }

    public OrderData(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }
}
