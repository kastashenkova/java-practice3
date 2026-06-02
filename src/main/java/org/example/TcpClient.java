package org.example;

import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 3333)) {
            System.out.println("Connected to " + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            throw new RuntimeException("Error while trying to connect to the server", e);
        }
    }
}
