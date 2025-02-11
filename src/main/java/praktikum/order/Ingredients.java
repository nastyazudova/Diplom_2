package praktikum.order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ingredients {
    private List<String> ingredients;

    public Ingredients(String[] ingredients) {
        this.ingredients = new ArrayList<>(Arrays.asList(ingredients));
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
