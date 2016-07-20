package com.twilioFull.taskRouter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.twilio.sdk.CapabilityToken.DomainException;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.TwilioTaskRouterClient;
import com.twilio.sdk.resource.instance.taskrouter.Reservation;
import com.twilio.sdk.resource.instance.taskrouter.Task;
import com.twilio.sdk.taskrouter.TaskRouterWorkerCapability;
import com.twilio.sdk.verbs.Enqueue;
import com.twilio.sdk.verbs.Gather;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

@SuppressWarnings("serial")
public class TaskRouter extends HttpServlet {

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
    public void service(final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        if (request.getPathInfo() == null || request.getPathInfo().isEmpty()) {
            return;
        }

        if (request.getPathInfo().equals("/assignment_callback")) {
            response.setContentType("application/json");

            final Map<String, String> dequeueInstruction = new HashMap<String, String>();
            dequeueInstruction.put("instruction", "dequeue");
            dequeueInstruction.put("from", "+12318034111");
            dequeueInstruction.put("post_work_activity_sid", "WAd7cf0f11199eea3eed45f53c55826f3b");

            response.getWriter().print(JSONObject.toJSONString(dequeueInstruction));
        } else if (request.getPathInfo().equals("/create_task")) {
            response.setContentType("application/json");
            final String taskAttributes = createTask();
            response.getWriter().print(taskAttributes);
        } else if (request.getPathInfo().equals("/accept_reservation")) {
            response.setContentType("application/json");
            final String taskSid = request.getParameter("TaskSid");
            final String reservationSid = request.getParameter("ReservationSid");
            response.getWriter().print(acceptReservation(taskSid, reservationSid));
        } else if (request.getPathInfo().equals("/incoming_call")) {
             response.setContentType("application/xml");
             response.getWriter().print(handleIncomingCall());
        } else if (request.getPathInfo().equals("/enqueue_call")) {
             response.setContentType("application/xml");
             response.getWriter().print(enqueueTask());
        } else if (request.getPathInfo().equals("/agents")) {
             generateAgentView(request, response);
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

    public String acceptReservation(final String taskSid, final String reservationSid) {
        final Reservation reservation = client.getReservation(workspaceSid, taskSid, reservationSid);
        try {
            reservation.accept();
        } catch (final TwilioRestException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reservation.toJSON();
    }

    public String handleIncomingCall() {
        final TwiMLResponse twiml = new TwiMLResponse();
        final Gather gather = new Gather();
        gather.setNumDigits(1);
        gather.setTimeout(5);
        gather.setAction("enqueue_call");

        final Say sayEspanol = new Say("Para Espanol oprime el uno.");
        sayEspanol.setLanguage("es");

        final Say sayEnglish = new Say("For English, please hold or press two.");
        sayEnglish.setLanguage("en");

        try {
            gather.append(sayEspanol);
            gather.append(sayEnglish);
            twiml.append(gather);
        } catch (final TwiMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return twiml.toXML();
    }

    public String enqueueTask() {
        final TwiMLResponse twiml = new TwiMLResponse();
        final Enqueue enqueue = new Enqueue();
        enqueue.setWorkflowSid(workflowSid);

        final com.twilio.sdk.verbs.Task task = new com.twilio.sdk.verbs.Task("{\"language\":\"en\",\"type\":\"call\"}");
        task.setPriority(5);
        task.setTimeout(60);
        try {
            enqueue.append(task);
            twiml.append(enqueue);
        } catch (final TwiMLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return twiml.toXML();
    }

    public void generateAgentView(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String workerSid = request.getParameter("WorkerSid");

        final TaskRouterWorkerCapability workerCapability =
                new TaskRouterWorkerCapability(accountSid, authToken, workspaceSid, workerSid);
        workerCapability.allowActivityUpdates();
        workerCapability.allowReservationUpdates();

        String workerToken = null;
        try {
            workerToken = workerCapability.generateToken();
        } catch (final DomainException e) {
            e.printStackTrace();
        }
        // Forward the token information to a JSP view
        response.setContentType("text/html");
        request.setAttribute("worker_token", workerToken);
        final RequestDispatcher view = request.getRequestDispatcher("/agent.jsp");
        view.forward(request, response);
    }
}
