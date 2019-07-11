package wgu_c195.app;

import wgu_c195.model.User;
import wgu_c195.util.DBUtil;

import java.util.Locale;
import javafx.application.Application;
import static javafx.application.Application.launch;

import javafx.stage.Stage;
import wgu_c195.util.LoggerUtil;
import wgu_c195.util.PageUtil;

/**
 *
 * @author JW
 */
public class App extends Application {

    public static App sInstance;
    private Stage stage;
    private User user;
    Locale locale = Locale.getDefault();

    public App() {
        sInstance = this;
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("C195");

        //Locale.setDefault(new Locale("fr", "FR"));
        //System.out.println(Locale.getDefault());

        DBUtil.init();
        LoggerUtil.init();

        PageUtil.getInstance().showLoginScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        DBUtil.disconnect();
        super.stop();
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
