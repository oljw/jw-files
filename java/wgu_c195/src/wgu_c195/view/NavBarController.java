package wgu_c195.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import wgu_c195.util.PageUtil;


public class NavBarController {
    @FXML
    void openAppointmentPage(ActionEvent event) {
        PageUtil.getInstance().showAppointmentScreen();
    }

    @FXML
    void openCustomerPage(ActionEvent event) {
        PageUtil.getInstance().showCustomerScreen();
    }

    @FXML
    void openReportPage(ActionEvent event) {
        PageUtil.getInstance().showReports();
    }
}
