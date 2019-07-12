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

public class AppointmentEditPage {

    private final ZoneId zid = ZoneId.systemDefault();
    private final ObservableList<String> startTimes = FXCollections.observableArrayList();
    private final ObservableList<String> endTimes = FXCollections.observableArrayList();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private ObservableList<Customer> masterData = FXCollections.observableArrayList();
    private Appointment selectedAppt;
    private Stage dialogStage;
    private boolean isOk;

    @FXML private Label label;
    @FXML private TextField title;
    @FXML private ComboBox<String> startComboBox;
    @FXML private ComboBox<String> endComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private DatePicker datePicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private TableView<Customer> customerSelectTableView;
    @FXML private TableColumn<Customer, String> customerNameApptColumn;

    @FXML
    private void onSaveClicked() {
        if (validate()) {
            if (isOk) update();
            else save();
            dialogStage.close();
        }
    }

    @FXML
    private void onCancelClicked() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> dialogStage.close());
    }

    public void init(Stage dialogStage) {
        this.dialogStage = dialogStage;

        showTypeList();
        customerNameApptColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        masterData = showCustomerList();

        FilteredList<Customer> filteredData = new FilteredList<>(masterData, p -> true);

        SortedList<Customer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(customerSelectTableView.comparatorProperty());
        customerSelectTableView.setItems(sortedData);
        LocalTime time = LocalTime.of(8, 0);

        while (!time.equals(LocalTime.of(17, 15))) {
            startTimes.add(time.format(dateTimeFormatter));
            endTimes.add(time.format(dateTimeFormatter));
            time = time.plusMinutes(15);
        }

        startTimes.remove(startTimes.size() - 1);
        endTimes.remove(0);
        datePicker.setValue(LocalDate.now());
        startComboBox.setItems(startTimes);
        endComboBox.setItems(endTimes);
        startComboBox.getSelectionModel().select(LocalTime.of(8, 0).format(dateTimeFormatter));
        endComboBox.getSelectionModel().select(LocalTime.of(8, 15).format(dateTimeFormatter));
    }

    public void setAppointment(Appointment appointment) {
        isOk = true;
        selectedAppt = appointment;

        String start = appointment.getStart();
        LocalDateTime startLDT = LocalDateTime.parse(start, dateDTF);
        String end = appointment.getEnd();
        LocalDateTime endLDT = LocalDateTime.parse(end, dateDTF);
        title.setText(appointment.getTitle());
        typeComboBox.setValue(appointment.getDescription());
        customerSelectTableView.getSelectionModel().select(appointment.getCustomer());
        datePicker.setValue(LocalDate.parse(appointment.getStart(), dateDTF));
        startComboBox.getSelectionModel().select(startLDT.toLocalTime().format(dateTimeFormatter));
        endComboBox.getSelectionModel().select(endLDT.toLocalTime().format(dateTimeFormatter));
    }

    private void save() {
        LocalDate localDate = datePicker.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);

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
            statement.setString(2, title.getText());
            statement.setString(3, typeComboBox.getValue());
            statement.setString(4, "");
            statement.setString(5, "");
            statement.setString(6, "");
            statement.setTimestamp(7, startsqlts);
            statement.setTimestamp(8, endsqlts);
            statement.setString(9, App.sInstance.getUser().getUsername());
            statement.setString(10, App.sInstance.getUser().getUsername());
            int result = statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void update() {
        LocalDate localDate = datePicker.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);

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
            statement.setString(2, title.getText());
            statement.setString(3, typeComboBox.getValue());
            statement.setTimestamp(4, startsqlts);
            statement.setTimestamp(5, endsqlts);
            statement.setString(6, App.sInstance.getUser().getUsername());
            statement.setString(7, selectedAppt.getAppointmentId());
            int result = statement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void showTypeList() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Consultation", "New Account", "Follow Up", "Close Account");
        typeComboBox.setItems(typeList);
    }

    private ObservableList<Customer> showCustomerList() {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT customer.customerId, customer.customerName " +
                            "FROM customer, address, city, country " +
                            "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId AND city.countryId = country.countryId");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customerList.add(new Customer(rs.getString("customer.customerId"), rs.getString("customer.customerName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    private boolean validate() {
        String title = this.title.getText();
        String type = typeComboBox.getValue();
        Customer customer = customerSelectTableView.getSelectionModel().getSelectedItem();
        LocalDate localDate = datePicker.getValue();
        LocalTime startTime = LocalTime.parse(startComboBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);
        LocalTime endTime = LocalTime.parse(endComboBox.getSelectionModel().getSelectedItem(), dateTimeFormatter);

        LocalDateTime startDT = LocalDateTime.of(localDate, startTime);
        LocalDateTime endDT = LocalDateTime.of(localDate, endTime);

        ZonedDateTime startUTC = startDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endUTC = endDT.atZone(zid).withZoneSameInstant(ZoneId.of("UTC"));

        String errorMessage = "";
        if (title == null || title.length() == 0) {
            errorMessage += "Enter appointment title.";
        }
        if (type == null || type.length() == 0) {
            errorMessage += "Select appointment type.";
        }
        if (customer == null) {
            errorMessage += "Select a Customer.";
        }
        if (startUTC == null) {
            errorMessage += "Select a Start time";
        }
        if (endUTC == null) {
            errorMessage += "Select an End time.";
        } else if (endUTC.equals(startUTC) || endUTC.isBefore(startUTC)) {
            errorMessage += "End time cannot be before start time.\n";
        } else try {
            if (checkConflict(startUTC, endUTC)) {
                errorMessage += "Time conflicts with existing appointment. Please select a different time.";
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentEditPage.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }
    }

    private boolean checkConflict(ZonedDateTime newStart, ZonedDateTime newEnd) throws SQLException {
        String apptID;
        String consultant;
        if (isOk) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
