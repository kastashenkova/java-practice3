package org.example.network;

import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
     public static void main(String[] args) {
          try (ServerSocket serverSocket = new ServerSocket(8089)) {
            while (true) {
                Socket socket = serverSocket.accept();
            }
          } catch (Exception e) {
            throw new RuntimeException("Error accepting connection", e);
          }
    }
}