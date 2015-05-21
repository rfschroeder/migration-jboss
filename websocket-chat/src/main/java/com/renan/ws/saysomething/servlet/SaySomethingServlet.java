package com.renan.ws.saysomething.servlet; 

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;

import com.chat.ws.saysomething.websocket.SaySomething;

public class SaySomethingServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		String destination = request.getParameter("destination");
		String nickname = request.getParameter("nickname");
		String text = request.getParameter("text");
		
		try {
			if (SaySomething.sessions.get(nickname) == null) {
				Session session = SaySomething.sessions.get(destination);
				if (destination != null && nickname != null && text != null) {
					if (session != null) {
						session.getBasicRemote().sendText(nickname + " said: " + text);
						printMessageOnScreen(response, "The message \"" + request.getParameter("text") + "\" has been sent to " + destination + ".");
					} else {
						printMessageOnScreen(response, "Invalid destination. Please inform a valid destination nickname!");
					}
				} else {
					printMessageOnScreen(response, "Invalid parameters! Please set <b>nickname</b>, <b>destination</b> and <b>text</b> attributes of this HTTP request.");
				}
			} else {
				printMessageOnScreen(response, "Nickname already in use. Please choose another one.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printMessageOnScreen(HttpServletResponse response, String message) throws IOException {
		response.setContentType("text/html");
		StringBuilder htmlBody = new StringBuilder();		
		htmlBody.append("<!DOCTYPE html>\n");
		htmlBody.append("<html lang='pt-br' class='no-js'>\n");
		htmlBody.append("<head>\n");		
		htmlBody.append("<title>Chat Destination Message</title>\n");
		htmlBody.append("</head>\n");
		htmlBody.append("<body>\n");
		htmlBody.append("<div>\n");
		htmlBody.append(message);
		htmlBody.append("\n");
		htmlBody.append("</div>\n");
		htmlBody.append("</body>\n");
		htmlBody.append("</html>");
		PrintWriter out = response.getWriter();
		out.println(htmlBody.toString());
		out.flush();
		out.close();
	}
}
