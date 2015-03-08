package com.sharathp.symptom_management.ui;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.sharathp.symptom_management.R;
import com.sharathp.symptom_management.activity.SettingsActivity;
import com.sharathp.symptom_management.activity.doctor.DoctorActivity;
import com.sharathp.symptom_management.activity.doctor.RecentCheckInsActivity;
import com.sharathp.symptom_management.login.Session;

public class DoctorDrawer {
    public static final int HOME_ID = 1;
    public static final int RECENT_CHECKINS_ID = 2;
    public static final int PATIENTS_LAST_CHECKIN_ID = 3;
    public static final int SETTINGS_ID = 4;
    public static final int ABOUT_ID = 5;
    public static final int RATE_ID = 6;

    private Drawer.Result mDrawer;
    private Activity mActivity;

    public DoctorDrawer(final Activity activity, final Toolbar toolbar) {
        mDrawer = getDrawer(activity, toolbar);
        mActivity = activity;
    }

    public void setDrawerItemAsSelected(final Integer itemId) {
        mDrawer.setSelectionByIdentifier(itemId, false);
    }

    private Drawer.Result getDrawer(final Activity activity, final Toolbar toolbar) {

        final String userName = Session.restore(activity).getUserName();
        final View headerView = activity.getLayoutInflater().inflate(R.layout.c_drawer_header, null, false);
        final TextView nameView = (TextView) headerView.findViewById(R.id.drawer_header_user_name_text_view);
        nameView.setText(userName);

        final Drawer.Result mDrawer = new Drawer()
                .withActivity(activity)
                .withHeader(headerView)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName(R.string.drawer_item_home).withIcon(R.drawable.ic_home).withIdentifier(HOME_ID),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_recent_checkins).withIcon(R.drawable.ic_recent).withIdentifier(RECENT_CHECKINS_ID),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_patients_last_checkin).withIcon(R.drawable.ic_checkin).withIdentifier(PATIENTS_LAST_CHECKIN_ID),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(R.drawable.ic_settings).withIdentifier(SETTINGS_ID),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_about).withIcon(R.drawable.ic_info).withIdentifier(ABOUT_ID),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_rate).withIcon(R.drawable.ic_thumb_up).withIdentifier(RATE_ID)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(final AdapterView<?> parent, final View view,
                                            final int position, final long id, final IDrawerItem drawerItem) {
                        Intent intent = null;
                        switch (drawerItem.getIdentifier()) {
                            case HOME_ID:
                                intent = new Intent(mActivity, DoctorActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                            case RECENT_CHECKINS_ID:
                                intent = new Intent(mActivity, RecentCheckInsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                break;
                            case PATIENTS_LAST_CHECKIN_ID:
                                // TODO - go to last checkin of each patient
                                break;
                            case SETTINGS_ID:
                                intent = new Intent(mActivity, SettingsActivity.class);
                                break;
                            case ABOUT_ID:
                                // TODO - go to about
                                break;
                            case RATE_ID:
                                // TODO - go to rate app in play store
                                break;
                        }
                        mActivity.startActivity(intent);
                        Toast.makeText(activity, "Clicked: " + drawerItem.getIdentifier(), Toast.LENGTH_LONG).show();
                    }
                })
                .build();
        return mDrawer;
    }}