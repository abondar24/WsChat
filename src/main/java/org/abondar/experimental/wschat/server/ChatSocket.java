package org.abondar.experimental.wschat.server;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@WebSocket
public class ChatSocket {

    private Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    public Session session;

    private ObjectMapper mapper = new ObjectMapper();

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        logger.info("Message received: " + message);

        if (message.contains("Username")) {
            String username = message.split(":")[1];
            if (!ChatRoom.getInstance().checkUsername(username)) {
                ChatRoom.getInstance().addUsername(username, this);
                session.getRemote().sendString("{}");
            } else {
                session.getRemote().sendString(ResponseUtil.USER_EXISTS);

            }

        } else if (message.contains("{")) {

            Message msg = mapper.readValue(message, Message.class);

            if (!ChatRoom.getInstance().checkUsername(msg.getSender()) ||
                    !ChatRoom.getInstance().checkUsername(msg.getRecepient()) && !"all".equals(msg.getRecepient())) {
                session.getRemote().sendString(ResponseUtil.UNKNOWN_USER);
            } else {
                if (msg.getRecepient().equals("all")) {
                    ChatRoom.getInstance().writeAllUsers(msg);
                } else {
                    ChatRoom.getInstance().writeSpecificUser(msg);

                }
            }


        } else {
            session.getRemote().sendString(ResponseUtil.UNKNOWN_MESSAGE_FORMAT);
        }

    }


    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        logger.info(session.getRemoteAddress().getHostString() + " connected");
        this.session = session;
        ChatRoom.getInstance().join(this);
        session.getRemote().sendString(ResponseUtil.CONNECTED);

        if (!ChatRoom.getInstance().clientUser.isEmpty()){
            session.getRemote().sendString("Users: " + ChatRoom.getInstance().clientUser.keySet());
        } else {
            session.getRemote().sendString(ResponseUtil.ENTER_USERNAME);
        }


    }


    @OnWebSocketClose
    public void OnClose(Session session, int status, String username) {
        logger.info(session.getRemoteAddress().getHostString() + " closed");
        ChatRoom.getInstance().removeName(username);
        ChatRoom.getInstance().remove(this);
        if (ChatRoom.getInstance().isRoomEmpty()) {
            logger.info("All clients have left");
        }


    }

}
