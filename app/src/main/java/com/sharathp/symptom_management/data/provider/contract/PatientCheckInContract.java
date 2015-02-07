package com.sharathp.symptom_management.data.provider.contract;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.sharathp.symptom_management.model.PatientCheckIn;

import java.util.Date;

public class PatientCheckInContract extends SymptomManagementContract {
    public static final String PATH_CHECKINS = "patient-checkins";

    public static final class PatientCheckInEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CHECKINS).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CHECKINS;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CHECKINS;

        // Column names
        public static final String COLUMN_CHECKIN_TIME = "checkin_time";
        public static final String COLUMN_SERVER_ID = "server_id";

        public static final String[] ALL_COLUMNS = new String[]{
                _ID,
                COLUMN_SERVER_ID,
                COLUMN_CHECKIN_TIME,
        };

        public static final int _ID_INDEX = 0;
        public static final int COLUMN_SERVER_ID_INDEX = 1;
        public static final int COLUMN_CHECKIN_TIME_INDEX = 2;

        public static PatientCheckIn readPatientCheckIn(final Cursor cursor) {
            if(cursor == null || cursor.isAfterLast()) {
                return null;
            }

            final long id = cursor.getLong(_ID_INDEX);
            final String serverId = cursor.getString(COLUMN_SERVER_ID_INDEX);
            final Date checkInTime = new Date(cursor.getLong(COLUMN_CHECKIN_TIME_INDEX));

            final PatientCheckIn patientCheckIn = new PatientCheckIn();
            patientCheckIn.setId(id);
            patientCheckIn.setCheckinTime(checkInTime);
            patientCheckIn.setServerId(serverId);

            return patientCheckIn;
        }

        public static ContentValues getContentValues(final PatientCheckIn patientCheckIn) {
            final ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_SERVER_ID, patientCheckIn.getServerId());
            contentValues.put(COLUMN_CHECKIN_TIME, patientCheckIn.getCheckinTime().getTime());
            return contentValues;
        }

        public static Uri buildPatientPatientCheckInsUri(final long patientId) {
            final Uri patientUri = PatientContract.PatientEntry.buildPatientUri(patientId);
            return Uri.withAppendedPath(patientUri, PATH_CHECKINS);
        }

        public static Uri buildPatientCheckInUri(final long patientCheckinId) {
            return ContentUris.withAppendedId(CONTENT_URI, patientCheckinId);
        }
    }
}