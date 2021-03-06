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
        //N???u ch??a verify webhook th?? comment 3 d??ng d?????i l???i
        try {
            setGetStartedButton("B???t ?????u");
            setGreetingText(new Payload[]{new Payload().setLocale("default").setText("Hapi HUTECH Bot l?? m???t chatbot t??? ?????ng" +
                    " gi??p sinh vi??n HUTECH c?? th??? xem chi ti???t th???i kh??a bi???u, ??i???m c??c h???c k??? v?? th??ng b??o m???i nh???t t??? tr?????ng HUTECH ngay tr??n ???ng d???ng Messenger " +
                    "h??y nh???p v??o n??t \"B???t ?????u\" ho???c g?? \"Ch??o\".")});
            setPersistentMenu(new Payload[]{new Payload().setLocale("default").setCallToActions(this.ctaInit())});
        } catch (Exception e) {
            //Silent is Golden
            logger.error(e.getMessage());
            //e.getStackTrace();
        }
    }

    private CallToActions[] ctaInit() {
        CallToActions cllta1 = new CallToActions();
        cllta1.setTitle("C??ch s??? d???ng");
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

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(b???t ?????u|ch??o|b???t ?????u l???i)$")
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
                        new Button().setContentType("text").setTitle("Xem th???i kh??a bi???u").setPayload("Xem th???i kh??a bi???u"),
                        new Button().setContentType("text").setTitle("Xem ??i???m").setPayload("Xem ??i???m"),
                        new Button().setContentType("text").setTitle("Xem th??ng b??o").setPayload("Xem th??ng b??o"),
                        new Button().setContentType("text").setTitle("Xem th??ng tin").setPayload("Xem th??ng tin"),
                        new Button().setContentType("text").setTitle("C??i ?????t").setPayload("C??i ?????t")
                };
                reply(event, new Message().setText("Xin ch??o " + stdunt.getName() + ", l???i l?? Hapi ????y ^^, b???n mu???n xem th??ng tin g?? ????").setQuickReplies(quickReplies));

                return;
            }
        }
        Button[] quickReplies = new Button[]{
                new Button().setContentType("text").setTitle("????ng nh???p").setPayload("????ng nh???p")
        };
        synchronized (_lock) {
            userData = this.getUser(fid);
        }
        reply(event, new Message().setAttachment(new Attachment().setType("image").setPayload(new Payload()
                .setReusable(true)
                .setUrl("https://cdn.notevn.com/TPWuM66xl.png") //userData.getProfilePic()
        )));
        reply(event, new Message()
                        .setText("B???n " + userData.getFirstName() + " cute d??? th????ng ??i ^^, ????y l?? l???n ?????u ti??n b???n s??? d???ng Hapi, h??y ????ng nh???p l???n ?????u b???n nh??!?")
                        .setQuickReplies(quickReplies)
                );
    }

    public User getUser(String id) {
        return restTemplate.getForEntity(routeHelper.getUserApi, User.class, id, this.pageAccessToken).getBody();
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Xem th??ng b??o)$")
    public void lookUpNotification(Event event) throws IOException {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.NotificationLookup(this.notificationRespository, this.notificationService));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(????ng nh???p|????ng k??)$")
    public void logMeIn(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.login(""));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(C??i ?????t)$"/*, next = "configCredentialsStep2"*/)
    public void configMyCredentials(Event event) {
        //startConversation(event, "configCredentialsStep2");
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.userConfigRequest());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(????ng xu???t)$")
    public void configCredentialsStep2(Event event) {
        Button[] quickReplies = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i")
        };
        ScheduleBot schedulebot = this.initial(event);
        reply(event, new Message().setAttachment(new Attachment().setType("image").setPayload(new Payload()
                .setReusable(true)
                .setUrl("https://cdn.notevn.com/9i9JSQXxe.png")
        )).setQuickReplies(quickReplies));
        reply(event, schedulebot.userConfigCome().setQuickReplies(quickReplies));
        //stopConversation(event);
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Xem ??i???m)$"/*, next = "viewPointStep2"*/)
    public void viewMyPoint(Event event) {
        //startConversation(event, "viewPointStep2");
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.showPointOpt());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(??i???m hi???n t???i|T???t c??? ??i???m)$")
    public void viewPointStep2(Event event) throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException, IOException {
        ScheduleBot schedulebot = this.initial(event);

        reply(event, schedulebot.detailPointOpt(event.getMessage().getText()));
        //stopConversation(event);
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(B??o l???i)$")
    public void bugReport(Event event) {
        String reply = "L???i ?? :((( b???n c?? th??? cho Hapi bi???t chi ti???t l???i l?? g?? b???ng nh???ng n??t ??? d?????i ???????c kh??ng ???, bu???n qu?? ??i";
        Button[] buttons = new Button[]{
                new Button().setType("web_url").setUrl("https://vlink.maxddns.com/K7JBPQbY").setTitle("Li??n h???"),
                new Button().setType("web_url").setUrl("https://vlink.maxddns.com/qx0VaGby").setTitle("Ph??p l?? (Email)")
        };
        Button[] buttons2 = new Button[]{
                new Button().setContentType("text").setTitle("B???t ?????u l???i").setPayload("B???t ?????u l???i"),
                new Button().setContentType("text").setTitle("B??o l???i").setPayload("B??o l???i")
        };
        reply(event, new Message().setAttachment(new Attachment().setType("template").setPayload(new Payload()
                .setTemplateType("button").setText(reply).setButtons(buttons))).setQuickReplies(buttons2));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "??i???m k??? (.*), (.*)-(.*)")
    public void specificSemesterPoint(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.viewSpecificPoint(event.getMessage().getText()));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(Xem th???i kh??a bi???u)$"/*, next = "askForWeek"*/)
    public void askForSemester(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException {
        //startConversation(event, "askForWeek");
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.startFirstInitial());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(HOW_TO_USE|C??ch x??? d???ng)$")
    public void howToUse(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.howToUse());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(TKB tu???n n??y)$")
    public void currentWeekSchedule(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, ParseException {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.currentWeekView());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(Xem th??ng tin)$")
    public void currentSessionInfomation(Event event) {
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.currentSessionDetail());
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK, EventType.QUICK_REPLY}, pattern = "^(?i)(H???c k??? (.*), (.*))$"/*, next = "showMeMySchedule"*/)
    public void askForWeek(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, ParseException {
        ScheduleBot schedulebot = this.initial(event);
        if (schedulebot.patternSearch("H???c k??? (.*), (.*)", event.getMessage().getText())) {
            startConversation(event, "showMeMySchedule");
            reply(event, schedulebot.startWeekInitial(event.getMessage().getText()));
        } else if(schedulebot.patternSearch("TKB tu???n n??y", event.getMessage().getText())) {
            reply(event, schedulebot.currentWeekView());
            //stopConversation(event);
        } else {
            //stopConversation(event);
        }
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK}, pattern = "^[0-9]*$")
    public void showMeMySchedule(Event event) throws BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, InvalidKeyException, IOException, ParseException {
        stopConversation(event);
        ScheduleBot schedulebot = this.initial(event);
        reply(event, schedulebot.getScheduleByWeekAndSemester(event.getMessage().getText(), false));
    }

    @Controller(events = {EventType.MESSAGE, EventType.POSTBACK}, pattern = "\\b(?!(?i)([0-9]|????ng nh???p|b???t ?????u|ch??o|b???t ?????u l???i|Xem th???i kh??a bi???u|????ng xu???t|C??i ?????t|??i???m k??? |TKB tu???n n??y|B??o l???i|Xem th??ng b??o|H???c k??? |B??o l???i|Xem ??i???m|C??ch x??? d???ng|HOW_TO_USE|get started))\\b\\S+")
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

    ) throws MalformedURLException {
        if (fid == "" || sid == 0 || pwd == "")
            return ResponseEntity.badRequest().build();
        ScheduleBot scheduleBasic = new ScheduleBot(decryptFid(fid), this.chatbotstudentRepository, this.chatbotstudentService, this.studentRepository, this.studentService);
        browserHelper studentBasicTest = new browserHelper(sid, pwd, this.studentRepository, this.studentService);
        Optional<Students> stdntVerify = this.studentService.findById(sid);

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
