package com.softline.csrv.migration.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {

    private static final Logger log = LoggerFactory.getLogger(DateParser.class);

    private static final SimpleDateFormat fullDateTempl = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    private static final SimpleDateFormat isoDateTempl = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat otherDateTempl = new SimpleDateFormat("ddMMyyHHmm");



    /**
     *
     * @param date
     * @return
     */
    public static Date parse(String date) throws ParseException {
        boolean isoDate = date.contains("-");
        boolean fullDate = date.contains("T");

        if (fullDate) {
            return fullDateTempl.parse(date);
        } else if (isoDate) {
            return isoDateTempl.parse(date);
        } else {
            return otherDateTempl.parse(date);
        }

    }
}
