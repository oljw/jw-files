package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import wgu_c195.app.App;
import wgu_c195.model.Appointment;
import wgu_c195.model.Customer;
import wgu_c195.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author JW
 */
public class AppointmentEditScreenController {

    private final ZoneId zid = ZoneId.systemDefault();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    ObservableList<Appointment> apptTimeList;
    @FXML
    private Label apptLabel;
    @FXML
    private TextField titleField;
    @FXML
    private ComboBox<String> startComboBox;
    @FXML
    private ComboBox<String> endComboBox;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button apptSaveButton;
    @FXML
    private Button apptCancelButton;
    @FXML
    private TableView<Customer> customerSelectTableView;
    @FXML
    private TableColumn<Customer, String> customerNameApptColumn;
    @FXML
    private TextField customerSearchField;
    private Stage dialogStage;
    private boolean okClicked = false;
    private Appointment selectedAppt;
    private ObservableList<Customer> masterData = FXCollections.observableArrayList();

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (validateAppointment()) {
            if (isOkClicked()) {
                updateAppt();
            } else {
                saveAppt();
            }
            dialogStage.close();
        }

    }

    @FXML
    private void handleCancel() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> dialogStage.close());

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;

        populateTypeList();
        customerNameApptColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        masterData = populateCustomerList();

        FilteredList<Customer> filteredData = new FilteredList<>(masterData, p -> true);

        customerSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return customer.getCustomerName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Customer> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(customerSelectTableView.comparatorProperty());

        customerSelectTableView.setItems(sortedData);

        LocalTime time = LocalTime.of(8, 0);
        do {
            startTimes.add(time.format(timeDTF));
            endTimes.add(time.format(timeDTF));
            time = time.plusMinutes(15);
        } while (!time.equals(LocalTime.of(17, 15)));
        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);

        datePicker.setValue(LocalDate.now());

        startComboBox.setItems(startTimes);
        endComboBox.setItems(endTimes);
        startComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(timeDTF));
        endComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(timeDTF));

    }

    public void setAppointment(Appointment appointment) {


        okClicked = true;
        selectedAppt = appointment;

        String start = appointment.getStart();

        LocalDateTime startLDT = LocalDateTime.parse(start, dateDTF);
        String end = appointment.getEnd();
        LocalDateTime endLDT = LocalDateTime.parse(end, dateDTF);

        apptLabel.setText("Edit Appointment");
        titleField.setText(appointment.getTitle());
        typeComboBox.setValue(appointment.getDescription());
        customerSelectTableView.getSelectionModel().select(appointment.getCustomer());
        datePicker.setValue(LocalDate.parse(appointment.getStart(), dateDTF));
        startComboBox.getSelectionModel().select(startLDT.toLocalTime().format(timeDTF));
        endComboBox.getSelectionModel().select(endLDT.toLocalTime().format(timeDTF));


    }

    private void saveAppt() {

        LocalDate localDate = datePicker.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);

        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));

        Timestamp startsqlts = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp endsqlts = Timestamp.valueOf(endUTC.toLocalDateTime());

        try {

            PreparedStatement statement = DBUtil.getConnection().prepareStatement("INSERT INTO appointment "
                    + "(customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy)"
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)");

            statement.setString(1, customerSelectTableView.getSelectionModel().getSelectedItem().getCustomerId());
            statement.setString(2, titleField.getText());
            statement.setString(3, typeComboBox.getValue());
            statement.setString(4, "");
            statement.setString(5, "");
            statement.setString(6, "");
            statement.setTimestamp(7, startsqlts);
            statement.setTimestamp(8, endsqlts);
            statement.setString(9, App.sInstance.getUser().getUsername());
            statement.setString(10, App.sInstance.getUser().getUsername());
            int result = statement.executeUpdate();
            if (result == 1) {
                System.out.println("YAY! New Appointment Save");
            } else {
                System.out.println("BOO! New Appointment Save");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    private void updateAppt() {

        LocalDate localDate = datePicker.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);

        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));

        Timestamp startsqlts = Timestamp.valueOf(startUTC.toLocalDateTime());
        Timestamp endsqlts = Timestamp.valueOf(endUTC.toLocalDateTime());

        try {

            PreparedStatement statement = DBUtil.getConnection().prepareStatement("UPDATE appointment "
                    + "SET customerId = ?, title = ?, description = ?, start = ?, end = ?, lastUpdate = CURRENT_TIMESTAMP, lastUpdateBy = ? "
                    + "WHERE appointmentId = ?");

            statement.setString(1, customerSelectTableView.getSelectionModel().getSelectedItem().getCustomerId());
            statement.setString(2, titleField.getText());
            statement.setString(3, typeComboBox.getValue());
            statement.setTimestamp(4, startsqlts);
            statement.setTimestamp(5, endsqlts);
            statement.setString(6, App.sInstance.getUser().getUsername());
            statement.setString(7, selectedAppt.getAppointmentId());
            int result = statement.executeUpdate();
            if (result == 1) {
                System.out.println("YAY! Update Appointment Save");
            } else {
                System.out.println("BOO! Update Appointment Save");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void populateTypeList() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Consultation", "New Account", "Follow Up", "Close Account");
        typeComboBox.setItems(typeList);
    }

    protected ObservableList<Customer> populateCustomerList() {

        String tCustomerId;
        String tCustomerName;

        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try (


                PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                        "SELECT customer.customerId, customer.customerName " +
                                "FROM customer, address, city, country " +
                                "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
                ResultSet rs = statement.executeQuery()) {


            while (rs.next()) {
                tCustomerId = rs.getString("customer.customerId");

                tCustomerName = rs.getString("customer.customerName");

                customerList.add(new Customer(tCustomerId, tCustomerName));

            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
        }


        return customerList;

    }

    private boolean validateAppointment() {
        String title = titleField.getText();
        String type = typeComboBox.getValue();
        Customer customer = customerSelectTableView.getSelectionModel().getSelectedItem();
        LocalDate localDate = datePicker.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), timeDTF);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), timeDTF);

        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));

        String errorMessage = "";
        if (title == null || title.length() == 0) {
            errorMessage += "Please enter an Appointment title.\n";
        }
        if (type == null || type.length() == 0) {
            errorMessage += "Please select an Appointment type.\n";
        }
        if (customer == null) {
            errorMessage += "Please Select a Customer.\n";
        }
        if (startUTC == null) {
            errorMessage += "Please select a Start time";
        }
        if (endUTC == null) {
            errorMessage += "Please select an End time.\n";
        } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)) {
            errorMessage += "End time must be after Start time.\n";
        } else try {
            if (hasApptConflict(startUTC, endUTC)) {
                errorMessage += "Appointment times conflict with Consultant's existing appointments. Please select a new time.\n";
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentEditScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid Appointment fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private boolean hasApptConflict(ZonedDateTime newStart, ZonedDateTime newEnd) throws SQLException {
        String apptID;
        String consultant;
        if (isOkClicked()) {
            apptID = selectedAppt.getAppointmentId();
            consultant = selectedAppt.getUser();
        } else {
            apptID = "0";
            consultant = App.sInstance.getUser().getUsername();
        }
        System.out.println("ApptID: " + apptID);

        try {


            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT * FROM appointment "
                            + "WHERE (? BETWEEN start AND end OR ? BETWEEN start AND end OR ? < start AND ? > end) "
                            + "AND (createdBy = ? AND appointmentID != ?)");
            statement.setTimestamp(1, Timestamp.valueOf(newStart.toLocalDateTime()));
            statement.setTimestamp(2, Timestamp.valueOf(newEnd.toLocalDateTime()));
            statement.setTimestamp(3, Timestamp.valueOf(newStart.toLocalDateTime()));
            statement.setTimestamp(4, Timestamp.valueOf(newEnd.toLocalDateTime()));
            statement.setString(5, consultant);
            statement.setString(6, apptID);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return true;
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
            e.printStackTrace();
        }
        return false;
    }

}
