package wgu_c195.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import wgu_c195.app.App;
import wgu_c195.model.Appointment;
import wgu_c195.view.*;

import java.io.IOException;

public class PageUtil {
    private static volatile PageUtil sInstance = null;
    private BorderPane navBar;

    private PageUtil() {}
    public static PageUtil getInstance() {
        if (sInstance == null) {
            synchronized (PageUtil.class) {
                if (sInstance == null)
                    sInstance = new PageUtil();
            }
        }
        return sInstance;
    }

    public void showNavBar() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/NavBar.fxml"));
            navBar = loader.load();

            Scene scene = new Scene(navBar);
            App.sInstance.getStage().setScene(scene);

            App.sInstance.getStage().show();
        } catch (IOException e) {
            e.getCause().printStackTrace();
        }
    }

    public void launchLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/LoginPage.fxml"));
            AnchorPane loginScreen = loader.load();

            LoginPageController controller = loader.getController();
            controller.setTexts();

            Scene scene = new Scene(loginScreen);
            App.sInstance.getStage().setScene(scene);
            App.sInstance.getStage().show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchAppointmentPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/AppointmentPage.fxml"));
            AnchorPane appointmentScreen = loader.load();

            navBar.setCenter(appointmentScreen);

            AppointmentPageController controller = loader.getController();
            controller.init();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchCustomerPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/CustomerPage.fxml"));
            AnchorPane customerScreen = loader.load();

            navBar.setCenter(customerScreen);

            CustomerPageController controller = loader.getController();
            controller.init();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchNewAppointmentDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/AppointmentEditPage.fxml"));
            AnchorPane newApptScreen = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(App.sInstance.getStage());
            Scene scene = new Scene(newApptScreen);
            dialogStage.setScene(scene);

            AppointmentEditPageController controller = loader.getController();
            controller.init(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchEditAppointmentDialog(Appointment appointment) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/AppointmentEditPage.fxml"));
            AnchorPane editApptScreen = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(App.sInstance.getStage());
            Scene scene = new Scene(editApptScreen);
            dialogStage.setScene(scene);

            AppointmentEditPageController controller = loader.getController();
            controller.init(dialogStage);
            controller.setAppointment(appointment);

            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launchCalendarPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/CalendarPage.fxml"));
            AnchorPane pane = loader.load();

            navBar.setCenter(pane);

            CalendarPageController controller = loader.getController();
            controller.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
