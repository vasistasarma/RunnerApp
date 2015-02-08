package com.fudfill.runner.slidingmenu.common;

/**
 * Created by praveenthota on 1/7/15.
 */
public class Runner {
    private String runnerId;
    private String name;
    private String emailId;
    private String mobile;
    private String latitude;
    private String longitude;
    private String lastUpdatedtime;

    public String getRunnerId() {
        return runnerId;
    }

    public void setRunnerId(String runnerId) {
        this.runnerId = runnerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastUpdatedtime() {
        return lastUpdatedtime;
    }

    public void setLastUpdatedtime(String time) {
        this.lastUpdatedtime = time;
    }
}
