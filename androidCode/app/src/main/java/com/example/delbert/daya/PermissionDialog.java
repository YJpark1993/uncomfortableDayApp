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
 * Created by YuJin on 2016-08-13.
 */
public class PermissionDialog extends DialogFragment {
    AlertDialog.Builder builder;
    String title;
    public PermissionDialog() {}
    public static PermissionDialog newInstance(String text) {
        Log.d("xxx", "PermissionDialog PermissionDialog");
        PermissionDialog pd = new PermissionDialog();
        Bundle args = new Bundle();
        args.putString("text", text);
        pd.setArguments(args);
        return pd;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("xxx", "onCreateDialog PermissionDialog");
        // Use the Builder class for convenient dialog construction
        title = getArguments().getString("text");
        builder = new AlertDialog.Builder(getActivity());
        // builder.setView(
        builder.setMessage("[" + title + "] 설정을 눌러 권한을 변경하세요.")
                .setNegativeButton("설정", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    .setData(Uri.parse("package:" + builder.getContext().getPackageName()));
                            builder.getContext().startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            builder.getContext().startActivity(intent);
                        }


                    }

                })
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //dismiss();
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