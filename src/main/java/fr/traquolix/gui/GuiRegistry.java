package fr.traquolix.gui;

import fr.traquolix.identifiers.Identifier;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
public class GuiRegistry {
    @Getter
    private static final GuiRegistry instance = new GuiRegistry();

    // A map that associates item identifiers with their corresponding AbstractItem objects
    private final ConcurrentMap<Identifier, AbstractGui> guiMap = new ConcurrentHashMap<>();

    // Private constructor to prevent external instantiation (singleton pattern)
    private GuiRegistry() {
    }

    public void registerGui(AbstractGui gui) {
        guiMap.put(gui.getIdentifier(), gui);
    }

    public void unregisterGui(Identifier gui) {
        guiMap.remove(gui);
    }

    public AbstractGui getGui(Identifier gui) {
        return guiMap.get(gui);
    }

    public int getSize() {
        return guiMap.size();
    }


}
