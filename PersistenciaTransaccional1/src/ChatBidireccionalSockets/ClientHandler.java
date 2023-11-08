
package ChatBidireccionalSockets;

/**
 *
 * @author YURANY ZURA
 */

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = bufferedReader.readLine();
            clientHandlers.add(this);
            System.out.println(clientUsername + " ha entrado al chat");
            broadcastMessage("SERVER: " + clientUsername + " ha entrado al chat");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void run() {
        String messageFromClient;

        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.equalsIgnoreCase(clientUsername + ": chao")) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                    break;
                }
                broadcastMessage(messageFromClient);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        System.out.println(clientUsername + " ha abandonado el chat");
        broadcastMessage("SERVER: " + clientUsername + " ha abandonado el chat");
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
        try {
            if (socket != null) {
                socket.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}