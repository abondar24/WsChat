package org.abondar.experimental.wschat.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatRoom {

    private static final ChatRoom instance = new ChatRoom();


    public Map<String, ChatSocket> clientUser = new HashMap<>();

    public List<ChatSocket> clients = new ArrayList<>();


    private ObjectMapper mapper = new ObjectMapper();

    public static ChatRoom getInstance() {
        return instance;
    }

    public boolean isRoomEmpty() {
        return clients.isEmpty();
    }

    public void join(ChatSocket socket) {
        clients.add(socket);
    }


    public void remove(ChatSocket socket) {
        clients.remove(socket);
    }

    public void addUsername(String name, ChatSocket client) {
        clientUser.put(name, client);
    }

    public Boolean checkUsername(String name) {
        return clientUser.containsKey(name);
    }


    public void removeName(String name) {
        clientUser.remove(name);
    }


    public void writeAllUsers(Message msg) throws JsonProcessingException {
        for (ChatSocket client : clients) {

            String message = mapper.writeValueAsString(msg);
            client.session.getRemote().sendStringByFuture(message);
        }
    }

    public void writeSpecificUser(Message msg) throws JsonProcessingException {
        ChatSocket client = findClientByUsername(msg.getRecipient());
        String message = mapper.writeValueAsString(msg);
        client.session.getRemote().sendStringByFuture(message);
    }

    public ChatSocket findClientByUsername(String username) {
        ChatSocket res = new ChatSocket();

        if (clientUser.containsKey(username)) {
            res = clientUser.get(username);
        }


        return res;
    }

}
