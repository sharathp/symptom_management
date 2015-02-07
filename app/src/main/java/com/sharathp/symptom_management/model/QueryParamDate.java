package com.sharathp.symptom_management.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QueryParamDate {
    private Date mDate;

    public QueryParamDate(final Date date) {
        mDate = date;
    }

    public Date getDate() {
        return mDate;
    }

    @Override
    public String toString() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        return sdf.format(mDate);
    }
}
