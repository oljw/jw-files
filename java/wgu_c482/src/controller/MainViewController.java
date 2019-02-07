/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 *
 * @author JW
 */
public class MainViewController implements Initializable {

    @FXML
    private TextField tf_part_search;
    @FXML
    private TableView<Part> tableview_part;
    @FXML
    private TableColumn<Part, Integer> col_part_id;
    @FXML
    private TableColumn<Part, String> col_part_name;
    @FXML
    private TableColumn<Part, Integer> col_part_inventory;
    @FXML
    private TableColumn<Part, Double> col_part_price;
    @FXML
    private TextField tf_product_search;
    @FXML
    private TableView<Product> tableview_product;
    @FXML
    private TableColumn<Product, Integer> col_product_id;
    @FXML
    private TableColumn<Product, String> col_product_name;
    @FXML
    private TableColumn<Product, Integer> col_product_inventory;
    @FXML
    private TableColumn<Product, Double> col_product_price;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updatePartTableview();
        updateProductTableview();
    }    

    @FXML
    private void onAddPartClick(ActionEvent event) throws IOException {
        Util.launchView(FXMLLoader.load(getClass().getResource("/view/PartAddView.fxml")), event);
    }
    
    @FXML
    private void onModifyPartClick(ActionEvent event) throws IOException {
        Part part = Util.getSelectedPart(tableview_part);
        if (part != null) {
            Inventory.setSelectedPartIndex(Inventory.getParts().indexOf(part));
            Util.launchView(FXMLLoader.load(getClass().getResource("/view/PartModifyView.fxml")), event);
        }
    }

    @FXML
    private void onDeletePartClick(ActionEvent event) {
        Part part = Util.getSelectedPart(tableview_part);
        if(part != null) {
            Inventory.deletePart(part);
            updatePartTableview();
        }
    }

    @FXML
    private void onSearchPartClick(ActionEvent event) {
        if (tf_part_search.getText().isEmpty()) return;
        Util.searchPart(tf_part_search, tableview_part);
    }

    @FXML
    private void onAddProductClick(ActionEvent event) throws IOException {
        Util.launchView(FXMLLoader.load(getClass().getResource("/view/ProductAddView.fxml")), event);

    }

    @FXML
    private void onModifyProductClick(ActionEvent event) {
    }

    @FXML
    private void onDeleteProductClick(ActionEvent event) {
        Product product = tableview_product.getSelectionModel().getSelectedItem();
        if (product == null) {
            Util.showError("Please select a part from table.");
            return;
        }
        Inventory.removeProduct(Inventory.getProducts().indexOf(product));
        updateProductTableview();
    }

    @FXML
    private void onSearchProductClick(ActionEvent event) {
        try {
            if (tf_product_search.getText().isEmpty()) return;
            Product product = Inventory.lookupProduct(Integer.parseInt(tf_product_search.getText()));
            if (product == null) {
                Util.showError("Result not found.");
                return;
            }
            tableview_product.requestFocus();
            tableview_product.getSelectionModel().select(product);
        } catch(NumberFormatException e) {
            Util.showError("Result not found. Search by Part ID.");
        }
    }

    @FXML
    private void onExitButtonClick(ActionEvent event) {
        Util.askExit();
    }
    
    private void updatePartTableview() {
        col_part_id.setCellValueFactory(new PropertyValueFactory<>("partId"));
        col_part_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_part_inventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        col_part_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableview_part.setItems(Inventory.getParts());
    }

    private void updateProductTableview() {
        col_product_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        col_product_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_product_inventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        col_product_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        tableview_product.setItems(Inventory.getProducts());
    }
}
