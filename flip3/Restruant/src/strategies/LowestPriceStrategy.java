package strategies;

import exceptions.OrderProcessingException;
import models.Restaurant;
import java.util.*;

public class LowestPriceStrategy implements OrderStrategy  {
    @Override
    public Map<Restaurant, List<String>> selectRestaurants(List<String> items, List<Restaurant> restaurants) throws OrderProcessingException {
        Map<Restaurant, List<String>> allocation = new HashMap<>();

        for (String item : items) {
            Restaurant selectedRestaurant = null;
            int lowestPrice = Integer.MAX_VALUE;

            for (Restaurant restaurant : restaurants) {
                Map<String, Integer> menu = restaurant.getMenu();

                if (menu.containsKey(item) && menu.get(item) < lowestPrice && restaurant.getCurrentCapacity() > 0) {
                    selectedRestaurant = restaurant;
                    lowestPrice = menu.get(item);
                }
            }



            if (selectedRestaurant == null) {
                return new HashMap<>();
            }

            allocation.computeIfAbsent(selectedRestaurant, k -> new ArrayList<>()).add(item);
        }

        return allocation;
    }
}
