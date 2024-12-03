

import  services.FoodOrderingSystem;
import  strategies.*;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            FoodOrderingSystem system = new FoodOrderingSystem(new LowestPriceStrategy());


            system.addRestaurant("A2B", Map.of("Idly", 40, "Vada", 30, "Paper Plain Dosa", 50), 1);
            System.out.println();
            system.addRestaurant("Rasaganga", Map.of("Idly", 45, "Set Dosa", 60, "Poori", 25), 1);
             System.out.println();
            system.addRestaurant("Eat Fit", Map.of("Idly", 30, "Vada", 40), 1);
             System.out.println();
              System.out.println("***********************************************************");
               System.out.println();

           // system.placeOrder(List.of("Idly", "Poori"));
            // System.out.println();
            //system.placeOrder(List.of("Idly", "Vada"));
             //System.out.println();
             //system.placeOrder(List.of("Idly"));
            //system.getSystemStats();

            system.placeOrder(List.of("Idly"));
            system.getSystemStats();

            system.placeOrder(List.of("Idly"));
            system.getSystemStats();



            system.placeOrder(List.of("Idly"));
            system.getSystemStats();

            system.placeOrder(List.of("Idly"));
            system.getSystemStats();
             //System.out.println();


            system.fulfillAllOrders("Rasaganga");
            System.out.println();
            system.fulfillAllOrders("A2B");
            System.out.println();

             system.placeOrder(List.of("Idly"));
            system.getSystemStats();



            system.getSystemStats();
             System.out.println();

            system.updateRestaurantMenu("Eat Fit", Map.of("Idly", 60, "Vada", 40));
             System.out.println();
            system.placeOrder(List.of("Idly"));
             System.out.println();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}





/*

--Onboard new restaurant with its menu and item processing capacity. The menu should be reflected in the food ordering system.
--A restaurant should be able to change its menu.
--Customers should be able to place an order by giving items. We can assume that each item will be of only 1 quantity.
--Implement one restaurant selection strategy (lowest price offered by the restaurant for that item.) Have the option of selection using a different strategy.
--System should be able to keep track of all items served by each restaurant, and the system should be aware of the remaining capacity of each restaurant at a given time.
--Once the order is fulfilled by the restaurant, the capacity should be replenished for the given restaurant.

 */