package application;

import java.sql.*;

public class MySQLConnection {
    private Connection connect = null;
    private static MySQLConnection instance;
    /*
    create table program(
        id INT NOT NULL AUTO_INCREMENT,
        name VARCHAR(100),
        PRIMARY KEY (id));

    create table node(
        id INT NOT NULL AUTO_INCREMENT,
        program_id INT,
        contained_text VARCHAR(100),
        source VARCHAR(4000),
        referenceID VARCHAR(100),
        PRIMARY KEY (id));

        insert into node values (default, 1,'YAY', 'hello','s1');
    */

    public MySQLConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //connect = DriverManager.getConnection("jdbc:mysql://localhost:13390/sde?user=spiralinks&password=spiralinks");
            connect = DriverManager.getConnection("jdbc:mysql://172.16.10.213/sde?user=spiralinks&password=spiralinks");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        instance = this;
    }

    public Boolean isConnected() {
        if (connect == null) {
            return false;
        }
        try {
            return !connect.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ResultSet runQuery(String query) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public PreparedStatement getPreparedStatement(String sql) {
        try {
            return connect.prepareStatement(sql);

            //statements allow to issue SQL queries to the database

            // preparedStatements can use variables and are more efficient
//            PreparedStatement preparedStatement =
//
//            preparedStatement.setString(1, "Test");
//            preparedStatement.setString(2, "TestEmail");
//            preparedStatement.setString(3, "TestWebpage");
//            preparedStatement.setDate(4, new java.sql.Date(2009, 12, 11));
//            preparedStatement.setString(5, "TestSummary");
//            preparedStatement.setString(6, "TestComment");
//            preparedStatement.executeUpdate();
//
//            preparedStatement = connect.getPreparedStatement("SELECT myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            // remove again the insert comment
//            preparedStatement = connect.getPreparedStatement("delete from FEEDBACK.COMMENTS where myuser= ? ; ");
//            preparedStatement.setString(1, "Test");
//            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void writeMetaData(ResultSet resultSet) throws SQLException {
        // now get some metadata from the database
        System.out.println("The columns in the table are: ");
        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            System.out.println("Column " + i + " " + resultSet.getMetaData().getColumnName(i));
        }
    }

    private void writeResultSet(ResultSet resultSet) throws SQLException {
        // resultSet is initialised before the first data set
        while (resultSet.next()) {
            // it is possible to get the columns via name
            // also possible to get the columns via the column number
            // which starts at 1
            // e.g., resultSet.getSTring(2);
            Integer id = resultSet.getInt("id");
            System.out.println("id: " + id);
        }
    }

    // you need to close all three to make sure
    private void close() {
        //close(resultSet);
        //close(statement);
        close(connect);
    }

    private void close(AutoCloseable c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            // don't throw now as it might leave following closables in undefined state
        }
    }

    public static MySQLConnection getInstance() {
        return instance;
    }
} 