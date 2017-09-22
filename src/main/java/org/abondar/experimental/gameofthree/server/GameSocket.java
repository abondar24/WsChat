package org.abondar.experimental.gameofthree.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.gameofthree.client.GameClientSocket;
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

        if (message.contains("Name:")) {
            playerName = message.substring(5, message.length());
            if (!GameSession.getInstance().checkName(playerName)) {
                GameSession.getInstance().addName(playerName);
                GameSession.getInstance().writeSpecificPlayer(playerName, "You are in game");
                GameSession.getInstance().writeSpecificPlayer(playerName, "Current initial number is "
                        + GameSession.getInstance().initialNumber);
            } else {
                session.getRemote().sendString("Player with such name already exists");
            }

        } else if (message.contains("{")) {
            ObjectMapper mapper = new ObjectMapper();
            Move move = mapper.readValue(message, Move.class);

            if (!GameSession.getInstance().checkName(move.getPlayerFrom())) {
                session.getRemote().sendString("You are not in game");
            }

            if (!GameSession.getInstance().checkName(move.getPlayerTo())) {
                session.getRemote().sendString("This player is not in game");
            }

            if (move.getResultingNumber() == 1) {
                GameSession.getInstance().initialNumber=0;
                GameSession.getInstance().writeAllPlayers("Game Over");
            }

            String newMove = mapper.writeValueAsString(move);

            GameSession.getInstance().writeSpecificPlayer(move.getPlayerTo(), newMove);
        } else if (message.matches("-?\\d+(\\.\\d+)?")) {
            if (GameSession.getInstance().initialNumber == 0) {
                GameSession.getInstance().initialNumber=Integer.valueOf(message);
                GameSession.getInstance().writeAllPlayers("Game has begun from number " +
                        message);

            } else {
                GameSession.getInstance().writeAllPlayers("The number is already initialized");
            }
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
        if (GameSession.getInstance().isPlayersEmpty()){
            GameSession.getInstance().initialNumber=0;
            logger.info("All players have left");
        }


    }

}
