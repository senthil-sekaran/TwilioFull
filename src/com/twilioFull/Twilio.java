package com.twilioFull;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.io.IOException;

import com.twilio.sdk.CapabilityToken.DomainException;
import com.twilio.sdk.client.TwilioCapability;

@SuppressWarnings("serial")
public class Twilio extends HttpServlet {

    public static String ACCOUNT_SID;
    public static String AUTH_TOKEN;
    
	public void init()
	{
		ServletContext context = getServletContext();
		ACCOUNT_SID = context.getInitParameter("TWILIO_ACCOUNT_SID");
		AUTH_TOKEN = context.getInitParameter("TWILIO_AUTH_TOKEN");
	}

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // This is a special Quickstart application sid - or configure your own
        // at twilio.com/user/account/apps
        String applicationSid = "AP252f22983c21c966756083c8274f3905";

        TwilioCapability capability = new TwilioCapability(ACCOUNT_SID, AUTH_TOKEN);
        capability.allowClientOutgoing(applicationSid);
        capability.allowClientIncoming("jenny");
        String token = null;
        try {
            token = capability.generateToken();
        } catch (DomainException e) {
            e.printStackTrace();
        }
        // Forward the token information to a JSP view
        response.setContentType("text/html");
        request.setAttribute("token", token);
        RequestDispatcher view = request.getRequestDispatcher("client.jsp");
        view.forward(request, response);
    }
}