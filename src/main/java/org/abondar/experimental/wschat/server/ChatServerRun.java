package org.abondar.experimental.wschat.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ChatServerRun {

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        ServletHolder holder = new ServletHolder("ws-chat", ChatServlet.class);

        context.setContextPath("/");
        context.addServlet(holder,"/chat");
        context.setWelcomeFiles(new String[]{"/index.html"});


        ServletHolder staticHolder = new ServletHolder("default",DefaultServlet.class);
        staticHolder.setInitParameter("resourceBase", ChatServerRun.class.getResource("/webapp").toExternalForm());
        context.addServlet(staticHolder, "/");

        server.setHandler(context);

        try {
            server.start();
            server.dump(System.err);
            server.join();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
