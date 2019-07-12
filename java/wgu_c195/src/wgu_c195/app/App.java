package wgu_c195.app;

import javafx.application.Application;
import javafx.stage.Stage;
import wgu_c195.model.User;
import wgu_c195.util.DBUtil;
import wgu_c195.util.LogUtil;
import wgu_c195.util.PageUtil;

public class App extends Application {

    public static App sInstance;
    private Stage stage;
    private User user;

    public App() {
        sInstance = this;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        this.stage.setTitle("C195");

//        Locale.setDefault(new Locale("fr", "FR"));
//        System.out.println(Locale.getDefault());

        DBUtil.init();
        LogUtil.init();

        PageUtil.getInstance().launchLoginPage();
    }

    @Override
    public void stop() throws Exception {
        DBUtil.disconnect();
        super.stop();
    }

    public Stage getStage() {
        return this.stage;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
