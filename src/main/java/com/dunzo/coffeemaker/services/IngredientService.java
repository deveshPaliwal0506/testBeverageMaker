package com.dunzo.coffeemaker.services;

import com.dunzo.coffeemaker.configuration.MachineConfigurationProperties;
import com.dunzo.coffeemaker.core.Ingredient;
import com.dunzo.coffeemaker.core.IngredientFetchResult;
import com.dunzo.coffeemaker.core.IngredientQuantityHolder;
import com.dunzo.coffeemaker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Service
public class IngredientService {


    //This is map of Ingredient to its corresponding container.
    private Map<Ingredient, IngredientContainer> mapIngredientToContainer;
    //A Lock, that needs to be acquired to refill or fetch ingredient
    //from ingredient container
    private final ReadWriteLock ingredientLock = new ReentrantReadWriteLock();

    private MachineConfigurationProperties properties;

    @Autowired
    public IngredientService(MachineConfigurationProperties properties) {
        this.properties = properties;

        mapIngredientToContainer = new ConcurrentHashMap<>();
        for (IngredientQuantityHolder ing : properties.getIngredientQuantityHolders()) {
            mapIngredientToContainer.put(ing.getIngredient(), new IngredientContainer(ing.getQuantity(), ing.getQuantity()));
        }
    }

    public float getIngredientQuantity(Ingredient ingredient) {
        if (isIngredientValid(ingredient))
            return mapIngredientToContainer.get(ingredient).quantity;

        throw new IllegalArgumentException("Invalid Ingredient");
    }

    public float getMaxIngredientQuantity(Ingredient ingredient) {
        if (isIngredientValid(ingredient))
            return mapIngredientToContainer.get(ingredient).maxQuantity;

        throw new IllegalArgumentException("Invalid Ingredient");
    }

    public boolean isIngredientValid(Ingredient ingredient) {
        return mapIngredientToContainer.containsKey(ingredient);
    }

    public void refillIngredient(Ingredient ingred, float quantity) {

        IngredientContainer container = mapIngredientToContainer.get(ingred);

        if (isIngredientValid(ingred)) {

            ingredientLock.writeLock().lock();
            try {
                if (container.maxQuantity >= container.quantity + quantity)
                    container.refillContainer(quantity);
                else
                    throw new IllegalArgumentException("Quantity over flow");
            }finally {
                ingredientLock.writeLock().unlock();
            }
            return;
        }
        throw new IllegalArgumentException("Invalid Ingredient");
    }

    /*
    An entry point to fetch all the ingredients from corresponding container.
    Only when lock is achieved on the re-entrant lock, ingredients can be retrieved.
     */
    public IngredientFetchResult fetchAndReduceIngredientWithSingleLock(List<IngredientQuantityHolder> ingredientToQuantity) throws InterruptedException {

        List<IngredientQuantityHolder> lstOfNonAvailableIngredients =
                ingredientToQuantity.stream().filter(elem ->
                        !mapIngredientToContainer.containsKey(elem.getIngredient()))
                        .collect(Collectors.toList());

        //Check if All Ingredients are present, if not
        //return un available.
        if (lstOfNonAvailableIngredients.size() > 0) {
            return new IngredientFetchResult(false, Optional.of(String.format(Constants.INGREDIENT_UNAVAILABLE_MESSAGE, lstOfNonAvailableIngredients.stream().map(elem -> elem.getIngredient().getIngredientName()).collect(Collectors.toList()).toString())));
        }

        //Take the lock on in gredient service
        //Fetching of ingredient.
        ingredientLock.writeLock().lock();

        try {

            List<IngredientQuantityHolder> lstOfInsufficientIngredients = ingredientToQuantity.stream().filter(elem ->
                    mapIngredientToContainer.get(elem.getIngredient()).quantity < elem.getQuantity())
                    .collect(Collectors.toList());

            //If any ingredient is less than required.
            //Return the Result as false.
            if (lstOfInsufficientIngredients.size() > 0) {
                return new IngredientFetchResult(false, Optional.of(String.format(Constants.INGREDIENT_QUANTITY_FINISHED_MESSAGE,lstOfInsufficientIngredients.stream().map(elem -> elem.getIngredient().getIngredientName()).collect(Collectors.toList()).toString())));
            }

            //For each required Ingredient
            //Reduce the quantity of ingredient in container.
            for (IngredientQuantityHolder ingrediantForUse : ingredientToQuantity) {
                IngredientContainer container = mapIngredientToContainer.get(ingrediantForUse.getIngredient());
                container.fetchAndReduceQuantity(ingrediantForUse.getQuantity());
            }
            return new IngredientFetchResult(true, Optional.empty());
        } finally {
            ingredientLock.writeLock().unlock();
        }
    }

    /*
    Ingredient Container, holds the container
    for each ingredient. Values are reduced from container when they are fetched.
     */
    private class IngredientContainer {

        private volatile float quantity;
        private float maxQuantity;

        private IngredientContainer(float quantity, float maxQuantity) {
            this.quantity = quantity;
            this.maxQuantity = maxQuantity;
        }

        private void refillContainer(float quantity) {
            this.quantity = this.quantity + quantity;
        }

        private void fetchAndReduceQuantity(float quantity) {
            this.quantity = this.quantity - quantity;
        }

    }

}
