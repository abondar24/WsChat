package org.abondar.experimental.gameofthree.server;


import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebSocket
public class GameOfThreeServer {

    private  Logger logger = LoggerFactory.getLogger(GameOfThreeServer.class);

    @OnWebSocketMessage
    public void onText(Session session,String message) throws IOException{
       logger.info("Message received: "+message);
        if (session.isOpen()){
            String resp = "challenge accepted";
            session.getRemote().sendString(resp);
        }
    }


    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException{
        logger.info(session.getRemoteAddress().getHostString() + "connected");
    }

    @OnWebSocketClose
    public void OnClose(Session session,int status, String reason){
        logger.info(session.getRemoteAddress().getHostString() + "closed");
    }

}
