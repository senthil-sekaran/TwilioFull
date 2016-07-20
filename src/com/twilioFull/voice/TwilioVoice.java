package com.twilioFull.voice;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

import com.twilio.sdk.verbs.TwiMLResponse;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.Client;
import com.twilio.sdk.verbs.Number;

@SuppressWarnings("serial")
public class TwilioVoice extends HttpServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String phoneNumber = request.getParameter("PhoneNumber");
        if (phoneNumber == null) {
           phoneNumber = "jenny";
        }

        /* Use this as the caller ID when making calls from a browser. */
        String callerId = "+12318034111";

        TwiMLResponse twiml = new TwiMLResponse();
        Dial dial = new Dial();
        try {
            if (Pattern.compile("^[\\d\\(\\)\\- \\+]+$").matcher(phoneNumber).matches()) {
                dial.append(new Number(phoneNumber));
                dial.setCallerId(callerId);
            } else {
                dial.append(new Client(phoneNumber));
            }
            twiml.append(dial);
        } catch (TwiMLException e) {
            e.printStackTrace();
        }
        response.setContentType("application/xml");
        response.getWriter().print(twiml.toXML());
    }
}