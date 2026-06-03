package org.example.network;

import java.net.ServerSocket;
import java.io.IOException;

public class TcpServer {
     public static void main(String[] args) {
          try (ServerSocket serverSocket = new ServerSocket(8089)) {
            System.out.println("Server started on port " + 8089);
            while (true) {
                serverSocket.accept();
            }
          } catch (IOException e) {
            throw new RuntimeException("Error accepting connection", e);
          }
    }
}
