package com.livetyping.moydom.utils;

import android.app.AlertDialog;
import android.content.Context;

import com.livetyping.moydom.R;

/**
 * Created by Ivan on 20.12.2017.
 */

public class AlertDialogUtils {

    public static void showAlertDone(Context context){
        new AlertDialog.Builder(context)
                .setTitle(R.string.your_application_is_accepted)
                .setMessage(R.string.we_will_contact_shortly)
                .setPositiveButton(R.string.good, (dialog, which) -> dialog.dismiss())
                .show();
    }
}
