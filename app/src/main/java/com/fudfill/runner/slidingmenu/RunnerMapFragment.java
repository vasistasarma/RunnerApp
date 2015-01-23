package com.fudfill.runner.slidingmenu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fudfill.runner.slidingmenu.common.Runner;
import com.fudfill.runner.slidingmenu.syncadapter.ServiceHandler;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RunnerMapFragment extends Fragment {
    private static View view;
    ProgressDialog pDialog;
    List<Runner> mRunnersList;
    private ArrayList<MarkerOptions> buddyLocOptions = new ArrayList<MarkerOptions>();
    private Bitmap markerImage;
    private float oldZoom=0;
    private GoogleMap gMap;
    private static final int INTERVAL = 25;
    private static final int MAP_ZOOM_LEVEL = 4;
    private static String TAG="RunnerMapFragment";

    private static String url = "http://192.168.1.64:8080/fudfildelivery/testserver?file=runners";

    // JSON Node names
    private static final String TAG_RUNNERS = "runners";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "emailId";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_PHONE_MOBILE = "mobile";

	public RunnerMapFragment() {

        mRunnersList = new ArrayList<Runner>();
	}
    private boolean initializeMap(){
        if(gMap == null){
            MapFragment mRunnerMapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.runner_map_fragment);
            gMap =mRunnerMapFragment.getMap();
        }
        return gMap != null;
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        /*if(view != null){
           // new GetRunnersLocation().execute(null, null, null);
            return view;
        }else*/ try{

        view= inflater.inflate(R.layout.fragment_runnermap,
				container, false);
        markerImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_runner);
        if(initializeMap()){
            new GetRunnersLocation().execute(null, null, null);

        }
        }catch (InflateException e){}

		return view;
	}

   /* public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.runner_map_fragment);
        if (f != null)
            getFragmentManager().beginTransaction().remove(f).commit();
        view = null;

    }*/
    /**
     * Async task class to get json by making HTTP call
     * */
    private class GetRunnersLocation extends AsyncTask<Void, Void, Void> {

        private JSONArray runners;

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

            Log.d(TAG,"Response: > " + jsonStr);



            if (jsonStr != null) {
                try {


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
                        tRunner.setName(name);
                        mRunnersList.add(tRunner);

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

            if (mRunnersList != null && !mRunnersList.isEmpty()) {
                 for(int i=0;i<mRunnersList.size();i++)
                 {
                     Runner tRunner = mRunnersList.get(i);
                     if(tRunner !=null)
                     {
                         Log.d(TAG,"Runner: "+i+" : "+tRunner);
                         double latitude = Double.parseDouble(tRunner.getLatitude());
                         double longitude =Double.parseDouble(tRunner.getLongitude());
                         CameraUpdate runnerPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 7);
                         gMap.moveCamera(runnerPosition);
                         gMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).
                                 title(tRunner.getName()+", "+tRunner.getMobile()).anchor(.5f, .5f).
                                 icon(BitmapDescriptorFactory.fromBitmap(markerImage)));
                     }
                 }

            } else {
                // Load the map with the runners location
                Log.d(TAG,"Runners List is empty ");
                CameraUpdate runnerPosition = CameraUpdateFactory.newLatLngZoom(new LatLng(28.6000264, 77.0532981), 13);
                gMap.moveCamera(runnerPosition);
                gMap.addMarker(new MarkerOptions().position(new LatLng(28.6000264, 77.0532981)).title("Runner 1").anchor(.5f, .5f).icon(BitmapDescriptorFactory.fromBitmap(markerImage)));
            }
        }

    }
}
