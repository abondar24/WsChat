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
public class GameSocket {

    private Logger logger = LoggerFactory.getLogger(GameSocket.class);

    public Session session;

    public String playerName;

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        logger.info("Message received: " + message);

        if (message.contains("Name:")){
            playerName=message.substring(5,message.length());
            if (!GameSession.getInstance().checkName(playerName)){
                GameSession.getInstance().addName(playerName);
                GameSession.getInstance().writeSpecificPlayer(playerName,"you are in game");
            } else {
                session.getRemote().sendString("Player with such name already exists");
            }

        } else {
            GameSession.getInstance().writeAllPlayers(message);
        }

    }


    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        logger.info(session.getRemoteAddress().getHostString() + " connected");
        this.session = session;
        GameSession.getInstance().join(this);

    }


    @OnWebSocketClose
    public void OnClose(Session session, int status, String reason) {
        logger.info(session.getRemoteAddress().getHostString() + " closed");
        GameSession.getInstance().removeName(playerName);
        GameSession.getInstance().remove(this);
    }

}
