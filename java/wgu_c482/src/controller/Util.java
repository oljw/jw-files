/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;

/**
 *
 * @author JW
 */
public class Util {
    
    public static void searchPart(TextField tf, TableView<Part> tv) {
        try {
            Part part = Inventory.lookupPart(Integer.parseInt(tf.getText()));
            if (part == null) {
                showError("Result not found.");
                return;
            }
            tv.requestFocus();
            tv.getSelectionModel().select(part);
        } catch(NumberFormatException e) {
            Util.showError("Result not found. Search by Part ID.");
        }
    }
   
    public static Part getSelectedPart(TableView<Part> tv) {
        Part part = tv.getSelectionModel().getSelectedItem();
        if (part == null) {
            Util.showError("Please select a part from table.");
            return null;
        }
        return part;
    }
    
    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait(); 
    }
    
    public static void launchView(Parent view, ActionEvent event) throws IOException {
        Scene scene = new Scene(view);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    public static boolean askCancel() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) return true;
        else return false;
    }
    
    public static void askExit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) System.exit(0);
    }
}
