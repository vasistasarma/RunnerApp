package com.fudfill.runner.slidingmenu.syncadapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.fudfill.runner.slidingmenu.MainActivity;
import com.fudfill.runner.slidingmenu.R;

/**
 * Created by praveenthota on 2/5/15.
 */
public class RunnerPublishTask extends AsyncTask<Object, Integer, Integer> {

        Context context;
        Context appContext;
        String post_url;
        String jsonData;

    ContentResolver mContentResolver;
    boolean syncResult = false;
    private NotificationManager mNotificationManager;
    private int notificationID = 101;

        @Override
        protected Integer doInBackground(Object... params) {
            // TODO Auto-generated method stub
            this.context = (Context) params[0];
            post_url = (String) params[1];
            jsonData = (String) params[2];

            syncResult = false;
            syncResult = syncToServer();
            return null;
        }

    @Override
    protected void onPostExecute(Integer integer) {
        displayNotification();
      }



    public boolean syncToServer() {
        // Retrieve student records
        String base_url = post_url;
        Log.d("Fudfill","JSON To be synced: "+jsonData+ post_url);

        ServiceHandler sh = new ServiceHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCallWithS(base_url, ServiceHandler.PUT,jsonData);
        Log.d("Fudfill","JSON To be synced Response: jsonStr");

        if(jsonStr!=null && jsonStr.contains("success"))
        {
           return true;
        }
        return false;
    }

    protected void displayNotification() {
        String title="Sync Status";
        String text;
        Log.i("Start", "notification");

      /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder =
                new NotificationCompat.Builder(context);
        if(syncResult)
        {
            text = "Updation Success";
        }
        else
        {
            text = "Updation failed";
        }
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setTicker("Location Update Alert!");
        mBuilder.setSmallIcon(R.drawable.ic_launcher);


      /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);

      /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

      /* notificationID allows you to update the notification later on. */
        mNotificationManager.notify(notificationID, mBuilder.build());
    }

    }

