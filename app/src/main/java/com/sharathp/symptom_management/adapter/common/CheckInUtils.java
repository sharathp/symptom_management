package com.sharathp.symptom_management.adapter.common;

import android.content.Context;
import android.text.format.DateUtils;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.model.Eating;
import com.sharathp.symptom_management.model.Pain;

import java.util.Calendar;
import java.util.Date;

public class CheckInUtils {

    public static CharSequence getDateString(final Date time, final Context context) {
        final long now = Calendar.getInstance().getTime().getTime();
        final String dateText = DateUtils.getRelativeTimeSpanString(
                time.getTime(), now, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
                .toString();
        return context.getString(R.string.patient_checkin_list_time_format, dateText);
    }

    public static int getEatImage(final Eating eating) {
        switch (eating) {
            case CANNOT_EAT:
                return R.drawable.eat_cannot;
            case NO:
                return R.drawable.eat_no;
            case SOME:
                return R.drawable.eat_some;
        }
        return R.drawable.eat_some;
    }

    public static int getPainImage(final Pain pain) {
        switch (pain) {
            case HIGH:
                return R.drawable.pain_high;
            case MEDIUM:
                return R.drawable.pain_med;
            case LOW:
                return R.drawable.pain_low;
        }
        return R.drawable.pain_low;
    }
}