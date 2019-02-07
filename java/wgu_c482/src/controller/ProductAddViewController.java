/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author JW
 */
public class ProductAddViewController implements Initializable {

    @FXML
    private TextField tf_id;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_inventory;
    @FXML
    private TextField tf_price;
    @FXML
    private TextField tf_min_inventory;
    @FXML
    private TextField tf_max_inventory;
    @FXML
    private TextField tf_search_part;
    @FXML
    private TableView<Part> tableview_add;
    @FXML
    private TableColumn<Part, Integer> col_add_part_id;
    @FXML
    private TableColumn<Part, String> col_add_part_name;
    @FXML
    private TableColumn<Part, Integer> col_add_part_inventory;
    @FXML
    private TableColumn<Part, Double> col_add_part_price;
    @FXML
    private TableView<Part> tableview_del;
    @FXML
    private TableColumn<Part, Integer> col_del_part_id;
    @FXML
    private TableColumn<Part, String> col_del_part_name;
    @FXML
    private TableColumn<Part, Integer> col_del_part_inventory;
    @FXML
    private TableColumn<Part, Double> col_del_part_price;

    private ObservableList<Part> chosenParts = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_id.setText(Inventory.getProducts().size() + "");
        tf_name.requestFocus();
        
        updateAddTableview();
        updateDelTableview();
    }    

    @FXML
    private void onAddPartClick(ActionEvent event) {
        Part part = Util.getSelectedPart(tableview_add);
        if (part != null) {
            chosenParts.add(part);
            updateDelTableview();
        }
    }

    @FXML
    private void onDeletePartClick(ActionEvent event) {
        Part part = Util.getSelectedPart(tableview_del);
        if (part != null) {
            chosenParts.remove(part);
            updateDelTableview();
        }
    }

    @FXML
    private void onSaveClick(ActionEvent event) throws IOException {
        try {
            Product product = new Product(
                    Integer.parseInt(tf_id.getText()),
                    tf_name.getText(),
                    Double.parseDouble(tf_price.getText()),
                    Integer.parseInt(tf_inventory.getText()),
                    Integer.parseInt(tf_min_inventory.getText()),
                    Integer.parseInt(tf_max_inventory.getText()),
                    chosenParts
            );
            
            Inventory.addProduct(product);
            Util.launchView(FXMLLoader.load(getClass().getResource("/view/MainView.fxml")), event);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Form contains invalid fields.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void onCancelClick(ActionEvent event) throws IOException {
        if (Util.askCancel()) {
            Util.launchView(FXMLLoader.load(getClass().getResource("/view/MainView.fxml")), event);
        }
    }
    
    @FXML
    private void onPartSearchBtnClick(ActionEvent event) {
        if (tf_search_part.getText().isEmpty()) return;
        Util.searchPart(tf_search_part, tableview_add);
    }
        
    private void updateAddTableview() {
        col_add_part_id.setCellValueFactory(new PropertyValueFactory<>("partId"));
        col_add_part_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_add_part_inventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        col_add_part_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableview_add.setItems(Inventory.getParts());
    }

    private void updateDelTableview() {
        col_del_part_id.setCellValueFactory(new PropertyValueFactory<>("partId"));
        col_del_part_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_del_part_inventory.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        col_del_part_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        tableview_del.setItems(chosenParts);
    }
}
