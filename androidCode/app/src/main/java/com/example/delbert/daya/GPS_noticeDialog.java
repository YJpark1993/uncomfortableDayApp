package com.example.delbert.daya;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by YuJin on 2016-08-28.
 */
public class GPS_noticeDialog extends DialogFragment {
    AlertDialog.Builder builder;
    String title;
    public GPS_noticeDialog() {}
    public static GPS_noticeDialog newInstance() {
        Log.d("xxx", "PermissionDialog PermissionDialog");
        GPS_noticeDialog gd = new GPS_noticeDialog();
        return gd;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("xxx", "onCreateDialog PermissionDialog");
        // Use the Builder class for convenient dialog construction
        builder = new AlertDialog.Builder(getActivity());
        // builder.setView(
        builder.setMessage("[GPS] 가 꺼져있습니다.\n설정을 눌러 GPS 위치정보를 켜주세요\n")
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
                .setNegativeButton("설정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
//

    public void doPositiveClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Positive click!");
    }

    public void doNegativeClick() {
        // Do stuff here.
        Log.i("FragmentAlertDialog", "Negative click!");
    }

}
