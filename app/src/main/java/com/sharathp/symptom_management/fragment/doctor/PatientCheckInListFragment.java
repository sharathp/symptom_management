package com.sharathp.symptom_management.fragment.doctor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.data.provider.contract.PatientCheckInContract;
import com.sharathp.symptom_management.fragment.BaseListFragment;
import com.sharathp.symptom_management.model.Eating;
import com.sharathp.symptom_management.model.Pain;
import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Calendar;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A list fragment representing a list of patient check-ins.
 */
public class PatientCheckInListFragment extends BaseListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = PatientCheckInListFragment.class.getSimpleName();
    private static final int PATIENT_CHECKINS_LOADER_ID = 0;
    private ListView mListView;

    private PatientCheckInListAdapter mPatientCheckInListAdapter;

    private long mPatientId;

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeListViewAndAdapter();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey(PatientAllDetailsFragment.ARG_PATIENT_ID)) {
            mPatientId = getArguments().getLong(PatientAllDetailsFragment.ARG_PATIENT_ID);
            loadPatientCheckIns();
        }
    }

    private void loadPatientCheckIns() {
        getLoaderManager().initLoader(PATIENT_CHECKINS_LOADER_ID, null, this);
    }

    private void initializeListViewAndAdapter() {
        mPatientCheckInListAdapter = new PatientCheckInListAdapter(getActivity(), null, 0);
        setListAdapter(mPatientCheckInListAdapter);
        // this is required as setting list adapter above, shows the list view
        setListShown(false);

        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int position, final long l) {
                final Cursor cursor = mPatientCheckInListAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    final PatientCheckIn patientCheckIn = PatientCheckInContract.PatientCheckInEntry.readPatientCheckIn(cursor);
                    Toast.makeText(getActivity(), patientCheckIn.getCheckinTime().toGMTString(), Toast.LENGTH_LONG);
                }
            }
        });
    }
    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
        switch (id) {
            case PATIENT_CHECKINS_LOADER_ID:
                final String sortOrder = PatientCheckInContract.PatientCheckInEntry.COLUMN_CHECKIN_TIME + " ASC";
                final CursorLoader cursorLoader = new CursorLoader(getActivity(), getPatientPatientCheckInsUri(),
                        PatientCheckInContract.PatientCheckInEntry.ALL_COLUMNS, null, null, sortOrder);
                return cursorLoader;
            default:
                return null;
        }
    }

    private Uri getPatientPatientCheckInsUri() {
        return PatientCheckInContract.PatientCheckInEntry.buildPatientPatientCheckInsUri(mPatientId);
    }

    @Override
    public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
        mPatientCheckInListAdapter.swapCursor(cursor);
        // show list view - list view will be initially hidden..
        setListShown(true);
    }

    @Override
    public void onLoaderReset(final Loader<Cursor> loader) {
        mPatientCheckInListAdapter.swapCursor(null);
    }

    // nested classes and interfaces..
    static class PatientCheckInListAdapter extends CursorAdapter {
        final Context mContext;

        PatientCheckInListAdapter(final Context context, final Cursor c, final int flags) {
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
    }

    static class ViewHolder {
        @InjectView(R.id.time_text_view)
        TextView mTimeTextView;

        @InjectView(R.id.medicine_image_view)
        ImageView mMedicineImageView;

        @InjectView(R.id.pain_image_view)
        ImageView mPainImageView;

        @InjectView(R.id.eat_image_view)
        ImageView mEatImageView;

        ViewHolder(final View view) {
            ButterKnife.inject(this, view);
        }
    }
}