import java.io.*;
import java.net.*;

class ClientHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Error setting up streams: " + e.getMessage());
        }
    }

    public void run() {
        try {
            System.out.println("Client connected from port: " + clientSocket.getPort());

            // Thread for reading from the client
            Thread readThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        String message;
                        while ((message = in.readLine()) != null) {
                            System.out.println("Client: " + message);
                            if (message.equalsIgnoreCase("exit")) break;
                        }
                    } catch (IOException e) {
                        System.out.println("Read error: " + e.getMessage());
                    }
                }
            });

            // Thread for writing messages to the client
            Thread writeThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                        String serverMessage;
                        while (true) {
                            serverMessage = userInput.readLine();
                            out.println(serverMessage);
                            if (serverMessage.equalsIgnoreCase("exit")) break;
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

            clientSocket.close();
            System.out.println("Client disconnected.");

        } catch (IOException | InterruptedException e) {
            System.out.println("Client handler error: " + e.getMessage());
        }
    }
}

public class ChatServer {
    public static void main(String[] args) {
        int port = 5002;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}