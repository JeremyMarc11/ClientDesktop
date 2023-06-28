package Clientdesktop;

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginadminUI extends JFrame {
    private JTextField adminnameField;
    private JPasswordField passwordField;

    public LoginadminUI() {
        setTitle("Login Admin");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        JPanel buttonPanel = new JPanel();

        JLabel adminnameLabel = new JLabel("Username:");
        adminnameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");

        panel.add(adminnameLabel);
        panel.add(adminnameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminname = adminnameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Create the JSON object for login data
                    JSONObject loginData = new JSONObject();
                    loginData.put("adminname", adminname);
                    loginData.put("password", password);

                    // Create the URL for loginuser endpoint
                    URL url = new URL("http://localhost:7000/loginadmin");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the login data
                    OutputStream os = connection.getOutputStream();
                    os.write(loginData.toString().getBytes());
                    os.flush();

                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }
                        String response = responseBuilder.toString();
                        System.out.println("Server Response: " + response);

                        // Show a message dialog based on the response
                        if (response.equals("Login Berhasil")) {
                            JOptionPane.showMessageDialog(null, "Login berhasil!");
                            // Open the main menu for user
                            openadminMainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Username atau password salah. Coba lagi.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to connect to the JSON server. Response code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while connecting to the JSON server.");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void openadminMainMenu() {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginadminUI().setVisible(true);
            }
        });
    }
}
