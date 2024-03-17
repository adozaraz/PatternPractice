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

public class AutomobileDAOImpl implements AutomobileDAO {

    private enum SQLQuery {
        GET("SELECT * FROM transport WHERE id = (?)"),
        INSERT("INSERT INTO transport (id, brand, modelsAmount) VALUES ((?), (?), (?)) RETURNING id"),
        DELETE("DELETE FROM transport WHERE id = (?)");

        final String QUERY;

        SQLQuery(String QUERY) {
            this.QUERY = QUERY;
        }
    }

    private DbConnection connection;

    private TransportModelDAO transportModelDAO;

    public AutomobileDAOImpl() throws SQLException {
        this.connection = DbConnection.getInstance();
        transportModelDAO = new TransportModelDAOImpl();
    }

    @Override
    public boolean create(Transport transport) {
        boolean result = false;
        UUID id = UUID.randomUUID();
        try (PreparedStatement statement = connection.getPreparedStatement(SQLQuery.INSERT.QUERY)) {
            statement.setString(1, id.toString());
            statement.setString(2, transport.getBrand());
            statement.setString(3, String.valueOf(transport.getModelsAmount()));
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
            statement.setString(1, uuid.toString());

            final ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                String brand = rs.getString("brand");
                String modelsAmount = rs.getString("modelsAmount");
                List<ModelDAO> models = transportModelDAO.getModelsForConcreteTransport(uuid);
                Automobile auto = new Automobile(brand, Integer.parseInt(modelsAmount));
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
            statement.setString(1, uuid.toString());

            result = statement.executeQuery().next();
            transportModelDAO.deleteByOwnerId(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

}
