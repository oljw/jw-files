package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_c195.app.App;
import wgu_c195.model.Appointment;
import wgu_c195.model.Customer;
import wgu_c195.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static wgu_c195.util.TimeUtil.convertTimeZone;

public class SchedulePage {
    private ObservableList<Appointment> schedule;

    @FXML private TableView<Appointment> scheduleTableView;
    @FXML private TableColumn<Appointment, ZonedDateTime> startColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, Customer> customerColumn;

    public void init() {
        showRemainingSchedule();

        startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
    }

    private void showRemainingSchedule() {
        schedule = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.description, "
                            + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId AND appointment.`start` >= CURRENT_DATE AND appointment.createdBy = ?"
                            + "ORDER BY `start`");
            statement.setString(1, App.sInstance.getUser().getUsername());
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                schedule.add(new Appointment(
                        rs.getString("appointment.appointmentId"),
                        convertTimeZone(rs.getTimestamp("appointment.start")),
                        convertTimeZone(rs.getTimestamp("appointment.end")),
                        rs.getString("appointment.title"),
                        rs.getString("appointment.description"),
                        new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName")),
                        rs.getString("appointment.createdBy")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        scheduleTableView.getItems().setAll(schedule);
    }
}
