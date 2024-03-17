package ru.ssau.patternpractice.model.dao;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.DbConnection;
import ru.ssau.patternpractice.model.Transport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransportModelDAOImpl implements TransportModelDAO {
    private enum SQLQuery {
        GET("SELECT * FROM transportModel WHERE id = (?)"),
        GET_BY_OWNER("SELECT * FROM transportModel WHERE ownerId = (?)"),
        INSERT("INSERT INTO transportModel (id, ownerId, name, cost) VALUES ((?), (?), (?), (?)) RETURNING id"),
        DELETE("DELETE FROM transportModel WHERE id = (?)"),
        DELETE_BY_OWNER("DELETE FROM transportModel WHERE ownerId = (?)");

        final String QUERY;

        SQLQuery(String QUERY) {
            this.QUERY = QUERY;
        }
    }

    private DbConnection connection;

    public TransportModelDAOImpl() throws SQLException {
        this.connection = DbConnection.getInstance();
    }

    @Override
    public boolean create(ModelDAO modelDAO) {
        boolean result = false;
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.INSERT.QUERY)) {
            statement.setString(1, UUID.randomUUID().toString());
            statement.setString(2, modelDAO.getOwnerId().toString());
            statement.setString(3, modelDAO.getName());
            statement.setString(4, String.valueOf(modelDAO.getCost()));
            result = statement.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<ModelDAO> read(UUID uuid) {
        Optional<ModelDAO> result = Optional.empty();

        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.GET.QUERY)) {
            statement.setString(1, uuid.toString());

            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String ownerId = rs.getString("ownerId");
                String name = rs.getString("name");
                String cost = rs.getString("cost");
                result = Optional.of(new ModelDAO(UUID.fromString(ownerId), Double.parseDouble(cost), name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
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
    public List<ModelDAO> getModelsForConcreteTransport(UUID ownerId) {
        ArrayList<ModelDAO> result = new ArrayList<>();
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.GET_BY_OWNER.QUERY)) {
            statement.setString(1, ownerId.toString());

            final ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String id = rs.getString("ownerId");
                String cost = rs.getString("cost");
                String name = rs.getString("name");
                result.add(new ModelDAO(UUID.fromString(id), Double.parseDouble(cost), name));
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
