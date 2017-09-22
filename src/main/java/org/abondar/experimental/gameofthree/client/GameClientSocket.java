package org.abondar.experimental.gameofthree.client;


import com.sun.org.apache.regexp.internal.RE;
import org.abondar.experimental.gameofthree.server.ResponseUtil;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

@WebSocket
public class GameClientSocket {
    private Logger logger = LoggerFactory.getLogger(GameClientSocket.class);
    private Session session;

    private CountDownLatch latch = new CountDownLatch(1);

    @OnWebSocketMessage
    public void onText(Session session, String message) throws IOException{
        logger.info("Message from server: " + message);

        if (message.equals(ResponseUtil.CONNECTED)){
            System.out.println("Enter player name");
            String playerName=GameSessionClient.getInstance().in.next();
            session.getRemote().sendString("Name:"+playerName);
        }

        if (message.equals(ResponseUtil.PLAYER_EXISTS)){
            System.out.println("Enter another name");
            String newName=GameSessionClient.getInstance().in.next();
            session.getRemote().sendString(newName);
        }

    }



    @OnWebSocketConnect
    public void onConnect(Session session){
        logger.info("Connected to server");
        this.session=session;
        latch.countDown();
    }

    public void sendMessage(String str){
        try {
            session.getRemote().sendString(str);
        } catch (IOException e){
            logger.error(e.getMessage());
        }

    }

    public CountDownLatch getLatch(){
        return latch;
    }
}
