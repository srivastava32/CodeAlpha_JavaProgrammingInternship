import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class ChatBot {

    String getResponse(String input) {

        input = input.toLowerCase().trim();

        if (input.contains("hello") ||
            input.contains("hi") ||
            input.contains("hey")) {

            return "Hello! How can I help you today?";
        }

        else if (input.contains("your name")) {
            return "I am JavaBot, your virtual assistant.";
        }

        else if (input.contains("java")) {
            return "Java is an object-oriented programming language.";
        }

        else if (input.contains("oops") ||
                 input.contains("object oriented")) {
            return "OOP has four main concepts: Encapsulation, " +
                   "Inheritance, Polymorphism and Abstraction.";
        }

        else if (input.contains("college")) {
            return "College is a place for learning and developing skills.";
        }

        else if (input.contains("time")) {
            return "You can check the current time on your device.";
        }

        else if (input.contains("how are you")) {
            return "I am doing great! Thanks for asking.";
        }

        else if (input.contains("help")) {
            return "You can ask me about Java, OOP, college, or greetings.";
        }

        else if (input.contains("thank")) {
            return "You're welcome!";
        }

        else if (input.contains("bye")) {
            return "Goodbye! Have a great day.";
        }

        else if (input.contains("what is") ||
                 input.contains("define")) {
            return "I am still learning about that topic. " +
                   "Try asking about Java or OOP.";
        }

        else {
            return "Sorry, I don't understand that yet. " +
                   "Please try another question.";
        }
    }
}

public class AIChatbot extends JFrame {

    JTextArea chatArea;
    JTextField inputField;

    ChatBot bot = new ChatBot();

    AIChatbot() {

        setTitle("Java AI Chatbot");
        setSize(600, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("JAVA AI CHATBOT",
                JLabel.CENTER);

        title.setFont(new Font("Arial", Font.BOLD, 25));

        add(title, BorderLayout.NORTH);

        chatArea = new JTextArea();

        chatArea.setEditable(false);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 16));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(chatArea);

        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));

        inputField = new JTextField();

        JButton sendButton = new JButton("Send");

        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(e -> sendMessage());

        inputField.addActionListener(e -> sendMessage());

        chatArea.append("Bot: Hello! I am JavaBot.\n");
        chatArea.append("Bot: Ask me anything about Java!\n\n");

        setVisible(true);
    }

    void sendMessage() {

        String message = inputField.getText().trim();

        if (message.isEmpty()) {
            return;
        }

        chatArea.append("You: " + message + "\n");

        String response = bot.getResponse(message);

        chatArea.append("Bot: " + response + "\n\n");

        inputField.setText("");

        chatArea.setCaretPosition(
                chatArea.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AIChatbot::new);
    }
}
