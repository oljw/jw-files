package wgu_c195.util;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class TimeUtil {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);

    private static final ZoneId zoneId = ZoneId.systemDefault();

    public static String convertTimeZone(Timestamp timestamp) {
        return timestamp.toInstant().atZone(zoneId).format(dateTimeFormatter);
    }
}
