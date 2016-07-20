package com.twilioFull.event;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.*;
@SuppressWarnings("serial")
public class EventCallbackFromWorkSpace extends HttpServlet{
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		Logger log = Logger.getLogger(EventCallbackFromWorkSpace.class.getName());
		log.info("I have recieved a Callback! The details of the event are given below!");
		log.info(req.getParameter("EventType")+", "+req.getParameter("AccountSid")+", "+req.getParameter("WorkspaceSid")+", "+req.getParameter("WorkspaceName")+", "+req.getParameter("EventDescription")+", "+req.getParameter("ResourceType"));
	}
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		doGet(req, res);
	}
}
