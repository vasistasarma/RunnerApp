package com.fudfill.runner.slidingmenu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.fudfill.runner.slidingmenu.adapter.CustomerOrderDetails;
import com.fudfill.runner.slidingmenu.adapter.CustomerOrderListAdapter;
import com.fudfill.runner.slidingmenu.adapter.CustomerWaypointDetails;

import java.util.ArrayList;
import java.util.List;

public class ItemsListFragment extends Fragment {
    private List<CustomerWaypointDetails> wayPoints;

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
        CustomerOrderListAdapter custOrderAdapter= new CustomerOrderListAdapter(wayPoints, this.getActivity().getApplicationContext());
        itemList.setIndicatorBounds(0,20);
        itemList.setAdapter(custOrderAdapter);
		return rootView;
	}
    //Adding random data for Testing
    private void initData(){

        wayPoints = new ArrayList<CustomerWaypointDetails>();
        CustomerWaypointDetails wayPoint1 = createWaypoint("1", "Vasista", "300");
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
        wayPoints.add(wayPoint4);

   }
    private CustomerWaypointDetails createWaypoint(String order, String name, String price) {
        return new CustomerWaypointDetails(order, name, price);
    }
    private List<CustomerOrderDetails> createItems(String itemName, int itemQty, int itemPrice) {
        List<CustomerOrderDetails> result = new ArrayList<CustomerOrderDetails>();

        for (int i=0; i < itemQty; i++) {
            CustomerOrderDetails item = new CustomerOrderDetails(itemName, itemPrice, itemQty*itemPrice);
            result.add(item);
        }

        return result;
    }

}
