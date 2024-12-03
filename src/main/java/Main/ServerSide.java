package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerSide {

    static ExecutorService executorService;
    static final int PORT = 8008;

    public static void main(String[] args) {


        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            executorService = Executors.newFixedThreadPool(3);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                executorService.submit(new Client(clientSocket));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }
    }
}