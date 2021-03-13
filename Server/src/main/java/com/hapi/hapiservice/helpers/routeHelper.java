package com.hapi.hapiservice.helpers;

public class routeHelper {
    public final static String apiPrefix = "api/v1/";

    public final static String studentAuth = apiPrefix + "auth";

    public final static String schedulePath = apiPrefix + "schedule/";

    public final static String pointPath = apiPrefix + "point/";

    public final static String getSemester = schedulePath + "semester";

    public final static String getWeek = schedulePath + "week";

    public final static String getSchedule = schedulePath + "detail";

    public final static String getCurrentPoint = pointPath + "current";

    public final static String getPSemeterList = pointPath + "semester";

    public final static String getPSemeterDetail = pointPath + "view";
}
