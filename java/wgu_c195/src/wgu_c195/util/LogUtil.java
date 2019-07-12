package wgu_c195.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogUtil {

    private static FileHandler handler = null;

    public static void init() {
        try {
            handler = new FileHandler("c195_log.txt", 512 * 512, 1, true);
            Logger logger = Logger.getLogger("");
            handler.setFormatter(new SimpleFormatter());
            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
