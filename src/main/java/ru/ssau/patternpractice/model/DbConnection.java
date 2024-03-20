package ru.ssau.patternpractice.model;

import java.io.*;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

public class DbConnection {
    private static DbConnection INSTANCE;

    private Connection connection;

    private Properties props;

    private String fileProperties;

    public DbConnection() throws SQLException {
        this("application.properties");
    }
    public DbConnection(Connection connection) {
        this.connection = connection;
    }

    public DbConnection(String fileProperties) throws SQLException {
        this.props = new Properties();
        setDatabaseConfig(fileProperties);
    }

    public void setDatabaseConfig(String fileProperties) throws SQLException {
        if (connection != null) {
            this.closeConnection();
        }
        this.fileProperties = fileProperties;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileProperties)) {
            props.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectToServer();
    }

    public void connectToServer() throws SQLException {
        connection = DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password")); //ПОМЕНЯТЬ БД
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    public Properties getProps() { return this.props; }


    public void changeUsernameProperty(String username) throws SQLException {
        if (connection != null) closeConnection();
        props.setProperty("db.user", username);
        connectToServer();
    }

    public void changePasswordProperty(String password) throws SQLException {
        if (connection != null) closeConnection();
        props.setProperty("db.password", password);
        connectToServer();
    }

    public void changeURL(String url) throws SQLException {
        if (connection != null) closeConnection();
        props.setProperty("db.url", url);
        connectToServer();
    }

    public PreparedStatement getPreparedStatement(String sqlQuery) throws SQLException {
        return connection.prepareStatement(sqlQuery);
    }

    public void changeProps(Properties props) throws SQLException {
        if (connection != null) closeConnection();
        this.props = props;
        connectToServer();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DbConnection that = (DbConnection) o;
        return Objects.equals(props, that.props) && Objects.equals(fileProperties, that.fileProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(props, fileProperties);
    }

    public void deleteInstance() {
        INSTANCE = null;
    }
}
