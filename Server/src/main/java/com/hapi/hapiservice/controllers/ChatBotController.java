package com.hapi.hapiservice.controllers;

import com.hapi.hapiservice.helpers.common.browserHelper;
import com.hapi.hapiservice.helpers.respository.ChatbotStudentRepository;
import com.hapi.hapiservice.helpers.respository.NotificationRespository;
import com.hapi.hapiservice.helpers.respository.StudentRepository;
import com.hapi.hapiservice.helpers.core.BaseBot;
import com.hapi.hapiservice.helpers.core.Controller;
import com.hapi.hapiservice.helpers.core.EventType;
import com.hapi.hapiservice.helpers.core.ScheduleBot;
import com.hapi.hapiservice.helpers.core.common.HapiBot;
import com.hapi.hapiservice.helpers.common.routeHelper;
import com.hapi.hapiservice.models.bot.ChatbotStudents;
import com.hapi.hapiservice.models.schedule.Students;
import com.hapi.hapiservice.models.bot.*;
import com.hapi.hapiservice.services.ChatbotStudentService;
import com.hapi.hapiservice.services.NotificationService;
import com.hapi.hapiservice.services.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;

@RestController
@HapiBot
public class ChatBotController extends BaseBot {
    private String fbSendUrl;
    private String fbMessengerProfileUrl;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    @Autowired
    private ChatbotStudentRepository chatbotstudentRepository;

    @Autowired
    private ChatbotStudentService chatbotstudentService;

    @Autowired
    private NotificationRespository notificationRespository;

    @Autowired
    private NotificationService notificationService;

    @Value("${fbBotToken}")
    private String fbToken;

    @Value("${fbPageAccessToken}")
    private String pageAccessToken;

    @Autowired
    protected RestTemplate restTemplate;

    protected routeHelper fbApiEndpoints;

    Logger logger = LoggerFactory.getLogger(ChatBotController.class);

    @PostConstruct
    private void constructFbSendUrl() {
        fbSendUrl = fbApiEndpoints.getFbSendUrl.replace("{PAGE_ACCESS_TOKEN}", getPageAccessToken());
        fbMessengerProfileUrl = fbApiEndpoints.getFbMessengerProfileUrl.replace("{PAGE_ACCESS_TOKEN}",
                getPageAccessToken());
        //Nếu chưa verify webhook thì comment 3 dòng dưới lại
        try {
            setGetStartedButton("Bắt đầu");
            setGreetingText(new Payload[]{new Payload().setLocale("default").setText("Hapi HUTECH Bot là một chatbot tự động" +
                    " giúp sinh viên HUTECH có thể xem chi tiết thời khóa biểu, điểm các học kỳ và thông báo mới nhất từ trường HUTECH ngay trên ứng dụng Messenger " +
                    "hãy nhấp vào nút \"Bắt đầu\" hoặc gõ \"Chào\".")});
            setPersistentMenu(new Payload[]{new Payload().setLocale("default").setCallToActions(this.ctaInit())});
        } catch (Exception e) {
            //Silent is Golden
            logger.error(e.getMessage());
            //e.getStackTrace();
        }
    }

    private CallToActions[] ctaInit() {
        CallToActions cllta1 = new CallToActions();
        cllta1.setTitle("Cách sử dụng");
        cllta1.setType("postback");
        cllta1.setPayload("HOW_TO_USE");

        return new CallToActions[]{
            cllta1
        };
    }

    public String getFbToken() {
        return this.fbToken;
    }

    public String getPageAccessToken() {
        return this.pageAccessToken;
    }

    @RequestMapping(value = {routeHelper.botWebHook}, method = RequestMethod.GET)
    public final ResponseEntity setupWebhookVerification(@RequestParam("hub.mode") String mode,
                                                         @RequestParam("hub.verify_token") String verifyToken,
                                                         @RequestParam("hub.challenge") String challenge) {
        if (EventType.SUBSCRIBE.name().equalsIgnoreCase(mode) && getFbToken().equals(verifyToken)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @ResponseBody
    @RequestMapping(value = {routeHelper.botWebHook}, method = RequestMethod.POST)
    public final ResponseEntity setupWebhookEndpoint(@RequestBody Callback callback) {
        try {
            if (!callback.getObject().equals("page")) {
                return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
            }
            for (Entry entry : callback.getEntry()) {
                if (entry.getMessaging() != null) {
                    for (Event event : entry.getMessaging()) {
                        if (event.getMessage() != null) {
                            if (event.getMessage().isEcho() != null &&
                                    event.getMessage().isEcho()) {
                                event.setType(EventType.MESSAGE_ECHO);
                            } else if (event.getMessage().getQuickReply() != null) {
                                event.setType(EventType.QUICK_REPLY);
                                sendTypingOnIndicator((User)event.getSender());
                            } else {
                                event.setType(EventType.MESSAGE);
                                sendTypingOnIndicator((User)event.getSender());
                            }
                        } else if (event.getDelivery() != null) {
                            event.setType(EventType.MESSAGE_DELIVERED);
                        } else if (event.getRead() != null) {
                            event.setType(EventType.MESSAGE_READ);
                        } else if (event.getPostback() != null) {
                            event.setType(EventType.POSTBACK);
                        } else if (event.getOptin() != null) {
                            event.setType(EventType.OPT_IN);
                        } else if (event.getReferral() != null) {
                            event.setType(EventType.REFERRAL);
                        } else if (event.getAccountLinking() != null) {
                            event.setType(EventType.ACCOUNT_LINKING);
                        } else {
                            return ResponseEntity.ok("Callback not supported yet!");
                        }
                        if (isConversationOn(event)) {
                            invokeChainedMethod(event);
                        } else {
                            invokeMethods(event);
                        }
                    }
                }
            }
        } catch (Exception e) {
            //Silent is Golden
            logger.error(e.getMessage());
            //e.getStackTrace();
        }
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    private void sendTypingOnIndicator(User recipient) {
        this.sendSeen(recipient);
        restTemplate.postForEntity(fbSendUrl,
                new Event().setRecipient(recipient).setSenderAction("typing_on"), Response.class);
    }

    private void sendTypingOffIndicator(User recipient) {
        restTemplate.postForEntity(fbSendUrl,
                new Event().setRecipient(recipient).setSenderAction("typing_off"), Response.class);
    }

    private void sendSeen(User recipient) {
        restTemplate.postForEntity(fbSendUrl,
                new Event().setRecipient(recipient).setSenderAction("mark_seen"), Response.class);
    }

    protected final ResponseEntity<String> reply(Event event) {
        //sendTypingOffIndicator(event.getRecipient());
        try {
            return restTemplate.postForEntity(fbSendUrl, event, String.class);
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage());
            //e.getStackTrace();
            return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    protected ResponseEntity<String> reply(Event event, String text) {
        Event response = new Event()
                .setMessagingType("RESPONSE")
                .setRecipient(event.getSender())
                .setMessage(new Message().setText(text));
        return reply(response);
    }

    protected ResponseEntity<String> reply(Event event, Message message) {
        Event response = new Event()
                .setMessagingType("RESPONSE")
                .setRecipient(event.getSender())
                .setMessage(message);
        return reply(response);
    }

    /**
     * https://developers.facebook.com/docs/messenger-platform/discovery/welcome-screen
     */
    protected final ResponseEntity<Response> setGetStartedButton(String payload) {
        Event event = new Event().setGetStarted(new Postback().setPayload(payload));
        return restTemplate.postForEntity(fbMessengerProfileUrl, event, Response.class);
    }

    /**
     * https://developers.facebook.com/docs/messenger-platform/discovery/welcome-screen
     */
    protected final ResponseEntity<Response> setGreetingText(Payload[] greeting) {
        Event event = new Event().setGreeting(greeting);
        return restTemplate.postForEntity(fbMessengerProfileUrl, event, Response.class);
    }

    /**
     * https://developers.facebook.com/docs/messenger-platform/discovery/welcome-screen
     */
    protected final ResponseEntity<Response> setPersistentMenu(Payload[] payload) {
        Event event = new Event().setPersistentMenu(payload);
        return restTemplate.postForEntity(fbMessengerProfileUrl, event, Response.class);
    }

    /**
     * https://developers.facebook.com/docs/messenger-platform/getting-started/app-setup
     */
    @RequestMapping(value = {routeHelper.botSubscribe}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public final void subscribeAppToPage() {
        try {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
            params.set("access_token", getPageAccessToken());
            restTemplate.postForEntity(fbApiEndpoints.getSubscribeUrl, params, String.class);
        } catch (Exception e) {
            //Silent is Golden
            logger.error(e.getMessage());
            //e.getStackTrace();
        }
    }

    protected final void startConversation(Event event, String methodName) {
        startConversation(event.getSender().getId(), methodName);
    }

    protected final void nextConversation(Event event) {
        nextConversation(event.getSender().getId());
    }

    protected final void stopConversation(Event event) {
        stopConversation(event.getSender().getId());
    }

    protected final boolean isConversationOn(Event event) {
        return isConversationOn(event.getSender().getId());
    }

    private void invokeMethods(Event event) {
        try {
            List<MethodWrapper> methodWrappers = eventToMethodsMap.get(event.getType().name().toUpperCase());
            if (methodWrappers == null) return;

            methodWrappers = new ArrayList<MethodWrapper>(methodWrappers);
            MethodWrapper matchedMethod =
                    getMethodWithMatchingPatternAndFilterUnmatchedMethods(getPatternFromEventType(event), methodWrappers);
            if (matchedMethod != null) {
                methodWrappers = new ArrayList<MethodWrapper>();
                methodWrappers.add(matchedMethod);
            }

            for (MethodWrapper methodWrapper : methodWrappers) {
                Method method = methodWrapper.getMethod();
                if (Arrays.asList(method.getParameterTypes()).contains(Matcher.class)) {
                    method.invoke(this, event, methodWrapper.getMatcher());
                } else {
                    method.invoke(this, event);
                }
            }
        } catch (Exception e) {
            //Silent is Golden
            logger.error(e.getMessage());
            //e.getStackTrace();
        }
    }

    private void invokeChainedMethod(Event event) {
        Queue<MethodWrapper> queue = conversationQueueMap.get(event.getSender().getId());

        if (queue != null && !queue.isEmpty()) {
            MethodWrapper methodWrapper = queue.peek();

            try {
                EventType[] eventTypes = methodWrapper.getMethod().getAnnotation(Controller.class).events();
                for (EventType eventType : eventTypes) {
                    if (eventType.name().equalsIgnoreCase(event.getType().name())) {
                        methodWrapper.getMethod().invoke(this, event);
                        return;
                    }
                }
            } catch (Exception e) {
                //Silent is Golden
                logger.error(e.getMessage());
                //e.getStackTrace();
            }
        }
    }

    private String getPatternFromEventType(Event event) {
        switch (event.getType()) {
            //case MESSAGE:
            //    return event.getMessage().getText();
            case QUICK_REPLY:
                return event.getMessage().getQuickReply().getPayload();
            case POSTBACK:
                return event.getPostback().getPayload();
            default:
                return event.getMessage().getText();
        }
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(bắt đầu|chào|bắt đầu lại)$")
    public void onGetStarted(Event event) {
        String fid = event.getSender().getId();
        Optional<ChatbotStudents> userVerify = this.chatbotstudentService.findById(fid);
        Object _lock = new Object();
        Students stdunt;
        User userData;

        if (userVerify.isPresent()) {
            synchronized (_lock) {
                stdunt = studentService.getStudent(userVerify.get().getSid());
            }
            if (stdunt != null)
            {
                Button[] quickReplies = new Button[]{
                        new Button().setContentType("text").setTitle("Xem thời khóa biểu").setPayload("Xem thời khóa biểu"),
                        new Button().setContentType("text").setTitle("Xem điểm").setPayload("Xem điểm"),
                        new Button().setContentType("text").setTitle("Xem thông báo").setPayload("Xem thông báo"),
                        new Button().setContentType("text").setTitle("Xem thông tin").setPayload("Xem thông tin"),
                        new Button().setContentType("text").setTitle("Cài đặt").setPayload("Cài đặt")
                };
                reply(event, new Message().setText("Xin chào " + stdunt.getName() + ", lại là Hapi đây ^^, bạn muốn xem thông tin gì ạ?").setQuickReplies(quickReplies));

                return;
            }
        }
        Button[] quickReplies = new Button[]{
                new Button().setContentType("text").setTitle("Đăng nhập").setPayload("Đăng nhập")
        };
        synchronized (_lock) {
            userData = this.getUser(fid);
        }
        reply(event, new Message().setAttachment(new Attachment().setType("image").setPayload(new Payload()
                .setReusable(true)
                .setUrl("https://cdn.notevn.com/TPWuM66xl.png") //userData.getProfilePic()
        )));
        reply(event, new Message()
                        .setText("Bạn " + userData.getFirstName() + " cute dễ thương ơi ^^, đây là lần đầu tiên bạn sử dụng Hapi, hãy đăng nhập lần đầu bạn nhé!?")
                        .setQuickReplies(quickReplies)
                );
    }

    public User getUser(String id) {
        return restTemplate.getForEntity(routeHelper.getUserApi, User.class, id, this.pageAccessToken).getBody();
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Xem thông báo)$")
    public void lookUpNotification(Event event) throws IOException {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.NotificationLookup(this.notificationRespository, this.notificationService));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Đăng nhập|Đăng ký)$")
    public void logMeIn(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.login(""));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Cài đặt)$"/*, next = "configCredentialsStep2"*/)
    public void configMyCredentials(Event event) {
        //startConversation(event, "configCredentialsStep2");
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.userConfigRequest());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Đăng xuất)$")
    public void configCredentialsStep2(Event event) {
        Button[] quickReplies = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại")
        };
        ScheduleBot schedulebot = this.initial(event);
        reply(event, new Message().setAttachment(new Attachment().setType("image").setPayload(new Payload()
                .setReusable(true)
                .setUrl("https://cdn.notevn.com/9i9JSQXxe.png")
        )).setQuickReplies(quickReplies));
        reply(event, schedulebot.userConfigCome().setQuickReplies(quickReplies));
        //stopConversation(event);
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Xem điểm)$"/*, next = "viewPointStep2"*/)
    public void viewMyPoint(Event event) {
        //startConversation(event, "viewPointStep2");
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.showPointOpt());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Điểm hiện tại|Tất cả điểm)$")
    public void viewPointStep2(Event event) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException {
        ScheduleBot schedulebot = this.initial(event);

        reply(event, schedulebot.detailPointOpt(event.getMessage().getText()));
        //stopConversation(event);
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Báo lỗi)$")
    public void bugReport(Event event) {
        String reply = "Lỗi ư :((( bạn có thể cho Hapi biết chi tiết lỗi là gì bằng những nút ở dưới được không ạ, buồn quá đi";
        Button[] buttons = new Button[]{
                new Button().setType("web_url").setUrl("https://vlink.maxddns.com/K7JBPQbY").setTitle("Liên hệ"),
                new Button().setType("web_url").setUrl("https://vlink.maxddns.com/qx0VaGby").setTitle("Pháp lý (Email)")
        };
        Button[] buttons2 = new Button[]{
                new Button().setContentType("text").setTitle("Bắt đầu lại").setPayload("Bắt đầu lại"),
                new Button().setContentType("text").setTitle("Báo lỗi").setPayload("Báo lỗi")
        };
        reply(event, new Message().setAttachment(new Attachment().setType("template").setPayload(new Payload()
                .setTemplateType("button").setText(reply).setButtons(buttons))).setQuickReplies(buttons2));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "Điểm kỳ (.*), (.*)-(.*)")
    public void specificSemesterPoint(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.viewSpecificPoint(event.getMessage().getText()));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Xem thời khóa biểu)$"/*, next = "askForWeek"*/)
    public void askForSemester(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        //startConversation(event, "askForWeek");
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.startFirstInitial());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(HOW_TO_USE|Cách xử dụng)$")
    public void howToUse(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.howToUse());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(TKB tuần này)$")
    public void currentWeekSchedule(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, InterruptedException, ParseException {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.currentWeekView());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(Xem thông tin)$")
    public void currentSessionInfomation(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.currentSessionDetail());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Học kỳ (.*), (.*))$"/*, next = "showMeMySchedule"*/)
    public void askForWeek(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, InterruptedException, ParseException {
        ScheduleBot schedulebot = this.initial(event);
        if (schedulebot.patternSearch("Học kỳ (.*), (.*)", event.getMessage().getText())) {
            startConversation(event, "showMeMySchedule");
            reply(event, schedulebot.startWeekInitial(event.getMessage().getText()));
        } else if(schedulebot.patternSearch("TKB tuần này", event.getMessage().getText())) {
            reply(event, schedulebot.currentWeekView());
            //stopConversation(event);
        } else {
            //stopConversation(event);
        }
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK}, pattern = "^[0-9]*$")
    public void showMeMySchedule(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, InterruptedException, ParseException {
        stopConversation(event);
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.getScheduleByWeekAndSemester(event.getMessage().getText(), false));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK}, pattern = "\\b(?!(?i)([0-9]|Đăng nhập|bắt đầu|chào|bắt đầu lại|Xem thời khóa biểu|Đăng xuất|Cài đặt|Điểm kỳ |TKB tuần này|Báo lỗi|Xem thông báo|Học kỳ |Báo lỗi|Xem điểm|Cách xử dụng|HOW_TO_USE|get started))\\b\\S+")
    public void defaultReply(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.onMessageCome(event.getMessage().getText()));
    }

    @CrossOrigin(origins = "https://2cll.com", maxAge = 3600)
    @RequestMapping(value = {routeHelper.saveFbCredentials}, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveFacebookCredentials(
            @RequestParam(value = "fid", defaultValue = "") String fid,
            @RequestParam(value = "sid", defaultValue = "") int sid,
            @RequestParam(value = "pwd", defaultValue = "") String pwd

    ) throws MalformedURLException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        if (fid == "" || sid == 0 || pwd == "")
            return ResponseEntity.badRequest().build();
        ScheduleBot scheduleBasic = new ScheduleBot(decryptFid(fid), this.chatbotstudentRepository, this.chatbotstudentService, this.studentRepository, this.studentService);
        browserHelper studentBasicTest = new browserHelper(sid, pwd, this.studentRepository, this.studentService);
        Optional<Students> stdntVerify = this.studentService.findById(sid);
        Optional<Students> reAuthenticate;

        String _back = studentBasicTest.conAuth(stdntVerify.isPresent());

        if (!stdntVerify.isPresent())
            stdntVerify = this.studentService.findById(sid);

        if (stdntVerify.isPresent())
            scheduleBasic.saveFbCredentials(sid);

        return ResponseEntity.ok(_back);
    }

    public String decryptFid (String strEncoded) {
        String decoded = new String(Base64.getDecoder().decode(strEncoded));
        return decoded;
    }

    private ScheduleBot initial(Event event) {
        return new ScheduleBot(
                event.getSender().getId(),
                this.chatbotstudentRepository,
                this.chatbotstudentService,
                this.studentRepository,
                this.studentService
        );
    }
}
