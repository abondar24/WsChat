package org.abondar.experimental.gameofthree.server;

import java.util.ArrayList;
import java.util.List;


public class GameSession {

    private static final GameSession instance = new GameSession();


    public List<GameSocket> players = new ArrayList<>();

    private List<String> names = new ArrayList<>();

    public volatile int initialNumber = 0;

    public static GameSession getInstance() {
        return instance;
    }

    public boolean isPlayersEmpty(){
        return players.isEmpty();
    }

    public void join(GameSocket socket) {
        players.add(socket);
    }


    public void remove(GameSocket socket) {
        players.remove(socket);
    }

    public void addName(String name) {
       names.add(name);
    }

    public Boolean checkName(String name){
        return names.contains(name);
    }


    public void removeName(String name) {
        names.remove(name);
    }


    public void writeAllPlayers(String msg) {
        for (GameSocket player : players) {
            player.session.getRemote().sendStringByFuture(msg);
        }
    }

    public void writeSpecificPlayer(String memberName, String message) {
        GameSocket player = findPlayerByName(memberName);
        player.session.getRemote().sendStringByFuture(message);
    }

    public GameSocket findPlayerByName(String memberName) {
        GameSocket res = new GameSocket();
        for (GameSocket player : players) {
            if (player.playerName.equals(memberName)) {
                res = player;
            }
        }

        return res;
    }

}
