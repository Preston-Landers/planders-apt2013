package com.appspot.cee_me.android;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.tz.FixedDateTimeZone;

/**
 * Turn an endpoint API DateTime back to a Joda DateTime
 */
public class DateUtils {

    public static DateTime getDateTimeFromJSONObject(com.appspot.cee_me.sync.model.DateTime apiDateTime) {
        return new DateTime(
                apiDateTime.getYear(),
                apiDateTime.getMonthOfYear(),
                apiDateTime.getDayOfMonth(),
                apiDateTime.getHourOfDay(),
                apiDateTime.getMinuteOfHour(),
                apiDateTime.getSecondOfMinute(),
                apiDateTime.getMillisOfSecond(),
                getDateTimeZoneFromJSONObject(apiDateTime.getZone())
        );

    }

    public static DateTimeZone getDateTimeZoneFromJSONObject(com.appspot.cee_me.sync.model.DateTimeZone apiDateTimeZone) {
        return DateTimeZone.forID(apiDateTimeZone.getId());
    }

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormat.forStyle("MS");
    }

    /**
     * Turn a DateTime to a YYYYMMDD style (f8) string
     * @param dateTime use null for current date
     * @return F8 style string YYYYMMDD
     */
    public static String getF8Date(DateTime dateTime) {
        if (dateTime == null) {
            dateTime = new DateTime();
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        return fmt.print(dateTime);
    }
}
