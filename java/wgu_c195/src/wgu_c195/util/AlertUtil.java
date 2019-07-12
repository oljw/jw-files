package wgu_c195.util;

public class AlertUtil {
    private static volatile AlertUtil sInstance = null;

    private AlertUtil() {}
    public static AlertUtil getInstance() {
        if (sInstance == null) {
            synchronized (AlertUtil.class) {
                if (sInstance == null)
                    sInstance = new AlertUtil();
            }
        }
        return sInstance;
    }

    public void confirmation() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Confirm Cancel");
//        alert.setHeaderText("Are you sure you want to Cancel?");
//        alert.showAndWait()
//                .filter(response -> response == ButtonType.OK)
//                .ifPresent(response -> dialogStage.close());
    }
}
