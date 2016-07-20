package com.twilioFull.voice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Call;
import com.twilio.sdk.resource.factory.CallFactory;

@SuppressWarnings("serial")
public class MakeACall extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
	{
		ServletContext context=getServletContext();
		String SID = context.getInitParameter("TWILIO_ACCOUNT_SID");
		String AUTH_TOKEN = context.getInitParameter("TWILIO_AUTH_TOKEN");
		TwilioRestClient client = new TwilioRestClient(SID, AUTH_TOKEN);
        Account mainAccount = client.getAccount();
        CallFactory callFactory = mainAccount.getCallFactory();
        Map<String, String> callParams = new HashMap<String, String>();
        callParams.put("To", "+918190896474");
        callParams.put("From", context.getInitParameter("TWILIO_NUMBER"));
        callParams.put("Url", "http://www.sendsmsusingtwilio.appspot.com/callxml");
		try {
			Call call = callFactory.create(callParams);
		} catch (TwilioRestException e) {
			e.printStackTrace();
		}
        System.out.println("OK!");
        resp.getWriter().println("Ok!");
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException
	{
		doGet(req, resp);
	}
}
