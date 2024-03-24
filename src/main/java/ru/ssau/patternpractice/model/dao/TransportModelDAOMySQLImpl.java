package ru.ssau.patternpractice.model.dao;

import ru.ssau.patternpractice.model.DbConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransportModelDAOMySQLImpl implements TransportModelDAO {
    private enum SQLQuery {
        GET("SELECT * FROM transportModel WHERE id = (?)"),
        GET_BY_OWNER("SELECT * FROM transportModel WHERE ownerId = (?)"),
        INSERT("INSERT INTO transportModel VALUES (?, ?, ?, ?)"),
        DELETE("DELETE FROM transportModel WHERE id = (?)"),
        DELETE_BY_OWNER("DELETE FROM transportModel WHERE ownerId = (?)"),
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS transportModel (id VARCHAR(255) PRIMARY KEY, ownerId VARCHAR(255), name VARCHAR(255), cost DECIMAL)");

        final String QUERY;

        SQLQuery(String QUERY) {
            this.QUERY = QUERY;
        }
    }

    private DbConnection connection;

    private static TransportModelDAOMySQLImpl INSTANCE = null;

    private TransportModelDAOMySQLImpl() throws SQLException {
        this.connection = new DbConnection();
        createTableIfNotExists();
    }

    private TransportModelDAOMySQLImpl(String fileProperties) throws SQLException {
        this.connection = new DbConnection(fileProperties);
        createTableIfNotExists();
    }

    public static TransportModelDAOMySQLImpl getInstance() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new TransportModelDAOMySQLImpl();
        }
        return INSTANCE;
    }

    public static TransportModelDAOMySQLImpl getInstance(String fileProperties) throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new TransportModelDAOMySQLImpl(fileProperties);
        }
        return INSTANCE;
    }

    @Override
    public boolean create(ModelDAO modelDAO) {
        int result = 0;
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.INSERT.QUERY)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, modelDAO.getOwnerId().toString());
            statement.setString(3, modelDAO.getName());
            statement.setDouble(4, modelDAO.getCost());
            result = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result == 1;
    }

    @Override
    public Optional<ModelDAO> read(UUID uuid) {
        Optional<ModelDAO> result = Optional.empty();

        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.GET.QUERY)) {
            statement.setString(1, uuid.toString());

            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                UUID ownerId = (UUID) rs.getObject("ownerId");
                String name = rs.getString("name");
                Double cost = rs.getDouble("cost");
                result = Optional.of(new ModelDAO(ownerId, cost, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void createTableIfNotExists() {
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.CREATE_TABLE.QUERY)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean update(ModelDAO modelDAO) {
        return false;
    }

    @Override
    public boolean delete(UUID uuid) {
        boolean result = false;

        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.DELETE.QUERY)) {
            statement.setString(1, uuid.toString());

            result = statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void closeConnection() throws SQLException {
        connection.closeConnection();
    }

    @Override
    public List<ModelDAO> getModelsForConcreteTransport(UUID ownerId) {
        ArrayList<ModelDAO> result = new ArrayList<>();
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.GET_BY_OWNER.QUERY)) {
            statement.setString(1, ownerId.toString());

            final ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                UUID id = UUID.fromString(rs.getString("ownerId"));
                Double cost = rs.getDouble("cost");
                String name = rs.getString("name");
                result.add(new ModelDAO(id, cost, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean deleteByOwnerId(UUID ownerId) {
        boolean result = false;

        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.DELETE_BY_OWNER.QUERY)) {
            statement.setString(1, ownerId.toString());

            result = statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
