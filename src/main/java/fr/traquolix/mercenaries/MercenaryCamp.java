package fr.traquolix.mercenaries;

import fr.traquolix.content.generalities.identifiers.Identifier;
import fr.traquolix.mercenaries.JackTheRipper.JackTheRipper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MercenaryCamp {
    ConcurrentMap<Identifier, AbstractMercenary> mercenaries = new ConcurrentHashMap<>();

    public AbstractMercenary getMercenaryById(String mercernaryid) {
        for (AbstractMercenary mercenary:mercenaries.values()) {
            if (mercenary.getIdentifier().getId().equals(mercernaryid)) {
                return mercenary;
            }
        }
        return null;
    }

    public void addMercenary(AbstractMercenary mercenary) {
        mercenaries.put(mercenary.getIdentifier(), mercenary);
    }
}
