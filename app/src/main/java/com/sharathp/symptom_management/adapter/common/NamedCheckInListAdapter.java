package com.sharathp.symptom_management.adapter.common;

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
import com.sharathp.symptom_management.data.provider.contract.RecentCheckInContract;
import com.sharathp.symptom_management.model.Eating;
import com.sharathp.symptom_management.model.Pain;
import com.sharathp.symptom_management.model.PatientCheckIn;
import com.sharathp.symptom_management.model.RecentCheckIn;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class NamedCheckInListAdapter extends CursorAdapter {

    public NamedCheckInListAdapter(final Context context, final Cursor c, final int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.c_list_item_checkin_with_name, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final RecentCheckIn recentCheckIn = RecentCheckInContract.RecentCheckInEntry.readRecentCheckIn(cursor);
        viewHolder.mNameTextView.setText(getPatientName(recentCheckIn));
        viewHolder.mTimeTextView.setText(CheckInUtils.getDateString(recentCheckIn.getCheckinTime(), context));
        viewHolder.mEatImageView.setImageResource(CheckInUtils.getEatImage(recentCheckIn.getEating()));
        viewHolder.mPainImageView.setImageResource(CheckInUtils.getPainImage(recentCheckIn.getPain()));
        if (recentCheckIn.isMedicated()) {
            viewHolder.mMedicineImageView.setImageResource(R.drawable.pill);
        }
    }

    private String getPatientName(final RecentCheckIn recentCheckIn) {
        return recentCheckIn.getPatient().getFirstName() + " " + recentCheckIn.getPatient().getLastName();
    }

    public static class ViewHolder {
        @InjectView(R.id.name_text_view)
        public TextView mNameTextView;

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