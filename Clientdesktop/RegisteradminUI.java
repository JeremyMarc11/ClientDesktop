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

public class RegisteradminUI extends JFrame {
    private JTextField adminnameField;
    private JTextField emailadminField;
    private JPasswordField passwordField;

    public RegisteradminUI() {
        setTitle("Register Account");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        JPanel buttonPanel = new JPanel();

        JLabel adminnameLabel = new JLabel("Username:");
        adminnameField = new JTextField();
        JLabel emailadminLabel = new JLabel("Email:");
        emailadminField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton backButton = new JButton("Back");
        JButton registerButton = new JButton("Register");

        panel.add(adminnameLabel);
        panel.add(adminnameField);
        panel.add(emailadminLabel);
        panel.add(emailadminField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(registerButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the RegisteradminUI window
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminname = adminnameField.getText();
                String emailadmin = emailadminField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Create the JSON object for register data
                    JSONObject registerData = new JSONObject();
                    registerData.put("adminname", adminname);
                    registerData.put("emailadmin", emailadmin);
                    registerData.put("password", password);

                    // Create the URL for register endpoint
                    URL url = new URL("http://localhost:7000/registeradmin");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the register data
                    OutputStream os = connection.getOutputStream();
                    os.write(registerData.toString().getBytes());
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
                        if (response.equals("Register Berhasil")) {
                            JOptionPane.showMessageDialog(null, "Register berhasil!");
                            // Open the login UI
                            openLoginUI();
                        } else {
                            JOptionPane.showMessageDialog(null, "Register gagal. Coba lagi.");
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

    private void openLoginUI() {
        dispose();

        // Add your code here to open the login UI
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new RegisteradminUI().setVisible(true);
            }
        });
    }
}
