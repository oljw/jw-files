/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author JW
 */
public class ProductModifyViewController implements Initializable {

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
        Product product = Inventory.getProducts().get(Inventory.getSelectedProductIndex());
        tf_id.setText(String.valueOf(product.getProductId()));
        tf_name.setText(product.getName());
        tf_inventory.setText(String.valueOf(product.getInventory()));
        tf_price.setText(String.valueOf(product.getPrice()));
        tf_min_inventory.setText(String.valueOf(product.getMin()));
        tf_max_inventory.setText(String.valueOf(product.getMax()));
        
        tf_name.requestFocus();
        
        updateAddTableview();
        updateDelTableview();
    }    

    @FXML
    private void onPartSearchBtnClick(ActionEvent event) {
        Util.searchPart(tf_search_part, tableview_add);
    }

    @FXML
    private void onAddPartClick(ActionEvent event) {
        Part part = Util.getSelectedPart(tableview_add);
        if (part != null) {
            Product product = Inventory.getProducts().get(Inventory.getSelectedProductIndex());
            product.getParts().add(part);
            updateDelTableview();
        }
    }

    @FXML
    private void onDeletePartClick(ActionEvent event) {
        Part part = Util.getSelectedPart(tableview_del);
        if (part != null) {
            Product product = Inventory.getProducts().get(Inventory.getSelectedProductIndex());
            product.getParts().remove(part);
            updateDelTableview();
        }
    }

    @FXML
    private void onCancelClick(ActionEvent event) throws IOException {
        Util.askCancel();
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
                Integer.parseInt(tf_max_inventory.getText())
            );
            product.setParts(Inventory.getProducts().get(Inventory.getSelectedProductIndex()).getParts());

            Inventory.updateProduct(Inventory.getSelectedPartIndex(), product);
            Util.launchView(FXMLLoader.load(getClass().getResource("/view/MainView.fxml")), event);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Form contains invalid field(s).");
            alert.showAndWait();
        }
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
        
        Product product = Inventory.getProducts().get(Inventory.getSelectedProductIndex());
        tableview_del.setItems(product.getParts());
    }
}
