package wgu.utils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/***
 * Class handles time utils.
 * */
public class TimeUtil {

    private static final LocalTime businessHoursStart = LocalTime.of(8, 0, 0);
    private static final LocalTime businessHoursEnd = LocalTime.of(22, 0, 0);


    /**
     * Method converts local date time with default system zone id to utc time.
     *
     * @param localDateTime
     * @return utcZoned.toLocalDateTime()
     */
    public static LocalDateTime convertTimeDateUTC(LocalDateTime localDateTime) {
        ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime utcZoned = ldtZoned.withZoneSameInstant(ZoneId.of("UTC"));

        return utcZoned.toLocalDateTime();
    }

    /**
     * Method converts local date time with default system zone id to est time.
     *
     * @param localDateTime
     * @return estZoned.toLocalDateTime()
     */
    public static LocalDateTime convertTimeDateEST(LocalDateTime localDateTime) {
        ZonedDateTime ldtZoned = localDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime estZoned = ldtZoned.withZoneSameInstant(ZoneId.of("US/Eastern"));

        return estZoned.toLocalDateTime();
    }

    /**
     * returns business start time
     *
     * @return businessHoursStart
     */
    public static LocalTime getBusinessHoursStart() {
        return businessHoursStart;
    }

    /**
     * returns business end time
     *
     * @return businessHoursEnd
     */
    public static LocalTime getBusinessHoursEnd() {
        return businessHoursEnd;
    }
}
