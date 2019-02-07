/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author JW
 */
public class Product {
    private static final String TAG = "Product.java";

    private List<Part> parts = new ArrayList<>();
    private IntegerProperty productId;
    private StringProperty name;
    private DoubleProperty price;
    private IntegerProperty inventory;
    private IntegerProperty min;
    private IntegerProperty max;

    public Product(IntegerProperty productId, StringProperty name, DoubleProperty price, IntegerProperty min, IntegerProperty max) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.min = min;
        this.max = max;
    }
    
    public void addPart(Part part) {
        this.parts.add(part);
    }

    public boolean removePart(int id) {
        return false;
    }
    
    public Part lookupPart(int id) {
        return null;
    }
    
    public List<Part> getParts() {
        return parts;
    }

    public void setParts(List<Part> parts) {
        this.parts = parts;
    }
    
    public int getProductId() {
        return productId.get();
    }

    public void setProductId(int productId) {
        this.productId.set(productId);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public double getPrice() {
        return price.get();
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getInventory() {
        return inventory.get();
    }

    public void setInventory(int inventory) {
        this.inventory.set(inventory);
    }

    public int getMin() {
        return min.get();
    }

    public void setMin(int min) {
        this.min.set(min);
    }

    public int getMax() {
        return max.get();
    }

    public void setMax(int max) {
        this.max.set(max);
    }
}
