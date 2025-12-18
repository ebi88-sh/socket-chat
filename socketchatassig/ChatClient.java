import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;

/**
 * Swing-based TCP Chat Client
 * - Connects to server using Socket
 * - Sends messages to server
 * - Receives messages asynchronously
 */
public class ChatClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private JTextArea chatArea;
    private JTextField messageField;
    private JTextField usernameField;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }

    public ChatClient() {
        createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("TCP Socket Chat Client");
        frame.setSize(400, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);

        messageField = new JTextField();
        usernameField = new JTextField();

        JButton connectButton = new JButton("Connect");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Username:"), BorderLayout.WEST);
        topPanel.add(usernameField, BorderLayout.CENTER);
        topPanel.add(connectButton, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.add(messageField, BorderLayout.SOUTH);

        connectButton.addActionListener(e -> connect());
        messageField.addActionListener(e -> sendMessage());

        frame.setVisible(true);
    }

    private void connect() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            String username = usernameField.getText();
            out.println(username);

            chatArea.append("Connected as " + username + "\n");

            // Thread to receive messages from server
            new Thread(this::receiveMessages).start();

        } catch (IOException e) {
            chatArea.append("Failed to connect to server\n");
        }
    }

    private void sendMessage() {
        if (out != null) {
            out.println(messageField.getText());
            messageField.setText("");
        }
    }

    private void receiveMessages() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                chatArea.append(message + "\n");
            }
        } catch (IOException ignored) {}
    }
}
