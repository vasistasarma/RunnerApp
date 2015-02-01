package com.fudfill.runner.slidingmenu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.fudfill.runner.slidingmenu.adapter.CustomerOrderDetails;
import com.fudfill.runner.slidingmenu.adapter.CustomerOrderListAdapter;
import com.fudfill.runner.slidingmenu.adapter.CustomerWaypointDetails;
import com.fudfill.runner.slidingmenu.syncadapter.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemsListFragment extends Fragment {
    private List<CustomerWaypointDetails> wayPoints;

    // URL to get contacts JSON
    private static String url = "http://192.168.1.64:8080/fudfildelivery/testserver?file=orders";
   // private static String url = "http://128.199.242.169/fudfildelivery/testserver?file=orders";
    CustomerOrderListAdapter custOrderAdapter;



    ProgressDialog pDialog;

    // JSON Node names
    private static final String TAG_ORDER = "order";
    private static final String TAG_ORDER_ID = "order_id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";
    private static final String TAG_ORDER_STATUS = "order_status";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_ITEMS_ID = "item_id";
    private static final String TAG_ITEM_NAME = "item_name";
    private static final String TAG_ITEM_COST = "item_cost";
    private static final String TAG_ITEM_COUNT = "item_count";





	public ItemsListFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_items,
				container, false);
        initData();
        ExpandableListView itemList = (ExpandableListView)rootView.findViewById(R.id.customerItemList);
        itemList.setIndicatorBounds(5, 5);
        custOrderAdapter= new CustomerOrderListAdapter(wayPoints, this.getActivity().getApplicationContext());
        itemList.setIndicatorBounds(0,20);
        itemList.setAdapter(custOrderAdapter);
		return rootView;
	}
    //Adding random data for Testing
    private void initData(){

        wayPoints = new ArrayList<CustomerWaypointDetails>();
        /*CustomerWaypointDetails wayPoint1 = createWaypoint("1", "Vasista", "300");
        wayPoint1.setItemList(createItems("Item 1", 2, 100));

        CustomerWaypointDetails wayPoint2 = createWaypoint("2", "Vivek", "1200");
        wayPoint2.setItemList(createItems("Item 2", 4, 100));


        CustomerWaypointDetails wayPoint3 = createWaypoint("3", "Manish", "1800");
        wayPoint3.setItemList(createItems("Item 3", 3, 100));


        CustomerWaypointDetails wayPoint4 = createWaypoint("4", "Nikhil", "3000");
        wayPoint4.setItemList(createItems("Item 4", 2, 100));

        wayPoints.add(wayPoint1);
        wayPoints.add(wayPoint2);
        wayPoints.add(wayPoint3);
        wayPoints.add(wayPoint4);*/
        new GetOrdersList().execute(null, null, null);

   }
    private CustomerWaypointDetails createWaypoint(String order, String name, String price,String address, String phone) {
        return new CustomerWaypointDetails(order, name, price, address,phone );
    }
    private List<CustomerOrderDetails> createItems(String itemName, int itemQty, int itemPrice) {
        List<CustomerOrderDetails> result = new ArrayList<CustomerOrderDetails>();

        for (int i=0; i < itemQty; i++) {
            CustomerOrderDetails item = new CustomerOrderDetails(itemName, itemPrice, itemQty*itemPrice);
            result.add(item);
        }

        return result;
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetOrdersList extends AsyncTask<Void, Void, Void> {

        private JSONArray order;

        private JSONArray items;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);

            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {

                    List<CustomerOrderDetails> tCustomerOrderDetails = new ArrayList<CustomerOrderDetails>();
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    order = jsonObj.getJSONArray(TAG_ORDER);

                    // looping through All orders
                    for (int i = 0; i < order.length(); i++) {
                        JSONObject c = order.getJSONObject(i);
                        int totalOrderCost =0;

                        String order_id = c.getString(TAG_ORDER_ID);
                        String name = c.getString(TAG_NAME);
                        String email = c.getString(TAG_EMAIL);
                        String address = c.getString(TAG_ADDRESS);
                        String order_status = c.getString(TAG_ORDER_STATUS);

                        // Location node is JSON Object
                        JSONObject location = c.getJSONObject(TAG_LOCATION);
                        String latitude = location.getString(TAG_LATITUDE);
                        String longitude = location.getString(TAG_LONGITUDE);
                        // Phone node is JSON Object
                       JSONObject phone = c.getJSONObject(TAG_PHONE);
                       String mobile = phone.getString(TAG_PHONE_MOBILE);
                        //String home = phone.getString(TAG_PHONE_HOME);
                        //String office = phone.getString(TAG_PHONE_OFFICE);

                        // tmp hashmap for single contact
                        HashMap<String, String> OrderMap = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        OrderMap.put(TAG_ORDER_ID, order_id);
                        OrderMap.put(TAG_NAME, name);
                        OrderMap.put(TAG_EMAIL, email);
                        OrderMap.put(TAG_PHONE_MOBILE, mobile);


                        // Getting JSON Array node
                        items = c.getJSONArray(TAG_ITEMS);


                        List <CustomerOrderDetails> tCustomerOrderList = new ArrayList<CustomerOrderDetails>();

                        for(int j =0; j<items.length();j++)
                        {
                            JSONObject item = items.getJSONObject(j);

                            String item_id = item.getString(TAG_ITEMS_ID);
                            String item_name = item.getString(TAG_ITEM_NAME);
                            String item_cost = item.getString(TAG_ITEM_COST);
                            String item_count = item.getString(TAG_ITEM_COUNT);
                            totalOrderCost += Integer.parseInt(item_cost)* Integer.parseInt(item_count);
                            CustomerOrderDetails tOrderItem = new CustomerOrderDetails(item_name,
                                    Integer.parseInt(item_count),Integer.parseInt(item_cost));
                            tCustomerOrderList.add(tOrderItem);

                        }
                        CustomerWaypointDetails tCustomerOrder = new CustomerWaypointDetails(order_id,name,
                                ""+totalOrderCost,address, mobile);
                        // Set the customer list
                        tCustomerOrder.setItemList(tCustomerOrderList);


                        wayPoints.add(tCustomerOrder);
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            // Notify data set had changed
            custOrderAdapter.notifyDataSetChanged();

        }

    }

}
