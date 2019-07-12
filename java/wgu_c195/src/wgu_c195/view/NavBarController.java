package wgu_c195.view;

import javafx.fxml.FXML;
import wgu_c195.util.PageUtil;


public class NavBarController {
    @FXML
    void openAppointmentPage() {
        PageUtil.getInstance().launchAppointmentPage();
    }

    @FXML
    void openCustomerPage() {
        PageUtil.getInstance().launchCustomerPage();
    }

    @FXML
    void openReportPage() {
        PageUtil.getInstance().launchCalendarPage();
    }
}
