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

public class DeleteKonserUI extends JFrame {
    private JTextField namakonserField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public DeleteKonserUI() {
        setTitle("Delete Konser");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));
        JPanel buttonPanel = new JPanel();

        JLabel namakonserLabel = new JLabel("Nama Konser:");
        namakonserField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton backButton = new JButton("Back");
        JButton cancelButton = new JButton("Delete");

        panel.add(namakonserLabel);
        panel.add(namakonserField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the DeleteKonserUI window
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String namakonser = namakonserField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Create the JSON object for delete konser data
                    JSONObject cancelData = new JSONObject();
                    cancelData.put("namakonser", namakonser);
                    cancelData.put("email", email);
                    cancelData.put("password", password);

                    // Create the URL for delete konser endpoint
                    URL url = new URL("http://localhost:7000/batalkonser");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the delete konser data
                    OutputStream os = connection.getOutputStream();
                    os.write(cancelData.toString().getBytes());
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
                        if (response.equals("Nama konser salah. Coba lagi.")) {
                            JOptionPane.showMessageDialog(null, "Nama konser salah. Coba lagi.");
                            opendeletekonserMainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Berhasil menghapus konser!");
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

    private void opendeletekonserMainMenu() {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DeleteKonserUI().setVisible(true);
            }
        });
    }
}
