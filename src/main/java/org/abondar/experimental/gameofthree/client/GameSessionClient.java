package org.abondar.experimental.gameofthree.client;


import java.util.Scanner;

public class GameSessionClient {

    private static final GameSessionClient instance = new GameSessionClient();

    public volatile   Scanner in = new Scanner(System.in);

    private    String playerName;

    public static GameSessionClient getInstance() {
        return instance;
    }


    public Scanner getIn() {
        return in;
    }

    public void setIn(Scanner in) {
        this.in = in;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
