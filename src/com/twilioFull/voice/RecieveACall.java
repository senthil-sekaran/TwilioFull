package com.twilioFull.voice;

import com.twilio.sdk.verbs.Dial;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.Number;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class RecieveACall extends HttpServlet {
	
  @Override
  public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	  resp.setContentType("application/xml");
    TwiMLResponse twiml = new TwiMLResponse();
    Say say = new Say("Hello Caller! This number belongs to Senthil!");
    say.setVoice("woman");
    Dial dial = new Dial();
    Number number = new Number("+918190896474");
    try {
      twiml.append(say);
      dial.append(number);
      twiml.append(dial);
    } catch (TwiMLException e) {
      throw new ServletException("Twilio error", e);
    }
    resp.setContentType("application/xml");
    resp.getWriter().print(twiml.toXML());
  }
}
