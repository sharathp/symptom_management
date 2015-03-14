package com.sharathp.symptom_management.fragment.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sharathp.symptom_management.model.Medication;

public class CheckInDetailsDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        return super.onCreateDialog(savedInstanceState);
    }


    static class MedicationsAdapter extends RecyclerView.Adapter<MedicationViewHolder> {

        @Override
        public MedicationViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
            return null;
        }

        @Override
        public void onBindViewHolder(final MedicationViewHolder medicationViewHolder, final int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }

    static class MedicationViewHolder extends RecyclerView.ViewHolder {

        MedicationViewHolder(final View itemView) {
            super(itemView);
        }
    }
}