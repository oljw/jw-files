package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_c195.model.Appointment;
import wgu_c195.model.Customer;
import wgu_c195.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class CalendarPage {

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

    @FXML
    void onMonthlyClicked() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus1Month = now.plusMonths(1);

        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        filteredData.setPredicate(row -> {
            LocalDate rowDate = LocalDate.parse(row.getStart(), dateTimeFormatter);
            return rowDate.isAfter(now.minusDays(1)) && rowDate.isBefore(nowPlus1Month);
        });
        appointmentTableView.setItems(filteredData);
    }

    @FXML
    void onWeeklyClicked() {
        LocalDate now = LocalDate.now();
        LocalDate nowPlus7 = now.plusDays(7);
        FilteredList<Appointment> filteredData = new FilteredList<>(appointments);
        filteredData.setPredicate(row -> {
            LocalDate rowDate = LocalDate.parse(row.getStart(), dateTimeFormatter);
            return rowDate.isEqual(now.minusDays(1)) && rowDate.isBefore(nowPlus7);
        });
        appointmentTableView.setItems(filteredData);
    }
}
