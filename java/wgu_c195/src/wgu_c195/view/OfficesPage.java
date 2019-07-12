package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import wgu_c195.model.Office;
import wgu_c195.util.DBUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class OfficesPage {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    private final ZoneId zoneId = ZoneId.systemDefault();

    private ObservableList<Office> offices;

    @FXML private TableView<Office> officesTableView;
    @FXML private TableColumn<Office, String> officeCityColumn;
    @FXML private TableColumn<Office, String> officeCountryColumn;

    public void init() {
        showAppointmentType();

        officeCityColumn.setCellValueFactory(new PropertyValueFactory<>("City"));
        officeCountryColumn.setCellValueFactory(new PropertyValueFactory<>("Country"));
    }

    private void showAppointmentType() {
        offices = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT city.countryId, city.city, country.countryId, country.country " +
                            "FROM city, country " +
                            "WHERE city.countryId = country.countryId"
            );
            ResultSet rs = statement.executeQuery();

            while (rs.next())
                offices.add(new Office(rs.getString(
                        "city"), rs.getString("country")));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        officesTableView.getItems().setAll(offices);
    }
}

