
package ChatBidireccionalSockets;

/**
 *
 * @author YURANY ZURA
 */

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    public Cliente(Socket socket, String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        } catch (IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void close(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Usuario:");
        String username = scanner.nextLine();
        System.out.println("IP del servidor:");
        String ipServer = scanner.nextLine();
        System.out.println("Puerto:");
        int port = scanner.nextInt();
        Socket socket = new Socket(ipServer, port);
        Cliente cliente = new Cliente(socket, username);
        cliente.listenForMessage();
        cliente.sendMessage();
    }

    public void sendMessage() {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(username + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = bufferedReader.readLine();
                        if (messageFromGroupChat == null || messageFromGroupChat.equalsIgnoreCase(username + ": chao")) {
                            close(socket, bufferedReader, bufferedWriter);
                            System.exit(1);
                        }
                        System.out.println(messageFromGroupChat);

                    } catch (IOException e) {
                        close(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
}

