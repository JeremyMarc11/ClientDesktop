package Clientdesktop;

import javax.swing.*;

import org.json.JSONObject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class EditKonserUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> genreField;
    private JTextField namakonserField;
    private JTextField tiketbiasaField;
    private JTextField tiketVIPField;

    public EditKonserUI() {
        setTitle("Edit Konser");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        JPanel buttonPanel = new JPanel();

        JLabel genreLabel = new JLabel("Genre:");
        genreField = new JComboBox<>(new String[]{"Rock", "Pop", "Jazz", "Klasik"});
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel namakonserLabel = new JLabel("Nama Konser:");
        namakonserField = new JTextField();
        JLabel tiketbiasaLabel = new JLabel("Harga Tiket Biasa:");
        tiketbiasaField = new JTextField();
        JLabel tiketVIPLabel = new JLabel("Harga Tiket VIP:");
        tiketVIPField = new JTextField();
        JButton backButton = new JButton("Back");
        JButton editButton = new JButton("Edit");

        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(genreLabel);
        panel.add(genreField);
        panel.add(namakonserLabel);
        panel.add(namakonserField);
        panel.add(tiketbiasaLabel);
        panel.add(tiketbiasaField);
        panel.add(tiketVIPLabel);
        panel.add(tiketVIPField);
        panel.add(new JLabel());
        buttonPanel.add(backButton);
        buttonPanel.add(editButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the EditKonserUI window
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String namakonser = namakonserField.getText();
                String biasa = tiketbiasaField.getText();
                String VIP = tiketVIPField.getText();
                String genre = genreField.getSelectedItem().toString();

                try {
                    // Create the JSON object for editkonser data
                    JSONObject editData = new JSONObject();
                    editData.put("email", email);
                    editData.put("password", password);
                    editData.put("namakonser", namakonser);
                    editData.put("tiketbiasa", biasa);
                    editData.put("tiketVIP", VIP);
                    editData.put("genre", genre);

                    // Create the URL for editkonser endpoint
                    URL url = new URL("http://localhost:7000/editkonser");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PUT");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the editkonser data
                    OutputStream os = connection.getOutputStream();
                    os.write(editData.toString().getBytes());
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

                        if (response.equals("Penambahan Berhasil")) {
                            JOptionPane.showMessageDialog(null, "Nama Konser salah. Coba lagi.");
                            openKonserMainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Edit Konser berhasil!");
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

    private void openKonserMainMenu() {
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {new EditKonserUI().setVisible(true);
            }
        });
    }
}
