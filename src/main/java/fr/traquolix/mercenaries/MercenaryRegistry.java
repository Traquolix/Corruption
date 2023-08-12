package fr.traquolix.mercenaries;

import fr.traquolix.content.generalities.identifiers.Identifier;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MercenaryRegistry {
    private static MercenaryRegistry instance;

    ConcurrentMap<Identifier, AbstractMercenary> mercenaries = new ConcurrentHashMap<>();

    public static MercenaryRegistry getInstance() {
        if (instance == null) {
            instance = new MercenaryRegistry();
        }
        return instance;
    }

    public void register(Identifier identifier, AbstractMercenary mercenary) {
        mercenaries.put(identifier, mercenary);
    }

    public AbstractMercenary getById(String mercernaryid) {
        for (AbstractMercenary mercenary:mercenaries.values()) {
            if (mercenary.getIdentifier().getId().equals(mercernaryid)) {
                return mercenary.clone();
            }
        }
        return null;
    }
}
