package wgu_c195.view;

import javafx.fxml.FXML;
import wgu_c195.util.PageUtil;


public class NavBar {
    @FXML
    void openAppointmentPage() {
        PageUtil.getInstance().launchAppointmentPage();
    }

    @FXML
    void openCustomerPage() {
        PageUtil.getInstance().launchCustomerPage();
    }

    @FXML
    void openCalendarPage() {
        PageUtil.getInstance().launchCalendarPage();
    }

    @FXML
    void openSchedulePage() {
        PageUtil.getInstance().launchSchedulePage();
    }

    @FXML
    void openTypeReportPage() {
        PageUtil.getInstance().launchTypeReportPage();
    }

    @FXML
    void openOfficesPage() {
        PageUtil.getInstance().launchOfficesPage();
    }
}
