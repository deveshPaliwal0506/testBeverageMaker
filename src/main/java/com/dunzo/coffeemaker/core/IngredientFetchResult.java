package com.dunzo.coffeemaker.core;

import java.util.Optional;

public class IngredientFetchResult {

    private final boolean allIngredientsFetched;
    private final Optional<String> failureReason;

    public IngredientFetchResult(boolean allIngredientsFetched,Optional<String> failureReason){
        this.allIngredientsFetched = allIngredientsFetched;
        this.failureReason = failureReason;
    }

    public boolean areAllIngredientsFetched() {
        return allIngredientsFetched;
    }
    public Optional<String> getFailureReason() {
        return failureReason;
    }

}
