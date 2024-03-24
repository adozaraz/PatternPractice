package ru.ssau.patternpractice.model.dao;

import ru.ssau.patternpractice.exception.DuplicateModelNameException;
import ru.ssau.patternpractice.model.Automobile;
import ru.ssau.patternpractice.model.DbConnection;
import ru.ssau.patternpractice.model.Transport;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AutomobileDAOPostgresqlImpl implements AutomobileDAO {

    private enum SQLQuery {
        GET("SELECT * FROM transport WHERE id = (?)::UUID"),
        INSERT("INSERT INTO transport (id, brand, modelsAmount) VALUES ((?), (?), (?)) returning id"),
        DELETE("DELETE FROM transport WHERE id = (?)::UUID"),
        CREATE_TABLE("CREATE TABLE IF NOT EXISTS transport (id UUID PRIMARY KEY, brand VARCHAR, modelsAmount INTEGER)");

        final String QUERY;

        SQLQuery(String QUERY) {
            this.QUERY = QUERY;
        }
    }

    private DbConnection connection;

    private TransportModelDAO transportModelDAO;

    private static AutomobileDAOPostgresqlImpl INSTANCE = null;

    private AutomobileDAOPostgresqlImpl() throws SQLException {
        this.connection = new DbConnection();
        createTableIfNotExists();
        transportModelDAO = TransportModelDAOMySQLImpl.getInstance();
    }

    private AutomobileDAOPostgresqlImpl(String fileProperties) throws SQLException {
        this.connection = new DbConnection(fileProperties);
        createTableIfNotExists();
        transportModelDAO = TransportModelDAOMySQLImpl.getInstance();
    }

    private AutomobileDAOPostgresqlImpl(String fileProperties, String transportModelFileProperties) throws SQLException {
        this.connection = new DbConnection(fileProperties);
        createTableIfNotExists();
        transportModelDAO = TransportModelDAOMySQLImpl.getInstance(transportModelFileProperties);
    }

    public static AutomobileDAOPostgresqlImpl getInstance() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new AutomobileDAOPostgresqlImpl();
        }
        return INSTANCE;
    }

    public static AutomobileDAOPostgresqlImpl getInstance(String fileProperties) throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new AutomobileDAOPostgresqlImpl(fileProperties);
        }
        return INSTANCE;
    }

    public static AutomobileDAOPostgresqlImpl getInstance(String fileProperties, String transportModelFileProperties) throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new AutomobileDAOPostgresqlImpl(fileProperties, transportModelFileProperties);
        }
        return INSTANCE;
    }

    @Override
    public boolean create(Transport transport) {
        boolean result = false;
        UUID id = (transport.getId() != null) ? transport.getId() : UUID.randomUUID();
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.INSERT.QUERY)) {
            statement.setObject(1, id);
            statement.setString(2, transport.getBrand());
            statement.setInt(3, transport.getModelsAmount());
            result = statement.executeQuery().next();
            List<String> modelNames = transport.getAllModelsNames();
            List<Double> modelCost = transport.getAllModelsCost();
            for (int i = 0; i < modelNames.size(); ++i) {
                transportModelDAO.create(new ModelDAO(id, modelCost.get(i), modelNames.get(i)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Optional<Transport> read(UUID uuid) {
        Optional<Transport> result = Optional.empty();

        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.GET.QUERY)) {
            statement.setObject(1, uuid);

            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String brand = rs.getString("brand");
                Integer modelsAmount = rs.getInt("modelsAmount");
                List<ModelDAO> models = transportModelDAO.getModelsForConcreteTransport(uuid);
                Automobile auto = new Automobile(brand, modelsAmount);
                auto.clearAllModels();
                for (ModelDAO model : models) {
                    auto.addNewModel(model.getName(), model.getCost());
                }

                result = Optional.of(auto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DuplicateModelNameException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean update(Transport transport) {
        return false;
    }

    @Override
    public boolean delete(UUID uuid) {
        boolean result = false;

        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.DELETE.QUERY)) {
            statement.setObject(1, uuid);

            result = statement.executeQuery().next();
            transportModelDAO.deleteByOwnerId(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void closeConnection() throws SQLException {
        connection.closeConnection();
        transportModelDAO.closeConnection();
    }

    private void createTableIfNotExists() {
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.CREATE_TABLE.QUERY)) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TransportModelDAO getTransportModelDAO() {
        return transportModelDAO;
    }
}
