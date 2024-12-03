package strategies;

import exceptions.OrderProcessingException;
import models.Restaurant;
import java.util.List;
import java.util.Map;

public interface OrderStrategy {
    Map<Restaurant, List<String>> selectRestaurants(List<String> items, List<Restaurant> restaurants) throws OrderProcessingException;
}
