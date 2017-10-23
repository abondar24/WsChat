package org.abondar.experimental.wschat.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.wschat.server.Message;
import org.abondar.experimental.wschat.server.ResponseUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@WebSocket
public class ClientSocket {
    private Session session;

    private CountDownLatch latch = new CountDownLatch(1);
    private ObjectMapper mapper = new ObjectMapper();

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException {


         if (message.contains("Users: ")) {
             System.out.println(message);
             System.out.println(ResponseUtil.ENTER_USERNAME);

         }else if (message.contains("{")) {
            Message msg = mapper.readValue(message, Message.class);
            if (msg.getMessage() != null) {
                System.out.println("Message from: " + msg.getSender());
                System.out.println("Text: "+msg.getMessage());
            }

        } else {
             System.out.println(message);
         }


    }


    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        latch.countDown();
    }





    public void sendMessage(String message) throws IOException {
        session.getRemote().sendString(message);
    }

    public CountDownLatch getLatch() {
        return latch;
    }


    public void closeSession() {
        System.out.println("You are disconnected");
        session.close();
    }

    public Session getSession() {
        return session;
    }

}
