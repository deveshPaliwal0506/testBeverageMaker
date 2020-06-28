package com.dunzo.coffeemaker.services;

public class bckUp {

     /*  private static final long INGREDIENT_CONTAINER_LOCK_TIMEOUT = 20000;
      private static final TimeUnit INGREDIENT_CONTAINER_LOCK_TIMEUNIT = TimeUnit.MILLISECONDS;
      private static final int FIXED_DELAY_CONTAINER_REDUCE_LOCK = 200;
      private static final int MAX_RANDOM_NUMBER_NANO_RANGE = 999999;
      private final Random rnd = new Random();

      private Map<Ingredient, ReadWriteLock> lockForEachContainer;

      lockForEachContainer.put(ing.getIngredient(), new ReentrantReadWriteLock());

      */


    /*
    Idea behind sending the list
    is to maintain the ordering.
    This will release and acquire lock in same order
    For specific beverage order
    */
    /*public BevarageResult fetchAndReduceIngredient(List<IngredientQuantityHolder> ingredientToQuantity) throws InterruptedException{

        boolean lockAcquired = true;
        int counterTillLockAcquired = 0;

        long stopTime = System.nanoTime() + INGREDIENT_CONTAINER_LOCK_TIMEUNIT.toNanos(INGREDIENT_CONTAINER_LOCK_TIMEOUT);

        while(true) {
            try {
                for (IngredientQuantityHolder ingrediantForUse : ingredientToQuantity) {
                    //System.out.println("ingredient -> "+ingrediantForUse.getIngredient().getIngredientName()+"  lock -> "+lockForEachContainer.get(ingrediantForUse.getIngredient()));
                    lockAcquired = lockAcquired && lockForEachContainer.get(ingrediantForUse.getIngredient()).writeLock().tryLock();
                    if (!lockAcquired) {
                        break;
                    }
                    else
                        counterTillLockAcquired++;
                }
                if (lockAcquired) {
                    List<IngredientQuantityHolder> lstOfInsufficientIngredients = ingredientToQuantity.stream().filter(elem ->
                            mapIngredientToContainer.get(elem.getIngredient()).quantity < elem.getQuantity())
                            .collect(Collectors.toList());

                    if (lstOfInsufficientIngredients.size() > 0) {
                       return new BevarageResult(false,Optional.of("Insufficient Ingredient - "+lstOfInsufficientIngredients.get(0).getIngredient().getIngredientName()));
                    }

                    for (IngredientQuantityHolder ingrediantForUse : ingredientToQuantity) {
                        IngredientContainer container = mapIngredientToContainer.get(ingrediantForUse.getIngredient());
                        container.fetchAndReduceQuantity(ingrediantForUse.getQuantity());
                    }
                    return new BevarageResult(true,Optional.empty());
                }
            } finally {
                while (counterTillLockAcquired > 0) {
                    lockForEachContainer.get(ingredientToQuantity.get(counterTillLockAcquired - 1).getIngredient()).writeLock().unlock();
                    counterTillLockAcquired--;
                }
            }

            if(System.nanoTime() > stopTime)
                return new BevarageResult(false, Optional.of("Could not get Bevarage container"));

            Thread.sleep(FIXED_DELAY_CONTAINER_REDUCE_LOCK,rnd.nextInt(MAX_RANDOM_NUMBER_NANO_RANGE));
        }
    }*/

}
