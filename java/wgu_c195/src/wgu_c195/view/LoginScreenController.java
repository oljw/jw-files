package wgu_c195.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import wgu_c195.app.App;
import wgu_c195.model.Appointment;
import wgu_c195.model.Customer;
import wgu_c195.model.User;
import wgu_c195.util.DBUtil;
import wgu_c195.util.LoggerUtil;
import wgu_c195.util.PageUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginScreenController {

    @FXML
    private Label errorMessage;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text usernameText;

    @FXML
    private Text passwordText;

    @FXML
    private Text titleText;

    @FXML
    private Button signinText;

    @FXML
    private Button cancelText;

    private ResourceBundle rb = ResourceBundle.getBundle("login", Locale.getDefault());

    private final ZoneId newzid = ZoneId.systemDefault();

    private final DateTimeFormatter timeDTF = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    private User user;
    ObservableList<Appointment> reminderList;
    private final static Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    @FXML
    void signIn() {
        if (usernameField.getText().length() == 0 || passwordField.getText().length() == 0) {
            errorMessage.setText(rb.getString("empty"));
        } else {
            validateUser(usernameField.getText(), passwordField.getText());
            if (this.user == null) {
                errorMessage.setText(rb.getString("incorrect"));
                return;
            }
            populateReminders();
            filterReminders();
            PageUtil.getInstance().showMenu();
            PageUtil.getInstance().showAppointmentScreen();
            LOGGER.log(Level.INFO, "{0} logged in", this.user.getUsername());
        }
    }

    private User validateUser(String username, String password) {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("userName"));
                user.setPassword(rs.getString("password"));
                user.setUserID(rs.getInt("userId"));
                App.sInstance.setUser(this.user);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void setTexts() {
        reminderList = FXCollections.observableArrayList();

        titleText.setText(rb.getString("title"));
        usernameText.setText(rb.getString("username"));
        passwordText.setText(rb.getString("password"));
        signinText.setText(rb.getString("signin"));
        cancelText.setText(rb.getString("cancel"));
    }

    private void filterReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);

        FilteredList<Appointment> filteredData = new FilteredList<>(reminderList);

        filteredData.setPredicate(row -> {
                    LocalDateTime rowDate = LocalDateTime.parse(row.getStart(), timeDTF);
                    return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
                }
        );
        if (filteredData.isEmpty()) {
            System.out.println("----> No reminders");
        } else {
            String type = filteredData.get(0).getDescription();
            String customer = filteredData.get(0).getCustomer().getCustomerName();
            String start = filteredData.get(0).getStart();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment Reminder");
            alert.setHeaderText("Reminder: You have the following appointment set for the next 15 minutes.");
            alert.setContentText("Your upcoming " + type + " appointment with " + customer +
                    " is currently set for " + start + ".");
            alert.showAndWait();
        }

    }

    private void populateReminders() {
        try {
            PreparedStatement statement = DBUtil.getConnection().prepareStatement(
                    "SELECT appointment.appointmentId, appointment.customerId, appointment.title, appointment.description, "
                            + "appointment.`start`, appointment.`end`, customer.customerId, customer.customerName, appointment.createdBy "
                            + "FROM appointment, customer "
                            + "WHERE appointment.customerId = customer.customerId AND appointment.createdBy = ? "
                            + "ORDER BY `start`");
            statement.setString(1, this.user.getUsername());
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

                reminderList.add(new Appointment(tAppointmentId, newLocalStart.format(timeDTF), newLocalEnd.format(timeDTF), tTitle, tType, tCustomer, tUser));

            }
        } catch (SQLException sqe) {
            System.out.println("Check your SQL");
            sqe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something besides the SQL went wrong.");
            e.printStackTrace();
        }
    }

}
