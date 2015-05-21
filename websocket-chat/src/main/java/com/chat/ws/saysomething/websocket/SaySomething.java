package com.chat.ws.saysomething.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.CloseReason;
import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/saySomething/{nickName}")
public class SaySomething {
	
	public static Map<String, Session> sessions = new HashMap<String, Session>(); 
	
    @OnMessage
    public void sayHello(String text, Session session, @PathParam("nickName") String nickname) throws IOException, EncodeException {
    	Iterator<Session> iSessions = session.getOpenSessions().iterator();
    	while(iSessions.hasNext()) {
    		Session pSession = iSessions.next();
    		pSession.getBasicRemote().sendText(nickname + " said: " + text);    		
    	}
    }
    
    @OnOpen
    public void helloOnOpen(Session session, @PathParam("nickName") String nickname) throws IOException {    	
    	sessions.put(nickname, session);
    	System.out.println("WebSocket opened: " + session.getId());
    }
    
    @OnClose
    public void helloOnClose(CloseReason reason, Session session, @PathParam("nickName") String nickname) {
    	sessions.remove(nickname);
        System.out.println("Closing a WebSocket due to " + reason.getReasonPhrase());
    }
}
