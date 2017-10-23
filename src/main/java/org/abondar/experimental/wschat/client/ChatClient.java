package org.abondar.experimental.wschat.client;

import java.util.Scanner;

public class ChatClient {

    private static final ChatClient instance = new ChatClient();
    public volatile Scanner in = new Scanner(System.in);

    public static ChatClient getInstance() {
        return instance;

    }

    public Scanner getIn() {
        return in;

    }

    public void setIn(Scanner in) {
        this.in = in;

    }


}
