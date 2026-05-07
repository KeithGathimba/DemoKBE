package main.java.org.acme;

import io.quarkus.websockets.next.OnOpen;
import io.quarkus.websockets.next.OnTextMessage; // NEU!
import io.quarkus.websockets.next.WebSocket;

@WebSocket(path = "/live")
public class LiveSocket {

    @OnOpen
    public String onOpen() {
        System.out.println("Neuer Client verbunden!");
        return "Verbindung zum Live-Shop steht (via WebSockets Next)!";
    }

    // NEU: Diese Funktion reagiert, wenn du im Frontend den Button klickst
    @OnTextMessage
    public String onMessage(String message) {
        System.out.println("Nachricht vom Frontend empfangen: " + message);

        // Wir schicken eine Antwort direkt an das Frontend zurück
        return "Backend bestätigt den Empfang von: '" + message + "'";
    }
}