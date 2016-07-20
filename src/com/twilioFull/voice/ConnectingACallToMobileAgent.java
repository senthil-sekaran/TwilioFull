package com.twilioFull.voice;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class ConnectingACallToMobileAgent extends HttpServlet {
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
	{
		res.setContentType("application/json");
		JSONObject json = null;
		try {
			json = new JSONObject(req.getParameter("WorkerAttributes"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(json != null)
		{
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("instruction", "dequeue");
			try {
				map.put("to", json.get("contact_uri").toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			map.put("from", "+12318034111");
			map.put("post_work_activity_sid", "WAd7cf0f11199eea3eed45f53c55826f3b");
			map.put("status_callback_url","http://1-dot-twiliofull.appspot.com/CallStatusCallback");
			JSONObject jsono = new JSONObject(map);
			res.getWriter().print(jsono);
		}
		else
		{
			return;
		}
	}
}
