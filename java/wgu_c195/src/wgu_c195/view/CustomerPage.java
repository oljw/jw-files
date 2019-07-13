package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class CustomerPage {
    private boolean isEdit = false;

    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> customerColumn;
    @FXML private TextField customerId;
    @FXML private TextField name;
    @FXML private TextField address;
    @FXML private TextField address2;
    @FXML private ComboBox<City> cityComboBox;
    @FXML private TextField postalCode;
    @FXML private TextField phone;
    @FXML private TextField country;
    @FXML private ButtonBar modifyButtonBar;
    @FXML private ButtonBar saveExitButtonBar;

    @FXML
    void onNewClicked() {
        isEdit = false;
        enableCustomerFields(true);
        saveExitButtonBar.setDisable(false);
        customerTable.setDisable(true);
        clearDetails();
        customerId.setText("Auto-Generated");
        modifyButtonBar.setDisable(true);
    }

    @FXML
    void onEditClick() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            isEdit = true;
            enableCustomerFields(true);
            saveExitButtonBar.setDisable(false);
            customerTable.setDisable(true);
            modifyButtonBar.setDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected");
            alert.setContentText("Select a Customer");
            alert.showAndWait();
        }
    }

    @FXML
    void onDeleteClick() {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + selectedCustomer.getCustomerName() + "?");
            alert.showAndWait()
                    /*  Usage of lambda expression to simplify the source code complexity.
                    Since the parameter of setPredicate function requires an anonymous class, using lambda expression is much
                    easier to read and write. Check the below usage without lambda expression */
                    .filter(response -> response == ButtonType.OK) // Retrieving data using lambda expression with just 'response' and can check right away in one line.
                    // Below is a usage without lambda expression. It save 8 lines of code by just using lambda expression.
                    /*
                    .filter(new Predicate<ButtonType>() {
                        @Override
                        public boolean test(ButtonType buttonType) {
                            if (buttonType == ButtonType.OK) {
                                return true;
                            }
                            return false;
                        }
                    }) */
                    .ifPresent(response -> {
                                delete(selectedCustomer);
                                PageUtil.getInstance().launchCustomerPage();
                            }
                    );
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Customer selected for Deletion");
            alert.setContentText("Select a Customer");
            alert.showAndWait();
        }
    }

    @FXML
    void onSaveClick() {
        if (validate()) {
            saveExitButtonBar.setDisable(true);
            customerTable.setDisable(false);

            if (isEdit) update();
            else save();

            PageUtil.getInstance().launchCustomerPage();
        }
    }

    @FXML
    void onCancelClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {
                            saveExitButtonBar.setDisable(true);
                            customerTable.setDisable(false);
                            clearDetails();
                            modifyButtonBar.setDisable(false);
                            isEdit = false;
                        }
                );
    }

    public void init() {
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        enableCustomerFields(false);

        showCityList();

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
                selectCountry(newval.toString());
        });

        customerTable.getItems().setAll(populateCustomerList());
        customerTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCustomerDetails(newValue));
    }

    @FXML
    private void showCustomerDetails(Customer selectedCustomer) {
        customerId.setText(selectedCustomer.getCustomerId());
        name.setText(selectedCustomer.getCustomerName());
        address.setText(selectedCustomer.getAddress());
        address2.setText(selectedCustomer.getAddress2());
        cityComboBox.setValue(selectedCustomer.getCity());
        country.setText(selectedCustomer.getCountry());
        postalCode.setText(selectedCustomer.getPostalCode());
        phone.setText(selectedCustomer.getPhone());
    }

    @FXML
    private void clearDetails() {
        customerId.clear();
        name.clear();
        address.clear();
        address2.clear();
        country.clear();
        postalCode.clear();
        phone.clear();
    }

    private void enableCustomerFields(boolean enable) {
        name.setEditable(enable);
        address.setEditable(enable);
        address2.setEditable(enable);
        postalCode.setEditable(enable);
        phone.setEditable(enable);
    }

    private List<Customer> populateCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT customer.customerId, customer.customerName, address.address, address.address2, address.postalCode, city.cityId, city.city, country.country, address.phone " +
                            "FROM customer, address, city, country " +
                            "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId " +
                            "ORDER BY customer.customerName"
            );
            ResultSet rs = statement.executeQuery();
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;

    }

    private void showCityList() {
        ObservableList<City> cities = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT cityId, city FROM city LIMIT 100;");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                cities.add(new City(rs.getInt("city.cityId"), rs.getString("city.city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        cityComboBox.setItems(cities);
    }

    @FXML
    private void selectCountry(String city) {
        switch (city) {
            case "Phoenix":
                country.setText("United States");
                break;
            case "New York":
                country.setText("United States");
                break;
            case "London":
                country.setText("England");
                break;
            default:
                break;
        }
    }

    private void save() {
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                    + "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)", Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, address.getText());
            ps.setString(2, address2.getText());
            ps.setInt(3, cityComboBox.getValue().getCityId());
            ps.setString(4, postalCode.getText());
            ps.setString(5, phone.getText());
            ps.setString(6, App.sInstance.getUser().getUsername());
            ps.setString(7, App.sInstance.getUser().getUsername());
            boolean res = ps.execute();
            int newAddressId = -1;
            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) newAddressId = rs.getInt(1);

            PreparedStatement psc = DBUtil.getConnection().prepareStatement("INSERT INTO customer "
                    + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + "VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");

            psc.setString(1, name.getText());
            psc.setInt(2, newAddressId);
            psc.setInt(3, 1);
            psc.setString(4, App.sInstance.getUser().getUsername());
            psc.setString(5, App.sInstance.getUser().getUsername());
            psc.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void delete(Customer customer) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("DELETE customer.*, address.* from customer, address WHERE customer.customerId = ? AND customer.addressId = address.addressId");
            statement.setString(1, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void update() {
        try {
            PreparedStatement ps = DBUtil.getConnection().prepareStatement("UPDATE address, customer, city, country "
                    + "SET address = ?, address2 = ?, address.cityId = ?, postalCode = ?, phone = ?, address.lastUpdate = CURRENT_TIMESTAMP, address.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
            ps.setString(1, address.getText());
            ps.setString(2, address2.getText());
            ps.setInt(3, cityComboBox.getValue().getCityId());
            ps.setString(4, postalCode.getText());
            ps.setString(5, phone.getText());
            ps.setString(6, App.sInstance.getUser().getUsername());
            ps.setString(7, customerId.getText());
            ps.executeUpdate();

            PreparedStatement psc = DBUtil.getConnection().prepareStatement("UPDATE customer, address, city "
                    + "SET customerName = ?, customer.lastUpdate = CURRENT_TIMESTAMP, customer.lastUpdateBy = ? "
                    + "WHERE customer.customerId = ? AND customer.addressId = address.addressId AND address.cityId = city.cityId");
            psc.setString(1, name.getText());
            psc.setString(2, App.sInstance.getUser().getUsername());
            psc.setString(3, customerId.getText());
            psc.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validate() {
        String msg = "";
        if (name.getText().length() == 0) msg += "Enter name.\n";
        if (address.getText().length() == 0) msg += "Enter an address.\n";

        if (postalCode.getText().length() == 0) msg += "Enter the postal code.\n";
        else if (postalCode.getText().length() > 5 || postalCode.getText().length() < 5)
            msg += "Enter a valid postal code.\n";

        if (phone.getText().length() == 0) msg += "Enter a phone number including the area code.\n";
        else if (phone.getText().length() < 9 || phone.getText().length() > 12 )
            msg += "Enter a valid phone number including the area code.\n";

        if (msg.isEmpty())
            return true;

        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Fields");
            alert.setHeaderText("Fix wrong fields.");
            alert.setContentText(msg);
            alert.showAndWait();
            return false;
        }
    }
}
