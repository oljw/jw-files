/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tf_id.setText(Inventory.getProducts().size() + "");
        tf_name.requestFocus();
    }    

    @FXML
    private void onPartSearchBtnClick(ActionEvent event) {
    }

    @FXML
    private void onAddPartClick(ActionEvent event) {
    }

    @FXML
    private void onDeletePartClick(ActionEvent event) {
    }

    @FXML
    private void onSaveClick(ActionEvent event) {
    }
    
    @FXML
    private void onCancelClick(ActionEvent event) {
    }
}
