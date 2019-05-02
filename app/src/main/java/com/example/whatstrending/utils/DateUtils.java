package com.example.whatstrending.utils;


import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

public class DateUtils {

    //NewsApi.org provides datetime as UTC+000
    private static final String UTC_PREFIX = "UTC";
    private static final int NEWS_API_UTC_OFFSET = 0;

    private static final String DATE_FORMAT_STRING = "MMM d, yyyy";

    private DateUtils() {}

    /*
    Solution to proper handling of UTC dates found
    @ https://stackoverflow.com/questions/6543174/how-can-i-parse-utc-date-time-string-into-something-more-readable
     */

    /**
     * @param dateString Date string in UTC format
     * @return Formatted date
     */
    public static String formatUTCDateString(String dateString) {
        Instant instant = Instant.parse(dateString);

        ZoneOffset zoneOffset = ZoneOffset.ofHours(NEWS_API_UTC_OFFSET);
        ZoneId zoneId = ZoneId.ofOffset(UTC_PREFIX, zoneOffset);
        ZonedDateTime zdt = ZonedDateTime.ofInstant(instant, zoneId);

        DateTimeFormatter dtf = DateTimeFormatter
                .ofPattern(DATE_FORMAT_STRING)
                .withLocale( Locale.US );
        return zdt.format(dtf);
    }
}
