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
                enterUsername();
                break;

            case ResponseUtil.USER_EXISTS:
                username = "";
                System.out.println(message);
                enterUsername();
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
            System.out.println(ResponseUtil.ENTER_USERNAME);
            enterUsername();

        } else if (message.contains("{")) {
            Message msg = mapper.readValue(message, Message.class);
            if (msg.getMessage() != null) {
                System.out.println("Message from: " + msg.getSender());
                System.out.println(msg.getMessage());
            }
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
        String recipient = ChatClient.getInstance().in.next();
        if (recipient.equals("cs")) {
            closeSession();
            return;
        }

        System.out.println("Enter message");
        String message = ChatClient.getInstance().in.next();
        if (message.equals("cs")) {
            closeSession();
            return;
        }


        Message mesg = new Message(username, recipient, message);

        String msg = mapper.writeValueAsString(mesg);
        session.getRemote().sendString(msg);


    }

    private void enterUsername() throws IOException {
        username = "";

        username = ChatClient.getInstance().in.next();
        if (username.equals("cs")) {
            closeSession();
            return;
        }

        session.getRemote().sendString("Username:" + username);
    }

    private void closeSession() {
        System.out.println("You are disconnected");
        session.close();
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
