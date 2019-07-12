package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_c195.model.Appointment;
import wgu_c195.model.Customer;
import wgu_c195.util.DBUtil;
import wgu_c195.util.PageUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class AppointmentScreenController {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId zoneId = ZoneId.systemDefault();
    private ObservableList<Appointment> appointments;

    @FXML private TableView<Appointment> appointmentTableView;
    @FXML private TableColumn<Appointment, ZonedDateTime> startColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, Customer> customerColumn;
    @FXML private TableColumn<Appointment, String> consultantColumn;

    public void init() {
        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        consultantColumn.setCellValueFactory(new PropertyValueFactory<>("user"));

        appointments = FXCollections.observableArrayList();
        showAppointmentList();
        appointmentTableView.getItems().setAll(appointments);
    }

    @FXML
    void onDeleteClicked() {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + selectedAppointment.getTitle() + " scheduled for " + selectedAppointment.getStart() + "?");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {
                                delete(selectedAppointment);
                                PageUtil.getInstance().launchAppointmentPage();
                            }
                    );
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment selected for Deletion");
            alert.setContentText("Please select an Appointment in the Table to delete");
            alert.showAndWait();
        }
    }

    @FXML
    void onEditClick() {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {
            PageUtil.getInstance().launchEditAppointmentDialog(selectedAppointment);
            PageUtil.getInstance().launchAppointmentPage();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Appointment selected");
            alert.setContentText("Please select an Appointment in the Table");
            alert.showAndWait();
        }
    }

    @FXML
    void onNewClicked() {
        PageUtil.getInstance().launchNewAppointmentDialog();
        PageUtil.getInstance().launchAppointmentPage();
    }

    private void showAppointmentList() {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.description, "
                            + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId "
                            + "ORDER BY `start`");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                ZonedDateTime zdtStart = rs.getTimestamp("appointment.start").toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime localStart = zdtStart.withZoneSameInstant(zoneId);

                ZonedDateTime zdtEnd = rs.getTimestamp("appointment.end").toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime localEnd = zdtEnd.withZoneSameInstant(zoneId);

                appointments.add(new Appointment(
                        rs.getString("appointment.appointmentId"),
                        localStart.format(dateTimeFormatter),
                        localEnd.format(dateTimeFormatter),
                        rs.getString("appointment.title"),
                        rs.getString("appointment.description"),
                        new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName")),
                        rs.getString("appointment.createdBy")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void delete(Appointment appointment) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("DELETE appointment.* FROM appointment WHERE appointment.appointmentId = ?");
            statement.setString(1, appointment.getAppointmentId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
