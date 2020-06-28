package com.dunzo.coffeemaker.services;

import com.dunzo.coffeemaker.configuration.BevarageApplication;
import com.dunzo.coffeemaker.configuration.MachineConfigurationProperties;
import com.dunzo.coffeemaker.core.BevarageResult;
import com.dunzo.coffeemaker.core.Beverage;
import com.dunzo.coffeemaker.core.Ingredient;
import com.dunzo.coffeemaker.core.IngredientQuantityHolder;
import com.dunzo.coffeemaker.services.BevarageOrderOperator;
import com.dunzo.coffeemaker.services.IngredientService;
import com.dunzo.coffeemaker.utils.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { BevarageApplication.class },
        initializers = { ConfigFileApplicationContextInitializer.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BevarageOperatorTest {

    @Autowired
    BevarageOrderOperator operator;

    @Autowired
    IngredientService ingredientService;

    @Autowired
    MachineConfigurationProperties configurationProperties;

    private static final String BEVERAGE_HOT_TEA = "hot_tea";
    private static final String BEVERAGE_GREEN_TEA = "green_tea";

    private static final String GREEN_MIXTURE_UNAVAILABLE = "green_mixture";
    private static final String HOT_WATER_INGREDIENT = "hot_water";

    private static final String HOT_TEA_SUCCESS_MESSAGE = String.format(Constants.SUCCESSFULL_BEVERAGE_CREATION,BEVERAGE_HOT_TEA);
    private static final String GREEN_TEA_UNAVAILABLE_MESSAGE = String.format(Constants.CANNOT_BE_PREPARED,BEVERAGE_GREEN_TEA).concat(
            String.format(Constants.INGREDIENT_UNAVAILABLE_MESSAGE, Arrays.asList(GREEN_MIXTURE_UNAVAILABLE)));


    @Test
    public void testBeverageCreation_Success_Scenario_For_HOT_TEA_As_Ingredients_Available() throws Exception{

        List<Beverage> beverages = configurationProperties.getListOfAllBeverages()
                .stream().filter(beverage -> beverage.getName().equals(BEVERAGE_HOT_TEA))
                .collect(Collectors.toList());

        List<BevarageResult> result = operator.orderGivenBeverages(beverages);

        for (BevarageResult res:result) {
            Assert.assertTrue(res.isBevarageCreated());
            Assert.assertTrue(res.getBevarageName().equals(BEVERAGE_HOT_TEA));
            Assert.assertEquals(res.getCreationMessage(),HOT_TEA_SUCCESS_MESSAGE);
        }
    }

    @Test
    public void testBeverageCreation_Unavailable_Scenario_For_GREEN_TEA_As_Ingredients_UnAvailable() throws Exception{

        List<Beverage> beverages = configurationProperties.getListOfAllBeverages()
                .stream().filter(beverage -> beverage.getName().equals(BEVERAGE_GREEN_TEA))
                .collect(Collectors.toList());

        List<BevarageResult> result = operator.orderGivenBeverages(beverages);

        for (BevarageResult res:result) {
            Assert.assertFalse(res.isBevarageCreated());
            Assert.assertTrue(res.getBevarageName().equals(BEVERAGE_GREEN_TEA));
            Assert.assertEquals(res.getCreationMessage(),GREEN_TEA_UNAVAILABLE_MESSAGE);
        }
    }

    @Test
    public void testBeverageCreation_Scenario_For_All_Ingredients() throws Exception{

        List<Beverage> beverages = configurationProperties.getListOfAllBeverages();
        List<BevarageResult> result = operator.orderGivenBeverages(beverages);
        int countofBeverageCreationSuccess = 0;

        for (BevarageResult res:result) {
            if(res.isBevarageCreated())
                countofBeverageCreationSuccess++;

            System.out.println(res.getCreationMessage());
        }

        Assert.assertTrue(countofBeverageCreationSuccess > 0);
    }

    @Test
    public void testBeverageCreation_Scenario_For_All_Ingredients_And_Refill_For_A_Drink_Again() throws Exception{

        List<Beverage> beverages = configurationProperties.getListOfAllBeverages();
        List<BevarageResult> result = operator.orderGivenBeverages(beverages);
        int countofBeverageCreationSuccess = 0;
        Set<String> beveragesFailed = new HashSet<>();

        for (BevarageResult res:result) {
            if(res.isBevarageCreated())
                countofBeverageCreationSuccess++;
            else
                beveragesFailed.add(res.getBevarageName());

            System.out.println(res.getCreationMessage());
        }

        Assert.assertTrue(countofBeverageCreationSuccess > 0);

        System.out.println(" Refill the Elements of Beverages that failed ");

        //This will refill beverages that were failed to create.
        //These are taken from beverages that failed in previous iteration.
        for (String bev:beveragesFailed) {
            Beverage beverage = beverages.stream().filter(elem -> elem.getName().equals(bev)).findFirst().get();
            beverage.getAllRequiredIngrediants().stream()
                    .forEach(ing -> {
                        if(ingredientService.isIngredientValid(ing))
                        ingredientService.refillIngredient(ing,ingredientService.getMaxIngredientQuantity(ing)
                                - ingredientService.getIngredientQuantity(ing));
                    });
        }

        List<Beverage> secondTryBeverageCreation = beverages.stream().filter(eachBeverage -> beveragesFailed.contains(eachBeverage.getName()))
                .collect(Collectors.toList());

        List<BevarageResult> resultForSecondLot = operator.orderGivenBeverages(secondTryBeverageCreation);
        int countofBeverageCreationSuccessSecondLot = 0;
        Set<String> beveragesFailedSecondLot = new HashSet<>();

        for (BevarageResult res:resultForSecondLot) {
            if(res.isBevarageCreated())
                countofBeverageCreationSuccessSecondLot++;
            else
                beveragesFailedSecondLot.add(res.getBevarageName());

            System.out.println(res.getCreationMessage());
        }

        Assert.assertTrue(countofBeverageCreationSuccessSecondLot > 0);

    }

}
