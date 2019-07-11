package wgu_c195.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import wgu_c195.app.App;
import wgu_c195.model.Appointment;
import wgu_c195.view.*;

import java.io.IOException;

public class PageUtil {
    private BorderPane menu;

    private PageUtil() {}

    private static volatile PageUtil sInstance = null;
    public static PageUtil getInstance() {
        if (sInstance == null) {
            synchronized (PageUtil.class) {
                if (sInstance == null)
                    sInstance = new PageUtil();
            }
        }
        return sInstance;
    }

    public void showLoginScreen() {
        try {
            // Load Login Screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/LoginScreen.fxml"));
            AnchorPane loginScreen = (AnchorPane) loader.load();

            // Give the controller access to the main app.
            LoginScreenController controller = loader.getController();
            controller.setTexts();

            // Show the scene containing the root layout.
            Scene scene = new Scene(loginScreen);
            App.sInstance.getStage().setScene(scene);
            App.sInstance.getStage().show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showMenu() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/NavBar.fxml"));
            menu = (BorderPane) loader.load();

            Scene scene = new Scene(menu);
            App.sInstance.getStage().setScene(scene);

            App.sInstance.getStage().show();
        } catch (IOException e) {
            e.getCause().printStackTrace();
        }
    }

    public void showAppointmentScreen() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/AppointmentScreen.fxml"));
            AnchorPane appointmentScreen = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(appointmentScreen);

            // Give the controller access to the main app.
            AppointmentScreenController controller = loader.getController();
            controller.setAppointmentScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showCustomerScreen() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/CustomerScreen.fxml"));
            AnchorPane customerScreen = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(customerScreen);

            // Give the controller access to the main app.
            CustomerScreenController controller = loader.getController();
            controller.setCustomerScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showReports() {
        try {
            // Load person overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/Reports.fxml"));
            TabPane tabPane = (TabPane) loader.load();

            // Set person overview into the center of root layout.
            menu.setCenter(tabPane);

            // Give the controller access to the main app.
            ReportsController controller = loader.getController();
            controller.setReports();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showNewApptScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/AppointmentEditScreen.fxml"));
            AnchorPane newApptScreen = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(App.sInstance.getStage());
            Scene scene = new Scene(newApptScreen);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AppointmentEditScreenController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showEditApptScreen(Appointment appointment) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(App.class.getResource("/wgu_c195/view/AppointmentEditScreen.fxml"));
            AnchorPane editApptScreen = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Appointment");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(App.sInstance.getStage());
            Scene scene = new Scene(editApptScreen);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            AppointmentEditScreenController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setAppointment(appointment);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
