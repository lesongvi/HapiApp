package com.hapi.hapiservice.helpers.common;

public class routeHelper {
    public final static String fbGraphApi = "https://graph.facebook.com/v3.2";

    public final static String apiPrefix = "api/v1/";

    public final static String studentAuth = apiPrefix + "auth";

    public final static String schedulePath = apiPrefix + "schedule/";

    public final static String pointPath = apiPrefix + "point/";

    public final static String botPath = apiPrefix + "bot/";

    public final static String studentPath = apiPrefix + "student/";

    public final static String appPath = apiPrefix + "app/";

    public final static String reautheticate = studentPath + "info";

    public final static String getSemester = schedulePath + "semester";

    public final static String getWeek = schedulePath + "week";

    public final static String getSchedule = schedulePath + "detail";

    public final static String getExamSche = schedulePath + "cexam";

    public final static String getCurrentPoint = pointPath + "current";

    public final static String getPSemeterList = pointPath + "semester";

    public final static String getPSemeterDetail = pointPath + "view";

    public final static String botWebHook = botPath + "webhook";

    public final static String botSubscribe = botPath + "subscribe";

    public final static String saveFbCredentials = botPath + "save";

    public final static String getFbGraphApi = fbGraphApi;

    public final static String getUserApi = fbGraphApi + "/{userId}?access_token={token}";

    public final static String getSubscribeUrl = fbGraphApi + "/me/subscribed_apps";

    public final static String getFbSendUrl = fbGraphApi + "/me/messages?access_token={PAGE_ACCESS_TOKEN}";

    public final static String getFbMessengerProfileUrl = fbGraphApi + "/me/messenger_profile?access_token={PAGE_ACCESS_TOKEN}";

    public final static String snapshotApiPath = apiPrefix + "snapshot/";

    public final static String snapshotNotification = snapshotApiPath + "notification";

    public final static String snapshotAllNotification = snapshotApiPath + "notifications";

    public final static String studentEvaluate = studentPath + "evaluate/";

    public final static String evaluateList = studentEvaluate + "list";

    public final static String evaluateView = studentEvaluate + "view";

    public final static String taRest = studentPath + "tarest/";

    public final static String takeARest = taRest + "request";

    public final static String restList = taRest + "list";

    public final static String usData = studentPath + "data/";

    public final static String usDetailData = usData + "detail";

    public final static String routeNationData = usData + "routenation";

    public final static String avatar = appPath + "avatar/";

    public final static String avatarModify = avatar + "modify";

    public final static String surveyPath = studentPath + "survey/";

    public final static String surveyList = surveyPath + "list";

    public final static String surveyDetail = surveyPath + "detail";

    public final static String surveyRequest = surveyPath + "request";
}
