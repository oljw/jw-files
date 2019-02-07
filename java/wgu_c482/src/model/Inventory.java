/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author JW
 */
public final class Inventory {
    private static final String TAG = "Inventory.java";

    private static ObservableList<Part> parts = FXCollections.observableArrayList();
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    private static int selectedPartIndex;

    private static int selectedProductIndex;
    
    public static ObservableList<Part> getParts() { return Inventory.parts; }

    public static ObservableList<Product> getProducts() { return Inventory.products;}

    public static int getSelectedPartIndex() {
        return Inventory.selectedPartIndex;
    }
    
    public static void setSelectedPartIndex(int selectedPartIndex) {
        Inventory.selectedPartIndex = selectedPartIndex;
    }
    
    public static void addPart(Part part) {
        Inventory.parts.add(part);
    }
    
    public static boolean deletePart(Part part) {
        if (Inventory.parts.contains(part))
            Inventory.parts.remove(part);
        else
            return false;
        return true;
    }
    
    public static Part lookupPart(String search) {
        for (Part part : Inventory.parts) {
            if (part.getName().contains(search)) {
                return part;
            }
        }
        return null;
    }
    
    public static void updatePart(int id, Part part) {
        Inventory.parts.set(id, part);
    }
    
    public static void addProduct(Product product) {
        Inventory.products.add(product);
    }
    
    public static boolean removeProduct(int id) {
        Inventory.products.remove(id);
        return false;
    }
    
    public static Product lookupProduct(int id) {
        return null;
    }
    
    public static void updateProduct(int id, Product product) {
        Inventory.products.set(id, product);
    }
    
    public static int getSelectedProductIndex() {
        return Inventory.selectedProductIndex;
    }
    
    public static void setSelectedProductIndex(int selectedProductIndex) {
        Inventory.selectedProductIndex = selectedProductIndex;
    }
}
