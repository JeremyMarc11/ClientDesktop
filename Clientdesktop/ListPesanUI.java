package Clientdesktop;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;

public class ListPesanUI extends JFrame {
    private JTable table;
    private JScrollPane scrollPane;

    public ListPesanUI() {
        setTitle("List Pesanan");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();

        // Create the table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Kode Tiket");
        tableModel.addColumn("Nama Lengkap");
        tableModel.addColumn("Nomor HP");
        tableModel.addColumn("Nama Konser");
        tableModel.addColumn("Jenis Tiket");

        JButton backButton = new JButton("Back");
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            dispose(); // Close the ListPesanUI window
        });

        // Fetch booking data from the server
        try {
            URL url = new URL("http://localhost:7000/listpesan");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

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

                // Parse the JSON response
                JSONObject jsonObject = new JSONObject(response);
                JSONArray dataArray = jsonObject.getJSONArray("response");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject pesan = dataArray.getJSONObject(i);
                    int kodetiket = pesan.getInt("kodetiket");
                    String namalengkap = pesan.getString("namalengkap");
                    String nomorhp = pesan.getString("nomorhp");
                    String namakonser = pesan.getString("namakonser");
                    String jenistiket = pesan.getString("jenistiket");

                    // Add the data to the table model
                    tableModel.addRow(new Object[]{kodetiket, namalengkap, nomorhp, namakonser, jenistiket});
                }
            } else {
                System.out.println("Failed to connect to the server. Response code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception ex) {
            System.out.println("Error occurred while connecting to the server.");
            ex.printStackTrace();
        }

        // Create the JTable and set the model
        table = new JTable(tableModel);

        // Create a scroll pane and add the table to it
        scrollPane = new JScrollPane(table);

        // Add the scroll pane to the frame
        add(scrollPane, BorderLayout.CENTER);
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ListPesanUI().setVisible(true);
            }
        });
    }
}
