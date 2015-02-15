package com.fudfill.runner.slidingmenu.common;

import android.util.Log;

/**
 * Created by praveenthota on 2/1/15.
 */
public class FudfillConfig {
    public final static boolean is_test_mode = true;
    public final static String TEST_SERVER_ADDR = "192.168.1.64:8080";
    public final static String LIVE_SERVER_ADDR = "104.155.226.254:80";
    public final static int timeoutConnection = 5000;
    public final static int timeoutSocket = 5000;
    public static String runnerId = null;

    public static void setRunnerId(String id) {
        runnerId = id;
    }

    public static String getRunnerId() {
        return runnerId;
    }

    public static String getServerAddr() {
        Log.d("FudfillConfig", ": Testmode:  " + is_test_mode);
        if (is_test_mode)
            return TEST_SERVER_ADDR;
        else
            return LIVE_SERVER_ADDR;
    }

    public static String getLoginUrl() {
        Log.d("FudfillConfig", ": Testmode:  " + is_test_mode);
        if (is_test_mode)
            return "/Fudfill/RESTAPI/login";
        else
            return "/Fudfill/RESTAPI/login";
    }

    public static String getRunnersLocationUrl() {
        Log.d("FudfillConfig", ": Testmode:  " + is_test_mode);
        if (is_test_mode)
            return TEST_SERVER_ADDR;
        else
            return "/Fudfill/RESTAPI/routeplan/search/";
    }

    public static String getRunnersItemslistUrl() {
        Log.d("FudfillConfig", ": Testmode:  " + is_test_mode);
        if (is_test_mode)
            return TEST_SERVER_ADDR;
        else
            return "/Fudfill/RESTAPI/routeplan/itemlist/" + runnerId;
    }

    public static String getRunnerPlannedMapUrl() {
        Log.d("FudfillConfig", ": Testmode:  " + is_test_mode);
        if (is_test_mode)
            return TEST_SERVER_ADDR;
        else
            return "/Fudfill/RESTAPI/routeplan/" + runnerId;
    }

    public static String getRunnerupdateLocUrl() {
        Log.d("FudfillConfig", ": Testmode:  " + is_test_mode);
        if (is_test_mode)
            return "/Fudfill/RESTAPI/runnerlocation/" + runnerId;
        else
            return "/Fudfill/RESTAPI/runnerlocation/" + runnerId;
    }
}
