package com.fudfill.runner.slidingmenu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fudfill.runner.slidingmenu.adapter.CustomerOrderDetails;
import com.fudfill.runner.slidingmenu.common.Runner;
import com.fudfill.runner.slidingmenu.syncadapter.ServiceHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RunnerMapFragment extends Fragment {

    ProgressDialog pDialog;
    List<Runner> mRunnersList;

    private static String url = "http://api.fudfill.com/runners/";

    // JSON Node names
    private static final String TAG_RUNNERS = "runners";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_PHONE_MOBILE = "mobile";

	public RunnerMapFragment() {

        mRunnersList = new ArrayList<Runner>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_runnermap,
				container, false);

		return rootView;
	}


    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetRunnersLocation extends AsyncTask<Void, Void, Void> {

        private JSONArray runners;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(RunnerMapFragment.this.getActivity().getApplicationContext());
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
                    runners = jsonObj.getJSONArray(TAG_RUNNERS);

                    // looping through All orders
                    for (int i = 0; i < runners.length(); i++) {
                        Runner tRunner = new Runner();
                        JSONObject runner = runners.getJSONObject(i);

                        String mobile = runner.getString(TAG_PHONE_MOBILE);
                        String name = runner.getString(TAG_NAME);
                        String email = runner.getString(TAG_EMAIL);
                        // Location node is JSON Object
                        JSONObject location = runner.getJSONObject(TAG_LOCATION);
                        String latitude = location.getString(TAG_LATITUDE);
                        String longitude = location.getString(TAG_LONGITUDE);

                        tRunner.setEmailId(email);
                        tRunner.setRunnerId(email);
                        tRunner.setLatitude(latitude);
                        tRunner.setLongitude(longitude);
                        tRunner.setMobile(mobile);
                        mRunnersList.add(tRunner);

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
            // Load the map with the runners location
        }

    }
}
