package com.fudfill.runner.slidingmenu.adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.fudfill.runner.slidingmenu.R;

/**
 * Created by Sowmya on 1/4/2015.
 */
public class CustomerOrderListAdapter extends BaseExpandableListAdapter{
    private Context context;
    private List<CustomerWaypointDetails> wayPointsList;
    private int itemLayoutId;
    private int groupLayoutId;

    public CustomerOrderListAdapter(List<CustomerWaypointDetails>wayPointsList, Context context){
        this.itemLayoutId=R.layout.customer_order_list;
        this.groupLayoutId=R.layout.customer_waypoints_list;
        this.wayPointsList=wayPointsList;
        this.context=context;
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

        TextView txtOrderOfDelivery = (TextView)v.findViewById(R.id.customer_waypoint_order);
        TextView txtWayPointName = (TextView)v.findViewById(R.id.customer_waypoint_name);
        TextView txtPriceToCollect = (TextView) v.findViewById(R.id.customer_waypoint_price);
        final ImageButton imgbtnItemDeliveryStatus = (ImageButton)v.findViewById(R.id.customer_waypoint_delivery_status);
        ImageButton imgbtnItemNotDelivered = (ImageButton)v.findViewById(R.id.customer_waypoint_not_delivered);

        imgbtnItemDeliveryStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgbtnItemDeliveryStatus.setImageResource(R.drawable.item_delivered);
                Toast.makeText(context, "Item Marked Delivered", Toast.LENGTH_SHORT).show();
                //update the delivery status flag to server here..
                // set or unset the flag..for now there is only setting of flag in here
            }
        });

        CustomerWaypointDetails waypointDetails = wayPointsList.get(groupPosition);

        txtOrderOfDelivery.setText(waypointDetails.getWaypointOrder());
        txtWayPointName.setText(waypointDetails.getWayPointName());
        txtPriceToCollect.setText(waypointDetails.getWayPointPrice());
        imgbtnItemDeliveryStatus.setFocusable(false);
        imgbtnItemNotDelivered.setFocusable(false);

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
}
