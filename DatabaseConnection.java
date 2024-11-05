package com.mycompany.test4;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

public class DatabaseConnection {
   private Connection connection;
    private final String databaseURL;
    private final String username;
    private final String password;
    
    public DatabaseConnection() {
        this.databaseURL = "jdbc:mysql://localhost:3306/railway_db";
        this.username = "root";
        this.password = "Swift26";
    }
    
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(databaseURL, username, password);
        System.out.println("Connected to database.");
    }
    
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    
    public void insertItem(String name,String password) throws SQLException {
        String insertItemSQL = "INSERT INTO user (name,  password) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertItemSQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();
        }
    }
    
    public void insertTicket(String name, int age,   int phone, String src, String dest, String gender) throws SQLException {
        String insertItemSQL = "INSERT INTO ticketbooking (name, age, phone, src, dest, gender) VALUES (?, ?, ?,?,?,?)";

        try (PreparedStatement preparedStatement2 = connection.prepareStatement(insertItemSQL)) {
            preparedStatement2.setString(1, name);
            preparedStatement2.setInt(2, age);
            preparedStatement2.setInt(3, phone);
            preparedStatement2.setString(4, src);
            preparedStatement2.setString(5, dest);
            preparedStatement2.setString(6, gender);
            preparedStatement2.executeUpdate();
        }
    }    
    
        // Method to call the cancel_ticket_by_info procedure
    public void callCancelTicketProcedure(String name, String src, String dest) throws SQLException {
        String callProcedureSQL = "{CALL cancel_ticket_by_info(?, ?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(callProcedureSQL)) {
            callableStatement.setString(1, name);
            callableStatement.setString(2, src);
            callableStatement.setString(3, dest);

            callableStatement.executeUpdate();
        }
    }
    
    public DefaultTableModel fetchAndDisplayTickets() throws SQLException {
        String sql = "SELECT name, phone, src, dest FROM ticketbooking";
        DefaultTableModel tm = new DefaultTableModel(new String[]{"Name", "Phone No", "Departure", "Arrival"}, 0);

        try {
            connect();

            PreparedStatement pst3 = connection.prepareStatement(sql);

            ResultSet rs = pst3.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("src"),
                    rs.getString("dest")
                };
                tm.addRow(row);
            }

            rs.close();
            pst3.close();
        } finally {
            disconnect();
        }

        return tm;
    }
    
    public void insertTrain(String tname, String tsrc, String tdest) throws SQLException {
        String insertItemSQL = "INSERT INTO train (train_name, source, destination) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement2 = connection.prepareStatement(insertItemSQL)) {
            preparedStatement2.setString(1, tname);
            preparedStatement2.setString(2, tsrc);
            preparedStatement2.setString(3, tdest);
            preparedStatement2.executeUpdate();
        }
    }
    
    public void deleteTrain(String tname, String tsrc, String tdest) throws SQLException {
        String deleteTrainSQL = "DELETE FROM train WHERE train_name = ? AND source = ? AND destination = ?";

        try (PreparedStatement preparedStatement3 = connection.prepareStatement(deleteTrainSQL)) {
            preparedStatement3.setString(1, tname);
            preparedStatement3.setString(2, tsrc);
            preparedStatement3.setString(3, tdest);
            preparedStatement3.executeUpdate();
        }
    }
    
    public DefaultTableModel trainview() throws SQLException {
        String sql = "SELECT train_name,source,destination FROM train";
        DefaultTableModel tm2 = new DefaultTableModel(new String[]{"Train Name", "Start Location", "End Location"}, 0);

        try {
            connect();
            PreparedStatement pst4 = connection.prepareStatement(sql);
            ResultSet rs = pst4.executeQuery();

            while (rs.next()) {
                Object[] row = {
                    rs.getString("train_name"),
                    rs.getString("source"),
                    rs.getString("destination")
                };
                tm2.addRow(row);
            }

            rs.close();
            pst4.close();
        } finally {
            disconnect();
        }

        return tm2;
    }
}
