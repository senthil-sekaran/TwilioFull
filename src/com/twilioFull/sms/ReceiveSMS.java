package com.twilioFull.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@SuppressWarnings("serial")
public class ReceiveSMS extends HttpServlet{
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		String fromNumber = req.getParameter("From");
	    String body = req.getParameter("Body");
	    String message = String.format("Hello, %s, you said %s", fromNumber, body);

	    TwiMLResponse twiml = new TwiMLResponse();
	    Message sms = new Message(message);
	    try {
	      twiml.append(sms);
	    } catch (TwiMLException e) {
	      throw new ServletException("Twilio error", e);
	    }

	    
	    ServletContext context=getServletContext();
		String SID = context.getInitParameter("TWILIO_ACCOUNT_SID");
		String AUTH_TOKEN = context.getInitParameter("TWILIO_AUTH_TOKEN");
		TwilioRestClient client = new TwilioRestClient(SID, AUTH_TOKEN);
	    Account account = client.getAccount();
	    MessageFactory messageFactory = account.getMessageFactory();
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("To", new String("+918190896474")));
	    params.add(new BasicNameValuePair("From", context.getInitParameter("TWILIO_NUMBER")));
	    params.add(new BasicNameValuePair("Body", body));
		try {
			com.twilio.sdk.resource.instance.Message sendsms = messageFactory.create(params);
		} catch (TwilioRestException e) {
			e.printStackTrace();
		}
	    
	    
	    res.setContentType("application/xml");
	    res.getWriter().print(twiml.toXML());
	}

}
