package ru.ssau.patternpractice.model.dao;

import ru.ssau.patternpractice.model.Transport;

import java.util.List;
import java.util.UUID;

public interface TransportModelDAO extends DAO<ModelDAO, UUID> {
    List<ModelDAO> getModelsForConcreteTransport(UUID ownerId);
    boolean deleteByOwnerId(UUID ownerId);
}
