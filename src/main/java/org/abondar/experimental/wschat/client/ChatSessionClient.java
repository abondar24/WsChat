package org.abondar.experimental.wschat.client;

import java.util.Scanner;

public class ChatSessionClient {

    private static final ChatSessionClient instance = new ChatSessionClient();
    public volatile Scanner in = new Scanner(System.in);

    public static ChatSessionClient getInstance() {
        return instance;

    }

    public Scanner getIn() {
        return in;

    }

    public void setIn(Scanner in) {
        this.in = in;

    }


}
