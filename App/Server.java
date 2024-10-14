package App;

import java.awt.datatransfer.Clipboard;
import java.io.IO;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    private Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    private void startServer () {

        Socket socket;
        try {

            while (!serverSocket.isClosed()) {
                socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler (socket);
                new Thread(clientHandler).start();
            }
        }
        catch (IOException e) {
            try {
                serverSocket.close();
            }
            catch (IOException e1) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket( 1334);
        Server server = new Server(serverSocket);
        server.startServer ();
    }
}
