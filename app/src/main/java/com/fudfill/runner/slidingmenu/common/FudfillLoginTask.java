package com.fudfill.runner.slidingmenu.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.fudfill.runner.slidingmenu.MainActivity;
import com.fudfill.runner.slidingmenu.syncadapter.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FudfillLoginTask extends AsyncTask<String, Integer, Integer> {
    private static String TAG = "FudfillLoginTask";
    ProgressDialog dialog;
    AlertDialog alert;
    Activity mActivity;
    Handler mHandler;
    Message msg;

    public FudfillLoginTask(Activity activity, Handler handler) {
        mActivity = activity;
        mHandler = handler;
    }

    @Override
    protected Integer doInBackground(String... params) {
        // TODO Auto-generated method stub
        JSONArray userDetails;
        ServiceHandler sh = new ServiceHandler();
        String username = (String) params[0];
        String password = (String) params[1];
        String post_url = "http://" + FudfillConfig.getServerAddr() +
                FudfillConfig.getLoginUrl()+"/"+username;
        String jsonData = "{ \"username\" : \"" + username + "\" , \"password\" : \"" + password + "\"}";

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCallWithS(post_url, ServiceHandler.PUT, jsonData);
        Log.d("Fudfill", "JSON To be synced Response: jsonStr");

        //  jsonStr = "{\"result\": \"success\" , \"runnerid\" : \"abc123\"}";

        if (jsonStr != null && jsonStr.contains("success")) {
            try {
                JSONObject tJson = new JSONObject(jsonStr);
                userDetails = tJson.getJSONArray("users");

                for (int i = 0; i < userDetails.length(); i++) {
                    JSONObject c = userDetails.getJSONObject(i);
                    String userId = c.getString("user_id");
                    String userEmail = c.getString("user_email");
                    String userContact = c.getString("user_contact_no");
                    Log.d("Fudfill", "LoginTask: RunnerId : " + userId);
                    FudfillConfig.setRunnerId(userId);
                    }

            } catch (JSONException ex) {
                ex.printStackTrace();
                return -1;
            }
            return 0;
        }
        return -1;

    }


    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        dialog = ProgressDialog.show(mActivity, "Logining...", "Please wait...");

    }

    @Override
    protected void onPostExecute(Integer result) {
        // TODO Auto-generated method stub
        dialog.dismiss();
        showNotification(result);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        // TODO Auto-generated method stub
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

    }


    private void showNotification(int status) {
        // Create the alert dialog with a alert builder.
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mActivity);
        builder.setTitle("Login Status")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        alert.dismiss();
                        msg.sendToTarget();
                    }
                });

        if (status == 0) {
            builder.setMessage("Login Success");
            msg = mHandler.obtainMessage(MainActivity.LOGIN_SUCCESS);
            msg.sendToTarget();
        } else {
            builder.setMessage("Login Failed");
            msg = mHandler.obtainMessage(MainActivity.LOGIN_FAIL);
            alert = builder.create();
            alert.show();
        }

    }
}
