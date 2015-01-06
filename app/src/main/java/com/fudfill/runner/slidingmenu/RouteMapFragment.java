package com.fudfill.runner.slidingmenu;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RouteMapFragment extends Fragment {

	public RouteMapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_routemap, container,
				false);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("https://goo.gl/maps/T9e9w"));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        startActivity(intent);

		return rootView;
	}
}
