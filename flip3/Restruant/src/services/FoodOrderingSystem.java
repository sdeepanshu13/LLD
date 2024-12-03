package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import models.*;
import strategies.*;
import exceptions.*;

public class FoodOrderingSystem {
    private final ConcurrentHashMap<String, Restaurant> restaurants;
    private OrderStrategy orderStrategy;
    private Map<String, List<String>> orders = new ConcurrentHashMap<>();

    public FoodOrderingSystem(OrderStrategy orderStrategy) {
        System.out.println("Initializing FoodOrderingSystem with OrderStrategy: " + orderStrategy.getClass().getSimpleName());
        this.restaurants = new ConcurrentHashMap<>();
        this.orderStrategy = orderStrategy;
    }

    public synchronized void addRestaurant(String name, Map<String, Integer> menu, int maxCapacity) {
        System.out.println("Adding restaurant: " + name + " with menu: " + menu + " and max capacity: " + maxCapacity);
        Restaurant restaurant = new RestaurantImpl(name, menu, maxCapacity);
        restaurants.put(name, restaurant);
        System.out.println("Restaurant " + name + " added. with capacity: " + restaurant.getMaxCapacity());
    }

    public synchronized void updateRestaurantMenu(String name, Map<String, Integer> newMenu) {
        System.out.println("Updating menu for restaurant: " + name + " with new menu: " + newMenu);
        Restaurant restaurant = restaurants.get(name);
        if (restaurant != null) {
            restaurant.updateMenu(newMenu);
            System.out.println("Menu updated for " + name);
        } else {
            System.out.println("Restaurant " + name + " not found for menu update.");
        }
    }

    public Restaurant getRestaurant(String name) {
        System.out.println("Getting restaurant: " + name);
        return restaurants.get(name);
    }

   public synchronized String placeOrder(List<String> items) throws OrderProcessingException {
    System.out.println("Placing order for items: " + items);
    Map<Restaurant, List<String>> allocation = orderStrategy.selectRestaurants(items, new ArrayList<>(restaurants.values()));

    if (allocation.isEmpty()) {
        throw new OrderProcessingException("Order cannot be fulfilled due to insufficient capacity.");
    }

    for (Map.Entry<Restaurant, List<String>> entry : allocation.entrySet()) {
        Restaurant restaurant = entry.getKey();
        List<String> itemsToServe = entry.getValue();


        orders.put(restaurant.getName(),itemsToServe);

        System.out.println("Allocating items " + itemsToServe + " to restaurant " + restaurant.getName());
        for (String item : itemsToServe) {
            restaurant.reduceCapacity(1);  // Reduce capacity for each item served
            System.out.println("Reduced capacity by 1 for " + restaurant.getName()+ " New capacity: " + restaurant.getCurrentCapacity());
        }
    }

    // Build a string with restaurant names
    StringBuilder restaurantNames = new StringBuilder();
    for (Restaurant restaurant : allocation.keySet()) {
        restaurantNames.append(restaurant.getName()).append(", ");
    }

    // Remove the trailing comma and space
    if (restaurantNames.length() > 0) {
        restaurantNames.setLength(restaurantNames.length() - 2);  // Remove last ", "
    }

    String result = "Order placed using [" + restaurantNames.toString() + "].";
    System.out.println(result);
    return result;
}


    public synchronized String getSystemStats() {
        System.out.println("Fetching system stats...");
        StringBuilder stats = new StringBuilder();
        for (Restaurant restaurant : restaurants.values()) {
            stats.append(restaurant.getName())
                    .append(": ")
                    .append(restaurant.getCurrentCapacity())
                    .append(" current capacity  ");
            System.out.println();
        }
        System.out.println("System Stats: \n" + stats);
        return stats.toString();
    }

    public synchronized void fulfillAllOrders(String rname) {
        System.out.println("Fulfilling orders for...   "+ rname);
        try {

                if (rname != null) {
                    for (int i = 0; i < orders.get(rname).size(); i++) {
                        List<String> items = orders.get(rname);
                        for (String item : items) {
                            getRestaurant(rname).replenishCapacity(1);
                            System.out.println("Fulfilled order for " + item + " at " + rname);
                        }
                    }
                }
                else throw new OrderProcessingException("Restaurant not found");
        } catch (Exception e) {
            System.err.println("Error while fulfilling orders: " + e.getMessage());
        }
    }


}
