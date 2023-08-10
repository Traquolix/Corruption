package fr.traquolix.entity;

import fr.traquolix.identifiers.Identifier;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class EntityRegistry {
    private static final EntityRegistry INSTANCE = new EntityRegistry();
@Getter
    private final ConcurrentMap<Identifier, AbstractEntity> EntityMap = new ConcurrentHashMap<>();

    // Private constructor to prevent external instantiation (singleton pattern)
    private EntityRegistry() {
    }

    public static EntityRegistry getInstance() {
        return INSTANCE;
    }

    public void registerEntity(AbstractEntity entity) {
        EntityMap.put(entity.getIdentifier(), entity);
    }


    public void unregisterEntity(Identifier identifier) {
        EntityMap.remove(identifier);
    }

    public AbstractEntity getEntity(Identifier identifier) {
        return EntityMap.get(identifier);
    }

    public int getSize() {
        return EntityMap.size();
    }
}
