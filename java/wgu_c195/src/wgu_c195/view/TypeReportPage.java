package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_c195.model.AppointmentReport;
import wgu_c195.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeReportPage {
    private ObservableList<AppointmentReport> appointmentReports;

    @FXML private TableView<AppointmentReport> typeReportTableView;
    @FXML private TableColumn<AppointmentReport, String> monthColumn;
    @FXML private TableColumn<AppointmentReport, String> typeColumn;
    @FXML private TableColumn<AppointmentReport, String> typeAmount;

    public void init() {
        showAppointmentType();
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        typeAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    private void showAppointmentType() {
        appointmentReports = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT MONTHNAME(`start`) AS \"Month\", description AS \"Type\", COUNT(*) as \"Amount\" "
                            + "FROM appointment "
                            + "GROUP BY MONTHNAME(`start`), description");
            ResultSet rs = statement.executeQuery();

            while (rs.next())
                appointmentReports.add(new AppointmentReport(rs.getString(
                        "Month"), rs.getString("Type"), rs.getString("Amount")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        typeReportTableView.getItems().setAll(appointmentReports);
    }
}
