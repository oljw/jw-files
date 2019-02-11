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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author JW
 */
public class Product {
    private static final String TAG = "Product.java";

    private ObservableList<Part> parts = FXCollections.observableArrayList();
    private IntegerProperty productId = new SimpleIntegerProperty();;
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty price = new SimpleDoubleProperty();
    private IntegerProperty inventory = new SimpleIntegerProperty();
    private IntegerProperty min = new SimpleIntegerProperty();
    private IntegerProperty max = new SimpleIntegerProperty();

    public Product(int productId, String name, double price, int inventory, int min, int max) {
        this.productId.set(productId);
        this.name.set(name);
        this.price.set(price);
        this.inventory.set(inventory);
        this.min.set(min);
        this.max.set(max);
    }
    
    public void addPart(Part part) {
        this.parts.add(part);
    }

    public boolean removePart(int id) {
        return false;
    }
    
    public Part lookupPart(int id) {
        for (Part part : parts) {
            if (part.getPartId() == id) {
                return part;
            }
        }
        return null;
    }
    
    public ObservableList<Part> getParts() {
        return parts;
    }

    public void setParts(ObservableList<Part> parts) {
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
