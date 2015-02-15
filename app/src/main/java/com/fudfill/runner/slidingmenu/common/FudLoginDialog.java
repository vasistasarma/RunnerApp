package com.fudfill.runner.slidingmenu.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.fudfill.runner.slidingmenu.R;

public class FudLoginDialog {

    private String EULA_PREFIX = "eula_";
    private Activity mActivity;
    private String TAG = "RWSApp";
    Dialog loginDialog = null;
    Handler mHandler;


    public FudLoginDialog(Activity context, Handler handler) {
        mActivity = context;
        mHandler = handler;

    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi;
    }

    public void show() {
        PackageInfo versionInfo = getPackageInfo();

        // the eulaKey changes every time you increment the version number in the AndroidManifest.xml
        final String eulaKey = EULA_PREFIX + versionInfo.versionCode;


        // Show the Eula
        String title = mActivity.getString(R.string.app_name) + " v" + versionInfo.versionName;
        LayoutInflater inflater = mActivity.getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
                .setView(inflater.inflate(R.layout.activity_logindialog, null))
                .setPositiveButton(android.R.string.ok, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String usernameStr;
                        String userPasswdStr;
                        EditText edUserName = (EditText) loginDialog.findViewById(R.id.username);
                        EditText edPasswd = (EditText) loginDialog.findViewById(R.id.password);
                        //EditText edServer = (EditText) loginDialog.findViewById(R.id.hostname);
                        usernameStr = edUserName.getText().toString();
                        userPasswdStr = edPasswd.getText().toString();
                        Log.d(TAG, "Username : " + usernameStr + ", Passwd: " + userPasswdStr);
                        if (usernameStr.length() > 0 && userPasswdStr.length() > 0) {
                            FudfillLoginTask loginTask = new FudfillLoginTask(mActivity, mHandler);
                            loginTask.execute(new String[]{usernameStr, userPasswdStr});
                            dialogInterface.dismiss();
                        } else {
                            mActivity.finish();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, new Dialog.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the activity as they have declined the EULA
                        mActivity.finish();
                    }

                });
        loginDialog = builder.create();
        loginDialog.show();
    }
}
 


