package com.twilioFull.taskManagement;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioTaskRouterClient;
import com.twilio.sdk.resource.instance.taskrouter.Task;

@SuppressWarnings("serial")
public class CreateATask extends HttpServlet {

    private String accountSid;
    private String authToken;
    private String workspaceSid;
    private String workflowSid;
    private TwilioTaskRouterClient client;

    @Override
    public void init() {
    	ServletContext context = getServletContext();
        accountSid = context.getInitParameter("TWILIO_ACCOUNT_SID");
        authToken = context.getInitParameter("TWILIO_AUTH_TOKEN");
        workspaceSid = context.getInitParameter("TWILIO_WORKSPACE_SID");
        workflowSid = context.getInitParameter("TWILIO_WORKFLOW_SID");
        client = new TwilioTaskRouterClient(accountSid, authToken);
    }

    // service() responds to both GET and POST requests.
    // You can also use doGet() or doPost()
    @Override
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        if (request.getPathInfo() == null || request.getPathInfo().isEmpty()) {
            return;
        }

        if (request.getPathInfo().equals("/assignment_callback")) {
            response.setContentType("application/json");
            System.out.println("Loop one");
            response.getWriter().print("{}");
        } else if (request.getPathInfo().equals("/create_task")) {
            response.setContentType("application/json");
            System.out.println("Loop two");
            final String taskAttributes = createTask();
            response.getWriter().print(taskAttributes);
        }
    }

    public String createTask() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("WorkflowSid", workflowSid);
        params.put("Attributes", "{\"selected_language\":\"en\"}");
        try {
            final Task task = client.createTask(workspaceSid, params);
            return task.toJSON();
        } catch (final TwilioRestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "{\"error\":\"could not create task\"}";
    }
}
