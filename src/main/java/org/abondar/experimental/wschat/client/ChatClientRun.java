package org.abondar.experimental.wschat.client;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class ChatClientRun {

    private static Logger logger = LoggerFactory.getLogger(ChatClientRun.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        String dest = "ws://127.0.0.1:8080/chat";

        WebSocketClient wsClient = new WebSocketClient();
        ChatClientSocket socket = new ChatClientSocket();
        try {

            wsClient.start();
            URI echoURI = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            wsClient.connect(socket, echoURI, request);
            socket.getLatch().await();

            Thread.sleep(Long.MAX_VALUE);

        } catch (Throwable t) {
                    logger.error(t.getMessage());

        } finally {
             try {
                wsClient.stop();

            } catch (Exception e) {
                logger.error(e.getMessage());

            }

        }
    }
}
