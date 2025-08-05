package pojo;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Order {
    private List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    // Генерация валидного заказа с ингредиентами
    public static Order validOrder(List<String> ingredientIds) {
        return new Order(ingredientIds);
    }

    // Заказ без ингредиентов
    public static Order emptyOrder() {
        return new Order(Collections.emptyList());
    }

    // Заказ с некорректными id ингредиентов
    public static Order invalidOrder() {
        return new Order(List.of(UUID.randomUUID().toString()));
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
