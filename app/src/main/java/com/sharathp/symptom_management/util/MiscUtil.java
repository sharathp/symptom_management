package com.sharathp.symptom_management.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MiscUtil {

    private MiscUtil() {
        // singleton
    }

    public static String fullyQualify(final String table, final String column) {
        final StringBuilder sb = new StringBuilder();
        sb.append(table);
        sb.append(".");
        sb.append(column);
        return sb.toString();
    }

    public static String convertToString(final Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        return sdf.format(date);
    }
}
