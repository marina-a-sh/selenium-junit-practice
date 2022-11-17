package org.groundwork.support;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateHelper {

    private static WeekFields weekFields = WeekFields.of(Locale.US);

    public static LocalDate recentSunday() {
        return LocalDate.now().minusDays(daysToRecentSunday());
    }

    public static int daysToRecentSunday() {
        return daysToRecentSundayFrom(LocalDate.now());
    }

    public static int daysToRecentSundayFrom(LocalDate date) {
        return date.get(weekFields.dayOfWeek()) - 1;
    }

    public static int todayDayOfWeek() { return LocalDate.now().get(weekFields.dayOfWeek()); }

    public static int dayOfWeek(LocalDate date) { return date.get(weekFields.dayOfWeek()); }

    public static boolean todayIsSaturday() { return todayDayOfWeek() == DayOfWeek.SATURDAY.get(weekFields.dayOfWeek()); }

    public static int sunday() { return DayOfWeek.SUNDAY.get(weekFields.dayOfWeek()); }

}
