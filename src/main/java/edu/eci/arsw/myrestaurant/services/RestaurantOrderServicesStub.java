package edu.eci.arsw.myrestaurant.services;


import com.fasterxml.jackson.databind.util.JSONPObject;
import edu.eci.arsw.myrestaurant.beans.impl.BasicBillCalculator;
import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.model.ProductType;

import netscape.javascript.JSObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RestaurantOrderServicesStub implements RestaurantOrderServices {

    
    BillCalculator calc = new BasicBillCalculator();

    public RestaurantOrderServicesStub() {
    }

    public void setBillCalculator(BillCalculator calc) {
        this.calc = calc;
    }

    @Override
    public Order getTableOrder(int tableNumber) {
        if (!tableOrders.containsKey(tableNumber)) {
            return null;
        } else {
            return tableOrders.get(tableNumber);
        }
    }

    @Override
    public Set<String> getAvailableProductNames() {
        return productsMap.keySet();
    }

    @Override
    public RestaurantProduct getProductByName(String product) throws OrderServicesException {
        if (!productsMap.containsKey(product)) {
            throw new OrderServicesException("Producto no existente:" + product);
        } else {
            return productsMap.get(product);
        }
    }

    @Override
    public Set<Integer> getTablesWithOrders() {
        return tableOrders.keySet();
    }

    @Override
    public void addNewOrderToTable(Order o) throws OrderServicesException {
        if (tableOrders.containsKey(o.getTableNumber())) {
            throw new OrderServicesException("La mesa tiene una orden abierta. Debe "
                    + "cerrarse la cuenta antes de crear una nueva.:" + o.getTableNumber());
        } else {
            tableOrders.put(o.getTableNumber(), o);
        }

    }

    @Override
    public void releaseTable(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        } else {
            tableOrders.remove(tableNumber);
        }

    }

    @Override
    public int calculateTableBill(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        } else {
            return calc.calculateBill(tableOrders.get(tableNumber), productsMap);
        }
    }
    public ArrayList<ArrayList<Object>> getOrders() throws OrderServicesException {
        ArrayList<ArrayList<Object>>  totalOrders = new ArrayList<>();
        for(Integer order : tableOrders.keySet() ){
            ArrayList<Object> order2 = new ArrayList<>();
            ArrayList<String> orderL = new ArrayList<>();
            //orderL.put(o, tableOrders.get(order).getOrderAmountsMap().get(o));
            for(String o : tableOrders.get(order).getOrderAmountsMap().keySet()){
                orderL.add(o+": "+tableOrders.get(order).getOrderAmountsMap().get(o));
            }
            //orderL.addAll(tableOrders.get(order).getOrderAmountsMap().keySet());
            order2.add(""+order);
            order2.add(orderL);
            order2.add("Total: "+calculateTableBill(order));
            totalOrders.add(order2);
        }
        return totalOrders;
    }
    private static final Map<String, RestaurantProduct> productsMap;

    private static final Map<Integer, Order> tableOrders;
    

    static {
        productsMap = new ConcurrentHashMap<>();
        tableOrders = new ConcurrentHashMap<>();        
        productsMap.put("PIZZA", new RestaurantProduct("PIZZA", 10000, ProductType.DISH));
        productsMap.put("HOTDOG", new RestaurantProduct("HOTDOG", 3000, ProductType.DISH));
        productsMap.put("COKE", new RestaurantProduct("COKE", 1300, ProductType.DRINK));
        productsMap.put("HAMBURGER", new RestaurantProduct("HAMBURGER", 12300, ProductType.DISH));
        productsMap.put("BEER", new RestaurantProduct("BEER", 2500, ProductType.DRINK));

        Order o = new Order(1);
        o.addDish("PIZZA", 3);
        o.addDish("HOTDOG", 1);
        o.addDish("COKE", 4);

        tableOrders.put(1, o);

        Order o2 = new Order(3);
        o2.addDish("HAMBURGER", 2);
        o2.addDish("COKE", 2);

        tableOrders.put(3, o2);
    }

}
