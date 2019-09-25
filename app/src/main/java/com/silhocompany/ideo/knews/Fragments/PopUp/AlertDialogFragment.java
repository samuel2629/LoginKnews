package com.silhocompany.ideo.knews.Fragments.PopUp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.silhocompany.ideo.knews.R;

/**
 * Created by Samuel on 29/01/2017.
 */

public class AlertDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.title_alert_dialog))
                .setMessage(context.getString(R.string.message_alert_dialog))
                .setPositiveButton(R.string.ok, null);
        return builder.create();
    }

}
