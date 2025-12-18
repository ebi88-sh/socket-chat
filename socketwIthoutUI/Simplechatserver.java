import java.io.*;
import java.net.*;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * SimpleChatServer.java
 * Console-based TCP chat server
 * Handles multiple clients using a thread pool
 */
public class Simplechatserver {

    private static final int PORT = 5000;
    private static final Set<ClientTask> clients =
            ConcurrentHashMap.newKeySet();
    private static final ExecutorService pool =
            Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        System.out.println("Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {

            while (true) {
                Socket socket = serverSocket.accept();
                ClientTask task = new ClientTask(socket);
                clients.add(task);
                pool.execute(task);
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }

    static void broadcast(String message) {
        for (ClientTask client : clients) {
            client.send(message);
        }
    }

    static void remove(ClientTask client) {
        clients.remove(client);
    }

    // Handles a single connected client
    static class ClientTask implements Runnable {

        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        ClientTask(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String message;
                while ((message = in.readLine()) != null) {
                    broadcast(message);
                }

            } catch (IOException e) {
                System.out.println("Client disconnected");
            } finally {
                remove(this);
                try {
                    socket.close();
                } catch (IOException ignored) {}
            }
        }

        void send(String message) {
            out.println(message);
        }
    }
}
