package com.twilioFull.taskManagement;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.sdk.TwilioTaskRouterClient;
import com.twilio.sdk.verbs.Enqueue;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@SuppressWarnings("serial")
public class EnqueueACallWithHighPiority extends HttpServlet {

    private String workflowSid;

    @Override
    public void init() {
    	ServletContext context = getServletContext();
        workflowSid = context.getInitParameter("TWILIO_WORKFLOW_SID");
    }
    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		response.setContentType("application/xml");
	    System.out.println("Call Task Enqueued with Piority 2!");
	    final String taskAttributes = enqueueTask();
	    response.getWriter().print(taskAttributes);
    }
    public String enqueueTask() {
        final TwiMLResponse twiml = new TwiMLResponse();
        final Enqueue enqueue = new Enqueue();
        enqueue.setWorkflowSid(workflowSid);

        final com.twilio.sdk.verbs.Task task = new com.twilio.sdk.verbs.Task("{\"language\":\"en\",\"type\":\"call\",\"priority\":\"2\"}");

        try {
            enqueue.append(task);
            twiml.append(enqueue);
        } catch (final TwiMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return twiml.toXML();
    }
}