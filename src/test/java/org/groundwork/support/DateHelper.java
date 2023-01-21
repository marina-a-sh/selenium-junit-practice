package org.groundwork.support;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateHelper {

    private static final WeekFields weekFields = WeekFields.of(Locale.US);
    private static final ZoneId zid = ZoneId.of("America/New_York");

    public static LocalDate now() {
        return LocalDate.now(zid);
    }

    public static LocalDateTime nowWithTime() {
        return LocalDateTime.now(zid);
    }

    public static LocalDate recentSunday() {
        return now().minusDays(daysToRecentSunday());
    }

    public static int daysToRecentSunday() {
        return daysToRecentSundayFrom(now());
    }

    public static int daysToRecentSundayFrom(LocalDate date) {
        return date.get(weekFields.dayOfWeek()) - 1;
    }

    public static int todayDayOfWeek() { return now().get(weekFields.dayOfWeek()); }

    public static int dayOfWeek(LocalDate date) { return date.get(weekFields.dayOfWeek()); }

    public static boolean todayIsSaturday() { return todayDayOfWeek() == DayOfWeek.SATURDAY.get(weekFields.dayOfWeek()); }

    public static int sunday() { return DayOfWeek.SUNDAY.get(weekFields.dayOfWeek()); }

}
