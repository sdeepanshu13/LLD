package models;

import java.util.Map;

public interface Restaurant {
    String getName();
    Map<String, Integer> getMenu();
    int getCurrentCapacity();
    void reduceCapacity(int amount);
    void replenishCapacity(int amount);
    int getMaxCapacity();
    void updateMenu(Map<String, Integer> newMenu);
}
