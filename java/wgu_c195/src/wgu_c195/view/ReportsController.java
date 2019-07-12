package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_c195.app.App;
import wgu_c195.model.Appointment;
import wgu_c195.model.AppointmentReport;
import wgu_c195.model.Customer;
import wgu_c195.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * FXML Controller class
 *
 * @author JW
 */
public class ReportsController {

    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId newzid = ZoneId.systemDefault();
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab schedTab;
    @FXML
    private TableView<Appointment> schedTableView;
    @FXML
    private TableColumn<Appointment, ZonedDateTime> startSchedColumn;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endSchedColumn;
    @FXML
    private TableColumn<Appointment, String> titleSchedColumn;
    @FXML
    private TableColumn<Appointment, String> typeSchedColumn;
    @FXML
    private TableColumn<Appointment, Customer> customerSchedColumn;
    @FXML
    private Tab apptTab;
    @FXML
    private TableView<AppointmentReport> apptTableView;
    @FXML
    private TableColumn<AppointmentReport, String> monthColumn;
    @FXML
    private TableColumn<AppointmentReport, String> typeColumn;
    @FXML
    private TableColumn<AppointmentReport, String> typeAmount;
    @FXML
    private Tab custTab;
    @FXML
    private BarChart barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private ObservableList<AppointmentReport> apptList;
    private ObservableList<Appointment> schedule;
    private ObservableList<PieChart.Data> pieChartData;

    public void setReports() {
        populateApptTypeList();
        populateCustBarChart();
        populateSchedule();

        startSchedColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endSchedColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        titleSchedColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeSchedColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        customerSchedColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));

        monthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        typeAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    private void populateApptTypeList() {
        apptList = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT MONTHNAME(`start`) AS \"Month\", description AS \"Type\", COUNT(*) as \"Amount\" "
                            + "FROM appointment "
                            + "GROUP BY MONTHNAME(`start`), description");
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {

                String month = rs.getString("Month");

                String type = rs.getString("Type");

                String amount = rs.getString("Amount");

                apptList.add(new AppointmentReport(month, type, amount));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        apptTableView.getItems().setAll(apptList);
    }

    private void populateCustBarChart() {

        ObservableList<XYChart.Data<String, Integer>> data = FXCollections.observableArrayList();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT city.city, COUNT(city) "
                            + "FROM customer, address, city "
                            + "WHERE customer.addressId = address.addressId "
                            + "AND address.cityId = city.cityId "
                            + "GROUP BY city");
            ResultSet rs = statement.executeQuery();


            while (rs.next()) {
                String city = rs.getString("city");
                Integer count = rs.getInt("COUNT(city)");
                data.add(new Data<>(city, count));
            }

        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
            e.printStackTrace();
        }
        series.getData().addAll(data);
        barChart.getData().add(series);
    }

    private void populateSchedule() {
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
                String tAppointmentId = rs.getString("appointment.appointmentId");
                Timestamp tsStart = rs.getTimestamp("appointment.start");
                ZonedDateTime newzdtStart = tsStart.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalStart = newzdtStart.withZoneSameInstant(newzid);

                Timestamp tsEnd = rs.getTimestamp("appointment.end");
                ZonedDateTime newzdtEnd = tsEnd.toLocalDateTime().atZone(ZoneId.of("UTC"));
                ZonedDateTime newLocalEnd = newzdtEnd.withZoneSameInstant(newzid);

                String tTitle = rs.getString("appointment.title");

                String tType = rs.getString("appointment.description");

                Customer tCustomer = new Customer(rs.getString("appointment.customerId"), rs.getString("customer.customerName"));

                String tUser = rs.getString("appointment.createdBy");

                schedule.add(new Appointment(tAppointmentId, newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF), tTitle, tType, tCustomer, tUser));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        schedTableView.getItems().setAll(schedule);
    }


}
