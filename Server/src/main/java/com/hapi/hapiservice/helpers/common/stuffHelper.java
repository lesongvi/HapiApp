package com.hapi.hapiservice.helpers.common;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;

public class stuffHelper {
    public stuffHelper () {}

    public WebClient initial() {
        WebClient webClient = new WebClient();
        webClient.getCache().clear();

        WebClientOptions webClientOpt = webClient.getOptions();
        webClientOpt.setThrowExceptionOnScriptError(false);
        webClientOpt.setThrowExceptionOnFailingStatusCode(false);
        webClientOpt.setJavaScriptEnabled(false); //AnhQuan? What a Stupidity!
        webClientOpt.setRedirectEnabled(true);
        //webClientOpt.waitForBackgroundJavaScript(20000);
        webClientOpt.setCssEnabled(false);
        webClientOpt.setTimeout(100000);

        return webClient;
    }
}
