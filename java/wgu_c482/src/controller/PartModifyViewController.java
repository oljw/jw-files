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
public class PartModifyViewController implements Initializable {

    @FXML
    private Label label_extra;
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
    @FXML
    private RadioButton rb_inhouse;
    @FXML
    private RadioButton rb_outsourced;

    private static boolean isInhouse;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Part part = Inventory.getParts().get(Inventory.getSelectedPartIndex());
        tf_id.setText(String.valueOf(part.getPartId()));
        tf_name.setText(part.getName());
        tf_inventory.setText(String.valueOf(part.getInventory()));
        tf_price.setText(String.valueOf(part.getPrice()));
        tf_min_inventory.setText(String.valueOf(part.getMin()));
        tf_max_inventory.setText(String.valueOf(part.getMax()));
        
        if (part instanceof Inhouse) {
            tf_extra.setText(String.valueOf(((Inhouse)part).getMachineID()));
            label_extra.setText("Machine ID");
            rb_inhouse.setSelected(true);
            isInhouse = true;
        } else {
            tf_extra.setText(((Outsourced)part).getCompanyName());
            label_extra.setText("Company Name");
            rb_outsourced.setSelected(true);
            isInhouse = false;
        }
    }

    @FXML
    private void onInHouseRadioBtnClick(ActionEvent event) {
        label_extra.setText("Machine ID");
        tf_extra.setPromptText("Mach ID");
    }

    @FXML
    private void onOutSourcedRadioBtnClick(ActionEvent event) {
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

            Inventory.updatePart(Inventory.getSelectedPartIndex(), part);
            Util.launchView(FXMLLoader.load(getClass().getResource("/view/MainView.fxml")), event);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
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
}
