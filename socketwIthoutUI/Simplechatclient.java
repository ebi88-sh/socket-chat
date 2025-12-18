import java.io.*;
import java.net.*;
import java.util.Scanner;

/*
 * SimpleChatClient.java
 * Console-based TCP chat client
 */
public class Simplechatclient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(
                        socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {

            // Thread to receive messages from server
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("Connection closed");
                }
            }).start();

            // Send user input to server
            while (true) {
                String input = scanner.nextLine();
                out.println(input);
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
