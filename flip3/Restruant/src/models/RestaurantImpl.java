package models;

import java.util.Map;

public class RestaurantImpl implements Restaurant {
    private String name;
    private Map<String, Integer> menu;
    private int currentCapacity;
    private final int maxCapacity;

    public RestaurantImpl(String name, Map<String, Integer> menu, int maxCapacity) {
        this.name = name;
        this.menu = menu;
        this.currentCapacity = maxCapacity;
        this.maxCapacity = maxCapacity;
    }

    @Override
    public synchronized String getName() {
        return name;
    }

    @Override
    public Map<String, Integer> getMenu() {
        return menu;
    }

    @Override
    public synchronized int getCurrentCapacity() {
        return currentCapacity;
    }

    @Override
    public synchronized void reduceCapacity(int amount) {

            this.currentCapacity -= amount;

    }

    @Override
    public synchronized void replenishCapacity(int amount) {
        if (this.currentCapacity + amount <= this.maxCapacity) {
            this.currentCapacity += amount;
        }
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    @Override
    public void updateMenu(Map<String, Integer> newMenu) {
        this.menu = newMenu;
    }
}
