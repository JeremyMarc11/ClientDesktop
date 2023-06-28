package Clientdesktop;

import javax.swing.*;

import org.json.JSONObject;

//import UI.ClientDesktop;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AdminUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminUI() {
        setTitle("Admin Login");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        JPanel buttonPanel = new JPanel();

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton loginButton = new JButton("Login");

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Register button action listener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for the Booking button
                RegisteradminUI RegisteruserUI = new RegisteradminUI();
                RegisteruserUI.setVisible(true);
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                try {
                    // Create the JSON object for loginuser data
                    JSONObject loginData = new JSONObject();
                    loginData.put("username", username);
                    loginData.put("password", password);

                    // Create the URL for loginuser endpoint
                    URL url = new URL("http://localhost:7000/loginuser");

                    // Create the HttpURLConnection
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    // Send the loginuser data
                    OutputStream os = connection.getOutputStream();
                    os.write(loginData.toString().getBytes());
                    os.flush();

                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        JOptionPane.showMessageDialog(null, "Login Berhasil!");

                        // Open the main menu
                        openMainMenu();
                    } else {
                        JOptionPane.showMessageDialog(null, "Login Gagal. Response code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while connecting to the JSON server.");
                    ex.printStackTrace();
                }
            }
        });
    }

    private void openMainMenu() {
        dispose();
        // Create the main menu frame
        JFrame mainMenuFrame = new JFrame("Main Menu");
        mainMenuFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        mainMenuFrame.setSize(400, 300);
        mainMenuFrame.setLocationRelativeTo(null);
        mainMenuFrame.setLayout(new GridLayout(6, 1));

        // Create the menu buttons
        JButton listkonserButton = new JButton("List Konser");
        JButton tambahkonserButton = new JButton("Tambah Konser");
        JButton listpesanButton = new JButton("List Pesanan");
        JButton editkonserButton = new JButton("Edit Konser");
        JButton deletekonserButton = new JButton("Delete Konser");
        JButton logoutButton = new JButton("Logout");

        // Add the buttons to the main menu frame
        mainMenuFrame.add(listkonserButton);
        mainMenuFrame.add(tambahkonserButton);
        mainMenuFrame.add(listpesanButton);
        mainMenuFrame.add(editkonserButton);
        mainMenuFrame.add(deletekonserButton);
        mainMenuFrame.add(logoutButton);

        // List konser button action listener
        listkonserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for the List konser button
                ListKonserUI listkonser = new ListKonserUI();
                listkonser.setVisible(true);
            }
        });

        // List pesan button action listener
        listpesanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the action for the List pesan button
                ListPesanUI listbooking = new ListPesanUI();
                listbooking.setVisible(true);
            }
        });

        // Tambah konser Button action listener
        tambahkonserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement the action for the Tambah konser button
                TambahkonserUI TambahkonserUI = new TambahkonserUI();
                TambahkonserUI.setVisible(true);
            }
        });

        // Edit konser Button action listener
        editkonserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement the action for the Edit konser button
                EditKonserUI EditKonserUI = new EditKonserUI();
                EditKonserUI.setVisible(true);
            }
        });

        // Delete konser Button action listener
        deletekonserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement the action for the Delete konser button
                DeleteKonserUI DeleteKonserUI = new DeleteKonserUI();
                DeleteKonserUI.setVisible(true);
            }
        });

        // Logout Button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenuFrame.dispose();
                new AdminUI().setVisible(true);
            }
        });
        // Display the main menu frame
        mainMenuFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AdminUI().setVisible(true);
            }
        });
    }
}
