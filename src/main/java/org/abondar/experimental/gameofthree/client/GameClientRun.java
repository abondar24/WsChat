package org.abondar.experimental.gameofthree.client;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Scanner;

public class GameClientRun {
    private static Logger logger = LoggerFactory.getLogger(GameClientRun.class);

    private static String playerName;
    public static void main(String[] args) {
        BasicConfigurator.configure();
        String dest="ws://127.0.0.1:8080/gameThree";

        WebSocketClient wsClient = new WebSocketClient();

        try{
            GameClientSocket socket = new GameClientSocket();
            wsClient.start();
            URI echoURI = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            wsClient.connect(socket,echoURI,request);
            socket.getLatch().await();

            System.out.println("Enter your name");
            Scanner in = new Scanner(System.in);

            playerName = in.next();
            socket.sendMessage("Name:"+playerName);

              //  System.out.println("Enter another name");
             //   playerName = in.next();
            System.out.println(GameSessionClient.getInstance().lastMessage);
               // socket.sendMessage("Name:"+playerName);


            Thread.sleep(Long.MAX_VALUE);


        } catch (Throwable t){

            logger.error(t.getMessage());
        } finally {
            try {
                wsClient.stop();
            } catch (Exception e){
                logger.error(e.getMessage());
            }

        }


    }
}
