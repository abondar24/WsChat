package org.abondar.experimental.wschat.server;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class ChatServlet extends WebSocketServlet {


    @Override
    public void configure(WebSocketServletFactory factory) {
        BasicConfigurator.configure();

        factory.register(ServerSocket.class);

    }
}
