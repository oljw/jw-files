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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Inhouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author JW
 */
public class PartAddViewController implements Initializable {

    @FXML
    private Label label_extra;
    @FXML
    private RadioButton rb_inhouse;
    @FXML
    private RadioButton rb_outsourced;
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
    private TextField tf_extra;

    private boolean isInhouse;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rb_inhouse.fire();
        tf_id.setText(Inventory.getParts().size() + "");
    }    

    @FXML
    private void onInHouseRadioBtnClick(ActionEvent event) {
        this.isInhouse = true;
        label_extra.setText("Machine ID");
        tf_extra.setPromptText("Mach ID");
    }

    @FXML
    private void onOutSourcedRadioBtnClick(ActionEvent event) {
        this.isInhouse = false;
        label_extra.setText("Company Name");
        tf_extra.setPromptText("Comp Nm"); 
    }

    @FXML
    private void onSaveClick(ActionEvent event) throws IOException {
        Part part;
        try {
            if (isInhouse) 
                part = new Inhouse(Integer.parseInt(tf_extra.getText()));
            else
                part = new Outsourced(tf_extra.getText());
            part.setPartId(Integer.parseInt(tf_id.getText()));
            part.setName(tf_name.getText());
            part.setPrice(Double.parseDouble(tf_price.getText()));
            part.setInventory(Integer.parseInt(tf_inventory.getText()));
            part.setMin(Integer.parseInt(tf_min_inventory.getText()));
            part.setMax(Integer.parseInt(tf_max_inventory.getText()));

            Inventory.addPart(part);
            launchMainView(event);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Form contains invalid fields.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onCancelClick(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to cancel?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) launchMainView(event);
    }
    
    private void launchMainView(ActionEvent event) throws IOException {
        Parent view = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
