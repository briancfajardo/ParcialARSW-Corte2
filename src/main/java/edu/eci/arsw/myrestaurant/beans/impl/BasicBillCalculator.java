package edu.eci.arsw.myrestaurant.beans.impl;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class BasicBillCalculator implements BillCalculator {


	//public Map<Integer, Order>  getOrders(){
	//	return new HashMap<>();
	//}
	@Override
	public int calculateBill(Order o,Map<String,RestaurantProduct> productsMap) {
		int total=0;
		for (String p:o.getOrderedDishes()){
                    RestaurantProduct rp=productsMap.get(p);
                    total+=o.getDishOrderedAmount(p)*rp.getPrice();                    
		}
		return total;
	}

}
