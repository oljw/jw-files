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
        launchViewOnClick(event, "/view/PartAddView.fxml");
    }
    
    @FXML
    private void onModifyPartClick(ActionEvent event) throws IOException {
        Part part = tableview_part.getSelectionModel().getSelectedItem();
        if (part == null) {
            showError("Please select a part from table.");
            return;
        }
        Inventory.setSelectedPartIndex(Inventory.getParts().indexOf(part));
        launchViewOnClick(event, "/view/PartModifyView.fxml");
    }

    @FXML
    private void onDeletePartClick(ActionEvent event) {
        Part part = tableview_part.getSelectionModel().getSelectedItem();
        if (part == null) {
            showError("Please select a part from table.");
            return;
        }
        Inventory.deletePart(part);
        updatePartTableview();
    }

    @FXML
    private void onSearchPartClick(ActionEvent event) {
        Part part = Inventory.lookupPart(tf_part_search.getText());
        if (part == null) {
            showError("Result not found.");
            return;
        }
        tableview_part.requestFocus();
        tableview_part.getSelectionModel().select(part);
    }

    @FXML
    private void onAddProductClick(ActionEvent event) throws IOException {
        launchViewOnClick(event, "/view/ProductAddView.fxml");
    }

    @FXML
    private void onModifyProductClick(ActionEvent event) {
    }

    @FXML
    private void onDeleteProductClick(ActionEvent event) {
    }

    @FXML
    private void onSearchProductClick(ActionEvent event) {
    }

    @FXML
    private void onExitButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) System.exit(0);
    }
    
    private void launchViewOnClick(ActionEvent event, String fxml) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource(fxml));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait(); 
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
