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

public class TambahkonserUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> genreField;
    private JTextField namakonserField;
    private JTextField tiketbiasaField;
    private JTextField tiketVIPField;


    public TambahkonserUI() {
        setTitle("Tambah Konser");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        JPanel buttonPanel = new JPanel();

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JLabel genreLabel = new JLabel("Genre:");
        genreField = new JComboBox<>(new String[]{"Rock", "Pop", "Jazz", "Klasik"});
        JLabel namakonserLabel = new JLabel("Nama Konser:");
        namakonserField = new JTextField();
        JLabel tiketbiasaLabel = new JLabel("Harga Tiket Biasa:");
        tiketbiasaField = new JTextField();
        JLabel tiketVIPLabel = new JLabel("Harga Tiket VIP:");
        tiketVIPField = new JTextField();
        JButton backButton = new JButton("Back");
        JButton tambahButton = new JButton("Tambahkan");

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
        buttonPanel.add(tambahButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the ListKonserUI window
        });

        tambahButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String genre = genreField.getSelectedItem().toString();
                String namakonser = namakonserField.getText();
                String tiketbiasa = tiketbiasaField.getText();
                String tiketVIP = tiketVIPField.getText();

                try {
                    JSONObject Datakonser = new JSONObject();
                    Datakonser.put("email", email);
                    Datakonser.put("password", password);
                    Datakonser.put("genre", genre);
                    Datakonser.put("namakonser", namakonser);
                    Datakonser.put("tiketbiasa", tiketbiasa);
                    Datakonser.put("tiketVIP", tiketVIP);
                    URL url = new URL("http://localhost:7000/tambahkonser");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the login data
                    OutputStream os = connection.getOutputStream();
                    os.write(Datakonser.toString().getBytes());
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
                            JOptionPane.showMessageDialog(null, "Penambahan berhasil!");
                            openKonserMainMenu();
                        } else {
                            JOptionPane.showMessageDialog(null, "Email atau password salah. Coba lagi.");
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
            public void run() {
                new TambahkonserUI().setVisible(true);
            }
        });
    }
}
