package com.hapi.hapiservice.helpers.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class jsonHelper {
    private final String body;

    public jsonHelper () {
        this(null);
    }

    public jsonHelper (final String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }

    public void readBody(InputStream responseContent) throws IOException {
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(responseContent, "utf-8"));
            {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
        } catch (Exception e) {
            //Silent is Golden
        }
    }
}
