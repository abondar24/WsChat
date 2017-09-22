package org.abondar.experimental.gameofthree.client;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class GameOfThreeClientRun {
    private static Logger logger = LoggerFactory.getLogger(GameOfThreeClientRun.class);

    public static void main(String[] args) {
        String dest="ws://127.0.0.1:8080/gameThree";
        BasicConfigurator.configure();
        WebSocketClient wsClient = new WebSocketClient();

        try{
            GameOfThreeClient client = new GameOfThreeClient();
            wsClient.start();
            URI echoURI = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            wsClient.connect(client,echoURI,request);
            client.getLatch().await();
            client.sendMessage("echo");
            client.sendMessage("test");
            Thread.sleep(10000l);

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
