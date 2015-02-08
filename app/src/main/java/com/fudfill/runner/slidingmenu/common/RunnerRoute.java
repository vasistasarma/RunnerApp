package com.fudfill.runner.slidingmenu.common;

import java.util.List;

/**
 * Created by vasista on 1/20/2015.
 */
public class RunnerRoute {
    private String runnerId;
    private String runnerName;
    private List<String> assignedRoutes;
    private String mobile;

    public String getRunnerId() {
        return this.runnerId;
    }

    public void setRunnerId(String runnerId) {
        this.runnerId = runnerId;
    }

    public String getRunnerName() {
        return this.runnerName;
    }

    public void setRunnerName(String runnerName) {
        this.runnerName = runnerName;
    }

    public List<String> getAssignedRoutes() {
        return assignedRoutes;
    }

    public void setAssignedRoutes(List<String> routes) {
        this.assignedRoutes = routes;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
