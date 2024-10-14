package App;

import org.w3c.dom.css.CSSStyleRule;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private String username;
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    private Client(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }
        catch (IOException e) {
            closeLink();
        }

    }

    private void readMessages() {

        new Thread(() -> {

            String inMessage;
            while (socket.isConnected()) {
                try {
                    inMessage = reader.readLine();
                    System.out.println(inMessage);
                }
                catch (IOException e) {
                    closeLink();
                }
            }

        }).start();

    }
    private void sendMessage() {
        while (socket.isConnected()) {
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            }
            catch (IOException e) {
                closeLink();
            }

        }
    }

    private void closeLink () {

        try {
            socket.close();
            reader.close();
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException{
        Client client = new Client(new Socket("192.168.0.242", 1334));
        client.readMessages();
        client.sendMessage();
    }
}
