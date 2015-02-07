package com.sharathp.symptom_management.fragment.doctor;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.fragment.BaseFragment;

import java.lang.ref.WeakReference;

import butterknife.InjectView;

/**
 * A fragment representing a single Patient detail screen.
 * This fragment is either contained in a {@link com.sharathp.symptom_management.activity.doctor.PatientListActivity}
 * in two-pane mode (on tablets) or a {@link com.sharathp.symptom_management.activity.doctor.PatientDetailActivity}
 * on handsets.
 */
public class PatientAllDetailsFragment extends BaseFragment {
    public static final String ARG_PATIENT_ID = "patient_id";

    private long mPatientId;

    @InjectView(R.id.patient_detail_view_pager)
    ViewPager mViewPager;

    @InjectView(R.id.patient_detail_view_tabs)
    PagerSlidingTabStrip mTabsStrip;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(PatientAllDetailsFragment.ARG_PATIENT_ID)) {
            mPatientId = getArguments().getLong(PatientAllDetailsFragment.ARG_PATIENT_ID);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.d_fragment_all_patient_details, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewPager.setAdapter(
                new PatientDetailsFragmentPagerAdapter(getChildFragmentManager(),
                        getArguments(), getActivity()));
        mTabsStrip.setViewPager(mViewPager);
    }

    private static class PatientDetailsFragmentPagerAdapter extends FragmentPagerAdapter {
        private final int mPageCount = 3;
        private final int mTabTitles[] = new int[] {
            R.string.doctor_patient_details_tab_title,
                R.string.doctor_patient_medications_tab_title,
                R.string.doctor_patient_checkins_tab_title,
        };

        private final Bundle mArguments;
        private final WeakReference<Context> mContext;


        PatientDetailsFragmentPagerAdapter(final FragmentManager fm,  final Bundle arguments,
                                           final Context context) {
            super(fm);
            this.mArguments = arguments;
            this.mContext = new WeakReference<Context>(context);
        }

        @Override
        public Fragment getItem(final int position) {
            Fragment fragment = null;
            switch(position) {
                case 0:
                    fragment = new PatientDetailFragment();
                    break;
                case 1:
                    fragment = new MedicationListFragment();
                    break;
                case 2:
                    fragment = new PatientCheckInListFragment();
                    break;
            }
            if(fragment == null) {
                throw new IllegalArgumentException("Invalid position: " + position);
            }

            final Bundle arguments = new Bundle();
            arguments.putLong(ARG_PATIENT_ID, mArguments.getLong(ARG_PATIENT_ID));
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            return mPageCount;
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            if(mContext.get() == null) {
                return "N/A";
            }

            return mContext.get().getString(mTabTitles[position]);
        }
    }
}