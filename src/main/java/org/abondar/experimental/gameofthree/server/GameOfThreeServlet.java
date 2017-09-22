package org.abondar.experimental.gameofthree.server;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

@WebServlet(urlPatterns = "/gameThree")
public class GameOfThreeServlet extends WebSocketServlet {


    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        BasicConfigurator.configure();
        webSocketServletFactory.register(GameOfThreeServer.class);
    }
}
