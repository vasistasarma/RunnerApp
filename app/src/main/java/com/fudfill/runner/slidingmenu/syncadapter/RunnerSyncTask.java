package com.fudfill.runner.slidingmenu.syncadapter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.fudfill.runner.slidingmenu.MainActivity;
import com.fudfill.runner.slidingmenu.R;
import com.fudfill.runner.slidingmenu.common.FudfillConfig;
import com.fudfill.runner.slidingmenu.common.RunnerProvider;

/**
 * Created by praveenthota on 2/5/15.
 */
public class RunnerSyncTask extends AsyncTask<Object, Integer, Integer> {

        Context context;
        Context appContext;

    ContentResolver mContentResolver;
    boolean syncResult = false;
    private NotificationManager mNotificationManager;
    private int notificationID = 100;

        @Override
        protected Integer doInBackground(Object... params) {
            // TODO Auto-generated method stub
            this.context = (Context) params[0];
            appContext = context.getApplicationContext();
            mContentResolver = context.getContentResolver();
            syncResult = false;

            syncResult = syncOrdersToServer();


            return null;
        }

    @Override
    protected void onPostExecute(Integer integer) {
        displayNotification();
        /*if(syncResult)
        {
            Toast.makeText(context,"Sync Success", Toast.LENGTH_SHORT).show();

        }
        else
        {
            Toast.makeText(context,"Sync failed", Toast.LENGTH_SHORT).show();
        }*/
    }



    public boolean syncOrdersToServer() {
        // Retrieve student records
        String URL = "content://com.fudfill.runner.provider.items/orders";
        String base_url = "http://"+ FudfillConfig.SERVER_ADDR+"/fudfildelivery/updateorder";
        Uri items = Uri.parse(URL);
        Cursor c = mContentResolver.query(items, null, null, null, "order_id");
        int count = c.getCount();
        Log.d("Fudfill", "Items to be synced: " + count);
        c.moveToFirst();
        if(count >0) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{ ");
            for (int index = 0; index < count; index++) {
                strBuilder.append("{ ");
                strBuilder.append("\"");
                strBuilder.append("order_id");
                strBuilder.append("\": \"");
                strBuilder.append(c.getString(c.getColumnIndex(RunnerProvider.ORDER_ID)));
                strBuilder.append("\",");
                strBuilder.append("\"");
                strBuilder.append("order_status");
                strBuilder.append("\": \"");
                strBuilder.append(c.getString(c.getColumnIndex(RunnerProvider.ORDER_STATUS)));
                strBuilder.append("\"");

                if(index == count-1)
                {
                    strBuilder.append(" }");
                }
                else
                {
                    strBuilder.append(" } , ");
                }
                c.moveToNext();
            }
            strBuilder.append(" }");
            Log.d("Fudfill","JSON To be synced: "+strBuilder.toString());

            if(strBuilder.length() > 5) {

                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCallWithS(base_url, ServiceHandler.PUT,strBuilder.toString());
                Log.d("Fudfill","JSON To be synced Response: jsonStr");

                if(jsonStr!=null && jsonStr.contains("success"))
                {
                    mContentResolver.delete(items,null,null);
                    Log.d("Fudfill","Deleting the item records");
                    return true;
                }
            }

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
        mBuilder.setTicker("Sync Alert!");
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

