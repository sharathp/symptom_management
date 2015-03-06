package com.sharathp.symptom_management.adapter.common;

/**
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.model.Eating;
import com.sharathp.symptom_management.model.Pain;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PatientCheckInListAdapter extends CursorAdapter {
    final Context mContext;

    public PatientCheckInListAdapter(final Context context, final Cursor c, final int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.c_list_item_checkin, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final PatientCheckIn patientCheckIn = PatientCheckInContract.PatientCheckInEntry.readPatientCheckIn(cursor);
        viewHolder.mTimeTextView.setText(getDateString(patientCheckIn.getCheckinTime()));
        viewHolder.mEatImageView.setImageResource(getEatImage(patientCheckIn.getEating()));
        viewHolder.mPainImageView.setImageResource(getPainImage(patientCheckIn.getPain()));
        if (patientCheckIn.isMedicated()) {
            viewHolder.mMedicineImageView.setImageResource(R.drawable.pill);
        }
    }

    private CharSequence getDateString(final Date time) {
        final long now = Calendar.getInstance().getTime().getTime();
        final String dateText = DateUtils.getRelativeTimeSpanString(
                time.getTime(), now, DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_RELATIVE)
                .toString();
        return mContext.getString(R.string.patient_checkin_list_time_format, dateText);
    }

    private int getEatImage(final Eating eating) {
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

    private int getPainImage(final Pain pain) {
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

    public static class ViewHolder {
        @InjectView(R.id.time_text_view)
        public TextView mTimeTextView;

        @InjectView(R.id.medicine_image_view)
        public ImageView mMedicineImageView;

        @InjectView(R.id.pain_image_view)
        public ImageView mPainImageView;

        @InjectView(R.id.eat_image_view)
        public ImageView mEatImageView;

        public ViewHolder(final View view) {
            ButterKnife.inject(this, view);
        }
    }
}