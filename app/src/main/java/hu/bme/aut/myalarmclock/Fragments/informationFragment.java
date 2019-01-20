package hu.bme.aut.myalarmclock.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import hu.bme.aut.myalarmclock.R;

public class informationFragment extends DialogFragment {

    public static final String TAG = "informationFragment";

    /*public interface informationFragmentListener {
    }

    private informationFragmentListener listener;*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        /*
        if (activity instanceof informationFragmentListener) {
            listener = (informationFragmentListener) activity;
        } else {
            throw new RuntimeException("Nem valósítja meg az informationFragmentListenert!");
        }
        */
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.information)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_information, null);
        return contentView;
    }
}
