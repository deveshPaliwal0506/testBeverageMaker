package com.dunzo.coffeemaker.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Beverage {

    private final List<IngredientQuantityHolder> ingrediantDetails;
    private final String name;
    private final Map<Ingredient,Float> mapIngredientToQuantity;

    public Beverage(String name,List<IngredientQuantityHolder> ingrediantDetails){
        this.name = name;
        this.ingrediantDetails = ingrediantDetails;
        this.mapIngredientToQuantity = ingrediantDetails.stream().collect(Collectors.toMap(IngredientQuantityHolder :: getIngredient, IngredientQuantityHolder :: getQuantity));

    }

    public List<Ingredient> getAllRequiredIngrediants(){
        return ingrediantDetails.stream().map(elem -> elem.getIngredient())
                .collect(Collectors.toList());
    }

    public List<IngredientQuantityHolder> getIngrediantDetails() {
        return ingrediantDetails;
    }

    public String getName() {
        return name;
    }

    public Float getQuantityForIngrediant(Ingredient ingrediant){
        return mapIngredientToQuantity.get(ingrediant);
    }

}
