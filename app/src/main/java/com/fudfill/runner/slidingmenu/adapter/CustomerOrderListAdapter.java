package com.fudfill.runner.slidingmenu.adapter;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fudfill.runner.slidingmenu.R;
import com.fudfill.runner.slidingmenu.common.RunnerProvider;
import com.fudfill.runner.slidingmenu.syncadapter.ServiceHandler;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Sowmya on 1/4/2015.
 */
public class CustomerOrderListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<CustomerWaypointDetails> wayPointsList;
    private int itemLayoutId;
    private int groupLayoutId;
    ProgressDialog pDialog;
    ContentResolver mContentResolver;
    boolean syncResult=false;

    public CustomerOrderListAdapter(List<CustomerWaypointDetails>wayPointsList, Context context){
        this.itemLayoutId=R.layout.customer_order_list;
        this.groupLayoutId=R.layout.customer_waypoints_list;
        this.wayPointsList=wayPointsList;
        this.context=context;
        mContentResolver = context.getContentResolver();
    }
 @Override
    public int getGroupCount() {
        return wayPointsList.size();
    }
public CustomerOrderListAdapter(Context customerItemList){
    this.context = customerItemList;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size = wayPointsList.get(groupPosition).getItemList().size();
        System.out.println("Children for Group ["+groupPosition+"] is ["+size+"]");
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return wayPointsList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return wayPointsList.get(groupPosition).getItemList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return wayPointsList.get(groupPosition).hashCode();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return wayPointsList.get(groupPosition).getItemList().get(childPosition).hashCode();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        if (null == v) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.customer_waypoints_list, parent, false);
        }

        final TextView txtOrderOfDelivery = (TextView)v.findViewById(R.id.customer_waypoint_order);
        TextView txtWayPointName = (TextView)v.findViewById(R.id.customer_waypoint_name);
        TextView txtPriceToCollect = (TextView) v.findViewById(R.id.customer_waypoint_price);
        TextView txtCustomerAddress = (TextView)v.findViewById(R.id.customer_waypoint_address);
        TextView txtCustomerPhone = (TextView)v.findViewById(R.id.customer_waypoint_phone);
        txtCustomerAddress.setMovementMethod(new ScrollingMovementMethod());
        txtCustomerAddress.setFocusable(false);
        final ImageButton imgbtnItemDeliveryStatus = (ImageButton)v.findViewById(R.id.customer_waypoint_delivery_status);
        ImageButton imgbtnItemNotDelivered = (ImageButton)v.findViewById(R.id.customer_waypoint_not_delivered);

        imgbtnItemDeliveryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtnItemDeliveryStatus.setImageResource(R.drawable.item_delivered);
                ContentValues values = new ContentValues();

                values.put(RunnerProvider.ORDER_ID,
                        txtOrderOfDelivery.getText().toString());

                values.put(RunnerProvider.ORDER_STATUS,"delivered");

                Uri uri = context.getContentResolver().insert(
                        RunnerProvider.CONTENT_URI, values);
                Toast.makeText(context, "Item Marked Delivered", Toast.LENGTH_SHORT).show();

                new SyncOrderList().execute(null, null, null);
                //update the delivery status flag to server here..
                // set or unset the flag..for now there is only setting of flag in here
            }
        });

        CustomerWaypointDetails waypointDetails = wayPointsList.get(groupPosition);

        txtOrderOfDelivery.setText(waypointDetails.getWaypointOrder());
        txtWayPointName.setText(waypointDetails.getWayPointName());
        txtPriceToCollect.setText(waypointDetails.getWayPointPrice());
        txtCustomerAddress.setText(waypointDetails.getWayPointAddress());
        txtCustomerPhone.setText(waypointDetails.getWayPointPhone());

        imgbtnItemDeliveryStatus.setFocusable(false);
        imgbtnItemNotDelivered.setFocusable(false);
        txtCustomerPhone.setFocusable(false);

       return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v==null){
            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.customer_order_list, parent, false);
        }
        CheckBox itemSelected = (CheckBox)v.findViewById(R.id.item_selected);
        TextView itemName = (TextView)v.findViewById(R.id.item_name);
        EditText itemQty = (EditText)v.findViewById(R.id.item_qty);
        EditText itemPrice = (EditText)v.findViewById(R.id.item_price);


        CustomerOrderDetails orderDetails = wayPointsList.get(groupPosition).getItemList().get(childPosition);
        itemSelected.setEnabled(true);
        itemName.setText(orderDetails.getItemName());
        itemQty.setText(String.valueOf(orderDetails.getQuantity()));
        itemPrice.setText(String.valueOf(orderDetails.getPrice()));

        return v;

        }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class SyncOrderList extends AsyncTask<Void, Void, Void> {

        private JSONArray order;

        private JSONArray items;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            syncResult = false;
            syncResult = syncOrdersToServer();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
             if(syncResult)
            {
                Toast.makeText(context,"Sync Success", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(context,"Sync failed", Toast.LENGTH_SHORT).show();
            }


        }

        public boolean syncOrdersToServer() {
            // Retrieve student records
            String URL = "content://com.fudfill.runner.provider.items/orders";
            String base_url = "http://192.168.1.64:8080/fudfildelivery/updateorder";
            Uri items = Uri.parse(URL);
            Cursor c = mContentResolver.query(items, null, null, null, "order_id");
            int count = c.getCount();
            Log.d("Fudfill","Items to be synced: "+count);
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

                    if(jsonStr.contains("success"))
                    {
                        mContentResolver.delete(items,null,null);
                        Log.d("Fudfill","Deleting the item records");
                        return true;
                    }
                }

            }

            return false;
        }

    }
}
