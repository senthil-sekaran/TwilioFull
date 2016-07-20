package com.twilioFull.voice;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioTaskRouterClient;
import com.twilio.sdk.resource.instance.taskrouter.Reservation;
import com.twilio.sdk.resource.instance.taskrouter.Task;

@SuppressWarnings("serial")
public class CallStatusCallback extends HttpServlet {
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		ServletContext context = getServletContext();
		TwilioTaskRouterClient client = new TwilioTaskRouterClient(context.getInitParameter("TWILIO_ACCOUNT_SID"), context.getInitParameter("TWILIO_AUTH_TOKEN"));
	    
		Reservation reservation = client.getReservation(context.getInitParameter("TWILIO_WORKSPACE_SID"), request.getParameter("TaskSid"), request.getParameter("ReservationSid"));
	    try {
			reservation.accept();
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Task task = client.getTask(context.getInitParameter("TWILIO_WORKSPACE_SID"), request.getParameter("TaskSid"));
	    Map<String, String> params = new HashMap<String, String>();
	    params.put("AssignmentStatus", "completed");
	    try {
			task.update(params);
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
