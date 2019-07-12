package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import wgu_c195.app.App;
import wgu_c195.model.City;
import wgu_c195.model.Customer;
import wgu_c195.util.DBUtil;
import wgu_c195.util.PageUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CustomerScreenController {

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, String> customerNameColumn;

    @FXML
    private TextField customerIdField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private TextField address2Field;

    @FXML
    private ComboBox<City> cityComboBox;

    @FXML
    private TextField postalCodeField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField countryField;

    @FXML
    private ButtonBar modifyButtonBar;

    @FXML
    private ButtonBar saveExitButtonBar;

    private boolean isEdit = false;
    private Stage dialogStage;

    @FXML
    void handleNewCustomer() {
        isEdit = false;
        enableCustomerFields();
        saveExitButtonBar.setDisable(false);
        customerTable.setDisable(true);
        clearCustomerDetails();
        customerIdField.setText("Auto-Generated");
        modifyButtonBar.setDisable(true);
    }

    @FXML
    void handleEditCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            isEdit = true;
            enableCustomerFields();
            saveExitButtonBar.setDisable(false);
            customerTable.setDisable(true);
            modifyButtonBar.setDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected");
            alert.setContentText("Please select a Customer in the Table");
            alert.showAndWait();
        }


    }

    @FXML
    void handleDeleteCustomer() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                                deleteCustomer(selectedCustomer);
                                PageUtil.getInstance().showCustomerScreen();
                            }
                    );
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected for Deletion");
            alert.setContentText("Please select a Customer in the Table to delete");
            alert.showAndWait();
        }

    }

    @FXML
    void handleSaveCustomer() {
        saveExitButtonBar.setDisable(true);
        customerTable.setDisable(false);
        if (isEdit == true) {
            updateCustomer();
        } else if (isEdit == false) {
            saveCustomer();
        }
        PageUtil.getInstance().showCustomerScreen();
    }

    @FXML
    void handleCancelCustomer() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                            saveExitButtonBar.setDisable(true);
                            customerTable.setDisable(false);
                            clearCustomerDetails();
                            modifyButtonBar.setDisable(false);
                            isEdit = false;
                        }
                );
    }

    public void setCustomerScreen() {
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        disableCustomerFields();

        populateCityList();

        cityComboBox.setConverter(new StringConverter<City>() {

            @Override
            public String toString(City object) {
                return object.getCity();
            }

            @Override
            public City fromString(String string) {
                return cityComboBox.getItems().stream().filter(ap ->
                        ap.getCity().equals(string)).findFirst().orElse(null);
            }
        });

        cityComboBox.valueProperty().addListener((obs, oldval, newval) -> {
            if (newval != null)
                showCountry(newval.toString());
        });

        customerTable.getItems().setAll(populateCustomerList());
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCustomerDetails(newValue));

    }

    @FXML
    private void showCustomerDetails(Customer selectedCustomer) {

        customerIdField.setText(selectedCustomer.getCustomerId());
        nameField.setText(selectedCustomer.getCustomerName());
        addressField.setText(selectedCustomer.getAddress());
        address2Field.setText(selectedCustomer.getAddress2());
        cityComboBox.setValue(selectedCustomer.getCity());
        countryField.setText(selectedCustomer.getCountry());
        postalCodeField.setText(selectedCustomer.getPostalCode());
        phoneField.setText(selectedCustomer.getPhone());

    }

    private void disableCustomerFields() {

        nameField.setEditable(false);
        addressField.setEditable(false);
        address2Field.setEditable(false);
        postalCodeField.setEditable(false);
        phoneField.setEditable(false);
    }

    private void enableCustomerFields() {

        nameField.setEditable(true);
        addressField.setEditable(true);
        address2Field.setEditable(true);
        postalCodeField.setEditable(true);
        phoneField.setEditable(true);
    }

    @FXML
    private void clearCustomerDetails() {
        customerIdField.clear();
        nameField.clear();
        addressField.clear();
        address2Field.clear();
        countryField.clear();
        postalCodeField.clear();
        phoneField.clear();
    }

    protected List<Customer> populateCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try (
                PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                        "SELECT customer.customerId, customer.customerName, address.address, address.address2, address.postalCode, city.cityId, city.city, country.country, address.phone " +
                                "FROM customer, address, city, country " +
                                "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
                                "ORDER BY customer.customerName"
                );
                ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                customerList.add(new Customer(
                                rs.getString("customer.customerId"),
                                rs.getString("customer.customerName"),
                                rs.getString("address.address"),
                                rs.getString("address.address2"),
                                new City(rs.getInt("city.cityId"), rs.getString("city.city")),
                                rs.getString("country.country"),
                                rs.getString("address.postalCode"),
                                rs.getString("address.phone")
                        )
                );
            }
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        }
        return customerList;

    }

    protected void populateCityList() {
        ObservableList<City> cities = FXCollections.observableArrayList();

        try (
                PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT cityId, city FROM city LIMIT 100;");
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                cities.add(new City(rs.getInt("city.cityId"), rs.getString("city.city")));
            }
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }

        cityComboBox.setItems(cities);

    }

    @FXML
    private void showCountry(String citySelection) {
        if (citySelection.equals("London")) {
            countryField.setText("England");
        } else if (citySelection.equals("Phoenix") || citySelection.equals("New York")) {
            countryField.setText("United States");
        }
    }

    private void saveCustomer() {
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, addressField.getText());
            ps.setString(2, address2Field.getText());
            ps.setInt(3, cityComboBox.getValue().getCityId());
            ps.setString(4, postalCodeField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, App.sInstance.getUser().getUsername());
            ps.setString(7, App.sInstance.getUser().getUsername());
            boolean res = ps.execute();
            int newAddressId = -1;
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                newAddressId = rs.getInt(1);
            }


            PreparedStatement psc = DBUtil.getConnection().prepareStatement("INSERT INTO customer "
                    + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");

            psc.setString(1, nameField.getText());
            psc.setInt(2, newAddressId);
            psc.setInt(3, 1);
            psc.setString(4, App.sInstance.getUser().getUsername());
            psc.setString(5, App.sInstance.getUser().getUsername());
            int result = psc.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteCustomer(Customer customer) {

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("DELETE customer.*, address.* from customer, address WHERE customer.customerId = ? AND customer.addressId = address.addressId");
            statement.setString(1, customer.getCustomerId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCustomer() {
        try {

            PreparedStatement ps = DBUtil.getConnection().prepareStatement("UPDATE address, customer, city, country "
                    + "SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");

            ps.setString(1, addressField.getText());
            ps.setString(2, address2Field.getText());
            ps.setInt(3, cityComboBox.getValue().getCityId());
            ps.setString(4, postalCodeField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, App.sInstance.getUser().getUsername());
            ps.setString(7, customerIdField.getText());

            int result = ps.executeUpdate();


            PreparedStatement psc = DBUtil.getConnection().prepareStatement("UPDATE customer, address, city "
                    + "SET customerName = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId");

            psc.setString(1, nameField.getText());
            psc.setString(2, App.sInstance.getUser().getUsername());
            psc.setString(3, customerIdField.getText());
            int results = psc.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
