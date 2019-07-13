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
import wgu_c195.util.LogUtil;
import wgu_c195.util.PageUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static wgu_c195.util.TimeUtil.convertTimeZone;
import static wgu_c195.util.TimeUtil.dateTimeFormatter;

public class LoginPage {

    private final static Logger LOGGER = Logger.getLogger(LogUtil.class.getName());

    private ResourceBundle rb = ResourceBundle.getBundle("string", Locale.getDefault());
    private User user;

    private ObservableList<Appointment> reminderList;

    @FXML private Label errorMessage;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Text usernameText;
    @FXML private Text passwordText;
    @FXML private Text titleText;
    @FXML private Button signInButton;

    @FXML
    void signIn() {
        if (usernameField.getText().length() == 0 || passwordField.getText().length() == 0) {
            errorMessage.setText(rb.getString("empty"));
        } else {
            validate(usernameField.getText(), passwordField.getText());
            if (this.user == null) {
                errorMessage.setText(rb.getString("incorrect"));
                return;
            }
            populateReminders();
            filterReminders();
            PageUtil.getInstance().showNavBar();
            PageUtil.getInstance().launchAppointmentPage();
            LOGGER.log(Level.INFO, "{0} logged in", this.user.getUsername());
        }
    }

    private void validate(String username, String password) {
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
                user = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setTexts() {
        reminderList = FXCollections.observableArrayList();
        titleText.setText(rb.getString("title"));
        usernameText.setText(rb.getString("username"));
        passwordText.setText(rb.getString("password"));
        signInButton.setText(rb.getString("signin"));
    }

    private void filterReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus15Min = now.plusMinutes(15);

        FilteredList<Appointment> filteredData = new FilteredList<>(reminderList);

        /*  Usage of lambda expression to simplify the source code complexity.
            Since the parameter of setPredicate function requires an anonymous class, using lambda expression is much
            easier to read and write. Check the below usage without lambda expression */
        filteredData.setPredicate(row -> { // Retrieving data using lambda expression with just 'row'.
            LocalDateTime rowDate = LocalDateTime.parse(row.getStart(), dateTimeFormatter); // Check the current time.
            return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min); // Return if data of 'row' fits the need we are looking for.
        });

        // This code below is 6 lines of code. Using lambda expression as above cuts down two lines at least.
        /*
        filteredData.setPredicate(new Predicate<Appointment>() {
            @Override
            public boolean test(Appointment appointment) {
                LocalDateTime rowDate = LocalDateTime.parse(appointment.getStart(), dateTimeFormatter);
                return rowDate.isAfter(now.minusMinutes(1)) && rowDate.isBefore(nowPlus15Min);
            }
        });
        */

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
                reminderList.add(new Appointment(
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
    }
}
