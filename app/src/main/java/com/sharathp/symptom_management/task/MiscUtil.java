package com.sharathp.symptom_management.task;

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
}
