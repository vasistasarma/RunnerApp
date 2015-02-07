package com.fudfill.runner.slidingmenu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fudfill.runner.slidingmenu.common.FudfillConfig;
import com.fudfill.runner.slidingmenu.common.RunnerRoute;
import com.fudfill.runner.slidingmenu.syncadapter.ServiceHandler;
import com.google.android.gms.maps.MapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RouteMapFragment extends Fragment {
    ProgressDialog pDialog;
    private View rootView;
    List<RunnerRoute> mAssignedRoutes;
    private static String url = "http://"+ FudfillConfig.getServerAddr()+"/fudfill/RESTAPI/routeplan/1";
    private static String TAG="RouteMapFragment";
    private static final String TAG_ROUTES = "runner";
    private static final String TAG_RUNNERNAME = "name";
    private static final String TAG_MAPURL = "routeshorturl";
    private static final String TAG_RUNNERID = "runnerId";
    private static final String TAG_EMAIL = "emailId";
    private static final String TAG_PHONE_MOBILE = "mobile";


    public RouteMapFragment() {
        mAssignedRoutes = new ArrayList<RunnerRoute>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_routemap, container,
                    false);
            new GetRouteMap().execute(null, null, null);
        }
      //  Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/dir/37.3772964,-121.9231012/37.3263615,-121.9671459/37.335775,-122.0245137/37.3746695,-122.0229961/@37.3461087,-122.031922,23308m/data=!3m2!1e3!4b1!4m2!4m1!3e0"));
        //intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        //startActivity(intent);

		return rootView;
	}
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.runner_map_fragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
         rootView = null;

    }
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetRouteMap extends AsyncTask<Void, Void, Void> {

        private JSONArray runnerMap;
        List<RunnerRoute> mRouteList;

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

            Log.d(TAG, "Response: > " + jsonStr);



            if (jsonStr != null) {
                try {


                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    runnerMap = jsonObj.getJSONArray(TAG_ROUTES);

                    // looping through All orders
                    for (int i = 0; i < runnerMap.length(); i++) {
                        RunnerRoute tRunnerRoute = new RunnerRoute();
                        JSONObject runner = runnerMap.getJSONObject(i);

                        String mobile = runner.getString(TAG_PHONE_MOBILE);
                        String name = runner.getString(TAG_RUNNERNAME);
                        String email = runner.getString(TAG_EMAIL);
                        // Location node is JSON Object
                        String url = runner.getString(TAG_MAPURL);
                        List mapUrl = new ArrayList();
                        mapUrl.add(url);

                        tRunnerRoute.setRunnerName(name);
                        tRunnerRoute.setMobile(mobile);
                        tRunnerRoute.setAssignedRoutes(mapUrl);
                        mAssignedRoutes.add(tRunnerRoute);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e(TAG, "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (mAssignedRoutes != null && !mAssignedRoutes.isEmpty()) {
                for(int i=0;i<mAssignedRoutes.size();i++)
                {
                    RunnerRoute tRunner = mAssignedRoutes.get(i);
                    String url = tRunner.getAssignedRoutes().get(i).toString();
                    if(tRunner !=null)
                    {
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                        startActivity(intent);

                    }
                }

            } else {
                // Load the map with the runners location
            Log.d("Nothing", "Nothing");
            }
        }

    }
}
