import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ChatServer
 * -----------
 * - Listens for client connections using ServerSocket (TCP)
 * - Handles multiple clients using a thread pool (ExecutorService)
 * - Broadcasts messages to all connected clients
 */
public class Chatserver {

    private static final int PORT = 5000;

    // Thread pool to handle clients concurrently
    private static final ExecutorService threadPool =
            Executors.newFixedThreadPool(10);

    // Thread-safe collection of connected clients
    private static final Set<ClientHandler> clients =
            ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {

        System.out.println("Chat Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                // Wait for a client connection
                Socket socket = serverSocket.accept();

                // Create handler for the connected client
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);

                // Assign client to a thread
                threadPool.execute(handler);
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    // Sends a message to all connected clients
    private static void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    // Handles communication with a single client
    private static class ClientHandler implements Runnable {

        private final Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                // Create input and output streams
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(
                        socket.getOutputStream(), true);

                // First message from client is the username
                username = in.readLine();
                broadcast("System: " + username + " joined the chat");

                // Read and broadcast client messages
                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(username + ": " + message);
                }

            } catch (IOException e) {
                System.out.println(username + " disconnected");
            } finally {
                // Remove client and notify others
                clients.remove(this);
                broadcast("System: " + username + " left the chat");

                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        void sendMessage(String message) {
            out.println(message);
        }
    }
}
