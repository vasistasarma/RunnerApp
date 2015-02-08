/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fudfill.runner.slidingmenu.syncadapter;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.fudfill.runner.slidingmenu.common.RunnerProvider;


class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";

    /**
     * URL to fetch content from during a sync.
     * <p/>
     * <p>This points to the Android Developers Blog. (Side note: We highly recommend reading the
     * Android Developer Blog to stay up to date on the latest Android platform developments!)
     */
    // private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";
    private static String url = "http://192.168.1.64:8080/fudfildelivery/updateorder";

    /**
     * Network connection timeout, in milliseconds.
     */
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;  // 15 seconds

    /**
     * Network read timeout, in milliseconds.
     */
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;  // 10 seconds

    /**
     * Content resolver, for performing database operations.
     */
    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[]{
            "ORDER_ID",
            "ORDER_STATUS"};

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ORDER_ID = 0;
    public static final int COLUMN_ORDER_STATUS = 1;

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    /**
     * Called by the Android system in response to a request to run the sync adapter. The work
     * required to read data from the network, parse it, and store it in the content provider is
     * done here. Extending AbstractThreadedSyncAdapter ensures that all methods within SyncAdapter
     * run on a background thread. For this reason, blocking I/O and other long-running tasks can be
     * run <em>in situ</em>, and you don't have to set up a separate thread for them.
     * .
     * <p/>
     * <p>This is where we actually perform any work required to perform a sync.
     * {@link android.content.AbstractThreadedSyncAdapter} guarantees that this will be called on a non-UI thread,
     * so it is safe to peform blocking I/O here.
     * <p/>
     * <p>The syncResult argument allows you to pass information back to the method that triggered
     * the sync.
     */
    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        syncOrdersToServer();
        Log.i(TAG, "Network synchronization complete");
    }


    public void syncOrdersToServer() {
        // Retrieve student records
        String URL = "content://com.fudfill.runner.provider.items/orders";
        Uri items = Uri.parse(URL);
        Cursor c = mContentResolver.query(items, null, null, null, "order_id");
        int count = c.getCount();
        c.moveToFirst();
        if (count > 0) {
            StringBuilder strBuilder = new StringBuilder();
            strBuilder.append("{");
            for (int index = 0; index < count; index++) {
                strBuilder.append("{");
                strBuilder.append("\"");
                strBuilder.append("order_id");
                strBuilder.append("\": ");
                strBuilder.append(c.getString(c.getColumnIndex(RunnerProvider.ORDER_ID)));
                strBuilder.append(",");
                strBuilder.append("\"");
                strBuilder.append("order_id");
                strBuilder.append("\": ");
                strBuilder.append(c.getString(c.getColumnIndex(RunnerProvider.ORDER_STATUS)));
                strBuilder.append("}");
                c.moveToNext();
            }
            strBuilder.append("}");
            Log.d("Fudfill", "JSON To be synced: " + strBuilder.toString());

            if (strBuilder.length() > 5) {

                ServiceHandler sh = new ServiceHandler();

                // Making a request to url and getting response
                String jsonStr = sh.makeServiceCallWithS(url, ServiceHandler.PUT, strBuilder.toString());

                if (jsonStr.contains("success")) {
                    mContentResolver.delete(items, null, null);
                    Log.d("Fudfill", "Deleting the item records");
                }
            }

        }


    }
}
