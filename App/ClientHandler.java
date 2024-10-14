package App;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable{

    private static List<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientUsername;


    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            clientHandlers.add(this);
            writer.write("Enter your username for this chat : ");
            writer.newLine();
            writer.flush();
            this.clientUsername = reader.readLine();
            System.out.println(this.clientUsername + " has joined the chat");
            broadcastMessage ("SERVER > " + this.clientUsername + " has joined the chat");
        }
        catch (IOException e) {
            closeLink();
        }
    }

    private void broadcastMessage (String message)  {

        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler != this) {
                try {
                    clientHandler.writer.write(message);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
                catch (IOException e) {
                    closeLink();
                }
            }
        }
    }

    private void closeLink () {
        try {
            socket.close();
            writer.close();
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeClientHandler () {
        clientHandlers.remove(this);
        System.out.println(this.clientUsername + " has left the chat");
        broadcastMessage(this.clientUsername + " has left the chat");
    }

    @Override
    public void run () {

        String message;
        while (socket.isConnected()) {

            try {
                message = reader.readLine();
                broadcastMessage(this.clientUsername + " : " + message);
            }
            catch (IOException e) {
                removeClientHandler();
                closeLink();
                break;
            }
        }

    }
}
