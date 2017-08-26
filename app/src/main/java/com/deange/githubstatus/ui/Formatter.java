package com.deange.githubstatus.ui;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public final class Formatter {

    private static final DateTimeFormatter FORMATTER_HM = new DateTimeFormatterBuilder()
            .appendClockhourOfHalfday(1)
            .appendLiteral(':')
            .appendMinuteOfHour(2)
            .appendHalfdayOfDayText()
            .toFormatter();

    private static final DateTimeFormatter FORMATTER_HM_MD = new DateTimeFormatterBuilder()
            .append(FORMATTER_HM)
            .appendLiteral(", ")
            .appendMonthOfYearShortText()
            .appendLiteral(' ')
            .appendDayOfMonth(1)
            .toFormatter();

    private Formatter() {
        throw new AssertionError();
    }

    public static String formatLocalDateTime(final LocalDateTime date) {
        // For events on today, print only the time
        if (date.toLocalDate().equals(LocalDate.now())) {
            return FORMATTER_HM.print(date);
        } else {
            return FORMATTER_HM_MD.print(date);
        }
    }

}
