package org.example;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8089)) {
            System.out.println("Connected to " + socket.getInetAddress());
        } catch (ConnectException e) {
            System.err.println("Connection refused, server may be shut down");
        } catch (IOException e) {
            throw new RuntimeException("Error while trying to connect to the server", e);
        }
    }
}
