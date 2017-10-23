package org.abondar.experimental.wschat.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abondar.experimental.wschat.server.Message;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import java.net.URI;
import java.util.Scanner;

public class ChatClientRun {


    public static void main(String[] args) {
        String dest = "ws://127.0.0.1:8080/chat";

        WebSocketClient wsClient = new WebSocketClient();
        ClientSocket socket = new ClientSocket();
        try {

            wsClient.start();
            URI echoURI = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            wsClient.connect(socket, echoURI, request);
            socket.getLatch().await();


            ObjectMapper mapper = new ObjectMapper();
            Scanner in = new Scanner(System.in);
            String username = "";
            while (true) {

                String text = in.nextLine();
                if (text.contains("Username:")) {
                    username = text.substring("Username:".length(), text.length());
                    socket.sendMessage(text);
                } else if (text.equals("cs")) {
                    socket.closeSession();
                    break;
                } else {
                    String[] data = text.split(",");
                    Message mesg = new Message(username, data[0], data[1]);
                    String msg = mapper.writeValueAsString(mesg);
                    socket.sendMessage(msg);
                }

            }

            Thread.sleep(Long.MAX_VALUE);

        } catch (Throwable t) {

            t.printStackTrace();
        } finally {
            try {
                wsClient.stop();

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
