package com.integ.task.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class DateTimeUtil {
    /**
     * Constructor
     */
    private DateTimeUtil(){}

    /**
     * Provides date time at start of the day
     * @param date Date to be converted
     * @return Date at start of the day
     * @throws ParseException If not able to parse the date
     */
    public static Date getDateAtDayStart(String date) throws ParseException {
        return Constant.SIMPLE_DATE_TIME_FORMAT.parse(date + " " + Constant.DAY_START_TIME);
    }
    /**
     * Provides date time at end of the day
     * @param date Date to be converted
     * @return Date at end of the day
     * @throws ParseException If not able to parse the date
     */
    public static Date getDateAtDayEnd(String date) throws ParseException {
        return Constant.SIMPLE_DATE_TIME_FORMAT.parse(date + " " + Constant.DAY_END_TIME);
    }
    /**
     * Provides date time at start of the day
     * @param date Date to be converted
     * @return Date at start of the day
     * @throws ParseException If not able to parse the date
     */
    public static java.sql.Date getSqlDateAtDayStart(String date) throws ParseException {
        return new java.sql.Date(getDateAtDayStart(date).getTime());
    }
    /**
     * Provides date time at end of the day
     * @param date Date to be converted
     * @return Date at end of the day
     * @throws ParseException If not able to parse the date
     */
    public static java.sql.Date getSqlDateAtDayEnd(String date) throws ParseException {
        return new java.sql.Date(getDateAtDayEnd(date).getTime());
    }

    /**
     * Provides calendar
     * @return Provides previous date
     */
    public static Date getPreviousDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, - 1);
        return calendar.getTime();
    }
    public static java.sql.Date getSqlPreviousDate(){
        return new java.sql.Date(getPreviousDate().getTime());
    }

    public static java.sql.Date getSqlDate(String dateTime) throws ParseException {
        return new java.sql.Date(getDate(dateTime).getTime());
    }

    public static Date getDate(String dateTime) throws ParseException {
        return new Date(Constant.SIMPLE_DATE_TIME_FORMAT.parse(dateTime).getTime());
    }

    public static String getMySqlDateString(String dateTime) throws ParseException {
        return getMySqlDateString(Constant.SIMPLE_DATE_TIME_FORMAT.parse(dateTime));
    }
    public static String getMySqlDateString(Date dateTime) {
        return Constant.SIMPLE_SQL_DATE_FORMAT.format(dateTime);
    }

    public static String getParsedDate(String dateStr) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        return Constant.SIMPLE_SQL_DATE_FORMAT.format(dateFormat.parse(dateStr));
    }

    public static LocalDateTime getLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date getDateOfLocalDateTime(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    }

    public static LocalDate getLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date getDateOfLocalDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    }

    public static String getLocalDateTimeByString(String date) {
        String cleanedDate = date.contains(".") ? date.split("\\.")[0] : date;

        // List of possible date formats
        List<String> patterns = Arrays.asList(
                "yyyy-MM-dd hh:mm:ss", // Expected pattern
                "dd-MMM-yyyy" // Incoming pattern
        );

        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDate localDate = LocalDate.parse(cleanedDate, formatter);

                // Convert to "yyyy-MM-dd" format
                return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                // Try next pattern
            }
        }

        throw new DateTimeParseException("Unable to parse date: " + date, date, 0);
    }

}
