package com.sharathp.symptom_management.adapter.common;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.model.PatientCheckIn;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckInListAdapter extends CursorAdapter {

    public CheckInListAdapter(final Context context, final Cursor c, final int flags) {
        super(context, c, flags);
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
        viewHolder.mTimeTextView.setText(CheckInUtils.getDateString(patientCheckIn.getCheckinTime(), context));
        viewHolder.mEatImageView.setImageResource(CheckInUtils.getEatImage(patientCheckIn.getEating()));
        viewHolder.mPainImageView.setImageResource(CheckInUtils.getPainImage(patientCheckIn.getPain()));
        if (patientCheckIn.isMedicated()) {
            viewHolder.mMedicineImageView.setImageResource(R.drawable.pill);
        }
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