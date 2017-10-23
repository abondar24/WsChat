package org.abondar.experimental.wschat.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.wschat.server.Message;
import org.abondar.experimental.wschat.server.ResponseUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@WebSocket
public class ChatClientSocket {
    private Logger logger = LoggerFactory.getLogger(ChatClientSocket.class);
    private Session session;

    private CountDownLatch latch = new CountDownLatch(1);
    private ObjectMapper mapper = new ObjectMapper();

    private String username = "";

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {
        logger.info("Message from server: " + message);

        switch (message) {
            case ResponseUtil.ENTER_USERNAME:
                System.out.println(message);
                username = ChatSessionClient.getInstance().in.next();
                session.getRemote().sendString("Username:" + username);
                break;

            case ResponseUtil.USER_EXISTS:
                username = "";
                System.out.println(message);
                username = ChatSessionClient.getInstance().in.next();
                session.getRemote().sendString("Username:" + username);
                break;

            case ResponseUtil.UNKNOWN_USER:
                System.out.println(message);
                break;

            case ResponseUtil.UNKNOWN_MESSAGE_FORMAT:
                System.out.println(message);
                break;


        }

        if (message.contains("Users: ")) {
            System.out.println(message);
        } else if (message.contains("{")) {
            Message msg = mapper.readValue(message, Message.class);
            System.out.println("Message from: " + msg.getSender());
            System.out.println(msg.getMessage());
            sendMessage();
        }


    }


    @OnWebSocketConnect
    public void onConnect(Session session) {
        logger.info("Connected to server");
        this.session = session;
        latch.countDown();
    }

    private void sendMessage() throws IOException {
        System.out.println("Enter recipient(enter all if you send to everyone)");
        String recipient = ChatSessionClient.getInstance().in.next();
        System.out.println("Enter message");
        String message = ChatSessionClient.getInstance().in.next();

        Message mesg = new Message(username, recipient, message);

        String msg = mapper.writeValueAsString(mesg);
        session.getRemote().sendString(msg);


    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
