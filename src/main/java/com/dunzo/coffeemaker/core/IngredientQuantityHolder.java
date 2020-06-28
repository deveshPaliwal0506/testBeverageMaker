package com.dunzo.coffeemaker.core;

public class IngredientQuantityHolder {

    private final Ingredient ingredient;
    private final Float quantity;

    public IngredientQuantityHolder(Ingredient ingredient,Float quantity){
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public Float getQuantity() {
        return quantity;
    }

}
