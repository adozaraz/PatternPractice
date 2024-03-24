package ru.ssau.patternpractice.model.dao;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface DAO<Entity, Key> {
    boolean create(Entity entity);
    Optional<Entity> read(Key key);
    boolean update(Entity entity);
    boolean delete(UUID uuid);
    void closeConnection() throws SQLException;
}
