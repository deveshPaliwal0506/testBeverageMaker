package com.dunzo.coffeemaker.services;

import com.dunzo.coffeemaker.configuration.MachineConfigurationProperties;
import com.dunzo.coffeemaker.core.*;
import com.dunzo.coffeemaker.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/*
This is a callable for each beverage.
Each Beverage creation request, is a new
Callable request submitted to Executor service.
 */
public class BeverageCreatorService implements Callable<BevarageResult> {

    private Beverage beverageToCreate;
    private IngredientService ingredientService;

    /*
    @beverageToCreate - Beverage name and configurations details.
    @ingredientService - Service to fdtch ingredients, shared by all Beverage creation services.
     */
    public BeverageCreatorService(Beverage beverageToCreate,
                                  IngredientService ingredientService){
        this.beverageToCreate = beverageToCreate;
        this.ingredientService = ingredientService;
    }

    @Override
    public BevarageResult call() throws Exception {
        IngredientFetchResult result = ingredientService.fetchAndReduceIngredientWithSingleLock(beverageToCreate.getIngrediantDetails());

        //Creates the Message for beverage creation.
        //This could be success or failure.
        //There are two types of failure,
        //1. Ingredient Unavailable
        //2. Ingredient Finished.
        String messageOfBeverageCreation = result.areAllIngredientsFetched() ? String.format(Constants.SUCCESSFULL_BEVERAGE_CREATION,beverageToCreate.getName())
        : String.format(Constants.CANNOT_BE_PREPARED,beverageToCreate.getName()).concat(result.getFailureReason().orElse(""));

        BevarageResult bevarageResult = new BevarageResult(result.areAllIngredientsFetched(),beverageToCreate.getName()
        ,messageOfBeverageCreation);

        return bevarageResult;
    }
}
