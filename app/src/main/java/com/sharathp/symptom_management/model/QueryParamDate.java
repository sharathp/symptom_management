package com.sharathp.symptom_management.model;

import com.sharathp.symptom_management.util.MiscUtil;

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
        return MiscUtil.convertToString(mDate);
    }
}
