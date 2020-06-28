package com.dunzo.coffeemaker.configuration;

import com.dunzo.coffeemaker.core.Beverage;
import com.dunzo.coffeemaker.core.Ingredient;
import com.dunzo.coffeemaker.core.IngredientQuantityHolder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
This replicates the Configurations
of Machine from YML to Java Objects.
All the classes across applications fetches
machine details from here.

e.g -> Ingredients loaded in Machine.
 -> Configuration of each beverage given.
 -> Number of outlets in machine.
 */

@ConfigurationProperties("machine")
public class MachineConfigurationProperties {

    private Map<String,Float> total_items_quantity;
    private Map<String,Map<String,Float>> beverages;
    private Map<String,Integer> outlets;

    private List<IngredientQuantityHolder> ingredientQuantityHolders;
    private Integer numberOfOutLets;
    private List<Beverage> listOfAllBeverages;

    @PostConstruct
    public void init(){

        ingredientQuantityHolders =
                total_items_quantity.keySet().stream().map(elem ->
                        new IngredientQuantityHolder(new Ingredient(elem),total_items_quantity.get(elem)))
                .collect(Collectors.toList());

        numberOfOutLets = outlets.get(outlets.keySet().stream().findFirst().get());

        listOfAllBeverages = beverages.keySet().stream().map(elem -> new Beverage(elem,beverages.get(elem).entrySet().stream()
        .map(key -> new IngredientQuantityHolder(new Ingredient(key.getKey()),key.getValue())).collect(Collectors.toList())))
                .collect(Collectors.toList());

    }

    public void setTotal_items_quantity(Map<String, Float> total_items_quantity) {
        this.total_items_quantity = total_items_quantity;
    }
    public void setBeverages(Map<String, Map<String, Float>> beverages) {
        this.beverages = beverages;
    }
    public void setOutlets(Map<String, Integer> outlets) {
        this.outlets = outlets;
    }

    public List<IngredientQuantityHolder> getIngredientQuantityHolders() {
        return ingredientQuantityHolders;
    }

    public Integer getNumberOfOutLets() {
        return numberOfOutLets;
    }

    public List<Beverage> getListOfAllBeverages() {
        return listOfAllBeverages;
    }

}
