import java.io.*;
import java.net.*;

public class ChatClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 5002;

        try (Socket socket = new Socket(serverAddress, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server. Type messages to send:");

            // Thread for reading messages from the server
            Thread readThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println("Server: " + serverMessage);
                            if (serverMessage.equalsIgnoreCase("exit")) break;
                        }
                    } catch (IOException e) {
                        System.out.println("Read error: " + e.getMessage());
                    }
                }
            });

            // Thread for writing messages to the server
            Thread writeThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        String clientMessage;
                        while (true) {
                            clientMessage = userInput.readLine();
                            out.println(clientMessage);
                            if (clientMessage.equalsIgnoreCase("exit")) break;
                        }
                    } catch (IOException e) {
                        System.out.println("Write error: " + e.getMessage());
                    }
                }
            });

            readThread.start();
            writeThread.start();

            readThread.join();
            writeThread.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}