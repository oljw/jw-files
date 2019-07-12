package wgu_c195.view;

import javafx.fxml.FXML;
import wgu_c195.util.PageUtil;


public class NavBarController {
    @FXML
    void openAppointmentPage() {
        PageUtil.getInstance().showAppointmentScreen();
    }

    @FXML
    void openCustomerPage() {
        PageUtil.getInstance().showCustomerScreen();
    }

    @FXML
    void openReportPage() {
        PageUtil.getInstance().showReports();
    }
}
