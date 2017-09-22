package org.abondar.experimental.gameofthree.client;


public class GameSessionClient {

    private static final GameSessionClient instance = new GameSessionClient();

    public volatile String lastMessage;

    public static GameSessionClient getInstance() {
        return instance;
    }


}
