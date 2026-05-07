package main.java;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.WebSocket;

@WebSocket(path = "/live")
public class LiveSocket {

    @OnOpen
    public String onOpen() {
        System.out.println("Neuer Client verbunden!");
        return "Verbindung zum Live-Shop steht (via WebSockets Next)!";
    }

}