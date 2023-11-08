/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ChatBidireccionalSockets;

import java.io.IOException;
import java.net.*;

/**
 *
 * @author YURANY ZURA
 */
public class Servidor {

    private ServerSocket serverSocket;
    
    private Servidor (ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    
    public static void main(String[] args) throws IOException{
        System.out.println("Esperando conexion de un cliente");
        ServerSocket serverSocket = new ServerSocket(8000);
        Servidor Servidor = new Servidor(serverSocket);
        Servidor.starServidor();
    } 
    
    public void starServidor(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException exception){
            System.out.println("El cliente cerro conexion");
        }
    }
}
