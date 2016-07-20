package com.twilioFull.sms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@SuppressWarnings("serial")
public class SendSMS extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		ServletContext context=getServletContext();
		String SID = context.getInitParameter("TWILIO_ACCOUNT_SID");
		String AUTH_TOKEN = context.getInitParameter("TWILIO_AUTH_TOKEN");
		TwilioRestClient client = new TwilioRestClient(SID, AUTH_TOKEN);
	    Account account = client.getAccount();
	    MessageFactory messageFactory = account.getMessageFactory();
	    List<NameValuePair> params = new ArrayList<NameValuePair>();
	    params.add(new BasicNameValuePair("To", new String("+918190896474")));
	    params.add(new BasicNameValuePair("From", context.getInitParameter("TWILIO_NUMBER")));
	    params.add(new BasicNameValuePair("Body", "Hello Senthil. This is happy to find, that you have understood how to send SMS!"));
		try {
			Message sms = messageFactory.create(params);
		} catch (TwilioRestException e) {
			e.printStackTrace();
		}
	      resp.getWriter().print("Ok");
	}
}
