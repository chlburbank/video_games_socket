package Main;

import java.net.Socket;

public class Client implements Runnable {

    private Socket clientSocket;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("Se ha conectado un cliente: " + clientSocket.getInetAddress());
        try {
            clientSocket.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
