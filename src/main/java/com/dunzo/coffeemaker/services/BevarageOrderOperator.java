package com.dunzo.coffeemaker.services;

import com.dunzo.coffeemaker.configuration.MachineConfigurationProperties;
import com.dunzo.coffeemaker.core.BevarageResult;
import com.dunzo.coffeemaker.core.Beverage;
import com.dunzo.coffeemaker.core.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class BevarageOrderOperator {

    @Autowired
    private MachineConfigurationProperties machineConfigurationProperties;
    @Autowired
    private IngredientService ingredientService;

    private ExecutorService executor;

    /*
    Number of fixed threads are initiated, as the number
    of outlets.
     */
    @PostConstruct
    public void init(){
        executor = Executors.newFixedThreadPool(machineConfigurationProperties.getNumberOfOutLets());
    }

    /*
    Takes all beverages from input YAML
    and submit to create all these Bevarages.
     */
    public List<BevarageResult> initiateBevarageCreation() throws InterruptedException,ExecutionException{

        List<Beverage> allBevaragesForOrder = machineConfigurationProperties.getListOfAllBeverages();
        List<BeverageCreatorService> allCallables =
                allBevaragesForOrder.stream().map(order -> new BeverageCreatorService(order,ingredientService))
                .collect(Collectors.toList());

        List<Future<BevarageResult>> allResults = executor.invokeAll(allCallables);
        executor.shutdown();

        List<BevarageResult> executedResults = new ArrayList<>();
        for (Future<BevarageResult> result:allResults) {
            executedResults.add(result.get());
        }
        return executedResults;
    }

    /*
    Takes the list of beverages that needs to be created, submitted as order
    Final result is returned.
     */
    public List<BevarageResult> orderGivenBeverages(List<Beverage> allBevaragesForOrder) throws InterruptedException,ExecutionException{
        List<BeverageCreatorService> allCallables =
                allBevaragesForOrder.stream().map(order -> new BeverageCreatorService(order,ingredientService))
                        .collect(Collectors.toList());

        List<Future<BevarageResult>> allResults = executor.invokeAll(allCallables);

        List<BevarageResult> executedResults = new ArrayList<>();
        for (Future<BevarageResult> result:allResults) {
            executedResults.add(result.get());
        }
        return executedResults;
    }


}
