package com.dunzo.coffeemaker.core;

public class Ingredient {

    private String ingredientName;

    public Ingredient(String name){
        this.ingredientName = name;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Ingredient))
            return false;
        if (obj == this)
            return true;
        return this.getIngredientName().equals(((Ingredient) obj).getIngredientName());
    }

    @Override
    public int hashCode() {
        return ingredientName.hashCode();
    }

    public static void main(String[] args){
        System.out.println(new Ingredient("hot_water").hashCode());
        System.out.println(new Ingredient("hot_water").hashCode());
        System.out.println(new Ingredient("hot_water").hashCode());
        System.out.println(new Ingredient("hot_water").hashCode());

    }
}
